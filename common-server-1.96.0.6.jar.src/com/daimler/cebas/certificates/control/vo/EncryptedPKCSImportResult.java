/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import com.daimler.cebas.common.control.vo.CEBASResult;
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
/*    */ public class EncryptedPKCSImportResult
/*    */   extends CEBASResult
/*    */ {
/*    */   @ApiModelProperty
/*    */   private String summary;
/*    */   @ApiModelProperty(position = 1)
/*    */   private List<? extends ImportResult> importResult;
/*    */   
/*    */   public EncryptedPKCSImportResult() {}
/*    */   
/*    */   public EncryptedPKCSImportResult(String summary, List<? extends ImportResult> importResult) {
/* 43 */     this.summary = summary;
/* 44 */     this.importResult = importResult;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSummary() {
/* 53 */     return this.summary;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<? extends ImportResult> getImportResult() {
/* 62 */     return this.importResult;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\EncryptedPKCSImportResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */