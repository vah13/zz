/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.config;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.CertificateUtil;
/*    */ import com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory;
/*    */ import com.daimler.cebas.certificates.control.vo.AbstractCertificateReplacementPackageInput;
/*    */ import com.daimler.cebas.certificates.entity.Certificate;
/*    */ import com.daimler.cebas.certificates.integration.vo.NonVsmCertificateRequest;
/*    */ import com.daimler.cebas.certificates.integration.vo.PKIEcuCertificateResponse;
/*    */ import com.daimler.cebas.certificates.integration.vo.PKILinkCertificateResponse;
/*    */ import com.daimler.cebas.certificates.integration.vo.VsmCertificateRequest;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.config.IDownloadCertificatesHandler;
/*    */ import com.daimler.cebas.zenzefi.certificates.integration.ZenZefiPublicKeyInfrastructureEsi;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class ZenZefiDownloadCertificatesHandler
/*    */   implements IDownloadCertificatesHandler
/*    */ {
/*    */   private ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi;
/*    */   private AbstractCertificateFactory factory;
/*    */   
/*    */   @Autowired
/*    */   public ZenZefiDownloadCertificatesHandler(ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi, AbstractCertificateFactory factory) {
/* 50 */     this.publicKeyInfrastructureEsi = publicKeyInfrastructureEsi;
/* 51 */     this.factory = factory;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> downloadEcuCertificates(Certificate backend, AbstractCertificateReplacementPackageInput input) {
/* 56 */     Certificate inputEcuCert = this.factory.getCertificateFromBase64(input.getCertificate());
/* 57 */     if (inputEcuCert.isVsmEcu()) {
/* 58 */       VsmCertificateRequest vsmCertificateRequest = new VsmCertificateRequest();
/* 59 */       vsmCertificateRequest.setCaId(backend.getSubjectKeyIdentifier().replaceAll("-", ""));
/* 60 */       vsmCertificateRequest.setVsmCert(input.getCertificate());
/* 61 */       vsmCertificateRequest.setVin(input.getTargetVIN());
/* 62 */       List<PKIEcuCertificateResponse> list = this.publicKeyInfrastructureEsi.downloadVsmCertificates(Collections.singletonList(vsmCertificateRequest), false);
/* 63 */       return (List<String>)list.stream().map(PKIEcuCertificateResponse::getEcuCert).collect(Collectors.toList());
/*    */     } 
/* 65 */     NonVsmCertificateRequest nonVsmCertificateRequest = new NonVsmCertificateRequest();
/* 66 */     nonVsmCertificateRequest.setCaId(backend.getSubjectKeyIdentifier().replaceAll("-", ""));
/* 67 */     nonVsmCertificateRequest.setCn(inputEcuCert.getSubject().replaceFirst("CN=", ""));
/* 68 */     nonVsmCertificateRequest.setSki(inputEcuCert.getSubjectKeyIdentifier().replaceAll("-", ""));
/* 69 */     List<String> ecuUniqueIds = new ArrayList<>(CertificateUtil.asList(inputEcuCert.getUniqueECUID(), ","));
/* 70 */     ecuUniqueIds.addAll(CertificateUtil.asList(input.getUniqueEcuId(), ","));
/* 71 */     nonVsmCertificateRequest.setEcuUniqueIds(ecuUniqueIds);
/* 72 */     List<PKIEcuCertificateResponse> ecus = this.publicKeyInfrastructureEsi.downloadNonVsmCertificates(Collections.singletonList(nonVsmCertificateRequest), false);
/* 73 */     return (List<String>)ecus.stream().map(PKIEcuCertificateResponse::getEcuCert).collect(Collectors.toList());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> downloadLinkCertificates(Certificate target, Certificate source) {
/* 79 */     List<PKILinkCertificateResponse> linkCerts = this.publicKeyInfrastructureEsi.downloadLinkCertificates(source.getSubjectKeyIdentifier(), target.getSubjectKeyIdentifier(), false);
/* 80 */     return (List<String>)linkCerts.stream().map(PKILinkCertificateResponse::getLinkCert).collect(Collectors.toList());
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\control\config\ZenZefiDownloadCertificatesHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */