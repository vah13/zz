package com.daimler.cebas.common.control.annotations;

import com.daimler.cebas.common.control.SafeStringValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = {SafeStringValidator.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface SafeString {
  String message() default "String contains invalid characters";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\annotations\SafeString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */