package ru.eqour.imageparsingrest.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.awt.*;
import java.io.IOException;

public class ColorSerializer extends StdSerializer<Color> {

    public ColorSerializer() {
        this(null);
    }

    protected ColorSerializer(Class<Color> t) {
        super(t);
    }

    @Override
    public void serialize(Color color, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("red", Integer.toString(color.getRed()));
        jsonGenerator.writeStringField("green", Integer.toString(color.getGreen()));
        jsonGenerator.writeStringField("blue", Integer.toString(color.getBlue()));
        jsonGenerator.writeEndObject();
    }
}
