/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*    */ import com.daimler.cebas.certificates.control.update.UpdateSteps;
/*    */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASSession;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.zenzefi.system.control.websocket.WebsocketController;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.context.annotation.Profile;
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
/*    */ @CEBASSession
/*    */ @Profile({"!AFTERSALES"})
/*    */ public class UpdateSession
/*    */   extends DefaultUpdateSession
/*    */ {
/*    */   @Autowired(required = false)
/*    */   private WebsocketController wsController;
/*    */   
/*    */   @Autowired
/*    */   public UpdateSession(Logger logger, MetadataManager metadataManager) {
/* 32 */     super(logger, metadataManager);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void run(String message) {
/* 37 */     super.run(message);
/* 38 */     if (this.wsController != null) {
/* 39 */       this.wsController.startCertificatesUpdate();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDidFailAllRetries(boolean didFailAllRetries) {
/* 51 */     super.setDidFailAllRetries(didFailAllRetries);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setNotRunning() {
/* 56 */     super.setNotRunning();
/* 57 */     if (!isRunning() && this.wsController != null) {
/* 58 */       this.wsController.stopCertificatesUpdate(this.lastDetailsAdded, this.updateType, this.didFailAllRetries);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateStep(UpdateSteps step, String details, UpdateType updateType) {
/* 64 */     super.updateStep(step, details, updateType);
/* 65 */     if (this.wsController != null) {
/* 66 */       this.wsController.newCertificatesUpdateStep(this.lastDetailsAdded, updateType);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateStep(UpdateSteps steps, String details, UpdateType updateType, String[] messageArguments) {
/* 72 */     super.updateStep(steps, details, updateType, messageArguments);
/* 73 */     if (this.wsController != null) {
/* 74 */       this.wsController.newCertificatesUpdateStep(this.lastDetailsAdded, updateType);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateStep(UpdateSteps steps, String details, UpdateType updateType, boolean error) {
/* 80 */     super.updateStep(steps, details, updateType, error);
/* 81 */     if (this.wsController != null) {
/* 82 */       this.wsController.newCertificatesUpdateStep(this.lastDetailsAdded, updateType);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Integer retry(String retryUrl) {
/* 88 */     Integer retry = super.retry(retryUrl);
/* 89 */     if (this.wsController != null) {
/* 90 */       this.wsController.certsUpdateRetry(this.lastDetailsAdded, this.currentRetry.intValue(), this.maxRetries.intValue(), retryUrl, this.nextRetryTimestamp);
/*    */     }
/* 92 */     return retry;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\UpdateSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */