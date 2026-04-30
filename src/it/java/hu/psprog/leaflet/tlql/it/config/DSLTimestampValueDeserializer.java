package hu.psprog.leaflet.tlql.it.config;

import hu.psprog.leaflet.tlql.ir.DSLTimestampValue;
import tools.jackson.core.JsonParser;
import tools.jackson.core.ObjectReadContext;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

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
    public DSLTimestampValue deserialize(JsonParser parser, DeserializationContext ctxt) {

        ObjectReadContext objectCodec = parser.objectReadContext();
        JsonNode node = objectCodec.readTree(parser);

        DSLTimestampValue.IntervalType intervalType = DSLTimestampValue.IntervalType.valueOf(node.get("intervalType").asString());
        LocalDateTime leftOfSimpleDateTime = LocalDateTime.parse(node.get("leftOrSimple").asString());

        DSLTimestampValue timestampValue;
        if (intervalType == DSLTimestampValue.IntervalType.NONE) {
            timestampValue = new DSLTimestampValue(leftOfSimpleDateTime);
        } else {
            LocalDateTime rightDateTime = LocalDateTime.parse(node.get("right").asString());
            timestampValue = new DSLTimestampValue(intervalType, leftOfSimpleDateTime, rightDateTime);
        }

        return timestampValue;
    }
}
