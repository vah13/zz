/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 */
package com.daimler.cebas.common.control.annotations;

import com.daimler.cebas.common.control.CEBASException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Lazy
@Transactional(propagation=Propagation.REQUIRED, rollbackFor={CEBASException.class})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface CEBASService {
}
