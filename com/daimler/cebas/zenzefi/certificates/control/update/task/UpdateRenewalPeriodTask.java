/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.CertificateUtil;
/*     */ import com.daimler.cebas.certificates.control.DurationParser;
/*     */ import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
/*     */ import com.daimler.cebas.certificates.control.SearchEngine;
/*     */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateTask;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.entity.PKIRole;
/*     */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*     */ import com.daimler.cebas.certificates.integration.vo.EnhancedRightsPermission;
/*     */ import com.daimler.cebas.certificates.integration.vo.Permission;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*     */ import com.daimler.cebas.zenzefi.certificates.entity.ZenZefiCertificate;
/*     */ import com.daimler.cebas.zenzefi.certificates.integration.ZenZefiPublicKeyInfrastructureEsi;
/*     */ import java.sql.Timestamp;
/*     */ import java.time.LocalDateTime;
/*     */ import java.time.ZoneId;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ @CEBASControl
/*     */ public class UpdateRenewalPeriodTask
/*     */   extends UpdateTask<ZenZefiPublicKeyInfrastructureEsi>
/*     */ {
/*     */   private SearchEngine searchEngine;
/*     */   private Session session;
/*     */   
/*     */   @Autowired
/*     */   UpdateRenewalPeriodTask(ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, UpdateSession updateSession, Logger logger, MetadataManager i18n, SearchEngine searchEngine, Session session) {
/*  65 */     super((PublicKeyInfrastructureEsi)publicKeyInfrastructureEsi, importCertificatesEngine, (DefaultUpdateSession)updateSession, logger, i18n);
/*  66 */     this.searchEngine = searchEngine;
/*  67 */     this.session = session;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional
/*     */   public void execute(List<Permission> permissions) {
/*  77 */     if (this.updateSession.isRunning()) {
/*  78 */       User currentUser = this.session.getCurrentUser();
/*  79 */       List<Certificate> backends = this.searchEngine.findAllBackends(currentUser);
/*  80 */       List<String> akis = (List<String>)backends.stream().map(Certificate::getSubjectKeyIdentifier).collect(Collectors.toList());
/*  81 */       List<ZenZefiCertificate> certificates = this.searchEngine.getCertsByAKI(currentUser, akis, ZenZefiCertificate.class);
/*  82 */       Map<CertificateType, List<Permission>> permissionsByType = (Map<CertificateType, List<Permission>>)permissions.stream().collect(Collectors.groupingBy(this::getRole));
/*     */       
/*  84 */       List<EnhancedRightsPermission> enhancedRightsPermissions = new ArrayList<>();
/*  85 */       if (permissionsByType.containsKey(CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  91 */         enhancedRightsPermissions = (List<EnhancedRightsPermission>)((List)permissionsByType.get(CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE)).stream().filter(p -> !CollectionUtils.isEmpty(p.getServices())).map(Permission::getServices).flatMap(Collection::stream).collect(Collectors.toList());
/*     */       }
/*     */       
/*  94 */       for (ZenZefiCertificate certificate : certificates) {
/*  95 */         if (CertificateType.ENHANCED_RIGHTS_CERTIFICATE == certificate.getType()) {
/*  96 */           enhancedRightsPermissions
/*  97 */             .stream()
/*  98 */             .filter(p -> CertificateUtil.isCertificateMatchingEnhancedRightsPermission((Certificate)certificate, p))
/*  99 */             .forEach(p -> setRenewalTime(certificate, p.getRenewal())); continue;
/* 100 */         }  if (permissionsByType.containsKey(certificate.getType())) {
/* 101 */           ((List)permissionsByType.get(certificate.getType()))
/* 102 */             .stream()
/* 103 */             .filter(p -> CertificateUtil.isCertificateMatchingPermission((Certificate)certificate, p))
/* 104 */             .forEach(p -> setRenewalTime(certificate, p.getRenewal()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private CertificateType getRole(Permission p) {
/* 111 */     return (CertificateType)PKIRole.getRoles().get(Integer.decode(p.getPkiRole()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setRenewalTime(ZenZefiCertificate certificate, String renewal) {
/* 121 */     LocalDateTime validTo = certificate.getValidTo().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
/* 122 */     LocalDateTime renewalDate = DurationParser.parse(renewal).minusFrom(validTo);
/* 123 */     certificate.setRenewal(String.valueOf(Timestamp.valueOf(renewalDate).getTime()));
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\UpdateRenewalPeriodTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */