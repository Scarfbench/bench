package org.apache.struts.examples.mailreader2.validation;

import org.apache.struts.examples.mailreader2.dto.RegistrationForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, RegistrationForm> {

    @Override
    public boolean isValid(RegistrationForm form, ConstraintValidatorContext context) {
        if (form.getPassword() == null && form.getPassword2() == null) {
            return true;
        }

        if (form.getPassword() == null || form.getPassword2() == null) {
            return false;
        }

        return form.getPassword().equals(form.getPassword2());
    }
}
