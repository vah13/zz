/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.annotations.SafeString
 *  javax.validation.ConstraintValidator
 *  javax.validation.ConstraintValidatorContext
 *  org.springframework.web.util.HtmlUtils
 */
package com.daimler.cebas.common.control;

import com.daimler.cebas.common.control.annotations.SafeString;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.web.util.HtmlUtils;

public class SafeStringValidator
implements ConstraintValidator<SafeString, String> {
    public void initialize(SafeString string) {
    }

    public boolean isValid(String string, ConstraintValidatorContext cxt) {
        return string == null ? true : string.equals(HtmlUtils.htmlEscape((String)string));
    }
}
