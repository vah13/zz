/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.security;
/*    */ 
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import javax.servlet.http.HttpSession;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
/*    */ @CEBASControl
/*    */ public class ZenzefiSessionHolder
/*    */ {
/* 30 */   private static final Logger LOG = Logger.getLogger(com.daimler.cebas.zenzefi.security.ZenzefiSessionHolder.class.getName());
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   private Map<String, HttpSession> sessionsMap = new ConcurrentHashMap<>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private OAuth2AuthenticationToken authentication;
/*    */ 
/*    */ 
/*    */   
/*    */   @Autowired
/*    */   private Logger logger;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addSession(HttpSession session) {
/* 51 */     this.sessionsMap.put(session.getId(), session);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void removeSession(HttpSession session) {
/* 60 */     String id = session.getId();
/* 61 */     if (this.sessionsMap.remove(id) != null) {
/* 62 */       this.logger.log(Level.FINEST, "000366", "HTTP Session: + " + id + " was removed from session holder", 
/* 63 */           getClass().getSimpleName());
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void destroySessions() {
/* 71 */     this.sessionsMap.values().forEach(this::invalidateSession);
/* 72 */     this.sessionsMap.clear();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void invalidateSession(HttpSession session) {
/* 81 */     if (session != null) {
/*    */       try {
/* 83 */         session.invalidate();
/* 84 */       } catch (IllegalStateException ex) {
/* 85 */         LOG.log(Level.FINEST, ex.getMessage(), ex);
/* 86 */         this.logger.log(Level.FINEST, "000366", "HTTP Session: + " + session
/* 87 */             .getId() + " was already invalidated, it can not be invalidated again", 
/*    */             
/* 89 */             getClass().getSimpleName());
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   public void setAuthentication(OAuth2AuthenticationToken authentication) {
/* 95 */     this.authentication = authentication;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\security\ZenzefiSessionHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */