package com.alexsmaliy.dl4s.service;

import com.google.common.collect.ImmutableSet;
import com.alexsmaliy.dl4s.index.ManagedIndex;
import com.alexsmaliy.dl4s.api.appconfig.DeepLearningForSearchConfiguration;
import com.alexsmaliy.dl4s.api.resource.DummyResource;
import com.alexsmaliy.dl4s.api.resource.LuceneResource;
import com.alexsmaliy.dl4s.api.resource.Resource;
import com.alexsmaliy.dl4s.api.resource.ResourceFactory;

import java.util.Collection;
import java.util.Set;

public final class ServiceFactory implements ResourceFactory {
    private final DummyResource dummyResource;
    private final LuceneResource luceneResource;

    private ServiceFactory(DeepLearningForSearchConfiguration configuration,
                           Set<ManagedIndex> managedIndexes) {
        this.dummyResource = createDummyResource(configuration);
        this.luceneResource = createLuceneResource(managedIndexes);
    }

    public static ServiceFactory create(DeepLearningForSearchConfiguration configuration,
                                        Set<ManagedIndex> managedIndexes) {
        return new ServiceFactory(configuration, managedIndexes);
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

    private static LuceneResource createLuceneResource(Set<ManagedIndex> managedIndexes) {
        return new LuceneService(managedIndexes);
    }
}
