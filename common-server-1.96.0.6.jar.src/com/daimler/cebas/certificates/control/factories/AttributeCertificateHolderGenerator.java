/*     */ package com.daimler.cebas.certificates.control.factories;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*     */ import com.daimler.cebas.certificates.control.vo.CertificateSignRequest;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.common.ObjectIdentifier;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Date;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1GeneralizedTime;
/*     */ import org.bouncycastle.asn1.ASN1Integer;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.DERBitString;
/*     */ import org.bouncycastle.asn1.DEROctetString;
/*     */ import org.bouncycastle.asn1.x509.AttCertIssuer;
/*     */ import org.bouncycastle.asn1.x509.AttributeCertificate;
/*     */ import org.bouncycastle.asn1.x509.AttributeCertificateInfo;
/*     */ import org.bouncycastle.asn1.x509.Extension;
/*     */ import org.bouncycastle.asn1.x509.ExtensionsGenerator;
/*     */ import org.bouncycastle.asn1.x509.GeneralName;
/*     */ import org.bouncycastle.asn1.x509.GeneralNames;
/*     */ import org.bouncycastle.asn1.x509.Holder;
/*     */ import org.bouncycastle.asn1.x509.IssuerSerial;
/*     */ import org.bouncycastle.asn1.x509.V2AttributeCertificateInfoGenerator;
/*     */ import org.bouncycastle.cert.X509AttributeCertificateHolder;
/*     */ import org.bouncycastle.cert.X509CertificateHolder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AttributeCertificateHolderGenerator
/*     */ {
/*  49 */   private static final String CLASS_NAME = AttributeCertificateHolderGenerator.class
/*  50 */     .getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   private static final Logger LOG = Logger.getLogger(AttributeCertificateHolderGenerator.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static X509AttributeCertificateHolder generate(Certificate parent, CertificateSignRequest signRequest, Logger logger) {
/*  78 */     String METHOD_NAME = "generate";
/*  79 */     logger.entering(CLASS_NAME, "generate");
/*     */     try {
/*  81 */       X509Certificate cert = parent.getCertificateData().getCert();
/*     */       
/*  83 */       X509CertificateHolder certificateHolder = new X509CertificateHolder(cert.getEncoded());
/*  84 */       V2AttributeCertificateInfoGenerator generator = new V2AttributeCertificateInfoGenerator();
/*     */ 
/*     */       
/*  87 */       Holder ans1Holder = new Holder(new IssuerSerial(certificateHolder.getIssuer(), certificateHolder.getSerialNumber()));
/*     */ 
/*     */ 
/*     */       
/*  91 */       ASN1Encodable parsedValue = certificateHolder.getExtension(new ASN1ObjectIdentifier(Extension.authorityKeyIdentifier.getId())).getParsedValue();
/*  92 */       generator.addAttribute(Extension.authorityKeyIdentifier.getId(), parsedValue);
/*     */       
/*  94 */       generator.setHolder(ans1Holder);
/*  95 */       generator.setSignature(certificateHolder.getSignatureAlgorithm());
/*  96 */       generator.setSerialNumber(new ASN1Integer(certificateHolder
/*  97 */             .getSerialNumber()));
/*  98 */       generator.setIssuer(new AttCertIssuer(new GeneralNames(new GeneralName(certificateHolder
/*  99 */                 .getIssuer()))));
/* 100 */       generator.setIssuerUniqueID(new DERBitString(parsedValue));
/*     */       
/* 102 */       generator.setStartDate(new ASN1GeneralizedTime(new Date()));
/* 103 */       generator.setEndDate(new ASN1GeneralizedTime(signRequest
/* 104 */             .getValidTo()));
/* 105 */       if (signRequest.getTargetSubjectKeyIdentifier() != null && 
/* 106 */         !signRequest.getTargetSubjectKeyIdentifier().isEmpty()) {
/* 107 */         ExtensionsGenerator extGen = new ExtensionsGenerator();
/*     */         
/* 109 */         addExtensionToGenerator(extGen, ObjectIdentifier.TARGET_SUBJECT_KEY_IDENTIFIER_OID.getOid(), signRequest
/* 110 */             .getTargetSubjectKeyIdentifier().getBytes(StandardCharsets.UTF_8), false);
/* 111 */         generator.setExtensions(extGen.generate());
/*     */       } 
/*     */ 
/*     */       
/* 115 */       AttributeCertificateInfo generateAttributeCertificateInfo = generator.generateAttributeCertificateInfo();
/* 116 */       generateAttributeCertificateInfo.toASN1Primitive().getEncoded();
/*     */ 
/*     */ 
/*     */       
/* 120 */       AttributeCertificate attribute = new AttributeCertificate(generateAttributeCertificateInfo, certificateHolder.getSignatureAlgorithm(), certificateHolder.toASN1Structure().getSignature());
/* 121 */       X509AttributeCertificateHolder result = new X509AttributeCertificateHolder(attribute);
/*     */       
/* 123 */       logger.exiting(CLASS_NAME, "generate");
/* 124 */       return result;
/* 125 */     } catch (IOException|java.security.cert.CertificateEncodingException e) {
/* 126 */       LOG.log(Level.FINEST, e.getMessage(), e);
/*     */       
/* 128 */       CEBASCertificateException zenzefiCertificateException = new CEBASCertificateException(e.getMessage(), "couldNotGenerateHolder");
/* 129 */       logger.logWithTranslation(Level.WARNING, "000048X", zenzefiCertificateException
/*     */           
/* 131 */           .getMessageId(), new String[] { e
/* 132 */             .getMessage() }, zenzefiCertificateException
/* 133 */           .getClass().getSimpleName());
/* 134 */       throw zenzefiCertificateException;
/*     */     } 
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
/*     */   private static void addExtensionToGenerator(ExtensionsGenerator extGen, String oid, byte[] value, boolean critical) {
/*     */     try {
/* 148 */       extGen.addExtension(new ASN1ObjectIdentifier(oid), critical, (ASN1Encodable)new DEROctetString(value));
/* 149 */     } catch (IOException e) {
/* 150 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 151 */       throw new CEBASCertificateException(e.getMessage());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\factories\AttributeCertificateHolderGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */