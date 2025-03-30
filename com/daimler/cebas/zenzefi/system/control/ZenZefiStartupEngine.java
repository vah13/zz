/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.system.control;
/*    */ 
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.security.SSLValidation;
/*    */ import com.daimler.cebas.zenzefi.configuration.control.PkiScopeManager;
/*    */ import com.daimler.cebas.zenzefi.configuration.control.ZenZefiPkiManager;
/*    */ import com.daimler.cebas.zenzefi.configuration.control.proxy.ProxyManager;
/*    */ import com.daimler.cebas.zenzefi.system.control.AbstractZenzefiStartUpEngine;
/*    */ import com.daimler.cebas.zenzefi.system.control.port.ServerPortManagement;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.boot.context.event.ApplicationReadyEvent;
/*    */ import org.springframework.context.ApplicationEvent;
/*    */ import org.springframework.transaction.annotation.Propagation;
/*    */ import org.springframework.transaction.annotation.Transactional;
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
/*    */ public class ZenZefiStartupEngine
/*    */   extends AbstractZenzefiStartUpEngine
/*    */ {
/*    */   @Autowired
/*    */   private SSLValidation sslValidation;
/*    */   @Autowired
/*    */   private ZenZefiPkiManager zenZefiPkiManager;
/*    */   @Autowired
/*    */   private ProxyManager proxyManager;
/*    */   @Autowired
/*    */   private PkiScopeManager pkiScopeManager;
/*    */   @Autowired
/*    */   private ServerPortManagement serverPortManagement;
/*    */   
/*    */   @Transactional(propagation = Propagation.REQUIRED)
/*    */   public void onApplicationEvent(ApplicationReadyEvent event) {
/* 59 */     startCoupling();
/* 60 */     startLogStore();
/* 61 */     super.onApplicationEvent(event);
/* 62 */     this.proxyManager.setStartupProxy();
/* 63 */     this.pkiScopeManager.setDbStateToSystem();
/* 64 */     this.sslValidation.configure();
/* 65 */     this.zenZefiPkiManager.backupPkiValues();
/* 66 */     this.zenZefiPkiManager.setPkiEnvironmentProperties();
/* 67 */     this.serverPortManagement.setZenZefiPort();
/* 68 */     checkForBackup();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void close() {
/* 73 */     this.serverPortManagement.removeZenZefiPort();
/* 74 */     super.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\system\control\ZenZefiStartupEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */