/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.ImportSession;
/*     */ import com.daimler.cebas.certificates.control.exceptions.StoreModificationNotAllowed;
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import org.aspectj.lang.ProceedingJoinPoint;
/*     */ import org.aspectj.lang.annotation.Around;
/*     */ import org.aspectj.lang.annotation.Aspect;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
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
/*     */ @Aspect
/*     */ @Component
/*     */ public class StoreLockingInterceptor
/*     */ {
/*  41 */   private String CLASS_NAME = com.daimler.cebas.zenzefi.certificates.control.StoreLockingInterceptor.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String IMPORT = "import";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   private static final List<String> ALLOWED_OPERATIONS_WHILE_PAUSE = Arrays.asList(new String[] { "updateFullCertificates", "updateDifferentialCertificates", "oauthLogin", "updateFullCertificatesV1", "updateFullCertificatesV2", "updateFullCertificatesV3", "updateDifferentialCertificatesV1", "updateDifferentialCertificatesV2", "updateDifferentialCertificatesV3" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UpdateSession updateSession;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MetadataManager i18n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ImportSession importSession;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public StoreLockingInterceptor(UpdateSession updateSession, Logger logger, MetadataManager i18n, ImportSession importSession) {
/*  87 */     this.updateSession = updateSession;
/*  88 */     this.logger = logger;
/*  89 */     this.i18n = i18n;
/*  90 */     this.importSession = importSession;
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Around("execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenzefiCertificatesResource.deleteCertificates(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV1.deleteCertificatesV1(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV2.deleteCertificatesV2(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV3.deleteCertificatesV3(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenzefiCertificatesResource.deleteCertificatesUsingIds(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV1.deleteCertificatesUsingIdsV1(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV2.deleteCertificatesUsingIdsV2(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV3.deleteCertificatesUsingIdsV3(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenzefiCertificatesResource.importCertificatesFromPaths(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV1.importCertificatesFromPathsV1(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV2.importCertificatesFromPathsV2(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV3.importCertificatesFromPathsV3(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResource.importCertificates(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResource.updateActiveForTesting(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResource.activateForTesting(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResource.activateForTestingBasedOnUseCase(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResource.deactivateForTesting(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResource.deactivateForTestingBasedOnUseCase(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenzefiCertificatesResource.restoreCertificates(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV1.restoreCertificatesV1(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV2.restoreCertificatesV2(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV3.restoreCertificatesV3(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenzefiCertificatesResource.importEncryptedPKCSPackage(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV1.importEncryptedPKCSPackageV1(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV2.importEncryptedPKCSPackageV2(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV3.importEncryptedPKCSPackageV3(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResource.updateFullCertificates(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV1.updateFullCertificatesV1(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV2.updateFullCertificatesV2(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV3.updateFullCertificatesV3(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResource.updateDifferentialCertificates(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV1.updateDifferentialCertificatesV1(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV2.updateDifferentialCertificatesV2(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV3.updateDifferentialCertificatesV3(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResource.checkSystemIntegrityWithoutReport(..)) ||execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenzefiCertificatesResource.checkSystemIntegrity(..)) ||execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV1.checkSystemIntegrityV1(..)) ||execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV2.checkSystemIntegrityV2(..)) ||execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV3.checkSystemIntegrityV3(..)) ||execution(* com.daimler.cebas.zenzefi.configuration.boundary.ConfigurationsResource.updateConfiguration(..)) || execution(* com.daimler.cebas.zenzefi.configuration.boundary.ConfigurationsResource.updateConfigurationRolesPriorityOfCurrentUser(..)) || execution(* com.daimler.cebas.zenzefi.configuration.boundary.ConfigurationsResource.setProxy(..)) || execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResource.deleteUsers(..)) ||execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResource.register(..)) ||execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResource.oauthRegister(..)) ||execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResource.oauthLogin(..)) ||execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResource.login(..)) ||execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResourceV1.loginV1(..)) ||execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResourceV2.loginV2(..)) ||execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResourceV3.loginV3(..)) ||execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResource.logoutUser(..)) ||execution(* com.daimler.cebas.zenzefi.users.boundary.UserBackendAppResource.logoutUser(..)) ||execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResourceV1.logoutUserV1(..)) ||execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResourceV2.logoutUserV2(..)) ||execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResourceV3.logoutUserV3(..))")
/*     */   public Object interceptAroundMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
/* 149 */     long currentTimeStamp = (new Date()).getTime();
/* 150 */     String methodName = joinPoint.getSignature().getName();
/* 151 */     if (this.updateSession.isRunning() || (this.updateSession
/* 152 */       .isPaused() && !ALLOWED_OPERATIONS_WHILE_PAUSE.contains(methodName))) {
/* 153 */       this.logger.log(Level.WARNING, "000282", this.i18n
/* 154 */           .getEnglishMessage("updateOperationNotAllowed"), this.CLASS_NAME);
/* 155 */       throw new StoreModificationNotAllowed(this.i18n
/* 156 */           .getMessage("updateOperationNotAllowed"));
/* 157 */     }  if (this.importSession.isRunning()) {
/* 158 */       this.logger.log(Level.WARNING, "000367", this.i18n
/* 159 */           .getEnglishMessage("operationNotAllowedImportIsRunning"), this.CLASS_NAME);
/* 160 */       throw new StoreModificationNotAllowed(this.i18n
/* 161 */           .getMessage(this.i18n.getMessage("operationNotAllowedImportIsRunning")));
/*     */     } 
/* 163 */     if ((currentTimeStamp - this.updateSession.getLastStopTimestamp()) / 1000L < 1L) {
/*     */       
/* 165 */       this.logger.log(Level.WARNING, "000282", this.i18n
/* 166 */           .getEnglishMessage("updateOperationNotAllowed"), this.CLASS_NAME);
/*     */       
/* 168 */       throw new StoreModificationNotAllowed(this.i18n
/* 169 */           .getMessage("updateOperationNotAllowed"));
/*     */     } 
/*     */     try {
/* 172 */       return joinPoint.proceed();
/* 173 */     } catch (CEBASException|javax.persistence.PersistenceException e) {
/* 174 */       checkRunningImport(methodName);
/* 175 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkRunningImport(String methodName) {
/* 186 */     if (this.importSession.isRunning() && methodName.contains("import")) {
/* 187 */       this.importSession.setNotRunning();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getErrorMessageFromException(Throwable e) {
/*     */     try {
/* 199 */       ObjectMapper mapper = new ObjectMapper();
/* 200 */       JsonNode jsonObj = mapper.readTree(e.getMessage());
/* 201 */       return jsonObj.get("message").textValue();
/* 202 */     } catch (IOException parseException) {
/*     */       
/* 204 */       return e.getMessage();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\control\StoreLockingInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */