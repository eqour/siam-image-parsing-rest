package ru.eqour.imageparsingrest.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.eqour.imageparsingrest.model.ConvertRequest;

import java.util.Arrays;

public class ConvertValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ConvertRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ConvertRequest request = (ConvertRequest) target;

        double[][] points = request.getPoints();
        if (points == null) {
            errors.rejectValue("points", "", "значение не должно быть null");
        } else if (Arrays.stream(points).anyMatch(p -> p == null || p.length != 2)) {
            errors.rejectValue("points", "",
                    "массив не должен содержать null, а длина вложенных массивов должна быть равна 2");
        }
    }
}
