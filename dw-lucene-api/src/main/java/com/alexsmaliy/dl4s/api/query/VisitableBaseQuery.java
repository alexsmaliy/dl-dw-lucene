package com.alexsmaliy.dl4s.api.query;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import org.immutables.value.Value;
import com.alexsmaliy.dl4s.api.ApiConstants;

@JsonTypeInfo(
    use = Id.NAME,
    include = As.WRAPPER_OBJECT
)
@JsonSubTypes({
    @Type(value = StringQuery.class, name = "string-query"),
})
public interface VisitableBaseQuery {

    @Value.Default
    default int maxResults() {
        return ApiConstants.Defaults.MAX_RESULTS_TO_RETURN;
    }

    /**
     * Extending classes/interfaces must override this method, returning
     * {@code visitor.visit(this)}, in order for the visitor to know the
     * runtime type of the query and invoke the matching method.
     */
    <T> T accept(QueryVisitor<T> visitor);

}
