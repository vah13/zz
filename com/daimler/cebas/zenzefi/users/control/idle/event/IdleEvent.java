/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control.idle.event;
/*    */ 
/*    */ import com.daimler.cebas.zenzefi.users.control.idle.UserIdleTimer;
/*    */ import org.springframework.context.ApplicationEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IdleEvent
/*    */   extends ApplicationEvent
/*    */ {
/*    */   private static final long serialVersionUID = -2831044454373850544L;
/*    */   private UserIdleTimer timer;
/*    */   
/*    */   public IdleEvent(Object source, UserIdleTimer timer) {
/* 19 */     super(source);
/* 20 */     this.timer = timer;
/*    */   }
/*    */   
/*    */   public void setLoggedOff() {
/* 24 */     this.timer.setLoggedIn(false);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\idle\event\IdleEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */