/*    */ package com.daimler.cebas.certificates.control.zkNoSupport;
/*    */ 
/*    */ import com.daimler.cebas.common.control.vo.CEBASResult;
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
/*    */ @ApiModel
/*    */ public class ZkNoMappingResult
/*    */   extends CEBASResult
/*    */ {
/*    */   @ApiModelProperty(value = "The Subject Key Identifier. It is sent as Base64 encoded bytes, and the length must be 20 bytes.", required = true)
/*    */   private String ski;
/*    */   @ApiModelProperty(value = "The ZK number.", required = true)
/*    */   private String zkNo;
/*    */   
/*    */   public ZkNoMappingResult() {}
/*    */   
/*    */   public ZkNoMappingResult(String ski, String zkNo) {
/* 35 */     this.ski = ski;
/* 36 */     this.zkNo = zkNo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSki() {
/* 43 */     return this.ski;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getZkNo() {
/* 50 */     return this.zkNo;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\zkNoSupport\ZkNoMappingResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */