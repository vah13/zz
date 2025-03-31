/*    */ package com.daimler.cebas.certificates.control.validation;
/*    */ 
/*    */ import com.daimler.cebas.certificates.entity.Certificate;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValidationRuleChecker
/*    */ {
/*    */   private static final String BACK_SLASH_N = "\n";
/*    */   private static final String ONE_SPACE = " ";
/*    */   
/*    */   public static Optional<ValidationError> check(Predicate<Certificate> rule, Certificate certificate, String errorMessage, String messageId) {
/* 37 */     return rule.test(certificate) ? Optional.<ValidationError>empty() : 
/* 38 */       Optional.<ValidationError>of(new ValidationError(certificate
/* 39 */           .getSubjectKeyIdentifier(), " " + errorMessage + "\n", messageId, null));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Optional<ValidationError> check(Predicate<Certificate> rule, Certificate certificate, String errorMessage, String messageId, String[] messageArgs) {
/* 57 */     return rule.test(certificate) ? Optional.<ValidationError>empty() : 
/* 58 */       Optional.<ValidationError>of(new ValidationError(certificate
/* 59 */           .getSubjectKeyIdentifier(), " " + errorMessage + "\n", messageId, messageArgs));
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\ValidationRuleChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */