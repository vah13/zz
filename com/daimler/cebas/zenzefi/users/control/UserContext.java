/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control;
/*     */ 
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASService;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.zenzefi.users.control.DeleteUserAccountsEngine;
/*     */ import com.daimler.cebas.zenzefi.users.control.login.AbstractLoginAttemptsStrategy;
/*     */ import com.daimler.cebas.zenzefi.users.control.login.AbstractLoginStrategy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASService
/*     */ public class UserContext
/*     */ {
/*     */   private Session session;
/*     */   private OAuth2AuthorizedClientService authClientService;
/*     */   private MetadataManager metadataManager;
/*     */   private Logger logger;
/*     */   private AbstractLoginStrategy loginStrategy;
/*     */   private AbstractLoginAttemptsStrategy loginAttemptsStrategy;
/*     */   private DeleteUserAccountsEngine deleteUserAccountsEngine;
/*  64 */   private static final ConcurrentLinkedQueue<String> loginMessages = new ConcurrentLinkedQueue<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public UserContext(Session session, OAuth2AuthorizedClientService authClientService, MetadataManager metadataManager, Logger logger, AbstractLoginStrategy loginStrategy, AbstractLoginAttemptsStrategy loginAttemptsStrategy, DeleteUserAccountsEngine deleteUserAccountsEngine) {
/*  90 */     this.session = session;
/*  91 */     this.authClientService = authClientService;
/*  92 */     this.metadataManager = metadataManager;
/*  93 */     this.logger = logger;
/*  94 */     this.loginStrategy = loginStrategy;
/*  95 */     this.loginAttemptsStrategy = loginAttemptsStrategy;
/*  96 */     this.deleteUserAccountsEngine = deleteUserAccountsEngine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Session getSession() {
/* 106 */     return this.session;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OAuth2AuthorizedClientService getAuthorizedClientService() {
/* 115 */     return this.authClientService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MetadataManager getMetadataManager() {
/* 124 */     return this.metadataManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getLogger() {
/* 133 */     return this.logger;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractLoginStrategy getLoginStrategy() {
/* 142 */     return this.loginStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractLoginAttemptsStrategy getLoginAttemptsStrategy() {
/* 151 */     return this.loginAttemptsStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeleteUserAccountsEngine getDeleteUserAccountsEngine() {
/* 160 */     return this.deleteUserAccountsEngine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized List<String> getLogingErrorMessages() {
/* 169 */     ArrayList<String> arrayList = new ArrayList<>(loginMessages);
/* 170 */     loginMessages.clear();
/* 171 */     return arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addLogingErrorMessage(String message) {
/* 180 */     loginMessages.add(message);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\UserContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */