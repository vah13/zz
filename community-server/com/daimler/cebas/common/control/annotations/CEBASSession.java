/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.stereotype.Component
 */
package com.daimler.cebas.common.control.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
@Retention(value=RetentionPolicy.RUNTIME)
public @interface CEBASSession {
}
