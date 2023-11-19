package annk.aims.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class VnPhoneValidator implements ConstraintValidator<VnPhone, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && (s.matches("0[0-9]{2,10}") || s.matches("\\+84[0-9]{2,10}") || s.matches("84[0-9]{2,10}"));
    }
}
