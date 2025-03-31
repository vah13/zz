/*    */ package com.daimler.cebas.common.control.vo;
/*    */ 
/*    */ import io.swagger.annotations.ApiModel;
/*    */ import io.swagger.annotations.ApiModelProperty;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ApiModel
/*    */ public class Enablement
/*    */ {
/*    */   @ApiModelProperty
/*    */   private boolean enable;
/*    */   
/*    */   public Enablement() {}
/*    */   
/*    */   public Enablement(boolean value) {
/* 20 */     this.enable = value;
/*    */   }
/*    */   
/*    */   public boolean isEnable() {
/* 24 */     return this.enable;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\vo\Enablement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */