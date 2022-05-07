package ru.eqour.imageparsingrest.controller;

import org.springframework.web.bind.annotation.*;
import ru.eqour.imageparsing.PerspectiveCorrector;
import ru.eqour.imageparsingrest.helper.ImageHelper;
import ru.eqour.imageparsingrest.model.PerspectiveRequest;
import ru.eqour.imageparsingrest.model.PerspectiveResponse;

import javax.validation.Valid;
import java.awt.image.BufferedImage;

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

}
