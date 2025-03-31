/*    */ package com.daimler.cebas.certificates.integration.vo;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NonVsmIdentifier
/*    */ {
/*    */   @JsonProperty("ski")
/*    */   private String subjectKeyIdentifier;
/*    */   @JsonProperty("cn")
/*    */   private String subject;
/*    */   @JsonProperty("ecuUniqueIds")
/*    */   private List<String> ecuUniqueIds;
/*    */   
/*    */   public NonVsmIdentifier() {}
/*    */   
/*    */   public NonVsmIdentifier(String subjectKeyIdentifier, String subject, List<String> ecuUniqueIds) {
/* 23 */     this.subjectKeyIdentifier = subjectKeyIdentifier;
/* 24 */     this.subject = subject;
/* 25 */     this.ecuUniqueIds = ecuUniqueIds;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSubjectKeyIdentifier() {
/* 32 */     return this.subjectKeyIdentifier;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSubjectKeyIdentifier(String subjectKeyIdentifier) {
/* 39 */     this.subjectKeyIdentifier = subjectKeyIdentifier;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSubject() {
/* 46 */     return this.subject;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSubject(String subject) {
/* 53 */     this.subject = subject;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> getEcuUniqueIds() {
/* 60 */     return this.ecuUniqueIds;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setEcuUniqueIds(List<String> ecuUniqueIds) {
/* 67 */     this.ecuUniqueIds = ecuUniqueIds;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 72 */     return "NonVsmIdentifier{subjectKeyIdentifier='" + this.subjectKeyIdentifier + '\'' + ", subject='" + this.subject + '\'' + ", ecuUniqueIds=" + this.ecuUniqueIds + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\integration\vo\NonVsmIdentifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */