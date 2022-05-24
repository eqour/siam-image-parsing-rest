package ru.eqour.imageparsingrest.helper;

import org.apache.tomcat.util.codec.binary.Base64;
import ru.eqour.imageparsingrest.exception.ConvertImageException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ImageHelper {

    public static String convertToBase64(BufferedImage image) {
        try {
            if (image == null) throw new NullPointerException();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", stream);
            String base64 = Base64.encodeBase64String(stream.toByteArray());
            stream.close();
            return base64;
        } catch (Exception e) {
            throw new ConvertImageException("Не удалось конвертировать изображение", e);
        }
    }

    public static BufferedImage convertToImage(String base64Image) {
        try {
            if (base64Image == null) throw new NullPointerException();
            byte[] bytes = Base64.decodeBase64(base64Image);
            ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            BufferedImage image = ImageIO.read(stream);
            stream.close();
            return image;
        } catch (Exception e) {
            throw new ConvertImageException("Не удалось конвертировать изображение", e);
        }
    }

}
