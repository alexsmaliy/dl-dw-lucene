package com.alexsmaliy.dl4s.api.document;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(using = ToStringSerializer.class)
@JsonDeserialize(using = FieldIdentifierDeserializerFromString.class)
public abstract class FieldIdentifier {

    private static final Joiner DEFAULT_JOINER = Joiner.on('.').skipNulls();

    abstract List<String> components();

    @Value.Check
    protected void check() {
        Preconditions.checkArgument(
            !components().isEmpty(),
            "Indexable field URI should be a nonempty list of strings!");
    }

    @Override
    public String toString() {
        return DEFAULT_JOINER.join(components());
    }

}
