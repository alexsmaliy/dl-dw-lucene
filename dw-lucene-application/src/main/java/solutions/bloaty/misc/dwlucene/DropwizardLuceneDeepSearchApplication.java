package solutions.bloaty.misc.dwlucene;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import solutions.bloaty.misc.dwlucene.healthcheck.DummyHealthcheck;
import solutions.bloaty.misc.dwlucene.persistence.TotalDirectoryManager;
import solutions.bloaty.misc.dwlucene.service.DummyService;
import solutions.bloaty.misc.dwlucene.service.LuceneService;
import solutions.bloaty.tuts.dw.deepsearch.api.appconfig.DropwizardLuceneDeepSearchConfig;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.DummyResource;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.LuceneResource;

public class DropwizardLuceneDeepSearchApplication extends Application<DropwizardLuceneDeepSearchConfig> {
    public static void main(String[] args) throws Exception {
        new DropwizardLuceneDeepSearchApplication().run(args);
    }

    @Override
    public void run(DropwizardLuceneDeepSearchConfig configuration,
                    Environment environment) throws Exception {
        manageManagedObjects(configuration, environment);
        registerResources(configuration, environment);
        registerHealthchecks(configuration, environment);
    }

    private static void manageManagedObjects(DropwizardLuceneDeepSearchConfig configuration,
                                             Environment environment) {
        TotalDirectoryManager totalDirectoryManager = new TotalDirectoryManager(configuration);
        environment.lifecycle().manage(totalDirectoryManager);
    }

    private static void registerResources(DropwizardLuceneDeepSearchConfig configuration,
                                          Environment environment) {
        registerDummyResource(configuration, environment);
        registerLuceneResource(configuration, environment);
    }

    private static void registerDummyResource(DropwizardLuceneDeepSearchConfig configuration,
                                              Environment environment) {
        String template = configuration.template();
        String defaultName = configuration.defaultName();
        DummyResource dummyResource = new DummyService(template, defaultName);
        environment.jersey().register(dummyResource);
    }

    private static void registerLuceneResource(DropwizardLuceneDeepSearchConfig configuration,
                                               Environment environment) {
        LuceneResource luceneResource = new LuceneService(configuration.lucene(), environment);
        environment.jersey().register(luceneResource);
    }

    private static void registerHealthchecks(DropwizardLuceneDeepSearchConfig configuration,
                                             Environment environment) {
        DummyHealthcheck dummy = new DummyHealthcheck("Dummy template: %s");
        environment.healthChecks().register("dummy", dummy);
    }

    @Override
    public void initialize(Bootstrap<DropwizardLuceneDeepSearchConfig> bootstrap) {
        // TODO
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
