/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control.idle;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.ImportSession;
/*     */ import com.daimler.cebas.common.control.CEBASProperty;
/*     */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*     */ import com.daimler.cebas.zenzefi.system.control.websocket.WebsocketController;
/*     */ import com.daimler.cebas.zenzefi.users.control.idle.AspectUserInteraction;
/*     */ import com.daimler.cebas.zenzefi.users.control.idle.UserIdleTimerTask;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.annotation.PreDestroy;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.ApplicationEventPublisher;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class UserIdleTimer
/*     */ {
/*  26 */   private static final String CLASS_NAME = AspectUserInteraction.class.getSimpleName();
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private Logger logger;
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private ApplicationEventPublisher eventPublisher;
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private AbstractConfigurator configurator;
/*     */ 
/*     */ 
/*     */   
/*     */   private Timer idleTimer;
/*     */ 
/*     */ 
/*     */   
/*     */   private UserIdleTimerTask timerTask;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isLoggedIn = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean autologout = true;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long INTERVAL = 10000L;
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private UpdateSession updateSession;
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private ImportSession importSession;
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private WebsocketController websocketController;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostConstruct
/*     */   public void setup() {
/*  83 */     long logoffNoUserAction = Long.valueOf(this.configurator.readProperty(CEBASProperty.LOGOFF_NO_USER_ACTION.name())).longValue();
/*     */ 
/*     */     
/*  86 */     this.idleTimer = new Timer();
/*  87 */     this.timerTask = new UserIdleTimerTask(this, this.eventPublisher, logoffNoUserAction);
/*  88 */     this.idleTimer.schedule((TimerTask)this.timerTask, 10000L, 10000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLoggedIn() {
/*  97 */     String METHOD_NAME = "isLoggedIn: " + this.isLoggedIn;
/*  98 */     this.logger.entering(CLASS_NAME, METHOD_NAME);
/*  99 */     this.logger.exiting(CLASS_NAME, METHOD_NAME);
/* 100 */     return this.isLoggedIn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLoggedIn(boolean loggedIn) {
/* 109 */     String METHOD_NAME = "setLoggedIn";
/* 110 */     this.logger.entering(CLASS_NAME, "setLoggedIn");
/* 111 */     this.isLoggedIn = loggedIn;
/* 112 */     this.timerTask.reset();
/* 113 */     this.logger.exiting(CLASS_NAME, "setLoggedIn");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutologout() {
/* 122 */     String METHOD_NAME = "isAutologout: " + this.autologout;
/* 123 */     this.logger.entering(CLASS_NAME, METHOD_NAME);
/* 124 */     this.logger.exiting(CLASS_NAME, METHOD_NAME);
/* 125 */     return this.autologout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutologout(boolean autologout) {
/* 134 */     String METHOD_NAME = "setAutologout";
/* 135 */     this.logger.entering(CLASS_NAME, "setAutologout");
/* 136 */     this.autologout = autologout;
/* 137 */     this.timerTask.reset();
/* 138 */     this.logger.exiting(CLASS_NAME, "setAutologout");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 145 */     String METHOD_NAME = "reset";
/* 146 */     this.logger.entering(CLASS_NAME, "reset");
/* 147 */     this.timerTask.reset();
/* 148 */     this.websocketController.timerResetEvent();
/* 149 */     this.logger.exiting(CLASS_NAME, "reset");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PreDestroy
/*     */   public void cleanUp() {
/* 157 */     if (this.idleTimer != null) {
/* 158 */       this.idleTimer.cancel();
/* 159 */       this.idleTimer.purge();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isUpdateRunning() {
/* 164 */     if (this.updateSession.isRunning() || this.updateSession.isPaused()) {
/* 165 */       return true;
/*     */     }
/* 167 */     return this.importSession.isRunning();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getExpirationTimestamp() {
/* 176 */     return this.timerTask.getExpirationTimestamp();
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\idle\UserIdleTimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */