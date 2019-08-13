package solutions.bloaty.misc.dwlucene.index;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solutions.bloaty.misc.dwlucene.Constants;
import solutions.bloaty.tuts.dw.deepsearch.api.document.ImmutableField;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class IndexInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexInitializer.class);

    private IndexInitializer() { /* utility class */ }

    public static IndexWriter getIndexWriter(Path rootIndexDir, String indexName) {
        IndexWriterConfig indexWriterConfig = getIndexWriterConfig();
        try {
            Path indexDir = getCanonicalIndexPath(rootIndexDir, indexName);
            Directory indexDirectory = FSDirectory.open(indexDir);
            return new IndexWriter(indexDirectory, indexWriterConfig);
        } catch (IOException e) {
            LOGGER.error("Unable to initialize index writer!", e);
            throw new RuntimeException("Unable to initialize index writer!", e);
        }
    }

    private static Path getCanonicalIndexPath(Path root, String indexName) {
        return root.resolve(IndexUtils.encodeIndexName(indexName));
    }

    public static SearcherManager getSearcherManager(IndexWriter indexWriter,
                                                     @Nullable SearcherFactory searcherFactory) {
        try {
            return new SearcherManager(indexWriter, searcherFactory);
        } catch (IOException e) {
            LOGGER.error("Unable to initialize index searcher!", e);
            throw new RuntimeException("Unable to initialize index searcher!", e);
        }
    }

    private static IndexWriterConfig getIndexWriterConfig() {
        CharArraySet stopwords = new CharArraySet(
            ImmutableSet.of("a", "an", "the"),
            Constants.Defaults.IGNORE_CASE);
        Map<String, Analyzer> fieldToAnalyzerMap = ImmutableMap.of(
            ImmutableField.of("pages").name(),
            new StopAnalyzer(stopwords),
            Constants.Defaults.SEARCHED_FIELD.name(),
            new WhitespaceAnalyzer());
        Analyzer analyzer = new PerFieldAnalyzerWrapper(new EnglishAnalyzer(), fieldToAnalyzerMap);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        config.setCommitOnClose(true);
        return config;
    }
}
