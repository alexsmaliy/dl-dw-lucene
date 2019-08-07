package solutions.bloaty.tuts.dw.deepsearch.api.appconfig;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.dropwizard.Configuration;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableDropwizardLuceneDeepSearchConfig.Builder.class)
public abstract class DropwizardLuceneDeepSearchConfig extends Configuration {
    public abstract String template();

    @Value.Default
    public String defaultName() {
        return "Stranger";
    }
}
