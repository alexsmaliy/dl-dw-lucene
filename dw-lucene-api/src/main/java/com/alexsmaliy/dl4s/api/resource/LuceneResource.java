package com.alexsmaliy.dl4s.api.resource;

import com.alexsmaliy.dl4s.api.document.Field;
import com.alexsmaliy.dl4s.api.document.ImmutableTitleField;
import com.alexsmaliy.dl4s.api.document.IndexableDocument;
import com.alexsmaliy.dl4s.api.query.VisitableBaseQuery;
import com.alexsmaliy.dl4s.api.response.HitCollection;
import com.alexsmaliy.dl4s.api.response.IndexingResponse;
import com.codahale.metrics.annotation.Timed;

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
    @Path("test")
    default Field test() {
        return ImmutableTitleField.builder().content("moo").build();
    }

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
    IndexingResponse index(IndexableDocument document);

    @POST
    @Path("query")
    @Timed
    HitCollection query(VisitableBaseQuery query);

}
