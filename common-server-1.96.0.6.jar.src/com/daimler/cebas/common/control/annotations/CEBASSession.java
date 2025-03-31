package com.daimler.cebas.common.control.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
@Retention(RetentionPolicy.RUNTIME)
public @interface CEBASSession {}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\annotations\CEBASSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */