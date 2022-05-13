package ru.eqour.imageparsingrest.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.eqour.imageparsingrest.model.PerspectiveRequest;

import java.util.HashSet;
import java.util.Set;

@Component
public class PerspectiveValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PerspectiveRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PerspectiveRequest request = (PerspectiveRequest) target;

        Integer height = request.getOutputHeight();
        Integer width = request.getOutputWidth();

        if (height == null) {
            errors.rejectValue("outputHeight", "", "значение не должно быть null");
        } else if (height < 1 || height > 65536) {
            errors.rejectValue("outputHeight", "", "значение должно быть в диапазоне от 1 до 65536");
        }

        if (width == null) {
            errors.rejectValue("outputWidth", "", "значение не должно быть null");
        } else if (width < 1 || width > 65536) {
            errors.rejectValue("outputWidth", "", "значение должно быть в диапазоне от 1 до 65536");
        }

        if (request.getImage() == null)
            errors.rejectValue("image", "", "значение не должно быть null");

        if (request.getPoints() == null)
            errors.rejectValue("points", "", "значение не должно быть null");

        if (request.getPoints() != null && request.getImage() != null
                && !validateInputPoints(request.getPoints(),
                request.getImage().getWidth(), request.getImage().getHeight())) {
            errors.rejectValue("points", "", "значение не должно быть null");
        }
    }

    private boolean validateInputPoints(int[][] points, int width, int height) {
        if (points == null || points.length != 4) {
            return false;
        }
        Set<Integer> pointsHash = new HashSet<>();
        for (int[] point : points) {
            if (point == null
                    || point.length != 2
                    || point[0] < 0
                    || point[0] >= Math.min(width, 0x10000)
                    || point[1] < 0
                    || point[1] >= Math.min(height, 0x10000)) {
                return false;
            } else {
                pointsHash.add(getHashByPoint(point[0], point[1]));
            }
        }
        return pointsHash.size() == 4;
    }

    private int getHashByPoint(int x, int y) {
        return (x << 16) | y;
    }
}
