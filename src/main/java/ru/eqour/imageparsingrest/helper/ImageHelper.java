package ru.eqour.imageparsingrest.helper;

import org.apache.tomcat.util.codec.binary.Base64;
import ru.eqour.imageparsingrest.exception.ConvertImageException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageHelper {

    public static String convertToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", stream);
        String base64 = Base64.encodeBase64String(stream.toByteArray());
        stream.close();
        return base64;
    }

    public static BufferedImage convertToImage(String base64Image) throws IOException {
        byte[] bytes = Base64.decodeBase64(base64Image);
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        BufferedImage image = ImageIO.read(stream);
        stream.close();
        return image;
    }

}
