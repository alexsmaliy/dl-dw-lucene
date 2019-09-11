package com.alexsmaliy.dl4s.api.document;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(
    use = Id.NAME,
    include = As.WRAPPER_OBJECT
)
@JsonSubTypes({
    @Type(value = IndexableField.class, name = IndexableField.DEFAULT_NAME),
    @Type(value = TitleField.class, name = TitleField.DEFAULT_NAME),
})
public interface Field {

    String name();

    String content();

}
