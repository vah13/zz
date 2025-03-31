/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import com.daimler.cebas.certificates.entity.Certificate;
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
/*    */ 
/*    */ @ApiModel
/*    */ public class RootOrBackendResult
/*    */   extends CEBASResult
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "Certificate type")
/*    */   private String type;
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "Certificate encoded bytes in base64 format")
/*    */   private String certificate;
/*    */   
/*    */   public RootOrBackendResult(String errorMessage) {
/* 33 */     super(errorMessage);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RootOrBackendResult(String type, byte[] certificate) {
/* 43 */     this.type = type;
/* 44 */     this.certificate = Base64.getEncoder().encodeToString(certificate);
/*    */   }
/*    */   
/*    */   public RootOrBackendResult(Certificate certificate) {
/* 48 */     this.type = RootOrBackend.fromType(certificate.getType()).name();
/* 49 */     this.certificate = Base64.getEncoder().encodeToString(certificate.getCertificateData().getOriginalBytes());
/*    */   }
/*    */   
/*    */   public String getType() {
/* 53 */     return this.type;
/*    */   }
/*    */   
/*    */   public String getCertificate() {
/* 57 */     return this.certificate;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\RootOrBackendResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */