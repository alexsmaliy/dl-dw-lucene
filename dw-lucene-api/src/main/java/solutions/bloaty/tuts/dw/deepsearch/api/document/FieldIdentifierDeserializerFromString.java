package solutions.bloaty.tuts.dw.deepsearch.api.document;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.google.common.base.Splitter;

import java.io.IOException;
import java.util.List;

public class FieldIdentifierDeserializerFromString extends FromStringDeserializer<FieldIdentifier> {
    private static final Splitter DEFAULT_SPLITTER =
        Splitter.on('.').omitEmptyStrings().trimResults();

    protected FieldIdentifierDeserializerFromString() {
        super(FieldIdentifier.class);
    }

    @Override
    public FieldIdentifier deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonToken token = parser.getCurrentToken();
        if (token == JsonToken.VALUE_STRING) {
            return buildFromString(parser.getText());
        }
        return (FieldIdentifier) context.handleUnexpectedToken(
            _valueClass,
            token,
            parser,
            "Cannot deserialize instance of `%s` out of %s token, expected %s.",
            FieldIdentifier.class.getName(),
            token,
            JsonToken.VALUE_STRING);
    }

    @Override
    protected FieldIdentifier _deserialize(String value, DeserializationContext ctxt) {
        return buildFromString(value);
    }

    private static FieldIdentifier buildFromString(String value) {
        List<String> components = DEFAULT_SPLITTER.splitToList(value);
        return ImmutableFieldIdentifier.builder().addAllComponents(components).build();
    }
}
