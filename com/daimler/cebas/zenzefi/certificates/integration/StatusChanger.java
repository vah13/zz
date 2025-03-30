/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.integration;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.repository.CertificateRepository;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.zenzefi.certificates.entity.Status;
/*     */ import com.daimler.cebas.zenzefi.certificates.entity.ZenZefiCertificate;
/*     */ import com.daimler.cebas.zenzefi.certificates.integration.PKIErrorPayload;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.transaction.annotation.Propagation;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ import org.springframework.web.client.HttpStatusCodeException;
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
/*     */ public class StatusChanger
/*     */ {
/*     */   private CertificateRepository repository;
/*  36 */   private ObjectMapper mapper = new ObjectMapper();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public StatusChanger(CertificateRepository repository) {
/*  45 */     this.repository = repository;
/*  46 */     this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public void moveCSRToPKIErrorCase(HttpStatusCodeException httpEx, String requestId) {
/*  57 */     Optional<ZenZefiCertificate> csrOptional = this.repository.findCertificate(ZenZefiCertificate.class, requestId);
/*  58 */     String body = httpEx.getResponseBodyAsString();
/*  59 */     if (csrOptional.isPresent()) {
/*  60 */       Optional<PKIErrorPayload> errorPayloadOptional = getErrorPayload(body);
/*  61 */       if (errorPayloadOptional.isPresent()) {
/*  62 */         PKIErrorPayload errorPayload = errorPayloadOptional.get();
/*  63 */         String statusCode = errorPayload.getStatusCode();
/*  64 */         String message = errorPayload.getMessage();
/*  65 */         if (StringUtils.isNoneEmpty(new CharSequence[] { message })) {
/*  66 */           message = "Message: " + message;
/*     */         } else {
/*  68 */           message = "";
/*     */         } 
/*  70 */         if (StringUtils.isNoneEmpty(new CharSequence[] { statusCode })) {
/*  71 */           statusCode = "Status Code: " + statusCode;
/*     */         } else {
/*  73 */           statusCode = "";
/*     */         } 
/*  75 */         ((ZenZefiCertificate)csrOptional.get()).moveToPkiErrorStatus("PKI Error " + message + " " + statusCode);
/*     */       } else {
/*  77 */         ((ZenZefiCertificate)csrOptional.get()).moveToPkiErrorStatus("Unable to parse PKI error payload");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public void moveCSRToNoPermissionsCase(String requestId) {
/*  89 */     Optional<ZenZefiCertificate> csrOptional = this.repository.findCertificate(ZenZefiCertificate.class, requestId);
/*  90 */     if (csrOptional.isPresent()) {
/*  91 */       ((ZenZefiCertificate)csrOptional.get()).moveToNoPermissionsStatus();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public void moveCSRToPKITimeoutState(String requestId) {
/* 102 */     Optional<ZenZefiCertificate> csrOptional = this.repository.findCertificate(ZenZefiCertificate.class, requestId);
/* 103 */     if (csrOptional.isPresent()) {
/* 104 */       ((ZenZefiCertificate)csrOptional.get()).moveToTimeoutStatus();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPKIErrorMessage(String body) {
/* 115 */     Optional<PKIErrorPayload> errorPayload = getErrorPayload(body);
/* 116 */     if (errorPayload.isPresent()) {
/* 117 */       return ((PKIErrorPayload)errorPayload.get()).getMessage();
/*     */     }
/* 119 */     return body;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Optional<PKIErrorPayload> getErrorPayload(String body) {
/*     */     try {
/* 131 */       PKIErrorPayload errorPayload = (PKIErrorPayload)this.mapper.readValue(body, PKIErrorPayload.class);
/* 132 */       return Optional.of(errorPayload);
/* 133 */     } catch (IOException e) {
/* 134 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public void moveAllCSRToPKITimeoutState(List<String> ids) {
/* 140 */     this.repository.updateCertificatesWithStatus(ids, Status.TIMEOUT.getGetCode());
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\integration\StatusChanger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */