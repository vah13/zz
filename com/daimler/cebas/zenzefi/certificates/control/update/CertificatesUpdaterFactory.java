/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.SecOCISException;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKICertificateRequest;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKIEnhancedCertificateRequest;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.AbstractZenzefiCertificatesUpdater;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.DifferentialCertificateUpdater;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.FullCertificateUpdater;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.util.Base64;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.ApplicationContext;
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
/*     */ @CEBASControl
/*     */ public class CertificatesUpdaterFactory
/*     */ {
/*  37 */   private static final Logger LOG = Logger.getLogger(CertificateParser.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String EMPTY = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String DASH = "-";
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
/*     */   private ApplicationContext context;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public CertificatesUpdaterFactory(MetadataManager i18n, ApplicationContext context) {
/*  70 */     this.i18n = i18n;
/*  71 */     this.context = context;
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
/*     */   public AbstractZenzefiCertificatesUpdater getUpdateCertificatesInstance(UpdateType type) {
/*  84 */     if (type == UpdateType.DIFFERENTIAL)
/*  85 */       return (AbstractZenzefiCertificatesUpdater)this.context.getBean(DifferentialCertificateUpdater.class); 
/*  86 */     if (type == UpdateType.FULL) {
/*  87 */       return (AbstractZenzefiCertificatesUpdater)this.context.getBean(FullCertificateUpdater.class);
/*     */     }
/*  89 */     throw new CEBASCertificateException(this.i18n
/*  90 */         .getMessage("unknownUpdateType"));
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
/*     */   public static PKICertificateRequest getSimplePKIRequest(Certificate timeCsr) {
/* 107 */     PKICertificateRequest pkiCertificateRequest = new PKICertificateRequest(timeCsr.getPkcs10Signature(), timeCsr.getParent().getSubjectKeyIdentifier().replace("-", "").toLowerCase(), timeCsr.getType().name());
/* 108 */     pkiCertificateRequest.setNotBasedOnPermission(true);
/* 109 */     pkiCertificateRequest.setInternalCSRId(timeCsr.getEntityId());
/* 110 */     return pkiCertificateRequest;
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
/*     */   public static PKIEnhancedCertificateRequest getEhhRightSimpleRequest(Certificate secOCISCsr, Certificate diagCert, Logger logger) {
/*     */     PKIEnhancedCertificateRequest pkiCertificateRequest;
/*     */     try {
/* 133 */       pkiCertificateRequest = new PKIEnhancedCertificateRequest(secOCISCsr.getPkcs10Signature(), diagCert.getParent().getSubjectKeyIdentifier().replace("-", "").toLowerCase(), secOCISCsr.getType().name(), Base64.getEncoder().encodeToString(diagCert
/* 134 */             .getCertificateData().getCert().getEncoded()));
/* 135 */       pkiCertificateRequest.setNotBasedOnPermission(true);
/* 136 */       pkiCertificateRequest.setInternalCSRId(secOCISCsr.getEntityId());
/* 137 */     } catch (CertificateEncodingException e) {
/* 138 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 139 */       logger.log(Level.WARNING, "000269", "Could not extract certificate bytes. Reason: " + e
/*     */           
/* 141 */           .getMessage(), com.daimler.cebas.zenzefi.certificates.control.update.CertificatesUpdaterFactory.class
/* 142 */           .getSimpleName());
/* 143 */       throw new SecOCISException("Invalid diagnostic holder certificate.");
/*     */     } 
/*     */     
/* 146 */     return pkiCertificateRequest;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\CertificatesUpdaterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */