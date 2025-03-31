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
/*    */ 
/*    */ 
/*    */ @ApiModel
/*    */ public class SecOCISInputV1
/*    */   extends AbstractSecOCISInput
/*    */   implements ISecOCIsInput
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "The target VIN. The size of the field is 17 characters.")
/*    */   private String targetVIN;
/*    */   
/*    */   public SecOCISInputV1() {}
/*    */   
/*    */   public SecOCISInputV1(String ecuCertificate, String backendCertSubjKeyId, String diagCertSerialNumber, String targetECU, String targetVIN) {
/* 47 */     super(ecuCertificate, backendCertSubjKeyId, diagCertSerialNumber, targetECU);
/*    */     
/* 49 */     this.targetVIN = targetVIN;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTargetVIN() {
/* 58 */     return this.targetVIN;
/*    */   }
/*    */ 
/*    */   
/*    */   @JsonIgnore
/*    */   public boolean isInvalid() {
/* 64 */     return (StringUtils.isEmpty(getBackendCertSubjKeyId()) || 
/* 65 */       StringUtils.isEmpty(getEcuCertificate()) || 
/* 66 */       StringUtils.isEmpty(getDiagCertSerialNumber()) || StringUtils.isEmpty(getTargetECU()));
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\SecOCISInputV1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */