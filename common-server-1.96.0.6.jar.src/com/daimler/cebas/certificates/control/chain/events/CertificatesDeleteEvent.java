/*    */ package com.daimler.cebas.certificates.control.chain.events;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.context.ApplicationEvent;
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
/*    */ public class CertificatesDeleteEvent
/*    */   extends ApplicationEvent
/*    */ {
/*    */   private static final long serialVersionUID = 4303738505537567864L;
/*    */   private List<String> ids;
/*    */   
/*    */   public CertificatesDeleteEvent(Object source, List<String> ids) {
/* 34 */     super(source);
/* 35 */     this.ids = ids;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> getIds() {
/* 44 */     return this.ids;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\chain\events\CertificatesDeleteEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */