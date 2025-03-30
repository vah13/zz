/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.integration.xentry;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASSession;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.context.annotation.Profile;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CEBASSession
/*    */ @Profile({"AFTERSALES"})
/*    */ public class XentryUpdateSession
/*    */   extends UpdateSession
/*    */ {
/*    */   @Autowired
/*    */   public XentryUpdateSession(Logger logger, MetadataManager metadataManager) {
/* 24 */     super(logger, metadataManager);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleInvalidToken(String message, UpdateType updateType) {
/* 29 */     updateStep(getCurrentStep(), "Authorization error in Xentry", updateType, true);
/* 30 */     setNotRunning();
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\integration\xentry\XentryUpdateSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */