package solutions.bloaty.misc.dwlucene.service;

import com.google.common.collect.ImmutableSet;
import solutions.bloaty.misc.dwlucene.index.ManagedIndex;
import solutions.bloaty.tuts.dw.deepsearch.api.appconfig.DeepLearningForSearchConfiguration;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.DummyResource;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.LuceneResource;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.Resource;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.ResourceFactory;

import java.util.Collection;

public final class ServiceFactory implements ResourceFactory {
    private final DummyResource dummyResource;
    private final LuceneResource luceneResource;

    private ServiceFactory(DeepLearningForSearchConfiguration configuration,
                           ManagedIndex managedIndex) {
        this.dummyResource = createDummyResource(configuration);
        this.luceneResource = createLuceneResource(managedIndex);
    }

    public static ServiceFactory create(DeepLearningForSearchConfiguration configuration,
                                        ManagedIndex managedIndex) {
        return new ServiceFactory(configuration, managedIndex);
    }

    @Override
    public Collection<Resource> resources() {
        return ImmutableSet.of(dummyResource, luceneResource);
    }

    private static DummyResource createDummyResource(DeepLearningForSearchConfiguration configuration) {
        String template = configuration.getApplicationConfiguration().template();
        String defaultName = configuration.getApplicationConfiguration().defaultName();
        return new DummyService(template, defaultName);
    }

    private static LuceneResource createLuceneResource(ManagedIndex managedIndex) {
        return new LuceneService(managedIndex);
    }
}
