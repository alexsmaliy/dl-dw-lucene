package solutions.bloaty.misc.dwlucene.service;

import io.dropwizard.setup.Environment;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import solutions.bloaty.tuts.dw.deepsearch.api.appconfig.LuceneConfig;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.LuceneResource;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class LuceneService implements LuceneResource {
    private final AtomicReference<IndexWriter> indexWriterAtomicReference;
    private final LuceneConfig luceneConfig;
    private final Environment environment;

    public LuceneService(LuceneConfig luceneConfig, Environment environment) {
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
}
