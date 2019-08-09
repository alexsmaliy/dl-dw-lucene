package solutions.bloaty.misc.dwlucene.service;

import io.dropwizard.setup.Environment;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solutions.bloaty.tuts.dw.deepsearch.api.appconfig.LuceneConfiguration;
import solutions.bloaty.tuts.dw.deepsearch.api.query.StringQuery;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.LuceneResource;

import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class LuceneService implements LuceneResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneService.class);

    private static final String DEFAULT_SEARCHED_FIELD = "title";
    private static final QueryParser DEFAULT_QUERY_PARSER = getDefaultQueryParser();

    private final AtomicReference<IndexWriter> indexWriterAtomicReference;
    private final LuceneConfiguration luceneConfig;
    private final Environment environment;

    private static QueryParser getDefaultQueryParser() {
        return new QueryParser(DEFAULT_SEARCHED_FIELD, new WhitespaceAnalyzer());
    }

    public LuceneService(LuceneConfiguration luceneConfig, Environment environment) {
        this.luceneConfig = luceneConfig;
        this.environment = environment;
        Directory indexDir;
        try {
            indexDir = FSDirectory.open(luceneConfig.indexDir());
        } catch (IOException e) {
            throw new RuntimeException("Unable to open Lucene index dir!", e);
        }
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        config.setCommitOnClose(true);
        IndexWriter writer;
        try {
            writer = new IndexWriter(indexDir, config);
        } catch (IOException e) {
            throw new RuntimeException("Unable to initialize index writer!", e);
        }
        this.indexWriterAtomicReference = new AtomicReference<>(writer);
    }

    @Override
    public boolean ping() {
        return true;
    }

    @Override
    public String query(StringQuery stringQuery) {
        try {
            Query query = DEFAULT_QUERY_PARSER.parse(stringQuery.query());
        } catch (ParseException e) {
            LOGGER.error("Unable to parse query!", e);
            throw new BadRequestException("Unable to parse query! " + e.getMessage());
        }
        return stringQuery.query();
    }
}
