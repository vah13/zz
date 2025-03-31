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
/*    */ 
/*    */ @ApiModel
/*    */ public class Ownership
/*    */   extends CEBASResult
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "base64 encoded proof", required = true)
/*    */   private String ecuProofOfOwnership;
/*    */   
/*    */   public Ownership(String errorMessage) {
/* 30 */     super(errorMessage);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Ownership(byte[] ecuProofOfOwnership) {
/* 40 */     this
/* 41 */       .ecuProofOfOwnership = Base64.getEncoder().encodeToString(ecuProofOfOwnership);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getEcuProofOfOwnership() {
/* 50 */     return this.ecuProofOfOwnership;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getErrorMessage() {
/* 55 */     return this.errorMessage;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 60 */     return "Ownership{ecuProofOfOwnership=" + this.ecuProofOfOwnership + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\Ownership.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */