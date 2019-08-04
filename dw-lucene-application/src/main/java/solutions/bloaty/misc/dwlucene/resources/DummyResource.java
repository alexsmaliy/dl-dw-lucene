package solutions.bloaty.misc.dwlucene.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.params.NonEmptyStringParam;
import solutions.bloaty.tuts.dw.deepsearch.api.messages.ImmutableSaying;
import solutions.bloaty.tuts.dw.deepsearch.api.messages.Saying;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class DummyResource {

    private final AtomicLong idCounter;
    private final String template;
    private final String defaultName;

    public DummyResource(String template, String defaultName) {
        idCounter = new AtomicLong();
        this.template = template;
        this.defaultName = defaultName;
    }

    @GET
    @Timed
    public Saying sayHello(@QueryParam("name") NonEmptyStringParam maybeName) {
        String content = String.format(template, maybeName.get().orElse(defaultName));
        return ImmutableSaying.builder()
                              .id(idCounter.incrementAndGet())
                              .content(content)
                              .build();
    }
}
