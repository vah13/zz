/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.config;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.config.handlers.IPkiKnownHandler;
/*    */ import com.daimler.cebas.certificates.control.repository.CertificateRepository;
/*    */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*    */ import com.daimler.cebas.certificates.control.vo.ImportResult;
/*    */ import com.daimler.cebas.certificates.entity.Certificate;
/*    */ import com.daimler.cebas.certificates.entity.CertificateType;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import com.daimler.cebas.zenzefi.certificates.entity.ZenZefiCertificate;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
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
/*    */ 
/*    */ @Component
/*    */ public class ZenZefiPkiKnownHandler
/*    */   implements IPkiKnownHandler<ZenZefiCertificate>
/*    */ {
/*    */   private Session session;
/*    */   private DefaultUpdateSession updateSession;
/*    */   private CertificateRepository certificateRepository;
/*    */   
/*    */   @Autowired
/*    */   public ZenZefiPkiKnownHandler(Session session, DefaultUpdateSession updateSession, CertificateRepository certificateRepository) {
/* 45 */     this.session = session;
/* 46 */     this.updateSession = updateSession;
/* 47 */     this.certificateRepository = certificateRepository;
/*    */   }
/*    */ 
/*    */   
/*    */   public ZenZefiCertificate updateBackendPkiKnown(Certificate certificate) {
/* 52 */     ZenZefiCertificate zenZefiCertificate = (ZenZefiCertificate)certificate;
/* 53 */     if (this.updateSession.isRunning()) {
/* 54 */       zenZefiCertificate.setPkiKnown(Boolean.valueOf(true));
/*    */     } else {
/* 56 */       zenZefiCertificate.setPkiKnown(Boolean.valueOf(false));
/*    */     } 
/*    */     
/* 59 */     return zenZefiCertificate;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean updateBackendPkiKnownOnIdentical(Certificate certificate) {
/* 64 */     if (this.updateSession.isRunning()) {
/* 65 */       Certificate identicalCert = this.certificateRepository.getCertificateBySignatureAndSPK(certificate.getUser(), certificate
/* 66 */           .getSignature(), certificate.getSubjectPublicKey());
/* 67 */       boolean isIdenticalByBytes = Arrays.equals(identicalCert.getCertificateData().getOriginalBytes(), certificate
/* 68 */           .getCertificateData().getOriginalBytes());
/* 69 */       if (isIdenticalByBytes) {
/* 70 */         ZenZefiCertificate zenZefiCertificate = (ZenZefiCertificate)identicalCert;
/* 71 */         if (Boolean.FALSE.equals(zenZefiCertificate.getPkiKnown())) {
/* 72 */           this.certificateRepository.updatePkiKnown(zenZefiCertificate.getEntityId(), Boolean.TRUE);
/* 73 */           return true;
/*    */         } 
/*    */       } 
/*    */     } 
/* 77 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updatePkiKnownForAllUnknownBackends(List<ImportResult> importResult) {
/* 82 */     if (this.updateSession.isNotRunning()) {
/*    */       return;
/*    */     }
/* 85 */     List<ZenZefiCertificate> backends = this.certificateRepository.findByType(ZenZefiCertificate.class, this.session
/* 86 */         .getCurrentUser(), CertificateType.BACKEND_CA_CERTIFICATE);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 91 */     List<String> pkiKnownBackCertIds = (List<String>)backends.stream().filter(certificate -> importResult.stream().anyMatch(())).map(Certificate::getEntityId).collect(Collectors.toList());
/* 92 */     this.certificateRepository.updatePkiKnownForUnknownBackends(this.session.getCurrentUser(), pkiKnownBackCertIds);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPKIKnown(Certificate certificate) {
/* 97 */     return Boolean.TRUE.equals(((ZenZefiCertificate)certificate).getPkiKnown());
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\control\config\ZenZefiPkiKnownHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */