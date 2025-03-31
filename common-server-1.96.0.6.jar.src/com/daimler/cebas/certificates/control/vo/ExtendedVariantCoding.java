/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnore;
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
/*    */ @ApiModel
/*    */ public class ExtendedVariantCoding
/*    */   extends VariantCoding
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "ZK Number value of backend parent")
/*    */   private String backendZkNo;
/*    */   @JsonIgnore
/*    */   private VariantCoding variantCoding;
/*    */   
/*    */   public ExtendedVariantCoding(String errorMessage) {
/* 27 */     super(errorMessage);
/* 28 */     this.variantCoding = new VariantCoding(errorMessage);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ExtendedVariantCoding(VariantCoding variantCoding, String backendZkNo) {
/* 38 */     super(variantCoding.getSignature(), variantCoding.getVarCodingCertificate(), new Date(variantCoding.getExpirationDate().longValue()), variantCoding.getSerialNumber());
/* 39 */     this.variantCoding = variantCoding;
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
/*    */   public VariantCoding getVariantCoding() {
/* 53 */     return this.variantCoding;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\ExtendedVariantCoding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */