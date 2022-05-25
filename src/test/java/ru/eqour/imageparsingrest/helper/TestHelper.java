package ru.eqour.imageparsingrest.helper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

public class TestHelper {

    public static final String REST_CONTROLLER_MAPPING = "/api";

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

    public static int[][] sortMinXMinY(int[][] values) {
        return Arrays.stream(values)
                .sorted(Comparator.comparing((int[] p) -> p[0])
                        .thenComparing(p -> p[1]))
                .toArray(int[][]::new);
    }

    public static boolean compareDouble2Array(double[][] arr1, double[][] arr2, double delta) {
        if (arr1.length == arr2.length) {
            for (int i = 0; i < arr1.length; i++) {
                if (arr1[i].length == arr2[i].length) {
                    for (int j = 0; j < arr1[i].length; j++) {
                        if (Math.abs(arr1[i][j] - arr2[i][j]) >= delta) {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }
}
