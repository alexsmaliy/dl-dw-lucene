package solutions.bloaty.tuts.dw.deepsearch.api.document;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableField.class)
@JsonDeserialize(as = ImmutableField.class)
public interface Field {
    @Value.Parameter
    String name();
}
