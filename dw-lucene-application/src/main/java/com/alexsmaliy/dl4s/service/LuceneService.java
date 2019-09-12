package com.alexsmaliy.dl4s.service;

import com.alexsmaliy.dl4s.ServerConstants;
import com.alexsmaliy.dl4s.api.document.FieldIdentifier;
import com.alexsmaliy.dl4s.api.document.ImmutableFieldIdentifier;
import com.alexsmaliy.dl4s.api.document.ImmutableIndexableField;
import com.alexsmaliy.dl4s.api.document.ImmutableTitleField;
import com.alexsmaliy.dl4s.api.document.IndexableDocument;
import com.alexsmaliy.dl4s.api.document.TitleField;
import com.alexsmaliy.dl4s.api.query.StringQuery;
import com.alexsmaliy.dl4s.api.query.VisitableBaseQuery;
import com.alexsmaliy.dl4s.api.resource.LuceneResource;
import com.alexsmaliy.dl4s.api.response.Hit;
import com.alexsmaliy.dl4s.api.response.HitCollection;
import com.alexsmaliy.dl4s.api.response.ImmutableHit;
import com.alexsmaliy.dl4s.api.response.ImmutableHitCollection;
import com.alexsmaliy.dl4s.api.response.ImmutableIndexingResponse;
import com.alexsmaliy.dl4s.api.response.IndexingResponse;
import com.alexsmaliy.dl4s.index.ManagedIndex;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TopDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LuceneService implements LuceneResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneService.class);

    private static final QueryParser DEFAULT_QUERY_PARSER =
        new QueryParser(TitleField.DEFAULT_NAME, new WhitespaceAnalyzer());

    private final Map<String, ManagedIndex> managedIndexes;

    LuceneService(Set<ManagedIndex> managedIndexes) {
        this.managedIndexes = Maps.uniqueIndex(managedIndexes, ManagedIndex::getIndexName);
    }

    @Override
    public boolean ping() {
        return true;
    }

    @Override
    public Set<String> listIndexes() {
        return ImmutableSet.copyOf(managedIndexes.keySet());
    }

    public IndexingResponse index(IndexableDocument document) {
        Document toIndex = new Document();
        IndexableField titleField = new TextField(
            document.title().name(),
            document.title().content(),
            Field.Store.YES);
        toIndex.add(titleField);
        document.fields().forEach(indexableField -> {
            IndexableField field = new TextField(
                indexableField.fieldIdentifier().toString(),
                indexableField.content(),
                Field.Store.YES);
            toIndex.add(field);
        });
        ManagedIndex primaryIndex = managedIndexes.get(ServerConstants.PRIMARY_INDEX_NAME);
        if (primaryIndex == null) {
            LOGGER.error("Unable to find primary index for searching!");
            throw new ServerErrorException(
                "Server error while searching!",
                Response.Status.INTERNAL_SERVER_ERROR);
        }
        IndexWriter indexWriter = primaryIndex.getIndexWriter();
        try {
            indexWriter.addDocument(toIndex);
        } catch (IOException e) {
            LOGGER.error(
                "Failed to add document to index! Index: {}. Document: {}.",
                primaryIndex, document, e);
            throw new ServerErrorException(
                "Server error while indexing!",
                Response.Status.INTERNAL_SERVER_ERROR);
        }
        try {
            long sequenceNumber = indexWriter.commit();
            return ImmutableIndexingResponse.builder().sequenceNumber(sequenceNumber).build();
        } catch (IOException e) {
            LOGGER.error("Failed to commit to primary index!", e);
            throw new ServerErrorException(
                "Server error while indexing!",
                Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public HitCollection query(VisitableBaseQuery query) {
        return query.accept(q -> {
            IndexSearcher tempSearcher = null;
            try {
                int maxResults = q.maxResults();
                Query parsedQuery = tryParseQuery(q);
                tempSearcher = tryAcquireSearcher();
                TopDocs topDocs = trySearch(tempSearcher, parsedQuery, maxResults);
                IndexReader indexReader = tempSearcher.getIndexReader();
                List<Hit> hits = Arrays.stream(topDocs.scoreDocs)
                    .map(result -> {
                        List<com.alexsmaliy.dl4s.api.document.Field> fields =
                            getFields(result, indexReader);
                        return ImmutableHit.builder()
                            .addAllFields(fields)
                            .score(result.score)
                            .build();
                    }).collect(ImmutableList.toImmutableList());
                return ImmutableHitCollection.of(hits);
            } finally {
                if (tempSearcher != null) {
                    tryReleaseSearcher(tempSearcher);
                }
            }
        });
    }

    private static List<com.alexsmaliy.dl4s.api.document.Field> getFields(ScoreDoc result,
                                                                          IndexReader indexReader) {
        try {
            return indexReader.document(result.doc).getFields().stream().map(f -> {
                com.alexsmaliy.dl4s.api.document.Field field = null;
                String fieldValue = f.stringValue();
                String fieldName = f.name();
                if (fieldName.equalsIgnoreCase(TitleField.DEFAULT_NAME)) {
                    field = ImmutableTitleField.builder().content(fieldValue).build();
                } else {
                    FieldIdentifier fieldIdentifier =
                        ImmutableFieldIdentifier.builder()
                            .components(Splitter.on('.').splitToList(fieldName))
                            .build();
                    field = ImmutableIndexableField.builder()
                        .fieldIdentifier(fieldIdentifier)
                        .content(fieldValue)
                        .build();
                }
                return field;
            }).collect(ImmutableList.toImmutableList());
        } catch (IOException e) {
            LOGGER.error(
                "Failed to retrieve a field from a query result. Results are incomplete!",
                e);
            return ImmutableList.of();
        }
    }

    private Query tryParseQuery(StringQuery stringQuery) {
        try {
            return DEFAULT_QUERY_PARSER.parse(stringQuery.query());
        } catch (ParseException e) {
            LOGGER.error("Unable to parse query!", e);
            throw new BadRequestException("Unable to parse query!");
        }
    }

    private IndexSearcher tryAcquireSearcher() {
        try {
            ManagedIndex index = managedIndexes.get(ServerConstants.PRIMARY_INDEX_NAME);
            if (index == null) {
                LOGGER.error("Unable to find primary index for searching!");
                throw new ServerErrorException(
                    "Server error while searching!",
                    Response.Status.INTERNAL_SERVER_ERROR);
            }
            SearcherManager searcherManager = index.getSearcherManager();
            searcherManager.maybeRefresh();
            return searcherManager.acquire();
        } catch (IOException e) {
            LOGGER.error("Failed to acquire index searcher!", e);
            throw new ServerErrorException(
                "Server error while searching!",
                Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private TopDocs trySearch(IndexSearcher indexSearcher, Query query, int maxResults) {
        try {
            return indexSearcher.search(query, maxResults);
        } catch (IOException e) {
            LOGGER.error("Failed to search!", e);
            throw new ServerErrorException(
                "Server error while searching!",
                Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private void tryReleaseSearcher(IndexSearcher indexSearcher) {
        try {
            ManagedIndex index = managedIndexes.get(ServerConstants.PRIMARY_INDEX_NAME);
            if (index == null) {
                LOGGER.error("Obtained a searcher for primary index, "
                    + "but now unable to find the index again to release the searcher!");
                throw new ServerErrorException(
                    "Server error while searching!",
                    Response.Status.INTERNAL_SERVER_ERROR);
            }
            index.getSearcherManager().release(indexSearcher);
        } catch (IOException e) {
            LOGGER.error("Failed to release index searcher for primary index after use!", e);
            throw new ServerErrorException(
                "Server error while searching!",
                Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
