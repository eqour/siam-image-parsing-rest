package ru.eqour.imageparsingrest.controller;

import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.eqour.imageparsing.AxisConverter;
import ru.eqour.imageparsing.ColorSelector;
import ru.eqour.imageparsing.DataSmoother;
import ru.eqour.imageparsing.PerspectiveCorrector;
import ru.eqour.imageparsingrest.model.*;
import ru.eqour.imageparsingrest.validation.*;

import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class Controller {

    @PostMapping("/perspective")
    public PerspectiveResponse perspective(@Valid @RequestBody PerspectiveRequest request) {
        BufferedImage inputImage = request.getImage();
        BufferedImage outputImage = PerspectiveCorrector.correct(inputImage, request.getPoints(),
                request.getOutputWidth(), request.getOutputHeight());
        return new PerspectiveResponse(outputImage);
    }

    @PostMapping("/color/entire")
    public ColorResponse color(@Valid @RequestBody ColorEntireRequest request) {
        BufferedImage inputImage = request.getImage();
        int[][] pixels = ColorSelector.select(inputImage, request.getColor(), request.getColorDifference());
        return new ColorResponse(pixels);
    }

    @PostMapping("/color/area")
    public ColorResponse color(@Valid @RequestBody ColorAreaRequest request) {
        BufferedImage inputImage = request.getImage();
        int[][] pixels = ColorSelector.select(inputImage, request.getColor(), request.getColorDifference(),
                request.getMinX(), request.getMinY(), request.getMaxX(), request.getMaxY());
        return new ColorResponse(pixels);
    }

    @PostMapping("/color/point")
    public ColorResponse color(@Valid @RequestBody ColorPointRequest request) {
        BufferedImage inputImage = request.getImage();
        int[][] pixels = ColorSelector.select(inputImage, request.getX(), request.getY(),
                request.getColorDifference(), request.getSearchRadius());
        return new ColorResponse(pixels);
    }

    @PostMapping("/smooth")
    public SmoothResponse smooth(@Valid @RequestBody SmoothRequest request) {
        Map.Entry<Integer, int[][]> smoothingResult = DataSmoother.thinPoints(request.getPoints(),
                request.getMaxIteration());
        return new SmoothResponse(smoothingResult.getKey(), smoothingResult.getValue());
    }

    @PostMapping("/convert")
    public ConvertResponse convert(@Valid @RequestBody ConvertRequest request) {
        double[][] originalPoints = request.getPoints();
        double[][] points = new double[request.getPoints().length][];
        for (int i = 0; i < points.length; i++) {
            points[i] = AxisConverter.convert(originalPoints[i],
                    request.getSrcSX(), request.getSrcSY(), request.getSrcEX(), request.getSrcEY(),
                    request.getDstSX(), request.getDstSY(), request.getDstEX(), request.getDstEY());
        }
        return new ConvertResponse(points);
    }

    @InitBinder
    private void bindValidator(WebDataBinder binder) {
        if (binder.getTarget() == null) return;
        List<Validator> validators = new ArrayList<>(Arrays.asList(
                new PerspectiveValidator(),
                new ColorValidator(),
                new ColorEntireValidator(),
                new ColorAreaValidator(),
                new ColorPointValidator(),
                new SmoothValidator(),
                new ConvertValidator()
        ));
        for (Validator validator : validators) {
            if (validator.supports(binder.getTarget().getClass()))
                binder.addValidators(validator);
        }
    }

}
