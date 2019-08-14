package solutions.bloaty.tuts.dw.deepsearch.api.resource;

import com.codahale.metrics.annotation.Timed;
import solutions.bloaty.tuts.dw.deepsearch.api.document.IndexableDocument;
import solutions.bloaty.tuts.dw.deepsearch.api.query.StringQuery;
import solutions.bloaty.tuts.dw.deepsearch.api.response.HitCollection;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;

@Path("lucene")
@Produces(MediaType.APPLICATION_JSON)
public interface LuceneResource extends Resource {

    @GET
    @Path("ping")
    @Timed
    boolean ping();

    @GET
    @Path("list-indexes")
    @Timed
    Set<String> listIndexes();

    @POST
    @Path("index")
    @Timed
    long index(IndexableDocument document);

    @POST
    @Path("query")
    @Timed
    HitCollection query(StringQuery stringQuery);

}
