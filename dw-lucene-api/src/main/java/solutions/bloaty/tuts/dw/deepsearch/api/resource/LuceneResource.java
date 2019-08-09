package solutions.bloaty.tuts.dw.deepsearch.api.resource;

import com.codahale.metrics.annotation.Timed;
import solutions.bloaty.tuts.dw.deepsearch.api.query.StringQuery;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("lucene")
@Produces(MediaType.APPLICATION_JSON)
public interface LuceneResource extends Resource {

    @GET
    @Path("ping")
    @Timed
    boolean ping();

    @POST
    @Path("query")
    @Timed
    String query(StringQuery stringQuery);

}
