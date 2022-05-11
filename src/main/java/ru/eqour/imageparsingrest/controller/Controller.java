package ru.eqour.imageparsingrest.controller;

import org.springframework.web.bind.annotation.*;
import ru.eqour.imageparsing.AxisConverter;
import ru.eqour.imageparsing.ColorSelector;
import ru.eqour.imageparsing.DataSmoother;
import ru.eqour.imageparsing.PerspectiveCorrector;
import ru.eqour.imageparsingrest.helper.ImageHelper;
import ru.eqour.imageparsingrest.model.*;

import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.util.Map;

@RestController
@RequestMapping("/")
public class Controller {

    @PostMapping("/perspective")
    public PerspectiveResponse perspective(@Valid @RequestBody PerspectiveRequest request) {
        BufferedImage inputImage = ImageHelper.convertToImage(request.getBase64Image());
        BufferedImage outputImage = PerspectiveCorrector.correct(inputImage, request.getPoints(),
                request.getOutputWidth(), request.getOutputHeight());
        return new PerspectiveResponse(ImageHelper.convertToBase64(outputImage));
    }

    @PostMapping("/color/simple")
    public ColorResponse color(@RequestBody ColorSimpleRequest request) {
        BufferedImage inputImage = ImageHelper.convertToImage(request.getBase64Image());
        int[][] pixels = ColorSelector.select(inputImage, request.getColor(), request.getColorDifference());
        return new ColorResponse(pixels);
    }

    @PostMapping("/color/border")
    public ColorResponse color(@RequestBody ColorBorderRequest request) {
        BufferedImage inputImage = ImageHelper.convertToImage(request.getBase64Image());
        int[][] pixels = ColorSelector.select(inputImage, request.getColor(), request.getColorDifference(),
                request.getMinX(), request.getMinY(), request.getMaxX(), request.getMaxY());
        return new ColorResponse(pixels);
    }

    @PostMapping("/color/point")
    public ColorResponse color(@RequestBody ColorPointRequest request) {
        BufferedImage inputImage = ImageHelper.convertToImage(request.getBase64Image());
        int[][] pixels = ColorSelector.select(inputImage, request.getX(), request.getY(),
                request.getColorDifference(), request.getSearchRadius());
        return new ColorResponse(pixels);
    }

    @PostMapping("/smooth")
    public SmoothResponse smooth(@RequestBody SmoothRequest request) {
        Map.Entry<Integer, int[][]> smoothingResult = DataSmoother.thinPoints(request.getPoints(),
                request.getMaxIteration());
        return new SmoothResponse(smoothingResult.getKey(), smoothingResult.getValue());
    }

    @PostMapping("/convert")
    public ConvertResponse convert(@RequestBody ConvertRequest request) {
        double[][] originalPoints = request.getPoints();
        double[][] points = new double[request.getPoints().length][];
        for (int i = 0; i < points.length; i++) {
            points[i] = AxisConverter.convert(originalPoints[i],
                    request.getSrcSX(), request.getSrcSY(), request.getSrcEX(), request.getSrcEY(),
                    request.getDstSX(), request.getDstSY(), request.getDstEX(), request.getDstEY());
        }
        return new ConvertResponse(points);
    }

}
