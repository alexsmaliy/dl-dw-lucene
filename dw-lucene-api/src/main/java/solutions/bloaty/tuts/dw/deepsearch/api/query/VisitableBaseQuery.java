package solutions.bloaty.tuts.dw.deepsearch.api.query;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.immutables.value.Value;
import solutions.bloaty.tuts.dw.deepsearch.api.ApiConstants;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.WRAPPER_OBJECT
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = StringQuery.class, name = "string-query"),
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
