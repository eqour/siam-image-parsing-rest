package ru.eqour.imageparsingrest.validation;

import org.springframework.validation.Errors;
import ru.eqour.imageparsingrest.model.ColorPointRequest;

import java.awt.image.BufferedImage;

public class ColorPointValidator extends ColorValidator {

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

        BufferedImage image = request.getImage();
        if (image != null && x != null && x >= image.getWidth())
            errors.rejectValue("x", "", "значение должно быть меньше ширины изображения");

        if (image != null && y != null && y >= image.getHeight())
            errors.rejectValue("y", "", "значение должно быть меньше высоты изображения");
    }
}
