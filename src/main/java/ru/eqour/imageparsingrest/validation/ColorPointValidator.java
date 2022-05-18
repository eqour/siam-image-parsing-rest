package ru.eqour.imageparsingrest.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.eqour.imageparsingrest.model.ColorPointRequest;
import ru.eqour.imageparsingrest.service.ImageDataCacheService;

import java.awt.image.BufferedImage;

@Component
public class ColorPointValidator extends ColorValidator {

    private final ImageDataCacheService imageService;

    @Autowired
    public ColorPointValidator(ImageDataCacheService imageService) {
        this.imageService = imageService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ColorPointRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        super.validate(target, errors);

        ColorPointRequest request = (ColorPointRequest) target;

        Integer x = request.getX();
        Integer y = request.getY();
        Integer radius = request.getSearchRadius();
        BufferedImage image = request.getImageId() == null ? null : imageService.getImageById(request.getImageId());

        if (x == null) {
            errors.rejectValue("x", "", "значение не должно быть null");
        } else if (x < 0) {
            errors.rejectValue("x", "", "значение должно быть не меньше 0");
        }

        if (y == null) {
            errors.rejectValue("y", "", "значение не должно быть null");
        } else if (y < 0) {
            errors.rejectValue("y", "", "значение должно быть не меньше 0");
        }

        if (radius == null) {
            errors.rejectValue("searchRadius", "", "значение не должно быть null");
        } else if (radius <= 0) {
            errors.rejectValue("searchRadius", "", "значение должно быть больше 0");
        }

        if (image != null && x != null && x >= image.getWidth())
            errors.rejectValue("x", "", "значение должно быть меньше ширины изображения");

        if (image != null && y != null && y >= image.getHeight())
            errors.rejectValue("y", "", "значение должно быть меньше высоты изображения");
    }
}
