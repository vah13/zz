/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.system.control.websocket;
/*    */ 
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WebSocketMessage
/*    */ {
/*    */   private final Map<String, Object> payload;
/*    */   private final String topic;
/*    */   
/*    */   public WebSocketMessage(Map<String, Object> payload, String topic) {
/* 14 */     this.payload = payload;
/* 15 */     this.topic = topic;
/*    */   }
/*    */   
/*    */   public Map<String, Object> getPayload() {
/* 19 */     return this.payload;
/*    */   }
/*    */   
/*    */   public String getTopic() {
/* 23 */     return this.topic;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\system\control\websocket\WebSocketMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */