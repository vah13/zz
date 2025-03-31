/*    */ package com.daimler.cebas.certificates.control.chain;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.SearchEngine;
/*    */ import com.daimler.cebas.certificates.control.config.handlers.IAddUnderBackendHandler;
/*    */ import com.daimler.cebas.certificates.control.validation.ValidationError;
/*    */ import com.daimler.cebas.certificates.entity.Certificate;
/*    */ import com.daimler.cebas.users.entity.User;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ public class AddUnderBackendHandlerDefault<T extends Certificate>
/*    */   implements IAddUnderBackendHandler<T>
/*    */ {
/*    */   protected UnderBackendChainOfTrust chain;
/*    */   protected SearchEngine searchEngine;
/*    */   
/*    */   public AddUnderBackendHandlerDefault(UnderBackendChainOfTrust chain, SearchEngine searchEngine) {
/* 19 */     this.chain = chain;
/* 20 */     this.searchEngine = searchEngine;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean addUnderBackend(CertificatePrivateKeyHolder<Certificate> holder, List<ValidationError> errors, T backend) {
/* 25 */     boolean result = false;
/* 26 */     Certificate certificate = holder.getCertificate();
/* 27 */     User user = certificate.getUser();
/* 28 */     Optional<Certificate> csr = this.searchEngine.findCsrByPublicKey(user, certificate.getSubjectPublicKey());
/* 29 */     boolean extendedValidation = this.chain.profileConfiguration.shouldDoExtendedValidation();
/* 30 */     String authorityKeyIdentifier = certificate.getAuthorityKeyIdentifier();
/* 31 */     String serialNo = certificate.getSerialNo();
/* 32 */     if (!extendedValidation || this.chain.repo.checkUniqueAuthKeyAndSN(user, authorityKeyIdentifier, serialNo, (Certificate)backend)) {
/* 33 */       if (holder.hasPrivateKey() && csr.isPresent()) {
/* 34 */         this.chain.errorHasPrivateKeyAndFoundCSR(certificate, errors);
/* 35 */       } else if (holder.hasPrivateKey() && !csr.isPresent()) {
/* 36 */         result = this.chain.addCertificateWithPrivateKey(holder, errors, (Certificate)backend, certificate, extendedValidation);
/* 37 */       } else if (!holder.hasPrivateKey() && csr.isPresent()) {
/* 38 */         result = this.chain.addCertificateWithCSR(holder, errors, certificate, csr.get(), extendedValidation);
/*    */       } else {
/* 40 */         result = this.chain.checkEcuCertAndCsrExists(errors, (Certificate)backend, certificate);
/*    */       } 
/*    */     } else {
/* 43 */       this.chain.errorCertificateWithSameAuthKeyAndSerialNumber(certificate, errors);
/*    */     } 
/*    */     
/* 46 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\chain\AddUnderBackendHandlerDefault.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */