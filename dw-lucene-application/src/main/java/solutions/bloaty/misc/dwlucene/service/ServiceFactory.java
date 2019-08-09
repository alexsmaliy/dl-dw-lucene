package solutions.bloaty.misc.dwlucene.service;

import com.google.common.collect.ImmutableSet;
import io.dropwizard.setup.Environment;
import solutions.bloaty.tuts.dw.deepsearch.api.appconfig.DeepLearningForSearchConfiguration;
import solutions.bloaty.tuts.dw.deepsearch.api.appconfig.LuceneConfiguration;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.Resource;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.DummyResource;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.LuceneResource;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.ResourceFactory;

import java.util.Collection;

public final class ServiceFactory implements ResourceFactory {
    private final DummyResource dummyResource;
    private final LuceneResource luceneResource;

    private ServiceFactory(DeepLearningForSearchConfiguration configuration,
                           Environment environment) {
        this.dummyResource = createDummyResource(configuration, environment);
        this.luceneResource = createLuceneResource(configuration, environment);
    }

    public static ServiceFactory create(DeepLearningForSearchConfiguration configuration,
                                        Environment environment) {
        return new ServiceFactory(configuration, environment);
    }

    @Override
    public Collection<Resource> resources() {
        return ImmutableSet.of(dummyResource, luceneResource);
    }

    private static DummyResource createDummyResource(DeepLearningForSearchConfiguration configuration,
                                                     Environment environment) {
        String template = configuration.getApplicationConfiguration().template();
        String defaultName = configuration.getApplicationConfiguration().defaultName();
        return new DummyService(template, defaultName);
    }

    private static LuceneResource createLuceneResource(DeepLearningForSearchConfiguration configuration,
                                                       Environment environment) {
        LuceneConfiguration luceneConfig = configuration.getApplicationConfiguration().lucene();
        return new LuceneService(luceneConfig, environment);
    }
}
