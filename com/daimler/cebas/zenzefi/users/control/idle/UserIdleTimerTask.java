/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control.idle;
/*    */ 
/*    */ import com.daimler.cebas.zenzefi.users.control.idle.UserIdleTimer;
/*    */ import com.daimler.cebas.zenzefi.users.control.idle.event.IdleEvent;
/*    */ import java.util.TimerTask;
/*    */ import org.springframework.context.ApplicationEvent;
/*    */ import org.springframework.context.ApplicationEventPublisher;
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
/*    */ public class UserIdleTimerTask
/*    */   extends TimerTask
/*    */ {
/*    */   private long logoffNoUserAction;
/*    */   private UserIdleTimer timer;
/*    */   private ApplicationEventPublisher eventPublisher;
/*    */   private IdleEvent idleEvent;
/*    */   private long expirationTime;
/*    */   
/*    */   public UserIdleTimerTask(UserIdleTimer timer, ApplicationEventPublisher eventPublisher, long logoffNoUserAction) {
/* 28 */     this.timer = timer;
/* 29 */     this.eventPublisher = eventPublisher;
/* 30 */     this.logoffNoUserAction = logoffNoUserAction;
/* 31 */     this.expirationTime = Long.MAX_VALUE;
/* 32 */     this.idleEvent = new IdleEvent(this, timer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 37 */     if (!this.timer.isLoggedIn() || !this.timer.isAutologout()) {
/*    */       return;
/*    */     }
/* 40 */     if (System.currentTimeMillis() < this.expirationTime) {
/*    */       return;
/*    */     }
/* 43 */     if (this.timer.isUpdateRunning()) {
/*    */       return;
/*    */     }
/*    */     
/* 47 */     reset();
/* 48 */     this.eventPublisher.publishEvent((ApplicationEvent)this.idleEvent);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void reset() {
/* 55 */     if (this.timer.isLoggedIn() && this.timer.isAutologout()) {
/* 56 */       this.expirationTime = System.currentTimeMillis() + this.logoffNoUserAction;
/*    */     } else {
/* 58 */       this.expirationTime = Long.MAX_VALUE;
/*    */     } 
/*    */   }
/*    */   
/*    */   public long getExpirationTimestamp() {
/* 63 */     return this.expirationTime;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\idle\UserIdleTimerTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */