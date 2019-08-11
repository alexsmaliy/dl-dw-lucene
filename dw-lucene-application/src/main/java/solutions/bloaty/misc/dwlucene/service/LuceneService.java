package solutions.bloaty.misc.dwlucene.service;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TopDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solutions.bloaty.misc.dwlucene.index.ManagedIndex;
import solutions.bloaty.tuts.dw.deepsearch.api.query.StringQuery;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.LuceneResource;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class LuceneService implements LuceneResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneService.class);

    private static final String DEFAULT_SEARCHED_FIELD = "title";
    private static final QueryParser DEFAULT_QUERY_PARSER = getDefaultQueryParser(DEFAULT_SEARCHED_FIELD);

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

    @Override
    public String query(StringQuery stringQuery) {
        IndexSearcher tempSearcher = null;
        TopDocs topDocs = null;
        try {
            Query parsedQuery = tryParseQuery(stringQuery);
            tempSearcher = tryAcquireSearcher();
            topDocs = trySearch(tempSearcher, parsedQuery);
        } finally {
            tryReleaseSearcher(tempSearcher);
            tempSearcher = null;
        }
        return "Number of hits: " + topDocs.totalHits.value;
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
