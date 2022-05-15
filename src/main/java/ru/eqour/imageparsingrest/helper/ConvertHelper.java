package ru.eqour.imageparsingrest.helper;

import java.util.function.Function;

public class ConvertHelper {

    public static double[][] processPoints(double[][] points, Function<double[], double[]> processor) {
        double[][] result = new double[points.length][];
        for (int i = 0; i < points.length; i++) {
            result[i] = processor.apply(points[i]);
        }
        return result;
    }

}
