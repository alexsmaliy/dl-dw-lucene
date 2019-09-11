package com.alexsmaliy.dl4s.api.document;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableIndexableDocument.class)
@JsonDeserialize(as = ImmutableIndexableDocument.class)
public interface IndexableDocument {
    TitleField title();
    List<IndexableField> fields();

    @Value.Check
    default void check() {
        Preconditions.checkArgument(
            !fields().isEmpty(),
            "Can't index a document with zero fields!");
    }
}
