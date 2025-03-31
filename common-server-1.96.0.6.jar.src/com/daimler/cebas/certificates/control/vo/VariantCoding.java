/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import com.daimler.cebas.common.control.vo.CEBASResult;
/*    */ import io.swagger.annotations.ApiModel;
/*    */ import io.swagger.annotations.ApiModelProperty;
/*    */ import java.util.Date;
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
/*    */ public class VariantCoding
/*    */   extends CEBASResult
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "Signature base64 string encoded")
/*    */   private String signature;
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "Variant coding certificate base64 string encoded")
/*    */   private String varCodingCertificate;
/*    */   @ApiModelProperty(dataType = "java.lang.Long", value = "Expiration date of Variant Coding Certificate")
/*    */   private Long expirationDate;
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "Serial number base64 string encoded")
/*    */   private String serialNumber;
/*    */   
/*    */   public VariantCoding(String errorMessage) {
/* 35 */     super(errorMessage);
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
/*    */   public VariantCoding(String signature, String varCodingCertificate, Date expirationDate, String serialNumber) {
/* 47 */     this.signature = signature;
/* 48 */     this.varCodingCertificate = varCodingCertificate;
/* 49 */     this.expirationDate = Long.valueOf(expirationDate.getTime());
/* 50 */     this.serialNumber = serialNumber;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSignature() {
/* 59 */     return this.signature;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getVarCodingCertificate() {
/* 68 */     return this.varCodingCertificate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Long getExpirationDate() {
/* 77 */     return this.expirationDate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSerialNumber() {
/* 86 */     return this.serialNumber;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\VariantCoding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */