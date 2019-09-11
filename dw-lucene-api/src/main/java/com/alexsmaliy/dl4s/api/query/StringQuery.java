package com.alexsmaliy.dl4s.api.query;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableStringQuery.class)
@JsonDeserialize(as = ImmutableStringQuery.class)
public interface StringQuery extends VisitableBaseQuery {

    String query();

    @Value.Check
    default void check() {
        Preconditions.checkArgument(
            !Strings.isNullOrEmpty(query()),
            "Query string cannot be empty!");
    }

    @Override
    default <T> T accept(QueryVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
