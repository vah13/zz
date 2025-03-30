/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.system.control.websocket;
/*    */ 
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.system.control.websocket.IWebsocketSessions;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.logging.Level;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.web.socket.CloseStatus;
/*    */ import org.springframework.web.socket.WebSocketSession;
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
/*    */ @CEBASControl
/*    */ public class WebSocketSessions
/*    */   implements IWebsocketSessions
/*    */ {
/*    */   @Autowired
/*    */   protected Logger logger;
/* 29 */   private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void remove(WebSocketSession session) {
/* 37 */     this.sessionMap.remove(session.getId());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void register(WebSocketSession session) {
/* 46 */     this.sessionMap.put(session.getId(), session);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {
/* 54 */     this.sessionMap.keySet().forEach(k -> {
/*    */           try {
/*    */             ((WebSocketSession)this.sessionMap.get(k)).close(CloseStatus.GOING_AWAY);
/*    */             
/*    */             this.sessionMap.remove(k);
/*    */             
/*    */             this.logger.log(Level.INFO, "000592", String.format("Websocket session [%s] has been stopped", new Object[] { k }), com.daimler.cebas.zenzefi.system.control.websocket.WebSocketSessions.class.getSimpleName());
/* 61 */           } catch (IOException e) {
/*    */             this.logger.log(Level.WARNING, "000593X", String.format("Error while closing websocket session [%s]: ", new Object[] { k }), e.getMessage());
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\system\control\websocket\WebSocketSessions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */