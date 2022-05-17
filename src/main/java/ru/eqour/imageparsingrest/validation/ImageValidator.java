package ru.eqour.imageparsingrest.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.eqour.imageparsingrest.model.ImageRequest;

import java.awt.image.BufferedImage;

public class ImageValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ImageRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ImageRequest request = (ImageRequest) target;

        BufferedImage image = request.getImage();

        if (image == null) {
            errors.rejectValue("image", "", "значение не должно быть null");
        } else {
            if (image.getHeight() > 65536) {
                errors.rejectValue("image", "высота изображения не должна быть больше 65536");
            }
            if (image.getWidth() > 65536) {
                errors.rejectValue("image", "ширина изображения не должна быть больше 65536");
            }
        }
    }
}
