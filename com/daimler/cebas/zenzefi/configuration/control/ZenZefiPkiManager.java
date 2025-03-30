/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.configuration.control;
/*    */ 
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.zenzefi.configuration.control.PkiProdEnvironmentManager;
/*    */ import com.daimler.cebas.zenzefi.configuration.control.PkiTestEnvironmentManager;
/*    */ import com.daimler.cebas.zenzefi.configuration.control.util.ZenZefiPkiAndOidcPropertiesManager;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CEBASControl
/*    */ public class ZenZefiPkiManager
/*    */ {
/*    */   private PkiProdEnvironmentManager pkiProdEnvironmentManager;
/*    */   private PkiTestEnvironmentManager pkiTestEnvironmentManager;
/*    */   private ZenZefiPkiAndOidcPropertiesManager pkiAndOAuthPropertiesManager;
/*    */   
/*    */   @Autowired
/*    */   public ZenZefiPkiManager(PkiProdEnvironmentManager pkiProdEnvironmentManager, PkiTestEnvironmentManager pkiTestEnvironmentManager, ZenZefiPkiAndOidcPropertiesManager pkiAndOAuthPropertiesManager) {
/* 50 */     this.pkiProdEnvironmentManager = pkiProdEnvironmentManager;
/* 51 */     this.pkiTestEnvironmentManager = pkiTestEnvironmentManager;
/* 52 */     this.pkiAndOAuthPropertiesManager = pkiAndOAuthPropertiesManager;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPkiEnvironmentProperties() {
/* 59 */     if (this.pkiAndOAuthPropertiesManager.getPkiEnvironment().equalsIgnoreCase("TEST")) {
/* 60 */       this.pkiTestEnvironmentManager.handleEnvironment();
/* 61 */     } else if (this.pkiAndOAuthPropertiesManager.getPkiEnvironment().equalsIgnoreCase("PROD")) {
/* 62 */       this.pkiProdEnvironmentManager.handleEnvironment();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void backupPkiValues() {
/* 67 */     this.pkiProdEnvironmentManager.storeOriginalPkiValues();
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\configuration\control\ZenZefiPkiManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */