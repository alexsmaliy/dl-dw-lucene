package solutions.bloaty.misc.dwlucene.error;

import com.fasterxml.jackson.databind.JsonMappingException;
import solutions.bloaty.misc.dwlucene.logging.DefaultLoggers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.concurrent.ThreadLocalRandom;

public class JsonMappingExceptionMapper implements ExceptionMapper<JsonMappingException> {
    @Override
    public Response toResponse(JsonMappingException e) {
        // Analogous to LoggingExceptionMapper in DW, but without the opinionated conversion logic.
        long errorId = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        DefaultLoggers.REQUEST_ERROR_LOGGER.error(
            "Problem deserializing something. (path: {}) (message: {}) (tracking id: {})",
            e.getPathReference(),
            e.getMessage(),
            errorId);
        return Response.status(Response.Status.BAD_REQUEST)
            .entity("Unable to interpret request. Cause: " + e.getMessage())
            .type(MediaType.TEXT_PLAIN_TYPE)
            .build();
    }
}
