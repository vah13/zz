/*    */ package com.daimler.cebas.certificates.integration.vo;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
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
/*    */ @JsonIgnoreProperties(ignoreUnknown = true)
/*    */ public class BackendIdentifier
/*    */ {
/*    */   @JsonIgnore
/*    */   public static final String ROOT_CA = "ROOT_CA";
/*    */   @JsonIgnore
/*    */   public static final String BACKEND_CA = "SUB_CA";
/*    */   @JsonProperty("ski")
/*    */   private String subjectKeyIdentifier;
/*    */   @JsonProperty("aki")
/*    */   private String authorityKeyIdentifier;
/*    */   @JsonProperty("zkNo")
/*    */   private String zkNo;
/*    */   @JsonProperty("ecuPackageTs")
/*    */   private String ecuPackageTs;
/*    */   @JsonProperty("type")
/*    */   private String type;
/*    */   @JsonProperty("state")
/*    */   private String pkiState;
/*    */   @JsonProperty("linkCertTs")
/*    */   private String linkCertTs;
/*    */   
/*    */   public BackendIdentifier() {}
/*    */   
/*    */   public BackendIdentifier(String subjectKeyIdentifier, String authorityKeyIdentifier, String zkNo, String ecuPackageTs) {
/* 48 */     this.subjectKeyIdentifier = subjectKeyIdentifier;
/* 49 */     this.authorityKeyIdentifier = authorityKeyIdentifier;
/* 50 */     this.zkNo = zkNo;
/* 51 */     this.ecuPackageTs = ecuPackageTs;
/*    */   }
/*    */   
/*    */   public BackendIdentifier(String subjectKeyIdentifier, String authorityKeyIdentifier, String zkNo, String ecuPackageTs, String type, String pkiState, String linkCertTs) {
/* 55 */     this.subjectKeyIdentifier = subjectKeyIdentifier;
/* 56 */     this.authorityKeyIdentifier = authorityKeyIdentifier;
/* 57 */     this.zkNo = zkNo;
/* 58 */     this.ecuPackageTs = ecuPackageTs;
/* 59 */     this.type = type;
/* 60 */     this.pkiState = pkiState;
/* 61 */     this.linkCertTs = linkCertTs;
/*    */   }
/*    */   
/*    */   public String getSubjectKeyIdentifier() {
/* 65 */     return this.subjectKeyIdentifier;
/*    */   }
/*    */   
/*    */   public String getAuthorityKeyIdentifier() {
/* 69 */     return this.authorityKeyIdentifier;
/*    */   }
/*    */   
/*    */   public String getZkNo() {
/* 73 */     return this.zkNo;
/*    */   }
/*    */   
/*    */   public String getEcuPackageTs() {
/* 77 */     return this.ecuPackageTs;
/*    */   }
/*    */   
/*    */   public String getType() {
/* 81 */     return this.type;
/*    */   }
/*    */   
/*    */   public String getPkiState() {
/* 85 */     return this.pkiState;
/*    */   }
/*    */   
/*    */   public String getLinkCertTs() {
/* 89 */     return this.linkCertTs;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 94 */     return "BackendIdentifier{subjectKeyIdentifier='" + this.subjectKeyIdentifier + '\'' + ", authorityKeyIdentifier='" + this.authorityKeyIdentifier + '\'' + ", zkNo='" + this.zkNo + '\'' + ", ecuPackageTs='" + this.ecuPackageTs + '\'' + ", type='" + this.type + '\'' + ", pkiState='" + this.pkiState + '\'' + ", linkCertTs='" + this.linkCertTs + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\integration\vo\BackendIdentifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */