package solutions.bloaty.misc.dwlucene;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import solutions.bloaty.misc.dwlucene.healthcheck.DummyHealthcheck;
import solutions.bloaty.misc.dwlucene.persistence.TotalDirectoryManager;
import solutions.bloaty.misc.dwlucene.service.ServiceFactory;
import solutions.bloaty.tuts.dw.deepsearch.api.appconfig.DeepLearningForSearchConfiguration;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.ResourceFactory;

public class DeepLearningForSearch extends Application<DeepLearningForSearchConfiguration> {
    public static void main(String[] args) throws Exception {
        new DeepLearningForSearch().run(args);
    }

    @Override
    public void run(DeepLearningForSearchConfiguration configuration,
                    Environment environment) {
        manageManagedObjects(configuration, environment);
        registerResources(configuration, environment);
        registerHealthchecks(configuration, environment);
    }

    private static void manageManagedObjects(DeepLearningForSearchConfiguration configuration,
                                             Environment environment) {
        TotalDirectoryManager totalDirectoryManager = new TotalDirectoryManager(configuration);
        environment.lifecycle().manage(totalDirectoryManager);
    }

    private static void registerResources(DeepLearningForSearchConfiguration configuration,
                                          Environment environment) {
        ResourceFactory resourceFactory = ServiceFactory.create(configuration, environment);
        resourceFactory.resources().forEach(resource -> environment.jersey().register(resource));
    }

    private static void registerHealthchecks(DeepLearningForSearchConfiguration configuration,
                                             Environment environment) {
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
