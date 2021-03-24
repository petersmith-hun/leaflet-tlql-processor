package hu.psprog.leaflet.tlql.it.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import hu.psprog.leaflet.tlql.ir.DSLTimestampValue;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * JSON deserializer for reading {@link DSLTimestampValue} objects for test scenarios.
 *
 * @author Peter Smith
 */
public class DSLTimestampValueDeserializer extends StdDeserializer<DSLTimestampValue> {

    public DSLTimestampValueDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public DSLTimestampValue deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {

        ObjectCodec objectCodec = parser.getCodec();
        JsonNode node = objectCodec.readTree(parser);

        DSLTimestampValue.IntervalType intervalType = DSLTimestampValue.IntervalType.valueOf(node.get("intervalType").asText());
        LocalDateTime leftOfSimpleDateTime = LocalDateTime.parse(node.get("leftOrSimple").asText());

        DSLTimestampValue timestampValue;
        if (intervalType == DSLTimestampValue.IntervalType.NONE) {
            timestampValue = new DSLTimestampValue(leftOfSimpleDateTime);
        } else {
            LocalDateTime rightDateTime = LocalDateTime.parse(node.get("right").asText());
            timestampValue = new DSLTimestampValue(intervalType, leftOfSimpleDateTime, rightDateTime);
        }

        return timestampValue;
    }
}
