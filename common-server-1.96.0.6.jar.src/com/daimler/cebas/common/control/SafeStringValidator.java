/*    */ package com.daimler.cebas.common.control;
/*    */ 
/*    */ import com.daimler.cebas.common.control.annotations.SafeString;
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import org.springframework.web.util.HtmlUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SafeStringValidator
/*    */   implements ConstraintValidator<SafeString, String>
/*    */ {
/*    */   public void initialize(SafeString string) {}
/*    */   
/*    */   public boolean isValid(String string, ConstraintValidatorContext cxt) {
/* 23 */     return (string == null) ? true : string
/* 24 */       .equals(HtmlUtils.htmlEscape(string));
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\SafeStringValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */