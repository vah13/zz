/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.stereotype.Repository
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 */
package com.daimler.cebas.common.control.annotations;

import com.daimler.cebas.common.control.CEBASException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Lazy
@Transactional(propagation=Propagation.MANDATORY, rollbackFor={CEBASException.class})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface CEBASRepository {
}
