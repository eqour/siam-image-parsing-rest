package ru.eqour.imageparsingrest.validation;

import org.springframework.validation.Errors;
import ru.eqour.imageparsingrest.model.ConvertModifyRequest;

public class ConvertModifyValidator extends ConvertValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ConvertModifyRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        super.validate(target, errors);

        ConvertModifyRequest request = (ConvertModifyRequest) target;

        if (request.getDx() == null) {
            errors.rejectValue("dx", "", "значение не должно быть null");
        }

        if (request.getDy() == null) {
            errors.rejectValue("dy", "", "значение не должно быть null");
        }
    }
}
