package solutions.bloaty.tuts.dw.deepsearch.api.document;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;
import org.immutables.value.Value;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableFieldIdentifier.class)
@JsonDeserialize(as = ImmutableFieldIdentifier.class)
public interface FieldIdentifier {
    List<String> components();

    @Value.Lazy
    default Path getAsPath() {
        Path path = Paths.get("/");
        for (String component : components()) {
            path.resolve(component);
        }
        return path;
    }

    @Value.Check
    default void check() {
        Preconditions.checkArgument(
            !components().isEmpty(),
            "Indexable field URI should be a nonempty list of strings!");
    }
}
