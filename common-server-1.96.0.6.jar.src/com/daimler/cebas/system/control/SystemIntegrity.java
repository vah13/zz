/*    */ package com.daimler.cebas.system.control;
/*    */ 
/*    */ import com.daimler.cebas.common.control.CEBASException;
/*    */ import com.daimler.cebas.common.control.HostUtil;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*    */ import com.daimler.cebas.configuration.entity.Configuration;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import com.daimler.cebas.users.control.crypto.UserCryptoEngine;
/*    */ import com.daimler.cebas.users.entity.User;
/*    */ import java.util.Optional;
/*    */ import java.util.logging.Level;
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
/*    */ public class SystemIntegrity
/*    */ {
/* 31 */   private static final String CLASS_NAME = SystemIntegrity.class
/* 32 */     .getSimpleName();
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
/*    */   public static void checkSystemIntegrity(UserCryptoEngine cryptoEngine, AbstractConfigurator configurator, Logger logger, Session session, MetadataManager i18n) {
/* 51 */     String METHOD_NAME = "checkSystemIntegrity";
/* 52 */     logger.entering(CLASS_NAME, "checkSystemIntegrity");
/* 53 */     User user = session.getDefaultUser();
/* 54 */     String secret = configurator.getSecret();
/* 55 */     String decryptedPassword = cryptoEngine.getAESDecryptedUserPassword(secret, user);
/* 56 */     String decryptedSalt = cryptoEngine.getAESDecryptedSalt(secret, user);
/* 57 */     String containerKeyDefaultUser = cryptoEngine.generateContainerKey(decryptedSalt, decryptedPassword, user);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 62 */     Optional<Configuration> machineName = user.getConfigurations().stream().filter(config -> config.getConfigKey().equals("MACHINE_NAME")).findFirst();
/*    */     
/* 64 */     CEBASException ex = new CEBASException(i18n.getMessage("systemIntegrityCheckFailedNoMachineNameConfig"), "systemIntegrityCheckFailedNoMachineNameConfig");
/*    */ 
/*    */ 
/*    */     
/* 68 */     Configuration machineNameConfig = machineName.<Throwable>orElseThrow(logger.logWithTranslationSupplier(Level.SEVERE, "000003X", ex));
/*    */     
/* 70 */     String currentMachineName = HostUtil.getMachineName(logger, i18n);
/* 71 */     String machineNameEncoded = machineNameConfig.getConfigValue();
/*    */     
/* 73 */     String machineNameDecoded = cryptoEngine.decryptMachineName(containerKeyDefaultUser, machineNameEncoded);
/*    */     
/* 75 */     if (!currentMachineName.equals(machineNameDecoded)) {
/*    */       
/* 77 */       CEBASException exception = new CEBASException(i18n.getEnglishMessage("systemIntegrityCheckFailedWrongMachineName"), "systemIntegrityCheckFailedWrongMachineName");
/*    */ 
/*    */       
/* 80 */       logger.logWithTranslation(Level.SEVERE, "000086X", exception
/* 81 */           .getMessageId(), exception
/* 82 */           .getClass().getSimpleName());
/* 83 */       throw exception;
/*    */     } 
/* 85 */     logger.exiting(CLASS_NAME, "checkSystemIntegrity");
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\SystemIntegrity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */