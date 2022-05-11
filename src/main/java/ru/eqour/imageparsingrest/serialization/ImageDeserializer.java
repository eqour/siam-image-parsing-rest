package ru.eqour.imageparsingrest.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.eqour.imageparsingrest.helper.ImageHelper;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageDeserializer extends StdDeserializer<BufferedImage> {

    public ImageDeserializer() {
        this(null);
    }

    protected ImageDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public BufferedImage deserialize(JsonParser jsonParser,
                                     DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.readValueAsTree();
        String base64 = node.get("base64").textValue();
        return ImageHelper.convertToImage(base64);
    }
}
