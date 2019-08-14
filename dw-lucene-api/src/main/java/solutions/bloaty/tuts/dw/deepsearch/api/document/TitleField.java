package solutions.bloaty.tuts.dw.deepsearch.api.document;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableTitleField.class)
@JsonDeserialize(as = ImmutableTitleField.class)
public interface TitleField extends Field {
    String DEFAULT_NAME = "title";

    @Override
    default String name() {
        return DEFAULT_NAME;
    }

    @Override
    @Value.Parameter
    String content();
}
