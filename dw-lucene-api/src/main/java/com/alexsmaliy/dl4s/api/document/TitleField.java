package com.alexsmaliy.dl4s.api.document;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableTitleField.class)
@JsonDeserialize(as = ImmutableTitleField.class)
@JsonTypeInfo(
    use = Id.NAME,
    include = As.WRAPPER_OBJECT
)
@JsonSubTypes({
    @Type(value = ImmutableTitleField.class, name = TitleField.DEFAULT_NAME),
})
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

