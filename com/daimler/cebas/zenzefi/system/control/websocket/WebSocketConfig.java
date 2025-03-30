/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.system.control.websocket;
/*    */ 
/*    */ import com.daimler.cebas.zenzefi.system.control.websocket.WebSocketSessions;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.messaging.converter.MessageConverter;
/*    */ import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
/*    */ import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
/*    */ import org.springframework.messaging.simp.config.ChannelRegistration;
/*    */ import org.springframework.messaging.simp.config.MessageBrokerRegistry;
/*    */ import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
/*    */ import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
/*    */ import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurationSupport;
/*    */ import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
/*    */ import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
/*    */ import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @EnableWebSocketMessageBroker
/*    */ public class WebSocketConfig
/*    */   extends WebSocketMessageBrokerConfigurationSupport
/*    */   implements WebSocketMessageBrokerConfigurer
/*    */ {
/*    */   @Autowired
/*    */   private WebSocketSessions sessions;
/*    */   
/*    */   public void configureMessageBroker(MessageBrokerRegistry config) {
/* 33 */     config.enableSimpleBroker(new String[] { "/topic" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerStompEndpoints(StompEndpointRegistry registry) {
/* 38 */     registry.addEndpoint(new String[] { "/ws" }).setAllowedOrigins(new String[] { "*" }).withSockJS();
/*    */   }
/*    */ 
/*    */   
/*    */   public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
/* 43 */     registry.addDecoratorFactory((WebSocketHandlerDecoratorFactory)new Object(this));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
/* 53 */     return super.configureMessageConverters(messageConverters);
/*    */   }
/*    */ 
/*    */   
/*    */   public void configureClientInboundChannel(ChannelRegistration registration) {
/* 58 */     super.configureClientInboundChannel(registration);
/*    */   }
/*    */ 
/*    */   
/*    */   public void configureClientOutboundChannel(ChannelRegistration registration) {
/* 63 */     super.configureClientOutboundChannel(registration);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
/* 68 */     super.addArgumentResolvers(argumentResolvers);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
/* 73 */     super.addReturnValueHandlers(returnValueHandlers);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\system\control\websocket\WebSocketConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */