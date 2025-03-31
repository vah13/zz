/*     */ package com.daimler.cebas.certificates.control.vo;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.validation.failure.ValidationState;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*     */ import io.swagger.annotations.ApiModel;
/*     */ import io.swagger.annotations.ApiModelProperty;
/*     */ import java.util.Date;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ @ApiModel
/*     */ public class CertificateSignRequest
/*     */   extends ValidationState
/*     */ {
/*     */   public static final String CSR_VERSION = "0.1";
/*     */   private static final String PROD_QUALIFIER_TEST_HEX_DEC = "0x00";
/*     */   private static final String PROD_QUALIFIER_TEST_TEXT = "Test";
/*     */   private static final String PROD_QUALIFIER_PRODUCTION_TEXT = "Production";
/*     */   @ApiModelProperty(value = "The certificate type. E.g. BACKEND_CA_CERTIFICATE, DIAGNOSTIC_AUTHENTICATION_CERTIFICATE, etc.", required = true)
/*     */   private String certificateType;
/*     */   @ApiModelProperty("The role of the user: 1=Supplier, 2=Development ENHANCED, 3=Production, 4=After-Sales ENHANCED, 5=After-Sales STANDARD, 6=After-Sales BASIC, 7=Internal Diagnostic Test Tool, 8=ePTI Test Tool")
/*     */   private String userRole;
/*     */   @ApiModelProperty("The target ECU. Maximum size of the field is 30 bytes.")
/*     */   private String targetECU;
/*     */   @ApiModelProperty("The target VIN. The size of the field is 17 characters.")
/*     */   private String targetVIN;
/*     */   @ApiModelProperty("The nonce. Must be Base64 encoded and have the length of 32 bytes.")
/*     */   private String nonce;
/*     */   @ApiModelProperty("The services.")
/*     */   private String services;
/*     */   @ApiModelProperty("Unique ECU ID. Maximum length is 30 bytes.")
/*     */   private String uniqueECUID;
/*     */   @ApiModelProperty("Special ECU. Maximum size is 1 byte.")
/*     */   private String specialECU;
/*     */   @ApiModelProperty("The parent certificate ID. Must be an UUID.")
/*     */   private String parentId;
/*     */   @JsonIgnore
/*     */   private String userId;
/*     */   @JsonFormat(pattern = "yyyy-MM-dd")
/*     */   @ApiModelProperty(value = "Valid to date. The date format is yyyy-MM-dd.", required = true)
/*     */   private Date validTo;
/*     */   @ApiModelProperty("The target subject key identifier.")
/*     */   private String targetSubjectKeyIdentifier;
/*     */   @ApiModelProperty(value = "The authority key identifier. It is sent as Base64 encoded bytes, and the length must be 20 bytes.", required = true)
/*     */   private String authorityKeyIdentifier;
/*     */   @ApiModelProperty("The subject.")
/*     */   private String subject;
/*     */   private String algorithmIdentifier;
/*     */   private String version;
/*     */   private String prodQualifier;
/*     */   private String signature;
/*     */   
/*     */   public CertificateSignRequest() {}
/*     */   
/*     */   public CertificateSignRequest(String certificateType, String userRole, String targetEcu, String targetVin, String nonce, String services, String uniqueECUID, String specialECU, Date validTo, String parentId, String userId, String algorithmIdentifier, String version, String prodQualifier, String signature) {
/* 185 */     this(certificateType, userRole, targetEcu, targetVin, nonce, services, uniqueECUID, specialECU, "", "", validTo, parentId, userId, algorithmIdentifier, version, prodQualifier, signature);
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
/*     */   public CertificateSignRequest(String subject, String certificateType, String userRole, String targetEcu, String targetVin, String nonce, String services, String uniqueECUID, String specialECU, Date validTo, String parentId, String userId, String algorithmIdentifier, String version, String prodQualifier, String signature, String authorityKeyIdentifier) {
/* 232 */     this.subject = subject;
/* 233 */     this.certificateType = certificateType;
/* 234 */     this.userRole = userRole;
/* 235 */     this.targetECU = targetEcu;
/* 236 */     this.targetVIN = targetVin;
/* 237 */     this.nonce = nonce;
/* 238 */     this.services = services;
/* 239 */     this.uniqueECUID = uniqueECUID;
/* 240 */     this.specialECU = specialECU;
/* 241 */     this.validTo = validTo;
/* 242 */     this.parentId = parentId;
/* 243 */     this.userId = userId;
/* 244 */     this.algorithmIdentifier = algorithmIdentifier;
/* 245 */     this.version = StringUtils.isEmpty(version) ? "0.1" : version;
/* 246 */     this.prodQualifier = prodQualifier;
/* 247 */     this.signature = signature;
/* 248 */     this.authorityKeyIdentifier = authorityKeyIdentifier;
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
/*     */   public CertificateSignRequest(String certificateType, String userRole, String targetEcu, String targetVin, String nonce, String services, String uniqueECUID, String specialECU, String targetSubjectKeyIdentifier, String authorityKeyIdentifier, Date validTo, String parentId, String userId, String algorithmIdentifier, String version, String prodQualifier, String signature) {
/* 294 */     this.certificateType = certificateType;
/* 295 */     this.userRole = userRole;
/* 296 */     this.targetECU = targetEcu;
/* 297 */     this.targetVIN = targetVin;
/* 298 */     this.nonce = nonce;
/* 299 */     this.services = services;
/* 300 */     this.uniqueECUID = uniqueECUID;
/* 301 */     this.specialECU = specialECU;
/* 302 */     this.targetSubjectKeyIdentifier = targetSubjectKeyIdentifier;
/* 303 */     this.authorityKeyIdentifier = authorityKeyIdentifier;
/* 304 */     this.validTo = validTo;
/* 305 */     this.parentId = parentId;
/* 306 */     this.userId = userId;
/* 307 */     this.algorithmIdentifier = algorithmIdentifier;
/* 308 */     this.version = "0.1";
/* 309 */     this.prodQualifier = prodQualifier;
/* 310 */     this.signature = signature;
/* 311 */     this.subject = "CN=";
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
/*     */   public CertificateSignRequest(String subject, String certificateType, String userRole, String targetEcu, String targetVin, String nonce, String services, String uniqueECUID, String specialECU, Date validTo, String parentId, String userId, String algorithmIdentifier, String version, String prodQualifier, String authorityKeyIdentifier) {
/* 354 */     this(subject, certificateType, userRole, targetEcu, targetVin, nonce, services, uniqueECUID, specialECU, validTo, parentId, userId, algorithmIdentifier, version, prodQualifier, (String)null, authorityKeyIdentifier);
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
/*     */   public CertificateSignRequest(String subject, String certificateType, String userRole, String targetEcu, String targetVin, String nonce, String services, String uniqueECUID, String specialECU, String targetSubjectKeyIdentifier, String authorityKeyIdentifier, Date validTo, String parentId, String userId, String algorithmIdentifier, String version, String prodQualifier, String signature) {
/* 402 */     this(certificateType, userRole, targetEcu, targetVin, nonce, services, uniqueECUID, specialECU, targetSubjectKeyIdentifier, authorityKeyIdentifier, validTo, parentId, userId, algorithmIdentifier, version, prodQualifier, signature);
/*     */ 
/*     */     
/* 405 */     this.subject = subject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCertificateType() {
/* 414 */     return this.certificateType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserRole() {
/* 423 */     return this.userRole;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTargetECU() {
/* 432 */     return this.targetECU;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTargetVIN() {
/* 441 */     return this.targetVIN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNonce() {
/* 450 */     return this.nonce;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServices() {
/* 459 */     return this.services;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUniqueECUID() {
/* 468 */     return this.uniqueECUID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSpecialECU() {
/* 477 */     return this.specialECU;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTargetSubjectKeyIdentifier() {
/* 486 */     return this.targetSubjectKeyIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAuthorityKeyIdentifier() {
/* 495 */     return this.authorityKeyIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getValidTo() {
/* 504 */     return this.validTo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidTo(Date validTo) {
/* 514 */     this.validTo = validTo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParentId() {
/* 523 */     return this.parentId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParentId(String parentId) {
/* 533 */     this.parentId = parentId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserId(String userId) {
/* 543 */     this.userId = userId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserId() {
/* 552 */     return this.userId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubject() {
/* 561 */     return this.subject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAlgorithmIdentifier() {
/* 570 */     return this.algorithmIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getVersion() {
/* 579 */     return this.version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSignature(String signature) {
/* 589 */     this.signature = signature;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSignature() {
/* 598 */     return this.signature;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getProdQualifier() {
/* 607 */     return this.prodQualifier;
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
/*     */   public Integer retrieveProdQualifierForCSR(CertificateSignRequest certificateSignRequest) {
/* 619 */     String prodQualifierStringValue = (certificateSignRequest.getProdQualifier() != null) ? certificateSignRequest.getProdQualifier() : "0x00";
/*     */     
/* 621 */     if (prodQualifierStringValue.equals("Test") || prodQualifierStringValue
/* 622 */       .equals("0x00"))
/* 623 */       return Integer.valueOf(0); 
/* 624 */     if (prodQualifierStringValue.equals("Production")) {
/* 625 */       return Integer.valueOf(1);
/*     */     }
/* 627 */     return Integer.valueOf(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 635 */     return "CertificateSignRequest [certificateType=" + this.certificateType + ", userRole=" + this.userRole + ", targetECU=" + this.targetECU + ", targetVIN=" + this.targetVIN + ", nonce=" + this.nonce + ", services=" + this.services + ", uniqueECUID=" + this.uniqueECUID + ", specialECU=" + this.specialECU + ", validTo=" + this.validTo + ", targetSubjectKeyIdentifier=" + this.targetSubjectKeyIdentifier + ", subject=" + this.subject + ", algorithmIdentifier=" + this.algorithmIdentifier + ", version=" + this.version + ", prodQualifier=" + this.prodQualifier + ", CSR=" + this.signature + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\CertificateSignRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */