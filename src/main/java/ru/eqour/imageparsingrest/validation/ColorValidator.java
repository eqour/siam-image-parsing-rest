package ru.eqour.imageparsingrest.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.eqour.imageparsingrest.model.ColorRequest;

@Component
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

        Long imageId = request.getImageId();
        if (imageId == null) {
            errors.rejectValue("imageId", "", "значение не должно быть null");
        } else if (imageId < 0) {
            errors.rejectValue("imageId", "", "значение должно быть не меньше 0");
        }
    }
}
