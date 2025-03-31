/*    */ package com.daimler.cebas.certificates.control.validation.failure;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValidationState
/*    */ {
/*    */   @ApiModelProperty(hidden = true)
/*    */   @JsonIgnore
/*    */   private boolean valid = true;
/*    */   
/*    */   public void setValid(boolean valid) {
/* 26 */     this.valid = valid;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isValid() {
/* 34 */     return this.valid;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\failure\ValidationState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */