package solutions.bloaty.tuts.dw.deepsearch.api.appconfig;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.nio.file.Path;
import java.nio.file.Paths;

@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableLuceneConfiguration.Builder.class)
public interface LuceneConfiguration {
    default Path indexDir() {
        return Paths.get("indexes");
    }
}
