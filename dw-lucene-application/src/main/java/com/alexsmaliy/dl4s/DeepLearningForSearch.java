package com.alexsmaliy.dl4s;

import com.alexsmaliy.dl4s.error.InvalidDefinitionExceptionMapper;
import com.alexsmaliy.dl4s.healthcheck.DummyHealthcheck;
import com.alexsmaliy.dl4s.persistence.TotalDirectoryManager;
import com.alexsmaliy.dl4s.service.ServiceFactory;
import com.google.common.collect.ImmutableSet;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import com.alexsmaliy.dl4s.error.JsonMappingExceptionMapper;
import com.alexsmaliy.dl4s.index.ManagedIndex;
import com.alexsmaliy.dl4s.api.appconfig.DeepLearningForSearchConfiguration;
import com.alexsmaliy.dl4s.api.resource.ResourceFactory;

import java.nio.file.Path;
import java.util.Set;

public class DeepLearningForSearch extends Application<DeepLearningForSearchConfiguration> {
    public static void main(String[] args) throws Exception {
        new DeepLearningForSearch().run(args);
    }

    @Override
    public void run(DeepLearningForSearchConfiguration configuration,
                    Environment environment) {
        /* SERVER-MANAGED OBJECTS */
        // on-disk directory manager
        TotalDirectoryManager totalDirectoryManager = new TotalDirectoryManager(configuration);
        environment.lifecycle().manage(totalDirectoryManager);
        // Lucene indexes
        Path indexDirPath = configuration.getApplicationConfiguration().lucene().indexesRootDir();
        ManagedIndex managedIndex = new ManagedIndex(indexDirPath, ServerConstants.PRIMARY_INDEX_NAME);
        environment.lifecycle().manage(managedIndex);

        /* REQUEST HANDLERS */
        Set<ManagedIndex> managedIndexes = ImmutableSet.of(managedIndex);
        ResourceFactory resourceFactory = ServiceFactory.create(configuration, managedIndexes);
        resourceFactory.resources().forEach(environment.jersey()::register);

        /* HEALTHCHECKS */
        DummyHealthcheck dummy = new DummyHealthcheck("Dummy template: %s");
        environment.healthChecks().register("dummy", dummy);

        /* CUSTOMIZED EXCEPTION HANDLING */
        environment.jersey().register(new InvalidDefinitionExceptionMapper());
        environment.jersey().register(new JsonMappingExceptionMapper());
    }

    @Override
    public void initialize(Bootstrap<DeepLearningForSearchConfiguration> bootstrap) {
        // nothing so far
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
