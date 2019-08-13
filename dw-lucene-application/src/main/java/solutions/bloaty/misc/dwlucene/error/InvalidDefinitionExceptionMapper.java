package solutions.bloaty.misc.dwlucene.error;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.concurrent.ThreadLocalRandom;

@Provider
public class InvalidDefinitionExceptionMapper implements ExceptionMapper<InvalidDefinitionException> {
    Logger LOGGER = LoggerFactory.getLogger("request-error");

    @Override
    public Response toResponse(InvalidDefinitionException e) {
        // Analogous to LoggingExceptionMapper in DW, but without the opinionated conversion logic.
        long errorId = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        LOGGER.error(
            "Problem deserializing something. (message: {}) (tracking id: {})",
            e.getMessage(),
            errorId);
        return Response.status(Response.Status.BAD_REQUEST)
            .entity("Unable to interpret request. Cause: " + e.getMessage())
            .type(MediaType.TEXT_PLAIN_TYPE)
            .build();
    }
}
