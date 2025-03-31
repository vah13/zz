/*     */ package com.daimler.cebas.certificates.control.update;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.exceptions.InvalidTokenException;
/*     */ import com.daimler.cebas.certificates.control.update.task.AbstractDownloadBackendsTask;
/*     */ import com.daimler.cebas.certificates.control.update.task.UpdateRootAndBackendsResult;
/*     */ import com.daimler.cebas.certificates.control.vo.DownloadBackendsResult;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.integration.vo.Permission;
/*     */ import com.daimler.cebas.common.control.ApplicationInvalidState;
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationEventPublisher;
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
/*     */ public abstract class AbstractCertificatesUpdater
/*     */ {
/*     */   protected Logger logger;
/*     */   protected MetadataManager i18n;
/*     */   protected DefaultUpdateSession updateSession;
/*     */   protected UpdateType updateType;
/*     */   protected AbstractDownloadBackendsTask downloadBackendsTask;
/*     */   @Autowired
/*     */   protected ApplicationEventPublisher applicationEventPublisher;
/*     */   @Value("${pki.base.url}")
/*     */   private String pkiBaseUrl;
/*     */   
/*     */   public AbstractCertificatesUpdater(Logger logger, MetadataManager i18n, DefaultUpdateSession updateSession, AbstractDownloadBackendsTask downloadBackendsTask) {
/*  85 */     this.logger = logger;
/*  86 */     this.i18n = i18n;
/*  87 */     this.updateSession = updateSession;
/*  88 */     this.downloadBackendsTask = downloadBackendsTask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void updateCertificates();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setUpdateType(UpdateType updateType) {
/* 102 */     this.updateType = updateType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DownloadBackendsResult addDownloadBackendsErrorState(Throwable e) {
/* 113 */     String errorMessage = e.getMessage().replaceAll("[\r\n]+", "");
/* 114 */     if (errorMessage.contains("Invalid token")) {
/* 115 */       invalidTokenErrorCase(errorMessage);
/* 116 */       errorMessage = handleInvalidToken();
/*     */     } else {
/* 118 */       generalErrorCase(this.i18n
/* 119 */           .getMessage("certificatesDownloadBackendsError", new String[] { errorMessage }));
/* 120 */       errorMessage = this.i18n.getEnglishMessage("certificatesDownloadBackendsError", new String[] { errorMessage });
/*     */     } 
/*     */     
/* 123 */     if (shouldLogStacktrace(e)) {
/* 124 */       this.logger.logToFileOnly(getClass().getSimpleName(), errorMessage, e);
/*     */     }
/* 126 */     this.logger.log(Level.SEVERE, "000462X", errorMessage, getClass().getSimpleName());
/* 127 */     return new DownloadBackendsResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Permission> addPermissionsErrorState(Throwable e) {
/* 137 */     String errorMessage = e.getMessage();
/* 138 */     if (errorMessage.contains("Invalid token")) {
/* 139 */       invalidTokenErrorCase(errorMessage);
/* 140 */       errorMessage = handleInvalidToken();
/*     */     } else {
/*     */       String message;
/* 143 */       if (e.getCause() != null && e.getCause() instanceof com.daimler.cebas.certificates.control.exceptions.EmptyPermissionsException) {
/* 144 */         message = this.i18n.getMessage("noPermissionsErrorWithLink", new String[] { this.pkiBaseUrl + "/ui/" }, this.updateSession
/* 145 */             .getSessionLocale());
/*     */       } else {
/* 147 */         message = this.i18n.getMessage("permissionsError");
/*     */       } 
/* 149 */       generalErrorCase(message);
/*     */     } 
/* 151 */     if (shouldLogStacktrace(e)) {
/* 152 */       this.logger.logToFileOnly(getClass().getSimpleName(), errorMessage, e);
/* 153 */       this.logger.log(Level.SEVERE, "000289X", "Error while downloading permissions, please check the logfile for more information", 
/*     */           
/* 155 */           getClass().getSimpleName());
/*     */     } else {
/* 157 */       this.logger.log(Level.SEVERE, "000289X", "Error while downloading permissions - " + errorMessage, 
/* 158 */           getClass().getSimpleName());
/*     */     } 
/*     */     
/* 161 */     return new ArrayList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Void addDownloadCertificatesErrorStateForEcu(Throwable e) {
/* 170 */     this.updateSession.resetEcuCondition(Boolean.valueOf(true));
/* 171 */     return addDownloadCertificatesErrorState(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Void addDownloadCertificatesErrorStateForLink(Throwable e) {
/* 181 */     this.updateSession.resetLinkCondition(Boolean.valueOf(true));
/* 182 */     return addDownloadCertificatesErrorState(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Void addDownloadCertificatesErrorState(Throwable e) {
/* 191 */     String errorMessage = e.getMessage();
/* 192 */     if (CEBASException.hasCause(e, InvalidTokenException.class)) {
/* 193 */       invalidTokenErrorCase(errorMessage);
/* 194 */       errorMessage = handleInvalidToken();
/*     */     } else {
/* 196 */       generalErrorCase(errorMessage);
/*     */     } 
/* 198 */     if (shouldLogStacktrace(e)) {
/* 199 */       this.logger.logToFileOnly(getClass().getSimpleName(), errorMessage, e);
/*     */     }
/* 201 */     this.logger.log(Level.SEVERE, "000461X", errorMessage, getClass().getSimpleName());
/* 202 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void invalidTokenErrorCase(String errorMessage) {
/* 211 */     this.updateSession.handleInvalidToken(errorMessage, this.updateType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String handleInvalidToken() {
/* 220 */     String errorMessage = (null == System.getProperty("spring.profiles.active")) ? "Update session failed due to invalid token. Authorization Error, user will be switched to offline mode" : "Update session failed due to invalid token. Authorization Error";
/*     */ 
/*     */     
/* 223 */     generalErrorCase(errorMessage);
/* 224 */     this.applicationEventPublisher.publishEvent((ApplicationEvent)new ApplicationInvalidState(this));
/* 225 */     return errorMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generalErrorCase(String errorMessage) {
/* 234 */     this.updateSession.updateStep(this.updateSession.getCurrentStep(), errorMessage, this.updateType, true);
/* 235 */     this.updateSession.setNotRunning();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<CertificateType, UpdateRootAndBackendsResult> addUpdateBackendsErrorState(Throwable e) {
/* 245 */     String errorMessage = e.getMessage();
/* 246 */     generalErrorCase(errorMessage);
/* 247 */     this.logger.log(Level.SEVERE, "000540X", errorMessage, getClass().getSimpleName());
/* 248 */     Map<CertificateType, UpdateRootAndBackendsResult> emptyResult = new HashMap<>();
/* 249 */     UpdateRootAndBackendsResult emptyBResult = new UpdateRootAndBackendsResult();
/* 250 */     emptyResult.put(CertificateType.ECU_CERTIFICATE, emptyBResult);
/* 251 */     emptyResult.put(CertificateType.BACKEND_CA_LINK_CERTIFICATE, emptyBResult);
/* 252 */     return emptyResult;
/*     */   }
/*     */   
/*     */   private boolean shouldLogStacktrace(Throwable e) {
/* 256 */     if (e == null) {
/* 257 */       return false;
/*     */     }
/* 259 */     if (e instanceof com.daimler.cebas.certificates.control.exceptions.EmptyPermissionsException || (e
/* 260 */       .getCause() != null && e.getCause() instanceof com.daimler.cebas.certificates.control.exceptions.EmptyPermissionsException)) {
/* 261 */       return false;
/*     */     }
/* 263 */     if (e instanceof InvalidTokenException || (e
/* 264 */       .getCause() != null && e.getCause() instanceof InvalidTokenException)) {
/* 265 */       return false;
/*     */     }
/* 267 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\contro\\update\AbstractCertificatesUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */