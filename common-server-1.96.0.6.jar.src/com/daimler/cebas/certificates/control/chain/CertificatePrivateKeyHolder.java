/*     */ package com.daimler.cebas.certificates.control.chain;
/*     */ 
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import java.security.PrivateKey;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
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
/*     */ public class CertificatePrivateKeyHolder<T extends Certificate>
/*     */ {
/*     */   private T certificate;
/*     */   private String fileName;
/*     */   private Optional<PrivateKey> privateKey;
/*     */   private String internalId;
/*     */   private List<ChainIdentifier> possibleReplaceableCertificates;
/*     */   
/*     */   public CertificatePrivateKeyHolder(T certificate, Optional<PrivateKey> privateKey) {
/*  55 */     this.certificate = certificate;
/*  56 */     this.privateKey = privateKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CertificatePrivateKeyHolder(String fileName, T certificate, Optional<PrivateKey> privateKey) {
/*  67 */     this(certificate, privateKey);
/*  68 */     this.fileName = fileName;
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
/*     */   public CertificatePrivateKeyHolder(String fileName, T certificate, Optional<PrivateKey> privateKey, String friendlyName) {
/*  80 */     this(fileName, certificate, privateKey);
/*  81 */     if (certificate.getType() == CertificateType.BACKEND_CA_CERTIFICATE) {
/*  82 */       certificate.setZkNo(friendlyName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getCertificate() {
/*  92 */     return this.certificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 101 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrivateKey getPrivateKey() {
/* 110 */     if (this.privateKey.isPresent()) {
/* 111 */       return this.privateKey.get();
/*     */     }
/* 113 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPrivateKey() {
/* 122 */     return this.privateKey.isPresent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CertificateType getType() {
/* 131 */     return this.certificate.getType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInternalId(String entityId) {
/* 141 */     this.internalId = entityId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInternalId() {
/* 150 */     return (this.certificate.getEntityId() != null) ? this.certificate.getEntityId() : this.internalId;
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
/*     */   public void addPossibleReplaceableCertificate(List<Certificate> certificates) {
/* 162 */     if (this.possibleReplaceableCertificates == null) {
/* 163 */       this.possibleReplaceableCertificates = new ArrayList<>();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 170 */     List<ChainIdentifier> collect = (List<ChainIdentifier>)certificates.stream().map(cert -> new ChainIdentifier(cert.getAuthorityKeyIdentifier(), cert.getSubjectKeyIdentifier(), cert.getSerialNo(), cert.getSubjectPublicKey())).collect(Collectors.toList());
/* 171 */     this.possibleReplaceableCertificates.addAll(collect);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ChainIdentifier> getPossibleReplacedCertificates() {
/* 180 */     if (this.possibleReplaceableCertificates == null) {
/* 181 */       return new ArrayList<>();
/*     */     }
/* 183 */     return this.possibleReplaceableCertificates;
/*     */   }
/*     */   
/*     */   public int getLevel() {
/* 187 */     return this.certificate.getType().getLevel();
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\chain\CertificatePrivateKeyHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */