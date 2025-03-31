/*    */ package com.daimler.cebas.certificates.control.update.task;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
/*    */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*    */ import com.daimler.cebas.certificates.control.update.UpdateSteps;
/*    */ import com.daimler.cebas.certificates.control.update.UpdateTask;
/*    */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*    */ import com.daimler.cebas.certificates.control.validation.PermissionsValidator;
/*    */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*    */ import com.daimler.cebas.certificates.integration.vo.Permission;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.logging.Level;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CEBASControl
/*    */ public abstract class DefaultDownloadPermissionsTask<T extends PublicKeyInfrastructureEsi>
/*    */   extends UpdateTask<T>
/*    */ {
/* 30 */   private static final String CLASS_NAME = DefaultDownloadPermissionsTask.class.getSimpleName();
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultDownloadPermissionsTask(T publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, DefaultUpdateSession updateSession, Logger logger, MetadataManager i18n) {
/* 35 */     super((PublicKeyInfrastructureEsi)publicKeyInfrastructureEsi, importCertificatesEngine, updateSession, logger, i18n);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Permission> execute(UpdateType updateType) {
/* 44 */     if (this.updateSession.isRunning()) {
/* 45 */       this.updateSession.updateStep(UpdateSteps.RETRIEVE_PERMISSIONS, "updateStartDownloadUserPermissions", updateType);
/*    */       
/* 47 */       logBeforeRetrievePermissions(updateType);
/* 48 */       List<Permission> permissions = this.publicKeyInfrastructureEsi.getPermissions();
/* 49 */       logAfterRetrievePermissions(updateType, permissions);
/* 50 */       this.updateSession.updateStep(UpdateSteps.RETRIEVE_PERMISSIONS, "updateStopDownloadUserPermissions", updateType);
/*    */       
/* 52 */       this.updateSession.addStepResult(UpdateSteps.RETRIEVE_PERMISSIONS, permissions);
/* 53 */       this.updateSession.resetRetries();
/*    */       
/* 55 */       return (List<Permission>)permissions.stream()
/* 56 */         .filter(permission -> PermissionsValidator.validate(permission, this.logger, this.i18n))
/* 57 */         .collect(Collectors.toList());
/*    */     } 
/* 59 */     return Collections.emptyList();
/*    */   }
/*    */ 
/*    */   
/*    */   private void logAfterRetrievePermissions(UpdateType updateType, List<Permission> permissions) {
/* 64 */     this.logger.log(Level.INFO, "000237", this.i18n
/* 65 */         .getEnglishMessage("updateReceivingUserPermissions", new String[] { updateType.name() }), CLASS_NAME);
/*    */     
/* 67 */     this.logger.log(Level.INFO, "000226", this.i18n.getEnglishMessage("updateReceivedUserPermissions", new String[] { updateType
/* 68 */             .name(), createPermissionsLoggingString(permissions) }), CLASS_NAME);
/* 69 */     this.logger.log(Level.INFO, "000238", this.i18n.getEnglishMessage("updateStopDownloadUserPermissions", new String[] { updateType
/* 70 */             .name() }), CLASS_NAME);
/*    */   }
/*    */   
/*    */   private void logBeforeRetrievePermissions(UpdateType updateType) {
/* 74 */     this.logger.log(Level.INFO, "000235", this.i18n.getEnglishMessage("updateStartDownloadUserPermissions", new String[] { updateType
/* 75 */             .name() }), CLASS_NAME);
/* 76 */     this.logger.log(Level.INFO, "000236", this.i18n.getEnglishMessage("updateRequestingUserPermissions", new String[] { updateType
/* 77 */             .name() }), CLASS_NAME);
/*    */   }
/*    */   
/*    */   private String createPermissionsLoggingString(List<Permission> permissions) {
/* 81 */     return permissions.isEmpty() ? "[]" : permissions.stream().map(Permission::toString).collect(Collectors.joining(","));
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\contro\\update\task\DefaultDownloadPermissionsTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */