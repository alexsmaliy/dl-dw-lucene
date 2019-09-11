package com.alexsmaliy.dl4s.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.immutables.value.Value;

import java.util.Optional;
import java.util.OptionalLong;

@Value.Immutable
@JsonSerialize(as = ImmutableIndexingResponse.class)
public interface IndexingResponse {

        @JsonInclude(Include.NON_ABSENT)
        Optional<String> error();

        @JsonInclude(Include.NON_ABSENT)
        @JsonSerialize(contentUsing = NumberSerializers.LongSerializer.class)
        OptionalLong sequenceNumber();

}
