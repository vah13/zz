/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control.backend;
/*    */ 
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import com.daimler.cebas.zenzefi.users.control.backend.IBackendLoginCheck;
/*    */ import org.springframework.context.annotation.Profile;
/*    */ 
/*    */ @CEBASControl
/*    */ @Profile({"AFTERSALES"})
/*    */ public class XentryBackendLoginCheck
/*    */   implements IBackendLoginCheck
/*    */ {
/*    */   private Session session;
/*    */   
/*    */   public XentryBackendLoginCheck(Session session) {
/* 16 */     this.session = session;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBackendStateValid() {
/* 21 */     return !this.session.isDefaultUser();
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\backend\XentryBackendLoginCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */