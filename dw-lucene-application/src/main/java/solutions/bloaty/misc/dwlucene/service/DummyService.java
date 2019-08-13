package solutions.bloaty.misc.dwlucene.service;

import io.dropwizard.jersey.params.NonEmptyStringParam;
import solutions.bloaty.tuts.dw.deepsearch.api.message.ImmutableSaying;
import solutions.bloaty.tuts.dw.deepsearch.api.message.Saying;
import solutions.bloaty.tuts.dw.deepsearch.api.resource.DummyResource;

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
    public Saying sayHello(NonEmptyStringParam maybeName) {
        String content = String.format(template, maybeName.get().orElse(defaultName));
        return ImmutableSaying.builder()
            .id(idCounter.incrementAndGet())
            .content(content)
            .build();
    }
}
