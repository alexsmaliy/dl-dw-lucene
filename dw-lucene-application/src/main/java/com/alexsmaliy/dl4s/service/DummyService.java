package com.alexsmaliy.dl4s.service;

import io.dropwizard.jersey.params.NonEmptyStringParam;
import com.alexsmaliy.dl4s.api.message.ImmutableSaying;
import com.alexsmaliy.dl4s.api.message.Saying;
import com.alexsmaliy.dl4s.api.resource.DummyResource;

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
