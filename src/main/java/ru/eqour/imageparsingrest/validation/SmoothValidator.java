package ru.eqour.imageparsingrest.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.eqour.imageparsingrest.model.SmoothRequest;

import java.util.Arrays;

@Component
public class SmoothValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SmoothRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SmoothRequest request = (SmoothRequest) target;

        Integer iteration = request.getMaxIteration();
        if (iteration == null) {
            errors.rejectValue("maxIteration", "", "значение не должно быть null");
        } else if (iteration < 1) {
            errors.rejectValue("maxIteration", "", "значение должно быть не меньше 1");
        }

        int[][] points = request.getPoints();
        if (points == null) {
            errors.rejectValue("points", "", "значение не должно быть null");
        } else if (Arrays.stream(points).anyMatch(p -> p == null || p.length != 2)) {
            errors.rejectValue("points", "",
                    "массив не должен содержать null, а длина вложенных массивов должна быть равна 2");
        }
    }
}
