/*     */ package com.daimler.cebas.certificates.boundary;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*     */ import com.daimler.cebas.certificates.control.exceptions.BadFormatException;
/*     */ import com.daimler.cebas.certificates.entity.UserRole;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.util.logging.Level;
/*     */ import org.apache.commons.lang3.StringUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractCertificatesResource
/*     */ {
/*     */   protected static final String MESSAGE = "message";
/*     */   protected static final String CERTIFICATES = "/certificates";
/*     */   protected static final String CERTIFICATES_REMOVE = "/certificates/remove";
/*     */   protected static final String LIST_CERTIFICATES = "/certificates/list";
/*     */   protected static final String LIST_CERTIFICATES_PAGINATED = "/certificates/listPaginated";
/*     */   protected static final String CERTIFICATES_SEARCH = "/certificates/search";
/*     */   protected static final String CHECK_SYSTEM_INTEGRITY = "/certificates/checkSystemIntegrity";
/*     */   protected static final String CHECK_SYSTEM_INTEGRITY_REPORT = "/certificates/checkSystemIntegrityReport";
/*     */   protected static final String CHECK_SYSTEM_INTEGRITY_XML_LOG = "/certificates/checkSystemIntegrityLog";
/*     */   protected static final String CERTIFICATES_RESTORE = "/certificates/restore";
/*     */   protected static final String CERTIFICATES_IMPORT_ENCRYPTED_PKCS_PACKAGE = "/certificates/importProductionCerts";
/*     */   protected static final String SYSTEM_INTEGRITY_LOG_EXISTANCE = "/certificates/checkSystemIntegrityLogExistance";
/*     */   protected static final String ENABLE_ROLLBACK_MODE = "/certificates/enableRollback";
/*     */   protected static final String DISABLE_ROLLBACK_MODE = "/certificates/disableRollback";
/*     */   protected static final String SIGN_ECU_REQUEST = "/certificates/signECU";
/*     */   protected static final String MORE_DETAILS = "/details";
/*     */   protected static final String UPDATE_CERTIFICATES_METRICS = "/certificates/update/metrics";
/*     */   protected static final String UPDATE_SESSION_ACTIVE = "/certificates/update/active";
/*     */   protected static final String CERTIFICATES_FULL_UPDATE = "/certificates/update/full";
/*     */   protected static final String CERTIFICATES_DIFFERENTIAL_UPDATE = "/certificates/update/differential";
/*     */   protected static final String CERTIFICATES_ROOT_BACKENDS = "/certificates/rootsAndBackends";
/*     */   protected Logger logger;
/*     */   protected CertificatesConfiguration certificateProfileConfiguration;
/*     */   protected MetadataManager requestMetaData;
/*     */   
/*     */   public AbstractCertificatesResource(Logger logger, CertificatesConfiguration certificateProfileConfiguration, MetadataManager requestMetaData) {
/* 153 */     this.logger = logger;
/* 154 */     this.certificateProfileConfiguration = certificateProfileConfiguration;
/* 155 */     this.requestMetaData = requestMetaData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validateUserRole(String userRole) {
/* 165 */     if (!StringUtils.isEmpty(userRole) && (!StringUtils.isNumeric(userRole) || (
/* 166 */       StringUtils.isNumeric(userRole) && Byte.valueOf(userRole).byteValue() > 8))) {
/*     */       
/* 168 */       BadFormatException ex = new BadFormatException(this.requestMetaData.getMessage("invalidFormatForUserRole", new String[] {
/* 169 */               UserRole.getValuesMap().toString()
/*     */             }), "invalidFormatForUserRole");
/* 171 */       this.logger.logWithTranslation(Level.WARNING, "000141X", ex.getMessageId(), new String[] {
/* 172 */             UserRole.getValuesMap().toString() }, ex.getClass().getSimpleName());
/* 173 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addRequestMetaData(String locale) {
/* 184 */     this.requestMetaData.setLocale(locale);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\boundary\AbstractCertificatesResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */