package solutions.bloaty.tuts.dw.deepsearch.api.resource;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.params.NonEmptyStringParam;
import solutions.bloaty.tuts.dw.deepsearch.api.message.Saying;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("hello-world")
@Produces(MediaType.APPLICATION_JSON)
public interface DummyResource extends Resource {

    @GET
    @Timed
    Saying sayHello(@QueryParam("name") NonEmptyStringParam maybeName);

}
