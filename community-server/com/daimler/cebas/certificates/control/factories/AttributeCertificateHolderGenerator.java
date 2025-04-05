/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException
 *  com.daimler.cebas.certificates.control.vo.CertificateSignRequest
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.common.ObjectIdentifier
 *  com.daimler.cebas.logs.control.Logger
 *  org.bouncycastle.asn1.ASN1Encodable
 *  org.bouncycastle.asn1.ASN1GeneralizedTime
 *  org.bouncycastle.asn1.ASN1Integer
 *  org.bouncycastle.asn1.ASN1ObjectIdentifier
 *  org.bouncycastle.asn1.DERBitString
 *  org.bouncycastle.asn1.DEROctetString
 *  org.bouncycastle.asn1.x509.AttCertIssuer
 *  org.bouncycastle.asn1.x509.AttributeCertificate
 *  org.bouncycastle.asn1.x509.AttributeCertificateInfo
 *  org.bouncycastle.asn1.x509.Extension
 *  org.bouncycastle.asn1.x509.ExtensionsGenerator
 *  org.bouncycastle.asn1.x509.GeneralName
 *  org.bouncycastle.asn1.x509.GeneralNames
 *  org.bouncycastle.asn1.x509.Holder
 *  org.bouncycastle.asn1.x509.IssuerSerial
 *  org.bouncycastle.asn1.x509.V2AttributeCertificateInfoGenerator
 *  org.bouncycastle.cert.X509AttributeCertificateHolder
 *  org.bouncycastle.cert.X509CertificateHolder
 */
package com.daimler.cebas.certificates.control.factories;

import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
import com.daimler.cebas.certificates.control.vo.CertificateSignRequest;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.common.ObjectIdentifier;
import com.daimler.cebas.logs.control.Logger;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.logging.Level;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.AttCertIssuer;
import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.bouncycastle.asn1.x509.AttributeCertificateInfo;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.Holder;
import org.bouncycastle.asn1.x509.IssuerSerial;
import org.bouncycastle.asn1.x509.V2AttributeCertificateInfoGenerator;
import org.bouncycastle.cert.X509AttributeCertificateHolder;
import org.bouncycastle.cert.X509CertificateHolder;

public class AttributeCertificateHolderGenerator {
    private static final String CLASS_NAME = AttributeCertificateHolderGenerator.class.getSimpleName();
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(AttributeCertificateHolderGenerator.class.getName());

    private AttributeCertificateHolderGenerator() {
    }

    public static X509AttributeCertificateHolder generate(Certificate parent, CertificateSignRequest signRequest, Logger logger) {
        String METHOD_NAME = "generate";
        logger.entering(CLASS_NAME, "generate");
        try {
            X509Certificate cert = parent.getCertificateData().getCert();
            X509CertificateHolder certificateHolder = new X509CertificateHolder(cert.getEncoded());
            V2AttributeCertificateInfoGenerator generator = new V2AttributeCertificateInfoGenerator();
            Holder ans1Holder = new Holder(new IssuerSerial(certificateHolder.getIssuer(), certificateHolder.getSerialNumber()));
            ASN1Encodable parsedValue = certificateHolder.getExtension(new ASN1ObjectIdentifier(Extension.authorityKeyIdentifier.getId())).getParsedValue();
            generator.addAttribute(Extension.authorityKeyIdentifier.getId(), parsedValue);
            generator.setHolder(ans1Holder);
            generator.setSignature(certificateHolder.getSignatureAlgorithm());
            generator.setSerialNumber(new ASN1Integer(certificateHolder.getSerialNumber()));
            generator.setIssuer(new AttCertIssuer(new GeneralNames(new GeneralName(certificateHolder.getIssuer()))));
            generator.setIssuerUniqueID(new DERBitString(parsedValue));
            generator.setStartDate(new ASN1GeneralizedTime(new Date()));
            generator.setEndDate(new ASN1GeneralizedTime(signRequest.getValidTo()));
            if (signRequest.getTargetSubjectKeyIdentifier() != null && !signRequest.getTargetSubjectKeyIdentifier().isEmpty()) {
                ExtensionsGenerator extGen = new ExtensionsGenerator();
                AttributeCertificateHolderGenerator.addExtensionToGenerator(extGen, ObjectIdentifier.TARGET_SUBJECT_KEY_IDENTIFIER_OID.getOid(), signRequest.getTargetSubjectKeyIdentifier().getBytes(StandardCharsets.UTF_8), false);
                generator.setExtensions(extGen.generate());
            }
            AttributeCertificateInfo generateAttributeCertificateInfo = generator.generateAttributeCertificateInfo();
            generateAttributeCertificateInfo.toASN1Primitive().getEncoded();
            AttributeCertificate attribute = new AttributeCertificate(generateAttributeCertificateInfo, certificateHolder.getSignatureAlgorithm(), certificateHolder.toASN1Structure().getSignature());
            X509AttributeCertificateHolder result = new X509AttributeCertificateHolder(attribute);
            logger.exiting(CLASS_NAME, "generate");
            return result;
        }
        catch (IOException | CertificateEncodingException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            CEBASCertificateException zenzefiCertificateException = new CEBASCertificateException(e.getMessage(), "couldNotGenerateHolder");
            logger.logWithTranslation(Level.WARNING, "000048X", zenzefiCertificateException.getMessageId(), new String[]{e.getMessage()}, zenzefiCertificateException.getClass().getSimpleName());
            throw zenzefiCertificateException;
        }
    }

    private static void addExtensionToGenerator(ExtensionsGenerator extGen, String oid, byte[] value, boolean critical) {
        try {
            extGen.addExtension(new ASN1ObjectIdentifier(oid), critical, (ASN1Encodable)new DEROctetString(value));
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASCertificateException(e.getMessage());
        }
    }
}
