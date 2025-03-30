/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control.backend;
/*    */ 
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import com.daimler.cebas.users.entity.User;
/*    */ import com.daimler.cebas.zenzefi.users.control.backend.IBackendLoginCheck;
/*    */ import org.springframework.context.annotation.Profile;
/*    */ 
/*    */ @CEBASControl
/*    */ @Profile({"!AFTERSALES"})
/*    */ public class DefaultBackendLoginCheck
/*    */   implements IBackendLoginCheck
/*    */ {
/*    */   private Session session;
/*    */   
/*    */   public DefaultBackendLoginCheck(Session session) {
/* 17 */     this.session = session;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBackendStateValid() {
/* 22 */     User backendAuthenticatedUser = this.session.getBackendAuthenticatedUser();
/* 23 */     return (backendAuthenticatedUser != null);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\backend\DefaultBackendLoginCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */