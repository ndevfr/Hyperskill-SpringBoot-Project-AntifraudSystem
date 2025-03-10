package antifraud.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CardNumberValidator implements ConstraintValidator<CardNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank() || value.length() != 16) {
            return false;
        }
        int luhCalculator = 0;
        for (int i = 0; i <= 15; i++) {
            int digit = Character.digit(value.charAt(i), 10);
            if (i % 2 == 0) {
                digit *= 2;
                digit = digit > 9 ? digit - 9 : digit;
                luhCalculator += digit;
            } else {
                luhCalculator += digit;
            }
        }
        return luhCalculator % 10 == 0;
    }
}