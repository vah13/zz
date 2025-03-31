/*     */ package com.daimler.cebas.users.control.factories;
/*     */ 
/*     */ import com.daimler.cebas.common.control.GenerateSecureRandom;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*     */ import com.daimler.cebas.configuration.entity.Configuration;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.crypto.UserCryptoEngine;
/*     */ import com.daimler.cebas.users.control.exceptions.UserException;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import com.daimler.cebas.users.entity.UserRole;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
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
/*     */ public class UserFactory
/*     */ {
/*  29 */   private static final String CLASS_NAME = UserFactory.class.getSimpleName();
/*     */ 
/*     */   
/*  32 */   private static final Logger LOG = Logger.getLogger(CLASS_NAME);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String defaultUserName;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int SECURE_RANDOM_BYTE_LENGTH = 36;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private AbstractConfigurator configurator;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UserCryptoEngine cryptoEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public UserFactory(UserCryptoEngine cryptoEngine) {
/*  66 */     this.cryptoEngine = cryptoEngine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends User> T getDefaultUser(Class<T> type) {
/*  77 */     String METHOD_NAME = "getDefaultUser";
/*  78 */     this.logger.entering(CLASS_NAME, "getDefaultUser");
/*     */     
/*  80 */     Configuration machineNameConfig = this.configurator.getMachineNameConfiguration();
/*  81 */     String secret = this.configurator.getSecret();
/*     */     
/*  83 */     String salt = GenerateSecureRandom.generateSecureNumber(36) + machineNameConfig.getConfigValue();
/*     */     
/*  85 */     String password = GenerateSecureRandom.generateSecureNumber(36);
/*     */     
/*  87 */     String encryptedPassword = this.cryptoEngine.getAESEncryptedUserPassword(secret, password);
/*  88 */     String encryptedSalt = this.cryptoEngine.getAESEncryptedSalt(secret, salt);
/*     */     try {
/*  90 */       User user = (User)type.newInstance();
/*  91 */       user.setUserName(defaultUserName);
/*  92 */       user.setRole(UserRole.DEFAULT);
/*  93 */       user.setUserPassword(encryptedPassword);
/*  94 */       user.setSalt(encryptedSalt);
/*  95 */       user.getConfigurations()
/*  96 */         .addAll(this.configurator.getDefaultUserProperties());
/*  97 */       String machineName = machineNameConfig.getConfigValue();
/*     */       
/*  99 */       String containerKeyDefaultUser = this.cryptoEngine.generateContainerKey(salt, password, user);
/*     */       
/* 101 */       String machineNameEncoded = this.cryptoEngine.encryptMachineName(containerKeyDefaultUser, machineName);
/* 102 */       machineNameConfig.setConfigValue(machineNameEncoded);
/* 103 */       user.getConfigurations().add(machineNameConfig);
/* 104 */       this.logger.exiting(CLASS_NAME, "getDefaultUser");
/* 105 */       return (T)user;
/* 106 */     } catch (InstantiationException|IllegalAccessException e) {
/* 107 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 108 */       throw new UserException(e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultUsername(String defaultUser) {
/* 119 */     defaultUserName = defaultUser;
/*     */   }
/*     */   
/*     */   public static String getDefaultUsername() {
/* 123 */     return defaultUserName;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\ceba\\users\control\factories\UserFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */