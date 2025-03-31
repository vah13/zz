/*    */ package com.daimler.cebas.certificates.control.validation;
/*    */ 
/*    */ import java.util.List;
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
/*    */ public class SystemIntegrityCheckError
/*    */ {
/*    */   private String certificateId;
/*    */   private List<String> errorMessages;
/*    */   private List<String> messageIds;
/*    */   
/*    */   public SystemIntegrityCheckError(String certificateId, List<String> errorMessages, List<String> messageIds) {
/* 29 */     this.certificateId = certificateId;
/* 30 */     this.errorMessages = errorMessages;
/* 31 */     this.messageIds = messageIds;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCertificateId() {
/* 40 */     return this.certificateId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> getErrorMessages() {
/* 49 */     return this.errorMessages;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> getMessageIds() {
/* 58 */     return this.messageIds;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setErrorMessages(List<String> errorMessages) {
/* 65 */     this.errorMessages = errorMessages;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 70 */     return "SystemIntegrityCheckError [certificateId=" + this.certificateId + ", errorMessages=" + this.errorMessages + ", messageIds=" + this.messageIds + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\SystemIntegrityCheckError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */