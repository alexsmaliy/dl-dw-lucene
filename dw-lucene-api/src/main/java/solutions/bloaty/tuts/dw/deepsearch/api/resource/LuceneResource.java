package solutions.bloaty.tuts.dw.deepsearch.api.resource;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.params.NonEmptyStringParam;
import solutions.bloaty.tuts.dw.deepsearch.api.messages.Saying;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/lucene")
@Produces(MediaType.APPLICATION_JSON)
public interface LuceneResource {

    @GET
    @Path("/ping")
    @Timed
    boolean ping();

}
