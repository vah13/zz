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
/*    */ public class ExtendedCertificateWithSNAndIssuerSNResult
/*    */   extends CertificateWithSNAndIssuerSNResult
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "ZK Number value of backend parent")
/*    */   private String backendZkNo;
/*    */   @JsonIgnore
/*    */   private CertificateWithSNAndIssuerSNResult certificateWithSNAndIssuerSNResult;
/*    */   
/*    */   public ExtendedCertificateWithSNAndIssuerSNResult(String errorMessage) {
/* 26 */     super(errorMessage);
/* 27 */     this.certificateWithSNAndIssuerSNResult = new CertificateWithSNAndIssuerSNResult(errorMessage);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ExtendedCertificateWithSNAndIssuerSNResult(CertificateWithSNAndIssuerSNResult certificateWithSNAndIssuerSNResult, String backendZkNo) {
/* 37 */     super(CertificateParser.decodeBase64(certificateWithSNAndIssuerSNResult.getCertificateData()), CertificateParser.decodeBase64(certificateWithSNAndIssuerSNResult.getSerialNumber()), 
/* 38 */         CertificateParser.decodeBase64(certificateWithSNAndIssuerSNResult.getIssuerSerialNumber()));
/* 39 */     this.certificateWithSNAndIssuerSNResult = certificateWithSNAndIssuerSNResult;
/* 40 */     this.backendZkNo = backendZkNo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getBackendZkNo() {
/* 49 */     return this.backendZkNo;
/*    */   }
/*    */   
/*    */   public CertificateWithSNAndIssuerSNResult getCertificateWithSNAndIssuerSNResult() {
/* 53 */     return this.certificateWithSNAndIssuerSNResult;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\ExtendedCertificateWithSNAndIssuerSNResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */