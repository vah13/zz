/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.importing.CertificatesImporter;
/*     */ import com.daimler.cebas.common.control.GenerateSecureRandom;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.common.entity.AbstractEntity;
/*     */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*     */ import com.daimler.cebas.configuration.entity.Configuration;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.control.UserRepository;
/*     */ import com.daimler.cebas.users.control.crypto.UserCryptoEngine;
/*     */ import com.daimler.cebas.users.control.exceptions.UserValidationException;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import com.daimler.cebas.zenzefi.users.control.UserContext;
/*     */ import com.daimler.cebas.zenzefi.users.control.validation.IUserValidator;
/*     */ import com.daimler.cebas.zenzefi.users.entity.ZenZefiUser;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import org.springframework.transaction.annotation.Propagation;
/*     */ import org.springframework.transaction.annotation.Transactional;
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
/*     */ @CEBASControl
/*     */ public class UserRegistrationEngine
/*     */ {
/*  38 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.users.control.UserRegistrationEngine.class
/*  39 */     .getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private IUserValidator userValidator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Session session;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MetadataManager i18n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractConfigurator configurator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UserCryptoEngine cryptoEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UserRepository repository;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CertificatesImporter certificatesImporter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserRegistrationEngine(IUserValidator userValidator, AbstractConfigurator configurator, UserCryptoEngine cryptoEngine, UserRepository repository, CertificatesImporter certificatesImporter, UserContext userContext) {
/* 102 */     this.logger = userContext.getLogger();
/* 103 */     this.userValidator = userValidator;
/* 104 */     this.session = userContext.getSession();
/* 105 */     this.i18n = userContext.getMetadataManager();
/* 106 */     this.configurator = configurator;
/* 107 */     this.cryptoEngine = cryptoEngine;
/* 108 */     this.repository = repository;
/* 109 */     this.certificatesImporter = certificatesImporter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public ZenZefiUser register(ZenZefiUser user) {
/* 121 */     String METHOD_NAME = "register";
/* 122 */     this.logger.entering(CLASS_NAME, "register");
/* 123 */     this.userValidator.validateUser((User)user);
/* 124 */     if (this.session.userExists(user.getUserName())) {
/* 125 */       user.setUserName("UserNameNotUnique");
/* 126 */       UserValidationException exception = new UserValidationException((User)user, this.i18n.getMessage("userAlreadyExists"), "userAlreadyExists");
/* 127 */       this.logger.logWithTranslation(Level.WARNING, "000005X", exception.getMessageId(), exception.getClass().getSimpleName());
/* 128 */       throw exception;
/*     */     } 
/*     */     
/* 131 */     String secret = this.configurator.getSecret();
/* 132 */     String salt = GenerateSecureRandom.generateSecureNumber(36);
/* 133 */     String machineName = this.configurator.getMachineNameConfiguration().getConfigValue();
/* 134 */     user.setSalt(this.cryptoEngine.getAESEncryptedSalt(secret, salt));
/* 135 */     user.setUserPassword(this.cryptoEngine.encryptMachineNameUsingPasswordHash(secret, machineName, (User)user));
/* 136 */     user.setConfigurations(getUserConfigurations());
/* 137 */     ZenZefiUser createdUser = (ZenZefiUser)this.repository.create((AbstractEntity)user);
/* 138 */     this.certificatesImporter.importFromBaseStore((User)user);
/* 139 */     this.logger.log(Level.INFO, "000036", this.i18n.getEnglishMessage("userWasRegistered", new String[] { user.getUserName() }), CLASS_NAME);
/* 140 */     this.logger.exiting(CLASS_NAME, "register");
/* 141 */     return createdUser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Configuration> getUserConfigurations() {
/* 150 */     List<Configuration> configurations = new ArrayList<>();
/* 151 */     this.configurator.getRegisteredUserProperties()
/* 152 */       .forEach(configuration -> configurations.add(this.configurator.copy(configuration)));
/*     */     
/* 154 */     return configurations;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\UserRegistrationEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */