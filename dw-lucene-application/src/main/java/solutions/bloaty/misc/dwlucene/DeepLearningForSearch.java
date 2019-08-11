package solutions.bloaty.misc.dwlucene;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import solutions.bloaty.misc.dwlucene.healthcheck.DummyHealthcheck;
import solutions.bloaty.misc.dwlucene.index.ManagedIndex;
import solutions.bloaty.misc.dwlucene.persistence.TotalDirectoryManager;
import solutions.bloaty.misc.dwlucene.service.ServiceFactory;
import solutions.bloaty.tuts.dw.deepsearch.api.appconfig.DeepLearningForSearchConfiguration;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.ResourceFactory;

import java.nio.file.Path;

public class DeepLearningForSearch extends Application<DeepLearningForSearchConfiguration> {
    public static void main(String[] args) throws Exception {
        new DeepLearningForSearch().run(args);
    }

    @Override
    public void run(DeepLearningForSearchConfiguration configuration,
                    Environment environment) {
        TotalDirectoryManager totalDirectoryManager = new TotalDirectoryManager(configuration);
        environment.lifecycle().manage(totalDirectoryManager);

        Path indexDirPath = configuration.getApplicationConfiguration().lucene().indexDir();
        ManagedIndex managedIndex = new ManagedIndex(indexDirPath);
        environment.lifecycle().manage(managedIndex);

        ResourceFactory resourceFactory = ServiceFactory.create(configuration, managedIndex);
        resourceFactory.resources().forEach(resource -> environment.jersey().register(resource));

        DummyHealthcheck dummy = new DummyHealthcheck("Dummy template: %s");
        environment.healthChecks().register("dummy", dummy);
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
