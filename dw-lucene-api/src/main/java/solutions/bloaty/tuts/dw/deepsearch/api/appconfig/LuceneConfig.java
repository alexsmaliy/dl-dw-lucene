package solutions.bloaty.tuts.dw.deepsearch.api.appconfig;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.dropwizard.Configuration;
import org.immutables.value.Value;

import java.nio.file.Path;
import java.nio.file.Paths;

@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableLuceneConfig.Builder.class)
public interface LuceneConfig {
    default Path indexDir() {
        return Paths.get("indexes");
    }
}
