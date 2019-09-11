package com.alexsmaliy.dl4s.api.appconfig;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableApplicationConfiguration.Builder.class)
public interface ApplicationConfiguration {

    String template();

    @Value.Default
    default String defaultName() {
        return "Stranger";
    }

    @Value.Default
    default LuceneConfiguration lucene() {
        return new ImmutableLuceneConfiguration.Builder().build();
    }

}
