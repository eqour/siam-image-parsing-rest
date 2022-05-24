package ru.eqour.imageparsingrest.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.eqour.imageparsing.AxisConverter;
import ru.eqour.imageparsing.ColorSelector;
import ru.eqour.imageparsing.DataSmoother;
import ru.eqour.imageparsing.PerspectiveCorrector;
import ru.eqour.imageparsingrest.config.SwaggerConfiguration;
import ru.eqour.imageparsingrest.helper.ConvertHelper;
import ru.eqour.imageparsingrest.helper.ImageHelper;
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
@Api(tags = {SwaggerConfiguration.IPR_CONTROLLER_TAG})
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

    @PostMapping("/image")
    @ApiOperation(value = "Загрука изображения")
    public ImageResponse image(@Valid @RequestBody ImageRequest request) {
        long imageId = counter.incrementAndGet();
        imageService.saveImage(imageId, ImageHelper.convertToImage(request.getImage()));
        return new ImageResponse(imageId);
    }

    @PostMapping("/perspective")
    @ApiOperation(value = "Корреция перспективы изображения")
    public PerspectiveResponse perspective(@Valid @RequestBody PerspectiveRequest request) {
        BufferedImage inputImage = ImageHelper.convertToImage(request.getImage());
        BufferedImage outputImage = PerspectiveCorrector.correct(inputImage, request.getPoints(),
                request.getOutputWidth(), request.getOutputHeight());
        long imageId = counter.incrementAndGet();
        imageService.saveImage(imageId, outputImage);
        return new PerspectiveResponse(imageId, ImageHelper.convertToBase64(outputImage));
    }

    @PostMapping("/color/entire")
    @ApiOperation(value = "Поиск пикселей по цвету у всего изображения")
    public ColorResponse color(@Valid @RequestBody ColorEntireRequest request) {
        BufferedImage inputImage = imageService.getImageById(request.getImageId());
        int[][] pixels = ColorSelector.select(inputImage, request.getColor().toColor(), request.getColorDifference());
        return new ColorResponse(pixels);
    }

    @PostMapping("/color/area")
    @ApiOperation(value = "Поиск пикселей по цвету в области изображения")
    public ColorResponse color(@Valid @RequestBody ColorAreaRequest request) {
        BufferedImage inputImage = imageService.getImageById(request.getImageId());
        int[][] pixels = ColorSelector.select(inputImage, request.getColor().toColor(), request.getColorDifference(),
                request.getMinX(), request.getMinY(), request.getMaxX(), request.getMaxY());
        return new ColorResponse(pixels);
    }

    @PostMapping("/color/point")
    @ApiOperation(value = "Поиск пикселей по заданной точке на изображении методом заливки")
    public ColorResponse color(@Valid @RequestBody ColorPointRequest request) {
        BufferedImage inputImage = imageService.getImageById(request.getImageId());
        int[][] pixels = ColorSelector.select(inputImage, request.getX(), request.getY(),
                request.getColorDifference(), request.getSearchRadius());
        return new ColorResponse(pixels);
    }

    @PostMapping("/smooth")
    @ApiOperation(value = "Сглаживание координат точек")
    public SmoothResponse smooth(@Valid @RequestBody SmoothRequest request) {
        Map.Entry<Integer, int[][]> smoothingResult = DataSmoother.thinPoints(request.getPoints(),
                request.getMaxIteration());
        return new SmoothResponse(smoothingResult.getKey(), smoothingResult.getValue());
    }

    @PostMapping("/convert/area")
    @ApiOperation(value = "Преобразование координат по заданным областям")
    public ConvertResponse convert(@Valid @RequestBody ConvertAreaRequest request) {
        return new ConvertResponse(ConvertHelper.processPoints(request.getPoints(),
                p -> AxisConverter.convert(p,
                        request.getSrcSX(), request.getSrcSY(), request.getSrcEX(), request.getSrcEY(),
                        request.getDstSX(), request.getDstSY(), request.getDstEX(), request.getDstEY()))
        );
    }

    @PostMapping("/convert/invertAxisY")
    @ApiOperation(value = "Перенос оси Y на другую позицию")
    public ConvertResponse convertInvertAxisY(@Valid @RequestBody ConvertInvertAxisRequest request) {
        return new ConvertResponse(ConvertHelper.processPoints(request.getPoints(),
                p -> AxisConverter.invertAxisY(p, request.getInvertedAxisPosition()))
        );
    }

    @PostMapping("/convert/invertAxisX")
    @ApiOperation(value = "Перенос оси X на другую позицию")
    public ConvertResponse convertInvertAxisX(@Valid @RequestBody ConvertInvertAxisRequest request) {
        return new ConvertResponse(ConvertHelper.processPoints(request.getPoints(),
                p -> AxisConverter.invertAxisX(p, request.getInvertedAxisPosition()))
        );
    }

    @PostMapping("/convert/transfer")
    @ApiOperation(value = "Параллельный перенос")
    public ConvertResponse convertTransfer(@Valid @RequestBody ConvertModifyRequest request) {
        return new ConvertResponse(ConvertHelper.processPoints(request.getPoints(),
                p -> AxisConverter.parallelTransfer(p, request.getDx(), request.getDy()))
        );
    }

    @PostMapping("/convert/invert")
    @ApiOperation(value = "Инвертирование координат")
    public ConvertResponse convertInvert(@Valid @RequestBody ConvertInvertRequest request) {
        return new ConvertResponse(ConvertHelper.processPoints(request.getPoints(),
                p -> AxisConverter.invert(p, request.getInvertByX(), request.getInvertByY()))
        );
    }

    @PostMapping("/convert/stretch")
    @ApiOperation(value = "Растяжение или сужение координат")
    public ConvertResponse convertStretch(@Valid @RequestBody ConvertModifyRequest request) {
        return new ConvertResponse(ConvertHelper.processPoints(request.getPoints(),
                p -> AxisConverter.stretch(p, request.getDx(), request.getDy()))
        );
    }

    @InitBinder
    private void bindValidator(WebDataBinder binder) {
        if (binder.getTarget() == null) return;
        List<Validator> validators = new ArrayList<>(Arrays.asList(
                new ImageValidator(),
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
