package solutions.bloaty.misc.dwlucene.service;

import io.dropwizard.jersey.params.NonEmptyStringParam;
import solutions.bloaty.tuts.dw.deepsearch.api.messages.ImmutableSaying;
import solutions.bloaty.tuts.dw.deepsearch.api.messages.Saying;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.DummyResource;

import javax.ws.rs.QueryParam;
import java.util.concurrent.atomic.AtomicLong;

public class DummyService implements DummyResource {

    private final AtomicLong idCounter;
    private final String template;
    private final String defaultName;

    public DummyService(String template, String defaultName) {
        idCounter = new AtomicLong();
        this.template = template;
        this.defaultName = defaultName;
    }

    @Override
    public Saying sayHello(@QueryParam("name") NonEmptyStringParam maybeName) {
        String content = String.format(template, maybeName.get().orElse(defaultName));
        return ImmutableSaying.builder()
            .id(idCounter.incrementAndGet())
            .content(content)
            .build();
    }
}
