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
/*    */ public class ExtendedCertificateWithSnSkiResult
/*    */   extends CertificateWithSnSkiResult
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "ZK Number value")
/*    */   private String zkNo;
/*    */   @JsonIgnore
/*    */   private CertificateWithSnSkiResult certificateWithSnSkiResult;
/*    */   
/*    */   public ExtendedCertificateWithSnSkiResult(String errorMessage) {
/* 26 */     super(errorMessage);
/* 27 */     this.certificateWithSnSkiResult = new CertificateWithSnSkiResult(errorMessage);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ExtendedCertificateWithSnSkiResult(CertificateWithSnSkiResult certificateWithSnSkiResult, String zkNo) {
/* 37 */     super(CertificateParser.decodeBase64(certificateWithSnSkiResult.getCertificateData()), CertificateParser.decodeBase64(certificateWithSnSkiResult.getSerialNumber()), 
/* 38 */         CertificateParser.decodeBase64(certificateWithSnSkiResult.getSubjectKeyIdentifier()));
/* 39 */     this.certificateWithSnSkiResult = certificateWithSnSkiResult;
/* 40 */     this.zkNo = zkNo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getZkNo() {
/* 49 */     return this.zkNo;
/*    */   }
/*    */   
/*    */   public CertificateWithSnSkiResult getCertificateWithSnSkiResult() {
/* 53 */     return this.certificateWithSnSkiResult;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\ExtendedCertificateWithSnSkiResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */