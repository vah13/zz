/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import com.daimler.cebas.common.control.vo.CEBASResult;
/*    */ import io.swagger.annotations.ApiModel;
/*    */ import io.swagger.annotations.ApiModelProperty;
/*    */ import java.util.Base64;
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
/*    */ @ApiModel
/*    */ public class CertificateResult
/*    */   extends CEBASResult
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "Certificate encoded bytes in base64 format")
/*    */   private String certificateData;
/*    */   
/*    */   public CertificateResult(String errorMessage) {
/* 29 */     super(errorMessage);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CertificateResult(byte[] certificateData) {
/* 39 */     this.certificateData = Base64.getEncoder().encodeToString(certificateData);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCertificateData() {
/* 48 */     return this.certificateData;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\CertificateResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */