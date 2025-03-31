/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*    */ import io.swagger.annotations.ApiModel;
/*    */ import io.swagger.annotations.ApiModelProperty;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class SecOCISInput
/*    */   extends AbstractSecOCISInput
/*    */   implements ISecOCIsInput
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "The target VIN. The size of the field is 17 characters.", required = true)
/*    */   private String targetVIN;
/*    */   
/*    */   public SecOCISInput() {}
/*    */   
/*    */   public SecOCISInput(String ecuCertificate, String backendCertSubjKeyId, String diagCertSerialNumber, String targetECU, String targetVIN) {
/* 45 */     super(ecuCertificate, backendCertSubjKeyId, diagCertSerialNumber, targetECU);
/*    */     
/* 47 */     this.targetVIN = targetVIN;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTargetVIN() {
/* 56 */     return this.targetVIN;
/*    */   }
/*    */ 
/*    */   
/*    */   @JsonIgnore
/*    */   public boolean isInvalid() {
/* 62 */     return (StringUtils.isEmpty(getBackendCertSubjKeyId()) || 
/* 63 */       StringUtils.isEmpty(getEcuCertificate()) || 
/* 64 */       StringUtils.isEmpty(getDiagCertSerialNumber()) || 
/* 65 */       StringUtils.isEmpty(getTargetECU()) || 
/* 66 */       StringUtils.isEmpty(getTargetVIN()));
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\SecOCISInput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */