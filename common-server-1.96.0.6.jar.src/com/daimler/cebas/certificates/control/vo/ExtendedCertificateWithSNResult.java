/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
/*    */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*    */ import io.swagger.annotations.ApiModel;
/*    */ import io.swagger.annotations.ApiModelProperty;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ApiModel
/*    */ public class ExtendedCertificateWithSNResult
/*    */   extends CertificateWithSNResult
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "ZK Number value of backend parent")
/*    */   private String backendZkNo;
/*    */   @JsonIgnore
/*    */   private CertificateWithSNResult certificateWithSNResult;
/*    */   
/*    */   public ExtendedCertificateWithSNResult(String errorMessage) {
/* 26 */     super(errorMessage);
/* 27 */     this.certificateWithSNResult = new CertificateWithSNResult(errorMessage);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ExtendedCertificateWithSNResult(CertificateWithSNResult certificateWithSNResult, String backendZkNo) {
/* 37 */     super(CertificateParser.decodeBase64(certificateWithSNResult.getCertificateData()), CertificateParser.decodeBase64(certificateWithSNResult.getSerialNumber()));
/* 38 */     this.certificateWithSNResult = certificateWithSNResult;
/* 39 */     this.backendZkNo = backendZkNo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getBackendZkNo() {
/* 48 */     return this.backendZkNo;
/*    */   }
/*    */   
/*    */   public CertificateWithSNResult getCertificateWithSNResult() {
/* 52 */     return this.certificateWithSNResult;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\ExtendedCertificateWithSNResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */