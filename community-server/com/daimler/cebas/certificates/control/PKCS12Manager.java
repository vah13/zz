/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.CertificateCryptoEngine
 *  com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder
 *  com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException
 *  com.daimler.cebas.certificates.control.exceptions.PkcsException
 *  com.daimler.cebas.certificates.control.factories.CertificateHolderFactory
 *  com.daimler.cebas.certificates.control.validation.CertificatesFieldsValidator
 *  com.daimler.cebas.certificates.control.vo.ImportResult
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateState
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.common.CryptoParser
 *  com.daimler.cebas.common.PublicKeyCryptoAlgorithms
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.entity.User
 *  com.secunet.provider.pkcs12.AttributeCertificate
 *  com.secunet.provider.pkcs12.Pkcs12Object
 *  com.secunet.provider.pkcs12.Pkcs12Object$BagType
 *  org.bouncycastle.cert.X509AttributeCertificateHolder
 *  org.hibernate.boot.model.naming.IllegalIdentifierException
 */
package com.daimler.cebas.certificates.control;

import com.daimler.cebas.certificates.CertificateCryptoEngine;
import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
import com.daimler.cebas.certificates.control.exceptions.PkcsException;
import com.daimler.cebas.certificates.control.factories.CertificateHolderFactory;
import com.daimler.cebas.certificates.control.validation.CertificatesFieldsValidator;
import com.daimler.cebas.certificates.control.vo.ImportResult;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateState;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.common.CryptoParser;
import com.daimler.cebas.common.PublicKeyCryptoAlgorithms;
import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.entity.User;
import com.secunet.provider.pkcs12.AttributeCertificate;
import com.secunet.provider.pkcs12.Pkcs12Object;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.naming.InvalidNameException;
import org.bouncycastle.cert.X509AttributeCertificateHolder;
import org.hibernate.boot.model.naming.IllegalIdentifierException;

public class PKCS12Manager {
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(PKCS12Manager.class.getSimpleName());
    private static final String CLASS_NAME = PKCS12Manager.class.getSimpleName();
    private static final String EMPTY = "";

    private PKCS12Manager() {
    }

    public static byte[] generatePKCS12(List<CertificatePrivateKeyHolder> certPrivateKeyHolders, Logger logger, String password, MetadataManager i18n) {
        String METHOD_NAME = "generatePKCS12";
        logger.entering(CLASS_NAME, "generatePKCS12");
        Pkcs12Object p12Object = new Pkcs12Object(password.toCharArray());
        certPrivateKeyHolders.forEach(holder -> PKCS12Manager.addEntryToPKCS(p12Object, holder, i18n, logger));
        return p12Object.getEncoded();
    }

    public static byte[] generateDiagnosticPKCS12(List<CertificatePrivateKeyHolder> certPrivateKeyHolders, Logger logger, String password, MetadataManager i18n) {
        String METHOD_NAME = "generateDiagnosticPKCS12";
        logger.entering(CLASS_NAME, "generateDiagnosticPKCS12");
        Pkcs12Object p12Object = new Pkcs12Object(password.toCharArray());
        certPrivateKeyHolders.forEach(holder -> PKCS12Manager.addEntryWithEnhancedCertsToPKCS(p12Object, holder, i18n, logger));
        return p12Object.getEncoded();
    }

    public static byte[] generateEncryptedPKCS12(List<CertificatePrivateKeyHolder> certificatePrivateKeyHolders, Logger logger, String password, PublicKey publicKey, MetadataManager i18n) throws GeneralSecurityException {
        byte[] generatedPKCS12 = PKCS12Manager.generatePKCS12(certificatePrivateKeyHolders, logger, password, i18n);
        return CertificateCryptoEngine.encryptECIES((byte[])generatedPKCS12, (PublicKey)publicKey);
    }

    public static byte[] generateEncryptedDiagnosticPKCS12(List<CertificatePrivateKeyHolder> certificatePrivateKeyHolders, Logger logger, String password, PublicKey publicKey, MetadataManager i18n) throws GeneralSecurityException {
        byte[] generatedPKCS12 = PKCS12Manager.generateDiagnosticPKCS12(certificatePrivateKeyHolders, logger, password, i18n);
        return CertificateCryptoEngine.encryptECIES((byte[])generatedPKCS12, (PublicKey)publicKey);
    }

    public static byte[] decryptPKCS12(byte[] pkcs12Data, PrivateKey privateKey) throws GeneralSecurityException {
        return CertificateCryptoEngine.decryptECIES((byte[])pkcs12Data, (PrivateKey)privateKey);
    }

    public static PublicKey getPublicKey(byte[] bytes) {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
            KeyFactory factory = KeyFactory.getInstance(PublicKeyCryptoAlgorithms.ECDH.name());
            return factory.generatePublic(spec);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASCertificateException("Cannot create public key from bytes! " + e.getMessage());
        }
    }

    public static List<Optional<CertificatePrivateKeyHolder>> readPKCS12(InputStream inputStream, String password, User user, Logger logger, CertificateHolderFactory certificateHolderFactory, List<ImportResult> importResults, String fileName, MetadataManager i18n) {
        Pkcs12Object p12input;
        String METHOD_NAME = "readPKCS12";
        logger.entering(CLASS_NAME, "readPKCS12");
        try {
            p12input = new Pkcs12Object(inputStream, password.toCharArray());
        }
        catch (IOException | CertificateEncodingException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            importResults.add(new ImportResult(fileName, EMPTY, EMPTY, i18n.getMessage("couldNotReadPrivateKey", new String[]{e.getMessage()}), false));
            logger.logWithTranslation(Level.SEVERE, "000121X", "errorGettingEncodedCertificateBytes", new String[]{e.getMessage()}, CLASS_NAME);
            return Collections.emptyList();
        }
        ArrayList<Optional<CertificatePrivateKeyHolder>> pkcsEntries = new ArrayList<Optional<CertificatePrivateKeyHolder>>();
        String friendlyName = PKCS12Manager.getFriendlyNameFromKeyBag(p12input, i18n, logger);
        Arrays.stream(p12input.getLocalKeyIds(Pkcs12Object.BagType.KeyBag)).forEach(keyId -> PKCS12Manager.extractCertificate(user, logger, certificateHolderFactory, importResults, fileName, i18n, p12input, pkcsEntries, keyId, friendlyName));
        Arrays.stream(p12input.getCertificates()).forEach(c -> {
            if (c instanceof X509Certificate) {
                PKCS12Manager.checkPublicKey(i18n, logger, (X509Certificate)c);
                pkcsEntries.add(Optional.of(certificateHolderFactory.createCertificateHolder(fileName, (X509Certificate)c, friendlyName, user)));
            } else {
                if (!(c instanceof AttributeCertificate)) return;
                pkcsEntries.add(Optional.of(certificateHolderFactory.createCertificateHolder(fileName, (AttributeCertificate)c, user)));
            }
        });
        logger.exiting(CLASS_NAME, "readPKCS12");
        return pkcsEntries;
    }

    private static void extractCertificate(User user, Logger logger, CertificateHolderFactory certificateHolderFactory, List<ImportResult> importResults, String fileName, MetadataManager i18n, Pkcs12Object p12input, List<Optional<CertificatePrivateKeyHolder>> pkcsEntries, byte[] keyId, String friendlyName) {
        try {
            X509Certificate pCert = (X509Certificate)p12input.getCertificate(keyId);
            PKCS12Manager.checkPublicKey(i18n, logger, pCert);
            PrivateKey pKey = p12input.getPrivateKey(keyId);
            pkcsEntries.add(Optional.of(certificateHolderFactory.createCertificateHolder(fileName, pCert, pKey, friendlyName, user)));
            p12input.remove(keyId);
        }
        catch (KeyStoreException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            importResults.add(new ImportResult(fileName, EMPTY, EMPTY, i18n.getMessage("couldNotReadPrivateKey", new String[]{e.getMessage()}), false));
            logger.logWithTranslation(Level.SEVERE, "000144X", "couldNotReadPrivateKey", new String[]{e.getMessage()}, CLASS_NAME);
        }
    }

    private static void addEntryToPKCS(Pkcs12Object p12Object, CertificatePrivateKeyHolder holder, MetadataManager i18n, Logger logger) {
        try {
            if (holder.hasPrivateKey()) {
                p12Object.add(holder.getPrivateKey(), (java.security.cert.Certificate)holder.getCertificate().getCertificateData().getCert());
            } else {
                p12Object.add((java.security.cert.Certificate)holder.getCertificate().getCertificateData().getCert());
            }
        }
        catch (CertificateEncodingException | InvalidNameException e) {
            PKCS12Manager.logAndThrowPkcsException(i18n, logger, e);
        }
    }

    private static void logAndThrowPkcsException(MetadataManager i18n, Logger logger, Exception originalException) {
        LOG.log(Level.FINEST, originalException.getMessage(), originalException);
        PkcsException exception = new PkcsException(i18n.getMessage("errorAddingPKCS12Entry"), "errorAddingPKCS12Entry");
        logger.logWithTranslation(Level.SEVERE, "000204X", exception.getMessageId(), exception.getClass().getSimpleName());
        throw exception;
    }

    private static void addEntryWithEnhancedCertsToPKCS(Pkcs12Object p12Object, CertificatePrivateKeyHolder holder, MetadataManager i18n, Logger logger) {
        try {
            X509Certificate cert = holder.getCertificate().getCertificateData().getCert();
            if (holder.hasPrivateKey()) {
                p12Object.add(holder.getPrivateKey(), (java.security.cert.Certificate)cert);
            } else {
                p12Object.add((java.security.cert.Certificate)cert);
            }
            List children = holder.getCertificate().getChildren();
            if (children == null) return;
            if (children.isEmpty()) return;
            children.stream().filter(child -> child.getType() == CertificateType.ENHANCED_RIGHTS_CERTIFICATE && child.getState().equals((Object)CertificateState.ISSUED)).forEach(child -> PKCS12Manager.addEnhancedRights(p12Object, i18n, logger, cert, child));
        }
        catch (CertificateEncodingException | InvalidNameException e) {
            PKCS12Manager.logAndThrowPkcsException(i18n, logger, e);
        }
    }

    private static void addEnhancedRights(Pkcs12Object p12Object, MetadataManager i18n, Logger logger, X509Certificate parentHolder, Certificate child) {
        try {
            X509AttributeCertificateHolder attributeCertHolder = child.getCertificateData().getAttributesCertificateHolder();
            p12Object.add((java.security.cert.Certificate)new AttributeCertificate(attributeCertHolder), (java.security.cert.Certificate)parentHolder);
        }
        catch (CertificateEncodingException | InvalidNameException e) {
            PKCS12Manager.logAndThrowPkcsException(i18n, logger, e);
        }
    }

    private static void checkPublicKey(MetadataManager i18n, Logger logger, X509Certificate certificate) {
        try {
            CryptoParser.validatePublicKey((byte[])certificate.getEncoded());
        }
        catch (IllegalArgumentException | IllegalIdentifierException e) {
            logger.log(Level.WARNING, "000417X", i18n.getEnglishMessage("thePublicKeyIsInvalid", new String[]{e.getMessage()}), CLASS_NAME);
            throw new CEBASException(i18n.getMessage("thePublicKeyIsInvalid", new String[]{e.getMessage()}));
        }
        catch (IOException | CertificateEncodingException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
        }
    }

    private static String getFriendlyNameFromKeyBag(Pkcs12Object p12input, MetadataManager i18n, Logger logger) {
        Set zkNOs = Arrays.stream(p12input.getFriendlyNames()).filter(CertificatesFieldsValidator::isZkNo).collect(Collectors.toSet());
        if (zkNOs.size() == 1) {
            return (String)zkNOs.iterator().next();
        }
        if (zkNOs.size() == 0) {
            logger.log(Level.WARNING, "000704X", i18n.getEnglishMessage("friendlyNameFailsRegexCheck"), CLASS_NAME);
        } else {
            logger.log(Level.WARNING, "000703X", i18n.getEnglishMessage("friendlyNameIsInconsistent"), CLASS_NAME);
        }
        return null;
    }
}
