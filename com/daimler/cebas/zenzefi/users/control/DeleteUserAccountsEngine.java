/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control;
/*    */ 
/*    */ import com.daimler.cebas.certificates.entity.Certificate;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.common.entity.AbstractEntity;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.users.control.UserRepository;
/*    */ import com.daimler.cebas.users.entity.User;
/*    */ import java.util.List;
/*    */ import java.util.logging.Level;
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
/*    */ @CEBASControl
/*    */ public class DeleteUserAccountsEngine
/*    */ {
/* 29 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.users.control.DeleteUserAccountsEngine.class
/* 30 */     .getSimpleName();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Logger logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private UserRepository repository;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private MetadataManager i18n;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DeleteUserAccountsEngine(Logger logger, UserRepository repository, MetadataManager i18n) {
/* 59 */     this.logger = logger;
/* 60 */     this.repository = repository;
/* 61 */     this.i18n = i18n;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void deleteAccount(String id) {
/* 71 */     String METHOD_NAME = "deleteAccount";
/* 72 */     this.logger.entering(CLASS_NAME, "deleteAccount");
/* 73 */     User user = this.repository.findUserById(id);
/* 74 */     this.repository.deleteManagedEntity((AbstractEntity)user);
/* 75 */     user.getConfigurations().clear();
/* 76 */     user.getKeyPairs().clear();
/* 77 */     List<Certificate> certificates = user.getCertificates();
/* 78 */     certificates.forEach(certificate -> this.repository.deleteManagedEntity((AbstractEntity)certificate));
/* 79 */     this.logger.log(Level.INFO, "000065", this.i18n
/* 80 */         .getEnglishMessage("deleteAccountWasCalled"), CLASS_NAME);
/* 81 */     this.logger.exiting(CLASS_NAME, "deleteAccount");
/*    */   }
/*    */   
/*    */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*    */   public void deleteAccNewTransc(String id) {
/* 86 */     deleteAccount(id);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\DeleteUserAccountsEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */