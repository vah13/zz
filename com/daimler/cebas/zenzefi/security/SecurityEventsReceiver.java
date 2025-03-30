/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.security;
/*     */ 
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.zenzefi.security.ZenzefiSessionHolder;
/*     */ import java.util.logging.Level;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.event.EventListener;
/*     */ import org.springframework.security.authentication.event.AuthenticationFailureExpiredEvent;
/*     */ import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
/*     */ import org.springframework.security.core.AuthenticationException;
/*     */ import org.springframework.security.web.session.HttpSessionCreatedEvent;
/*     */ import org.springframework.security.web.session.HttpSessionDestroyedEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASControl
/*     */ public class SecurityEventsReceiver
/*     */ {
/*     */   @Autowired
/*     */   private Logger logger;
/*     */   @Autowired
/*     */   private ZenzefiSessionHolder sessionHolder;
/*     */   @Autowired
/*     */   private Session session;
/*     */   
/*     */   @EventListener
/*     */   public void authentificationFailure(AuthenticationFailureExpiredEvent event) {
/*  47 */     AuthenticationException exception = event.getException();
/*  48 */     this.sessionHolder.destroySessions();
/*  49 */     this.session.setTransitionValid(false);
/*  50 */     this.logger.logWithException("000297X", "OAuth flow failed - " + exception.getMessage(), new CEBASException(exception
/*  51 */           .getMessage()));
/*  52 */     if (isSSLException((Throwable)exception)) {
/*  53 */       this.logger.log(Level.SEVERE, "000476X", "Please check SSL connection. ZZ can't connect with the Authorization Server", 
/*     */           
/*  55 */           getClass().getSimpleName());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventListener
/*     */   public void authentificationSuccess(AuthenticationSuccessEvent event) {
/*  66 */     this.logger.log(Level.INFO, "000298", "Oauth flow succeeded for the user with the ID: " + event
/*  67 */         .getAuthentication().getName(), event
/*  68 */         .getClass().getSimpleName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventListener
/*     */   public void sessionsCreatedEvent(HttpSessionCreatedEvent event) {
/*  78 */     HttpSession httpSession = event.getSession();
/*  79 */     this.sessionHolder.addSession(httpSession);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventListener
/*     */   public void sessionsDestroyEvent(HttpSessionDestroyedEvent event) {
/*  89 */     HttpSession httpSession = event.getSession();
/*  90 */     this.sessionHolder.removeSession(httpSession);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isSSLException(Throwable throwable) {
/* 101 */     boolean foundSslException = false;
/* 102 */     if (throwable != null) {
/* 103 */       Throwable currentThrowable = throwable;
/*     */       while (true) {
/* 105 */         if (currentThrowable != null && currentThrowable instanceof javax.net.ssl.SSLHandshakeException) {
/* 106 */           foundSslException = true;
/*     */           break;
/*     */         } 
/* 109 */         if (currentThrowable == throwable.getCause() || throwable.getCause() == null) {
/*     */           break;
/*     */         }
/* 112 */         currentThrowable = throwable.getCause();
/*     */       } 
/*     */     } 
/*     */     
/* 116 */     return foundSslException;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\security\SecurityEventsReceiver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */