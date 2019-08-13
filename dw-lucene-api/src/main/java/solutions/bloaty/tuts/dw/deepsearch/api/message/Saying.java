package solutions.bloaty.tuts.dw.deepsearch.api.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.common.base.Preconditions;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableSaying.class)
@JsonDeserialize(as = ImmutableSaying.class)
public interface Saying {

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(as = Long.class)
    long id();

    String content();

    @Value.Check
    default void check() {
        Preconditions.checkArgument(id() < 1000L && id() >= 0,
                "Saying ID must be between 0-999, instead was: %s", id());
    }
}
