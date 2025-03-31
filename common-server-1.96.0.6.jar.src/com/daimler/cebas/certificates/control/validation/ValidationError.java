/*    */ package com.daimler.cebas.certificates.control.validation;
/*    */ 
/*    */ import org.apache.commons.lang3.builder.EqualsBuilder;
/*    */ import org.apache.commons.lang3.builder.HashCodeBuilder;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValidationError
/*    */ {
/*    */   private String errorMessage;
/*    */   private String subjectKeyIdentifier;
/*    */   private String messageId;
/*    */   private String[] messageArgs;
/*    */   
/*    */   public ValidationError() {}
/*    */   
/*    */   public ValidationError(String subjectKeyIdentifier, String errorMessage, String messageId, String[] messageArgs) {
/* 46 */     this.subjectKeyIdentifier = subjectKeyIdentifier;
/* 47 */     this.errorMessage = errorMessage;
/* 48 */     this.messageId = messageId;
/* 49 */     this.messageArgs = messageArgs;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getErrorMessage() {
/* 58 */     return this.errorMessage;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSubjectKeyIdentifier() {
/* 67 */     return this.subjectKeyIdentifier;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessageId() {
/* 74 */     return this.messageId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] getMessageArgs() {
/* 81 */     return this.messageArgs;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 86 */     return EqualsBuilder.reflectionEquals(this, obj, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 91 */     return HashCodeBuilder.reflectionHashCode(this, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\ValidationError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */