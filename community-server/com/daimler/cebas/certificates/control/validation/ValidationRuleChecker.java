/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.validation.ValidationError
 *  com.daimler.cebas.certificates.entity.Certificate
 */
package com.daimler.cebas.certificates.control.validation;

import com.daimler.cebas.certificates.control.validation.ValidationError;
import com.daimler.cebas.certificates.entity.Certificate;
import java.util.Optional;
import java.util.function.Predicate;

public class ValidationRuleChecker {
    private static final String BACK_SLASH_N = "\n";
    private static final String ONE_SPACE = " ";

    private ValidationRuleChecker() {
    }

    public static Optional<ValidationError> check(Predicate<Certificate> rule, Certificate certificate, String errorMessage, String messageId) {
        return rule.test(certificate) ? Optional.empty() : Optional.of(new ValidationError(certificate.getSubjectKeyIdentifier(), ONE_SPACE + errorMessage + BACK_SLASH_N, messageId, null));
    }

    public static Optional<ValidationError> check(Predicate<Certificate> rule, Certificate certificate, String errorMessage, String messageId, String[] messageArgs) {
        return rule.test(certificate) ? Optional.empty() : Optional.of(new ValidationError(certificate.getSubjectKeyIdentifier(), ONE_SPACE + errorMessage + BACK_SLASH_N, messageId, messageArgs));
    }
}
