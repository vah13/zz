/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.SafeStringValidator
 *  javax.validation.Constraint
 *  javax.validation.Payload
 */
package com.daimler.cebas.common.control.annotations;

import com.daimler.cebas.common.control.SafeStringValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy={SafeStringValidator.class})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface SafeString {
    public String message() default "String contains invalid characters";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
