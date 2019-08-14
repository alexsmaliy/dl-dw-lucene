package solutions.bloaty.misc.dwlucene;

import com.google.common.collect.ImmutableSet;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import solutions.bloaty.misc.dwlucene.healthcheck.DummyHealthcheck;
import solutions.bloaty.misc.dwlucene.index.ManagedIndex;
import solutions.bloaty.misc.dwlucene.persistence.TotalDirectoryManager;
import solutions.bloaty.misc.dwlucene.service.ServiceFactory;
import solutions.bloaty.misc.dwlucene.error.InvalidDefinitionExceptionMapper;
import solutions.bloaty.tuts.dw.deepsearch.api.appconfig.DeepLearningForSearchConfiguration;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.ResourceFactory;

import java.nio.file.Path;
import java.util.Set;

public class DeepLearningForSearch extends Application<DeepLearningForSearchConfiguration> {
    public static void main(String[] args) throws Exception {
        new DeepLearningForSearch().run(args);
    }

    @Override
    public void run(DeepLearningForSearchConfiguration configuration,
                    Environment environment) {
        TotalDirectoryManager totalDirectoryManager = new TotalDirectoryManager(configuration);
        environment.lifecycle().manage(totalDirectoryManager);

        Path indexDirPath = configuration.getApplicationConfiguration().lucene().indexesRootDir();
        ManagedIndex managedIndex = new ManagedIndex(indexDirPath, Constants.PRIMARY_INDEX_NAME);
        environment.lifecycle().manage(managedIndex);

        Set<ManagedIndex> managedIndexes = ImmutableSet.of(managedIndex);
        ResourceFactory resourceFactory = ServiceFactory.create(configuration, managedIndexes);
        resourceFactory.resources().forEach(resource -> environment.jersey().register(resource));

        DummyHealthcheck dummy = new DummyHealthcheck("Dummy template: %s");
        environment.healthChecks().register("dummy", dummy);

        environment.jersey().register(new InvalidDefinitionExceptionMapper());
    }

    @Override
    public void initialize(Bootstrap<DeepLearningForSearchConfiguration> bootstrap) {
        // nothing yet
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
