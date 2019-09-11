package com.alexsmaliy.dl4s.api.document;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableIndexableField.class)
@JsonDeserialize(as = ImmutableIndexableField.class)
@JsonTypeInfo(
    use = Id.NAME,
    include = As.WRAPPER_OBJECT
)
@JsonSubTypes({
    @Type(value = ImmutableTitleField.class, name = IndexableField.DEFAULT_NAME),
})
public interface IndexableField extends Field {

    String DEFAULT_NAME = "indexable-field";

    @Override
    default String name() {
        return DEFAULT_NAME;
    }

    @Value.Parameter
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = FieldIdentifierDeserializerFromString.class)
    FieldIdentifier fieldIdentifier();

    @Override
    @Value.Parameter
    String content();

}
