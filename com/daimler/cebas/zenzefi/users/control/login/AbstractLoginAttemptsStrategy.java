/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control.login;
/*    */ 
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import com.daimler.cebas.zenzefi.users.control.DeleteUserAccountsEngine;
/*    */ import com.daimler.cebas.zenzefi.users.entity.ZenZefiUser;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractLoginAttemptsStrategy
/*    */ {
/*    */   protected Logger logger;
/*    */   protected MetadataManager i18n;
/*    */   protected AbstractConfigurator configurator;
/*    */   protected Session session;
/*    */   protected DeleteUserAccountsEngine deleteUserAccountsEngine;
/*    */   
/*    */   public AbstractLoginAttemptsStrategy(Logger logger, MetadataManager i18n, AbstractConfigurator configurator, Session session, DeleteUserAccountsEngine deleteUserAccountsEngine) {
/* 60 */     this.logger = logger;
/* 61 */     this.i18n = i18n;
/* 62 */     this.configurator = configurator;
/* 63 */     this.session = session;
/* 64 */     this.deleteUserAccountsEngine = deleteUserAccountsEngine;
/*    */   }
/*    */   
/*    */   public abstract void handleInvalidLoginAttempt(ZenZefiUser paramZenZefiUser, String paramString);
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\login\AbstractLoginAttemptsStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */