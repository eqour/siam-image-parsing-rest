package ru.eqour.imageparsingrest.helper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageTestHelper {

    public BufferedImage loadBufferedImageFromResources(String name) {
        try {
            ClassLoader loader = getClass().getClassLoader();
            URL resource = loader.getResource(name);
            if (resource != null) {
                return ImageIO.read(resource);
            } else {
                throw new RuntimeException("Не удалось найти ресурс " + name);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static boolean compareBufferedImages(BufferedImage image1, BufferedImage image2) {
        if (image1.getWidth() == image2.getWidth() && image1.getHeight() == image2.getHeight()) {
            for (int i = 0; i < image1.getHeight(); i++) {
                for (int j = 0; j < image1.getWidth(); j++) {
                    Color c1 = new Color(image1.getRGB(j, i));
                    Color c2 = new Color(image2.getRGB(j, i));
                    if (c1.getRed() != c2.getRed() || c1.getGreen() != c2.getGreen() || c1.getBlue() != c2.getBlue()) {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }
}
