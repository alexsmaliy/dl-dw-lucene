package com.alexsmaliy.dl4s.api.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableHitCollection.class)
@JsonDeserialize(as = ImmutableHitCollection.class)
public interface HitCollection {

    @Value.Parameter
    List<Hit> hits();

}
