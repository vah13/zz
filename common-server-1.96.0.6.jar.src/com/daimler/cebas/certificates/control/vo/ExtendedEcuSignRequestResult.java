/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*    */ import io.swagger.annotations.ApiModelProperty;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExtendedEcuSignRequestResult
/*    */   extends EcuSignRequestResult
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "ZK Number value of backend parent")
/*    */   private String backendZkNo;
/*    */   @JsonIgnore
/*    */   private EcuSignRequestResult ecuSignRequestResult;
/*    */   
/*    */   public ExtendedEcuSignRequestResult() {
/* 22 */     this.ecuSignRequestResult = new EcuSignRequestResult();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ExtendedEcuSignRequestResult(EcuSignRequestResult ecuSignRequestResult, String backendZkNo) {
/* 32 */     super(ecuSignRequestResult.getSignature(), ecuSignRequestResult.getEcuCertificate(), ecuSignRequestResult.getExpirationDate(), ecuSignRequestResult.getSerialNumber());
/* 33 */     this.ecuSignRequestResult = ecuSignRequestResult;
/* 34 */     this.backendZkNo = backendZkNo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getBackendZkNo() {
/* 43 */     return this.backendZkNo;
/*    */   }
/*    */   
/*    */   public EcuSignRequestResult getEcuSignRequestResult() {
/* 47 */     return this.ecuSignRequestResult;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\ExtendedEcuSignRequestResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */