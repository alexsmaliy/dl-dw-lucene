package solutions.bloaty.tuts.dw.deepsearch.api.appconfig;

import io.dropwizard.Configuration;
import org.immutables.value.Value;

@Value.Immutable
public abstract class DropwizardLuceneDeepSearchConfig extends Configuration {
    public abstract String template();

    @Value.Default
    public String defaultName() {
        return "Stranger";
    }
}
