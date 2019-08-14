package solutions.bloaty.tuts.dw.deepsearch.api.document;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableIndexableField.class)
@JsonDeserialize(as = ImmutableIndexableField.class)
public interface IndexableField extends Field {
    String DEFAULT_NAME = "indexable-field";

    @Override
    default String name() {
        return DEFAULT_NAME;
    }

    @Value.Parameter
    FieldIdentifier fieldIdentifier();

    @Override
    @Value.Parameter
    String content();
}
