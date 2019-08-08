package solutions.bloaty.tuts.dw.deepsearch.api.appconfig;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.dropwizard.Configuration;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableDeepLearningForSearchConfiguration.Builder.class)
public abstract class DeepLearningForSearchConfiguration extends Configuration {
    public abstract String template();

    @Value.Default
    public String defaultName() {
        return "Stranger";
    }

    @Value.Default
    public LuceneConfiguration lucene() {
        return new ImmutableLuceneConfiguration.Builder().build();
    }

    @Override
    @Value.Default
    public String toString() {
        return super.toString();
    }
}
