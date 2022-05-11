package ru.eqour.imageparsingrest.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.eqour.imageparsingrest.helper.ImageHelper;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageSerializer extends StdSerializer<BufferedImage> {

    public ImageSerializer() {
        this(null);
    }

    protected ImageSerializer(Class<BufferedImage> t) {
        super(t);
    }

    @Override
    public void serialize(BufferedImage bufferedImage, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        String base64 = ImageHelper.convertToBase64(bufferedImage);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("base64", base64);
        jsonGenerator.writeEndObject();
    }
}
