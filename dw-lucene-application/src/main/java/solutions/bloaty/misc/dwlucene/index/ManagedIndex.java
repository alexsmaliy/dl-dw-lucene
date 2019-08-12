package solutions.bloaty.misc.dwlucene.index;

import io.dropwizard.lifecycle.Managed;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.SearcherManager;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;

public class ManagedIndex implements Managed {
    private final IndexWriter indexWriter;
    private final SearcherManager searcherManager;

    public ManagedIndex(Path indexDirPath) {
        indexWriter = IndexInitializer.getIndexWriter(indexDirPath);
        searcherManager = IndexInitializer.getSearcherManager(indexWriter, null);
    }

    @Override
    public void start() {}

    @Override
    public void stop() throws IOException {
        searcherManager.close();
        indexWriter.close();
    }

    @Nullable
    public IndexWriter getIndexWriter() {
        return this.indexWriter;
    }

    @Nullable
    public SearcherManager getSearcherManager() {
        return this.searcherManager;
    }
}
