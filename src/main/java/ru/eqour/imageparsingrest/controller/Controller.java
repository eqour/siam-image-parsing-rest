package ru.eqour.imageparsingrest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.eqour.imageparsing.AxisConverter;
import ru.eqour.imageparsing.ColorSelector;
import ru.eqour.imageparsing.DataSmoother;
import ru.eqour.imageparsing.PerspectiveCorrector;
import ru.eqour.imageparsingrest.helper.ConvertHelper;
import ru.eqour.imageparsingrest.model.*;
import ru.eqour.imageparsingrest.service.ImageDataCacheService;
import ru.eqour.imageparsingrest.validation.*;

import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/")
public class Controller {

    private final AtomicLong counter = new AtomicLong();
    private final ImageDataCacheService imageService;
    private final ColorAreaValidator colorAreaValidator;
    private final ColorPointValidator colorPointValidator;

    @Autowired
    public Controller(ImageDataCacheService imageService,
                      ColorAreaValidator colorAreaValidator, ColorPointValidator colorPointValidator) {
        this.imageService = imageService;
        this.colorAreaValidator = colorAreaValidator;
        this.colorPointValidator = colorPointValidator;
    }


    @PostMapping("/perspective")
    public PerspectiveResponse perspective(@Valid @RequestBody PerspectiveRequest request) {
        BufferedImage inputImage = request.getImage();
        BufferedImage outputImage = PerspectiveCorrector.correct(inputImage, request.getPoints(),
                request.getOutputWidth(), request.getOutputHeight());
        long imageId = counter.incrementAndGet();
        imageService.saveImage(imageId, outputImage);
        return new PerspectiveResponse(imageId, outputImage);
    }

    @PostMapping("/color/entire")
    public ColorResponse color(@Valid @RequestBody ColorEntireRequest request) {
        BufferedImage inputImage = imageService.getImageById(request.getImageId());
        int[][] pixels = ColorSelector.select(inputImage, request.getColor(), request.getColorDifference());
        return new ColorResponse(pixels);
    }

    @PostMapping("/color/area")
    public ColorResponse color(@Valid @RequestBody ColorAreaRequest request) {
        BufferedImage inputImage = imageService.getImageById(request.getImageId());
        int[][] pixels = ColorSelector.select(inputImage, request.getColor(), request.getColorDifference(),
                request.getMinX(), request.getMinY(), request.getMaxX(), request.getMaxY());
        return new ColorResponse(pixels);
    }

    @PostMapping("/color/point")
    public ColorResponse color(@Valid @RequestBody ColorPointRequest request) {
        BufferedImage inputImage = imageService.getImageById(request.getImageId());
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

    @PostMapping("/convert/area")
    public ConvertResponse convert(@Valid @RequestBody ConvertAreaRequest request) {
        return new ConvertResponse(ConvertHelper.processPoints(request.getPoints(),
                p -> AxisConverter.convert(p,
                        request.getSrcSX(), request.getSrcSY(), request.getSrcEX(), request.getSrcEY(),
                        request.getDstSX(), request.getDstSY(), request.getDstEX(), request.getDstEY()))
        );
    }

    @PostMapping("/convert/invertAxisY")
    public ConvertResponse convertInvertAxisY(@Valid @RequestBody ConvertInvertAxisRequest request) {
        return new ConvertResponse(ConvertHelper.processPoints(request.getPoints(),
                p -> AxisConverter.invertAxisY(p, request.getInvertedAxisPosition()))
        );
    }

    @PostMapping("/convert/invertAxisX")
    public ConvertResponse convertInvertAxisX(@Valid @RequestBody ConvertInvertAxisRequest request) {
        return new ConvertResponse(ConvertHelper.processPoints(request.getPoints(),
                p -> AxisConverter.invertAxisX(p, request.getInvertedAxisPosition()))
        );
    }

    @PostMapping("/convert/transfer")
    public ConvertResponse convertTransfer(@Valid @RequestBody ConvertModifyRequest request) {
        return new ConvertResponse(ConvertHelper.processPoints(request.getPoints(),
                p -> AxisConverter.parallelTransfer(p, request.getDx(), request.getDy()))
        );
    }

    @PostMapping("/convert/invert")
    public ConvertResponse convertStretch(@Valid @RequestBody ConvertInvertRequest request) {
        return new ConvertResponse(ConvertHelper.processPoints(request.getPoints(),
                p -> AxisConverter.invert(p, request.getInvertByX(), request.getInvertByY()))
        );
    }

    @PostMapping("/convert/stretch")
    public ConvertResponse convertStretch(@Valid @RequestBody ConvertModifyRequest request) {
        return new ConvertResponse(ConvertHelper.processPoints(request.getPoints(),
                p -> AxisConverter.stretch(p, request.getDx(), request.getDy()))
        );
    }

    @InitBinder
    private void bindValidator(WebDataBinder binder) {
        if (binder.getTarget() == null) return;
        List<Validator> validators = new ArrayList<>(Arrays.asList(
                new PerspectiveValidator(),
                new ColorValidator(),
                new ColorEntireValidator(),
                colorAreaValidator,
                colorPointValidator,
                new SmoothValidator(),
                new ConvertValidator(),
                new ConvertAreaValidator(),
                new ConvertInvertAxisValidator(),
                new ConvertInvertValidator(),
                new ConvertModifyValidator()
        ));
        for (Validator validator : validators) {
            if (validator.supports(binder.getTarget().getClass()))
                binder.addValidators(validator);
        }
    }

}
