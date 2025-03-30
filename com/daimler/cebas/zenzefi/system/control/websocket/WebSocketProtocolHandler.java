/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.system.control.websocket;
/*    */ 
/*    */ import com.daimler.cebas.zenzefi.system.control.websocket.WebSocketSessions;
/*    */ import java.util.Optional;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.messaging.MessageChannel;
/*    */ import org.springframework.messaging.SubscribableChannel;
/*    */ import org.springframework.web.socket.WebSocketSession;
/*    */ import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;
/*    */ 
/*    */ public class WebSocketProtocolHandler
/*    */   extends SubProtocolWebSocketHandler {
/*    */   @Autowired
/*    */   private Optional<WebSocketSessions> webSocketSessions;
/*    */   
/*    */   public WebSocketProtocolHandler(MessageChannel clientInboundChannel, SubscribableChannel clientOutboundChannel) {
/* 17 */     super(clientInboundChannel, clientOutboundChannel);
/*    */   }
/*    */ 
/*    */   
/*    */   public void afterConnectionEstablished(WebSocketSession session) throws Exception {
/* 22 */     if (this.webSocketSessions.isPresent()) {
/* 23 */       ((WebSocketSessions)this.webSocketSessions.get()).register(session);
/*    */     }
/* 25 */     super.afterConnectionEstablished(session);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\system\control\websocket\WebSocketProtocolHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */