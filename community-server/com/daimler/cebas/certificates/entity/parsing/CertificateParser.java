/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.certificates.entity.PKIRole
 *  com.daimler.cebas.certificates.entity.UserRole
 *  com.daimler.cebas.common.CryptoParser
 *  com.daimler.cebas.common.ObjectIdentifier
 *  com.daimler.cebas.common.control.HexUtil
 *  com.daimler.cebas.exceptions.CryptoException
 *  org.hibernate.boot.model.naming.IllegalIdentifierException
 *  org.springframework.util.StreamUtils
 */
package com.daimler.cebas.certificates.entity.parsing;

import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.certificates.entity.PKIRole;
import com.daimler.cebas.certificates.entity.UserRole;
import com.daimler.cebas.common.CryptoParser;
import com.daimler.cebas.common.ObjectIdentifier;
import com.daimler.cebas.common.control.HexUtil;
import com.daimler.cebas.exceptions.CryptoException;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.util.StreamUtils;

public class CertificateParser {
    private static final Logger LOG = Logger.getLogger(CertificateParser.class.getName());

    private CertificateParser() {
    }

    public static String getSignature(Object cert) throws CEBASCertificateException {
        try {
            return HexUtil.bytesToHex((byte[])CryptoParser.getSignature((Object)cert));
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static byte[] getSignatureRaw(Object cert) throws CEBASCertificateException {
        try {
            return CryptoParser.getSignature((Object)cert);
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static String getVersion(Object cert) throws CEBASCertificateException {
        try {
            return CryptoParser.getVersion((Object)cert);
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static String getSerialNumber(Object cert) throws CEBASCertificateException {
        try {
            return HexUtil.omitLeadingZeros((String)HexUtil.bytesToHex((byte[])CryptoParser.getSerialNumber((Object)cert)));
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static String getIssuerSerialNumber(Object holder) {
        try {
            return HexUtil.bytesToHex((byte[])CryptoParser.getIssuerSerialNumber((Object)holder));
        }
        catch (CryptoException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            return "";
        }
    }

    public static String getSignAlgorithm(Object cert) throws CEBASCertificateException {
        try {
            return CryptoParser.getSignAlgorithm((Object)cert);
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static CertificateType getPKIRole(Object cert) throws CEBASCertificateException {
        CertificateType type = CertificateType.NO_TYPE;
        try {
            Optional pkiRoleOptional = CryptoParser.getExtensionPrimitive((Object)cert, (String)ObjectIdentifier.PKI_ROLE_OID.getOid());
            if (!pkiRoleOptional.isPresent()) return type;
            byte[] pkiRole = (byte[])pkiRoleOptional.get();
            type = (CertificateType)PKIRole.getRoles().get(pkiRole[0]);
            return type;
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            return CertificateType.NO_TYPE;
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static byte[] getPKIRoleRaw(Object cert) throws CEBASCertificateException {
        byte[] pkiRole = null;
        try {
            Optional pkiRoleOptional = CryptoParser.getExtensionPrimitive((Object)cert, (String)ObjectIdentifier.PKI_ROLE_OID.getOid());
            if (!pkiRoleOptional.isPresent()) return pkiRole;
            pkiRole = (byte[])pkiRoleOptional.get();
            return pkiRole;
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASCertificateException(e.getMessage());
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static String getUserRole(Object cert) throws CEBASCertificateException {
        try {
            Optional userRole = CryptoParser.getExtensionPrimitive((Object)cert, (String)ObjectIdentifier.USER_ROLE_OID.getOid());
            StringBuilder buffer = new StringBuilder();
            userRole.ifPresent(roles -> {
                if (((byte[])roles).length <= 0) return;
                buffer.append(roles[0]);
            });
            String role = buffer.toString();
            return role.equals("") ? role : UserRole.getUserRoleFromByte((byte)new Byte(role)).getText();
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASCertificateException(e.getMessage());
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static byte[] getUserRoleRaw(Object cert) throws CEBASCertificateException {
        try {
            byte[] userRoleByte = null;
            Optional userRole = CryptoParser.getExtensionPrimitive((Object)cert, (String)ObjectIdentifier.USER_ROLE_OID.getOid());
            if (!userRole.isPresent()) return userRoleByte;
            userRoleByte = (byte[])userRole.get();
            return userRoleByte;
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASCertificateException(e.getMessage());
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static String getTargetEcus(Object cert) throws CEBASCertificateException {
        try {
            return CryptoParser.getExtensionPrimitives((Object)cert, (String)ObjectIdentifier.TARGET_ECU_OID.getOid());
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASCertificateException(e.getMessage());
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static String getTargetVin(Object cert) throws CEBASCertificateException {
        try {
            return CryptoParser.getExtensionPrimitives((Object)cert, (String)ObjectIdentifier.TARGET_VIN_OID.getOid());
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASCertificateException(e.getMessage());
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static String getProdQualifier(Object cert) throws CEBASCertificateException {
        try {
            StringBuilder result = new StringBuilder();
            Optional prodQualifier = CryptoParser.getExtensionPrimitive((Object)cert, (String)ObjectIdentifier.PROD_QUALIFIER_OID.getOid());
            prodQualifier.ifPresent(qualifier -> result.append(qualifier[0]));
            return result.toString();
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASCertificateException("Could not get prod qualifier");
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static byte[] getProdQualifierRaw(Object cert) throws CEBASCertificateException {
        try {
            byte[] qualifier = null;
            Optional prodQualifier = CryptoParser.getExtensionPrimitive((Object)cert, (String)ObjectIdentifier.PROD_QUALIFIER_OID.getOid());
            if (!prodQualifier.isPresent()) return qualifier;
            qualifier = (byte[])prodQualifier.get();
            return qualifier;
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASCertificateException("Could not get prod qualifier");
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static String getUniqueEcuIds(Object cert) throws CEBASCertificateException {
        try {
            return CryptoParser.getExtensionPrimitives((Object)cert, (String)ObjectIdentifier.UNIQUE_ECU_OID.getOid());
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASCertificateException(e.getMessage());
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static String getSpecialEcu(Object cert) throws CEBASCertificateException {
        StringBuilder buffer = new StringBuilder();
        try {
            Optional optional = CryptoParser.getExtensionPrimitive((Object)cert, (String)ObjectIdentifier.SPECIAL_ECU_OID.getOid());
            if (!optional.isPresent()) return buffer.toString();
            byte[] result = (byte[])optional.get();
            if (result.length != 0) {
                buffer.append(result[0]);
            } else {
                LOG.warning("000208X The special ECU property is not available.");
            }
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            LOG.warning("000208X The special ECU property is not available. Reason: " + e.getMessage());
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
        return buffer.toString();
    }

    public static byte[] getSpecialEcuRaw(Object cert) throws CEBASCertificateException {
        byte[] specialEcu = null;
        try {
            Optional optional = CryptoParser.getExtensionPrimitive((Object)cert, (String)ObjectIdentifier.SPECIAL_ECU_OID.getOid());
            if (!optional.isPresent()) return specialEcu;
            specialEcu = (byte[])optional.get();
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            LOG.warning("000206X SpecialECU data was not extracted. Reason: " + e.getMessage());
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
        return specialEcu;
    }

    public static String getTargetSubjectKeyIdentifier(Object cert) throws CEBASCertificateException {
        String result = null;
        try {
            Optional optional = CryptoParser.getExtensionPrimitive((Object)cert, (String)ObjectIdentifier.TARGET_SUBJECT_KEY_IDENTIFIER_OID.getOid());
            if (!optional.isPresent()) return result != null ? result.trim() : "";
            result = HexUtil.bytesToHex((byte[])((byte[])optional.get()));
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            LOG.warning("000210X Target Subject Key Identifier is not available. Reason: " + e.getMessage());
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
        return result != null ? result.trim() : "";
    }

    public static boolean containsTargetSubjectKeyIdentifier(Object cert) throws CEBASCertificateException {
        try {
            Optional optional = CryptoParser.getExtensionPrimitive((Object)cert, (String)ObjectIdentifier.TARGET_SUBJECT_KEY_IDENTIFIER_OID.getOid());
            return optional.isPresent();
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            return false;
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static String getSubjectPublicKey(Object cert) throws CEBASCertificateException {
        try {
            return HexUtil.bytesToHex((byte[])CryptoParser.getSubjectPublicKey((Object)cert));
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static byte[] getSubjectPublicKeyBytes(Object cert) throws CEBASCertificateException {
        try {
            return CryptoParser.getSubjectPublicKey((Object)cert);
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static boolean[] getKeyUsage(Object cert) throws CEBASCertificateException {
        try {
            return CryptoParser.getKeyUsage((Object)cert);
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static String getBasicConstraints(Object cert) throws CEBASCertificateException {
        try {
            return CryptoParser.getBasicConstraints((Object)cert);
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static String getSubjectKeyIdentifier(Object cert) throws CEBASCertificateException {
        try {
            StringBuilder buffer = new StringBuilder();
            Optional subjectKeyIdentifier = CryptoParser.getExtensionPrimitive((Object)cert, (String)ObjectIdentifier.SUBJECT_KEY_IDENTIFIER_OID.getOid());
            subjectKeyIdentifier.ifPresent(ski -> buffer.append(HexUtil.bytesToHex((byte[])ski)));
            return buffer.toString().trim();
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASCertificateException(e.getMessage());
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static byte[] getSubjectKeyIdentifierRaw(Object cert) throws CEBASCertificateException {
        try {
            Optional subjectKeyIdentifier = CryptoParser.getExtensionPrimitive((Object)cert, (String)ObjectIdentifier.SUBJECT_KEY_IDENTIFIER_OID.getOid());
            return subjectKeyIdentifier.orElseGet(() -> new byte[0]);
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASCertificateException(e.getMessage());
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static String getServices(Object cert) throws CEBASCertificateException {
        try {
            return CryptoParser.getAttributePrimitives((Object)cert, (String)ObjectIdentifier.SERVICES_OID.getOid());
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASCertificateException(e.getMessage());
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static String getAuthorityKeyIdentifier(Object cert) throws CEBASCertificateException {
        try {
            return HexUtil.bytesToHex((byte[])CryptoParser.getAuthorityKeyIdentifier((Object)cert));
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            return "";
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static byte[] getAuthorityKeyIdentifierRaw(Object cert) throws CEBASCertificateException {
        try {
            return CryptoParser.getAuthorityKeyIdentifier((Object)cert);
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            return new byte[0];
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static String getNonce(Object cert) throws CEBASCertificateException {
        String str = null;
        try {
            Optional optional = CryptoParser.getExtensionPrimitive((Object)cert, (String)ObjectIdentifier.NONCE_OID.getOid());
            if (!optional.isPresent()) return str != null ? str.trim() : "";
            str = HexUtil.bytesToHex((byte[])((byte[])optional.get()));
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            LOG.warning("000207X Nonce is not available. Reason: " + e.getMessage());
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
        return str != null ? str.trim() : "";
    }

    public static byte[] getNonceRaw(Object cert) throws CEBASCertificateException {
        byte[] nonce = null;
        try {
            Optional optional = CryptoParser.getExtensionPrimitive((Object)cert, (String)ObjectIdentifier.NONCE_OID.getOid());
            if (!optional.isPresent()) return nonce;
            nonce = (byte[])optional.get();
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            LOG.warning("000209X Nonce data was not extracted. Reason: " + e.getMessage());
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
        return nonce;
    }

    public static boolean hasNonce(Object cert) throws CEBASCertificateException {
        try {
            Optional optional = CryptoParser.getExtensionPrimitive((Object)cert, (String)ObjectIdentifier.NONCE_OID.getOid());
            return optional.isPresent();
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            LOG.warning("000207X Nonce is not available. Reason: " + e.getMessage());
            return false;
        }
        catch (CryptoException e) {
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static int getValidityStrength(long currentMillis, long validFromMillis, long validToMillis) {
        if (validToMillis < currentMillis) {
            return 0;
        }
        double result = (double)((currentMillis - validFromMillis) * 100L) / (double)(validToMillis - validFromMillis);
        return 100 - (int)result;
    }

    public static boolean isInvalidPublicKey(InputStream certificateInput) {
        try {
            byte[] encoded = StreamUtils.copyToByteArray((InputStream)certificateInput);
            return !CryptoParser.validatePublicKey((byte[])encoded);
        }
        catch (IOException | IllegalArgumentException | IllegalIdentifierException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            return true;
        }
    }

    public static String getSubjectKeyIdentifierFromPublicKey(String publicKeyKex) throws CEBASCertificateException {
        try {
            byte[] publicKeyBytes = CertificateParser.hexStringToByteArray(publicKeyKex.trim());
            return HexUtil.bytesToHex((byte[])CryptoParser.getSubjectKeyIdentifierFromPublicKey((byte[])publicKeyBytes));
        }
        catch (CryptoException | IllegalArgumentException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static String getSubjectKeyIdentifierFromPublicKey(PublicKey publicKey) throws CEBASCertificateException {
        try {
            return HexUtil.bytesToHex((byte[])CryptoParser.getSubjectKeyIdentifierFromPublicKey((PublicKey)publicKey));
        }
        catch (CryptoException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASCertificateException(e.getMessage());
        }
    }

    public static byte[] hexStringToByteArray(String value) {
        if (value != null) return HexUtil.hexStringToByteArray((String)value);
        return null;
    }

    public static String hexToBase64(String s) {
        if (s == null) {
            return null;
        }
        if (!StringUtils.isBlank(s)) return Base64.getEncoder().encodeToString(CertificateParser.hexStringToByteArray(s));
        return "";
    }

    public static byte[] decodeBase64(String s) {
        if (s != null) return Base64.getDecoder().decode(s);
        return null;
    }
}
