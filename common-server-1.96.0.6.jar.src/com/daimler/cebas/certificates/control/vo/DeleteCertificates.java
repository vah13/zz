/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import io.swagger.annotations.ApiModel;
/*    */ import io.swagger.annotations.ApiModelProperty;
/*    */ import java.util.List;
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
/*    */ public class DeleteCertificates
/*    */ {
/*    */   private boolean all;
/*    */   @ApiModelProperty(dataType = "java.lang.String", required = true, value = "List of authorityKeyIdentifier/serial number, base64 encoded")
/*    */   private List<DeleteCertificateModel> pairs;
/*    */   
/*    */   public DeleteCertificates() {}
/*    */   
/*    */   public DeleteCertificates(List<DeleteCertificateModel> models, boolean all) {
/* 44 */     this.pairs = models;
/* 45 */     this.all = all;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<DeleteCertificateModel> getModels() {
/* 54 */     return this.pairs;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isAll() {
/* 63 */     return this.all;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\DeleteCertificates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */