/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
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
/*    */ public class CheckOwnershipInput
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "ECU challenge, Base64 encoded", required = true)
/*    */   private String ecuChallenge;
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "The backend subject key identifier. It is sent as Base64 encoded bytes. The length is 20 bytes.", required = true)
/*    */   private String backendCertSubjKeyId;
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "The serial number. It is sent as Base64 encoded bytes. The maximum length is 16 bytes.", required = true)
/*    */   private String serialNumber;
/*    */   
/*    */   public CheckOwnershipInput() {}
/*    */   
/*    */   public CheckOwnershipInput(String ecuChallenge, String backendCertSubjKeyId, String serialNumber) {
/* 55 */     this.ecuChallenge = ecuChallenge;
/* 56 */     this.backendCertSubjKeyId = backendCertSubjKeyId;
/* 57 */     this.serialNumber = serialNumber;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBackendCertSubjKeyId(String backendCertSubjKeyId) {
/* 66 */     this.backendCertSubjKeyId = backendCertSubjKeyId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getBackendCertSubjKeyId() {
/* 75 */     return this.backendCertSubjKeyId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getEcuChallenge() {
/* 84 */     return this.ecuChallenge;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSerialNumber() {
/* 93 */     return this.serialNumber;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\CheckOwnershipInput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */