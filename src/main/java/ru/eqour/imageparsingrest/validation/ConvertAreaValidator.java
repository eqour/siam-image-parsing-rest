package ru.eqour.imageparsingrest.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.eqour.imageparsingrest.model.ConvertAreaRequest;

@Component
public class ConvertAreaValidator extends ConvertValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ConvertAreaRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        super.validate(target, errors);

        ConvertAreaRequest request = (ConvertAreaRequest) target;

        Double srcSX = request.getSrcSX();
        Double srcSY = request.getSrcSY();
        Double srcEX = request.getSrcEX();
        Double srcEY = request.getSrcEY();
        Double dstSX = request.getDstSX();
        Double dstSY = request.getDstSY();
        Double dstEX = request.getDstEX();
        Double dstEY = request.getDstEY();

        if (srcSX == null) errors.rejectValue("srcSX", "", "значение не должно быть null");
        if (srcSY == null) errors.rejectValue("srcSY", "", "значение не должно быть null");
        if (srcEX == null) errors.rejectValue("srcEX", "", "значение не должно быть null");
        if (srcEY == null) errors.rejectValue("srcEY", "", "значение не должно быть null");
        if (dstSX == null) errors.rejectValue("dstSX", "", "значение не должно быть null");
        if (dstSY == null) errors.rejectValue("dstSY", "", "значение не должно быть null");
        if (dstEX == null) errors.rejectValue("dstEX", "", "значение не должно быть null");
        if (dstEY == null) errors.rejectValue("dstEY", "", "значение не должно быть null");

        if (srcEX != null && srcSX != null && srcEX - srcSX == 0) {
            errors.rejectValue("srcEX", "",
                    "разница между начальной и конечной позицией не должна быть равна 0");
            errors.rejectValue("srcSX", "",
                    "разница между начальной и конечной позицией не должна быть равна 0");
        }

        if (srcEY != null && srcSY != null && srcEY - srcSY == 0) {
            errors.rejectValue("srcEY", "",
                    "разница между начальной и конечной позицией не должна быть равна 0");
            errors.rejectValue("srcSY", "",
                    "разница между начальной и конечной позицией не должна быть равна 0");
        }

        if (dstEX != null & dstSX != null && dstEX - dstSX == 0) {
            errors.rejectValue("dstEX", "",
                    "разница между начальной и конечной позицией не должна быть равна 0");
            errors.rejectValue("dstSX", "",
                    "разница между начальной и конечной позицией не должна быть равна 0");
        }

        if (dstEY != null && dstSY != null && dstEY - dstSY == 0) {
            errors.rejectValue("dstEY", "",
                    "разница между начальной и конечной позицией не должна быть равна 0");
            errors.rejectValue("dstSY", "",
                    "разница между начальной и конечной позицией не должна быть равна 0");
        }
    }
}
