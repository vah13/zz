/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.DeleteCertificatesEngine;
/*    */ import com.daimler.cebas.certificates.control.SearchEngine;
/*    */ import com.daimler.cebas.certificates.control.chain.events.DeleteCertsFromKnownBackends;
/*    */ import com.daimler.cebas.certificates.entity.Certificate;
/*    */ import com.daimler.cebas.certificates.entity.CertificateType;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import org.springframework.context.event.EventListener;
/*    */ import org.springframework.transaction.annotation.Transactional;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CEBASControl
/*    */ public class DeleteEventsForKnownPKIBackendsListener
/*    */ {
/*    */   private DeleteCertificatesEngine deleteCertificatesEngine;
/*    */   private SearchEngine searchEngine;
/*    */   private Session session;
/*    */   
/*    */   public DeleteEventsForKnownPKIBackendsListener(Session session, SearchEngine searchEngine, DeleteCertificatesEngine deleteCertificatesEngine) {
/* 27 */     this.deleteCertificatesEngine = deleteCertificatesEngine;
/* 28 */     this.searchEngine = searchEngine;
/* 29 */     this.session = session;
/*    */   }
/*    */ 
/*    */   
/*    */   @EventListener
/*    */   @Transactional
/*    */   public void deleteUnderBackend(DeleteCertsFromKnownBackends event) {
/* 36 */     List<ImportResult> backends = event.getBackends();
/* 37 */     backends.forEach(this::delete);
/*    */   }
/*    */ 
/*    */   
/*    */   private void delete(ImportResult backend) {
/* 42 */     Optional<Certificate> backendCertOptional = this.searchEngine.findCertificateBySkiAkiAndType(this.session.getCurrentUser(), backend
/* 43 */         .getSubjectKeyIdentifier(), backend
/* 44 */         .getAuthorityKeyIdentifier(), CertificateType.BACKEND_CA_CERTIFICATE);
/*    */     
/* 46 */     if (backendCertOptional.isPresent()) {
/* 47 */       Certificate backendCertificate = backendCertOptional.get();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 53 */       List<String> ids = (List<String>)backendCertificate.getChildren().stream().filter(this::noTime).filter(this::noVSMCertificate).map(Certificate::getEntityId).collect(Collectors.toList());
/* 54 */       if (ids.isEmpty()) {
/*    */         return;
/*    */       }
/* 57 */       this.deleteCertificatesEngine.deleteCertificates(ids);
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean noVSMCertificate(Certificate certificate) {
/* 62 */     return this.searchEngine.hasPrivateKey(certificate);
/*    */   }
/*    */   
/*    */   private boolean noTime(Certificate certificate) {
/* 66 */     return (certificate.getType() != CertificateType.TIME_CERTIFICATE);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\DeleteEventsForKnownPKIBackendsListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */