package com.alexsmaliy.dl4s.api.response;

import com.alexsmaliy.dl4s.api.document.Field;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableHit.class)
@JsonDeserialize(as = ImmutableHit.class)
public interface Hit {

    @JsonSerialize(using = NumberSerializers.FloatSerializer.class)
    float score();

    List<Field> fields();

}
