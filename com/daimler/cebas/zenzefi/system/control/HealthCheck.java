/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.system.control;
/*    */ 
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.boot.actuate.health.Health;
/*    */ import org.springframework.boot.actuate.health.HealthIndicator;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class HealthCheck
/*    */   implements HealthIndicator
/*    */ {
/*    */   private Session session;
/*    */   
/*    */   @Autowired
/*    */   public HealthCheck(Session session) {
/* 21 */     this.session = session;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Health health() {
/*    */     try {
/* 28 */       this.session.getCurrentUser();
/* 29 */       return Health.up().build();
/* 30 */     } catch (Exception e) {
/* 31 */       return Health.down().withDetail("message", e.getMessage()).build();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\system\control\HealthCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */