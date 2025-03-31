/*    */ package com.daimler.cebas.system.control.websocket;
/*    */ 
/*    */ import org.springframework.beans.factory.InitializingBean;
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
/*    */ public abstract class WebsocketAbstractController
/*    */   implements InitializingBean
/*    */ {
/*    */   protected static WebsocketAbstractController instance;
/*    */   
/*    */   public abstract void updateAlertMessages();
/*    */   
/*    */   public static WebsocketAbstractController getInstance() {
/* 26 */     return instance;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\websocket\WebsocketAbstractController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */