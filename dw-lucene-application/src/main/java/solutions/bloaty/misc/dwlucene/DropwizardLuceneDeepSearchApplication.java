package solutions.bloaty.misc.dwlucene;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import solutions.bloaty.misc.dwlucene.resources.DummyResource;
import solutions.bloaty.tuts.dw.deepsearch.api.appconfig.DropwizardLuceneDeepSearchConfig;

public class DropwizardLuceneDeepSearchApplication extends Application<DropwizardLuceneDeepSearchConfig> {
    public static void main(String[] args) throws Exception {
        new DropwizardLuceneDeepSearchApplication().run(args);
    }

    @Override
    public void run(DropwizardLuceneDeepSearchConfig configuration,
                    Environment environment) throws Exception {
        DummyResource dummyResource = new DummyResource(
            configuration.template(),
            configuration.defaultName()
        );
        environment.jersey().register(dummyResource);
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
