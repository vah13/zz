/*    */ package com.daimler.cebas.common.control;
/*    */ 
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import org.springframework.context.MessageSource;
/*    */ import org.springframework.web.context.annotation.RequestScope;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CEBASControl
/*    */ @RequestScope
/*    */ public class RequestMetadata
/*    */   extends CEBASI18N
/*    */ {
/* 20 */   private String correlationId = "";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RequestMetadata(MessageSource messageSource) {
/* 28 */     super(messageSource);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCorrelationId(String correlationId) {
/* 37 */     this.correlationId = correlationId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCorrelationId() {
/* 46 */     return this.correlationId;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\RequestMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */