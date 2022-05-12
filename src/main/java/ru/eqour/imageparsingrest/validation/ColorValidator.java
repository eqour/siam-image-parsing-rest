package ru.eqour.imageparsingrest.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.eqour.imageparsingrest.model.ColorRequest;

import java.awt.image.BufferedImage;

public class ColorValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ColorRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ColorRequest request = (ColorRequest) target;

        Double colorDifference = request.getColorDifference();
        if (colorDifference == null) {
            errors.rejectValue("colorDifference", "", "значение не должно быть null");
        } else if (colorDifference < 0) {
            errors.rejectValue("colorDifference", "", "значение должно быть не меньше 0");
        }

        BufferedImage image = request.getImage();
        if (image == null) {
            errors.rejectValue("image", "", "значение не должно быть null");
        } else if (image.getWidth() > 65536 || image.getHeight() > 65536) {
            errors.rejectValue("image", "", "изображение не должно быть больше 65536 пикселей в ширину или высоту");
        }
    }
}
