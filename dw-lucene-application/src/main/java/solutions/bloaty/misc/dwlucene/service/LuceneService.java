package solutions.bloaty.misc.dwlucene.service;

import com.google.common.collect.ImmutableList;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solutions.bloaty.misc.dwlucene.Constants;
import solutions.bloaty.misc.dwlucene.index.ManagedIndex;
import solutions.bloaty.tuts.dw.deepsearch.api.document.Field;
import solutions.bloaty.tuts.dw.deepsearch.api.query.StringQuery;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.LuceneResource;
import solutions.bloaty.tuts.dw.deepsearch.api.response.HitCollection;
import solutions.bloaty.tuts.dw.deepsearch.api.response.ImmutableHitCollection;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LuceneService implements LuceneResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneService.class);

    private static final QueryParser DEFAULT_QUERY_PARSER =
        getDefaultQueryParser(Constants.DEFAULT_SEARCHED_FIELD.name());

    private final IndexWriter indexWriter;
    private final SearcherManager searcherManager;

    private static QueryParser getDefaultQueryParser(String fieldName) {
        return new QueryParser(fieldName, new WhitespaceAnalyzer());
    }

    LuceneService(ManagedIndex managedIndex) {
        this.indexWriter = managedIndex.getIndexWriter();
        this.searcherManager = managedIndex.getSearcherManager();
    }

    @Override
    public boolean ping() {
        return true;
    }

    public void index(String toIndex) {

    }

    @Override
    public HitCollection query(StringQuery stringQuery) {
        IndexSearcher tempSearcher = null;
        try {
            Query parsedQuery = tryParseQuery(stringQuery);
            tempSearcher = tryAcquireSearcher();
            TopDocs topDocs = trySearch(tempSearcher, parsedQuery);
            IndexReader indexReader = tempSearcher.getIndexReader();
            List<String> hits = Arrays.stream(topDocs.scoreDocs)
                  .flatMap(result -> getField(result, indexReader, Constants.DEFAULT_SEARCHED_FIELD).stream())
                  .collect(ImmutableList.toImmutableList());
            return ImmutableHitCollection.of(hits);
        } finally {
            if (tempSearcher != null) {
                tryReleaseSearcher(tempSearcher);
            }
        }
    }

    private static Optional<String> getField(ScoreDoc result,
                                             IndexReader indexReader,
                                             Field field) {
        try {
            return Optional.of(indexReader.document(result.doc).get(field.name()));
        } catch (IOException e) {
            LOGGER.error("Failed to retrieve a field from a query result. Results are incomplete!", e);
            return Optional.empty();
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
            return searcherManager.acquire();
        } catch (IOException e) {
            LOGGER.error("Failed to acquire index searcher!", e);
            throw new ServerErrorException(
                "Server error while searching!",
                Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private TopDocs trySearch(IndexSearcher indexSearcher, Query query) {
        try {
            return indexSearcher.search(query, 5);
        } catch (IOException e) {
            LOGGER.error("Failed to search!", e);
            throw new ServerErrorException(
                "Server error while searching!",
                Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private void tryReleaseSearcher(IndexSearcher indexSearcher) {
        try {
            searcherManager.release(indexSearcher);
        } catch (IOException e) {
            LOGGER.error("Failed to release index searcher after use!", e);
            throw new ServerErrorException(
                "Server error while searching!",
                Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
