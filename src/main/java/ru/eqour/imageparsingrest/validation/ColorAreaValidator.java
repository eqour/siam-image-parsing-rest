package ru.eqour.imageparsingrest.validation;

import org.springframework.validation.Errors;
import ru.eqour.imageparsingrest.model.ColorAreaRequest;

public class ColorAreaValidator extends ColorEntireValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ColorAreaRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        super.validate(target, errors);

        ColorAreaRequest request = (ColorAreaRequest) target;

        Integer minX = request.getMinX();
        Integer minY = request.getMinY();
        Integer maxX = request.getMaxX();
        Integer maxY = request.getMaxY();

        if (minX == null) {
            errors.rejectValue("minX", "", "значение не должно быть null");
        } else if (minX < 0) {
            errors.rejectValue("minX", "", "значение должно быть не меньше 0");
        }

        if (minY == null) {
            errors.rejectValue("minY", "", "значение не должно быть null");
        } else if (minY < 0) {
            errors.rejectValue("minY", "", "значение должно быть не меньше 0");
        }

        if (maxX == null) {
            errors.rejectValue("maxX", "", "значение не должно быть null");
        } else if (request.getImage() != null && maxX > request.getImage().getWidth() - 1) {
            errors.rejectValue("maxX", "", "значение должно быть меньше ширины изображения");
        }

        if (maxY == null) {
            errors.rejectValue("maxY", "", "значение не должно быть null");
        } else if (request.getImage() != null && maxY > request.getImage().getHeight() - 1) {
            errors.rejectValue("maxY", "", "значение должно быть меньше высоты изображения");
        }
    }
}
