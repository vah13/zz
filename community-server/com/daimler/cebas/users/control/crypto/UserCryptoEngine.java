/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.CertificateCryptoEngine
 *  com.daimler.cebas.certificates.control.crypto.CustomPKCS8EncodedKeySpec
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.common.CryptoTools
 *  com.daimler.cebas.common.PublicKeyCryptoAlgorithms
 *  com.daimler.cebas.common.control.HexUtil
 *  com.daimler.cebas.common.control.SecurityProviders
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.exceptions.UserCryptoException
 *  com.daimler.cebas.users.control.factories.UserFactory
 *  com.daimler.cebas.users.entity.User
 *  com.daimler.cebas.users.entity.UserKeyPair
 *  org.bouncycastle.asn1.ASN1Sequence
 *  org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
 *  org.bouncycastle.asn1.x9.X9ECParameters
 *  org.bouncycastle.crypto.BlockCipher
 *  org.bouncycastle.crypto.CipherParameters
 *  org.bouncycastle.crypto.ec.CustomNamedCurves
 *  org.bouncycastle.crypto.engines.AESEngine
 *  org.bouncycastle.crypto.modes.CBCBlockCipher
 *  org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher
 *  org.bouncycastle.crypto.params.KeyParameter
 *  org.bouncycastle.jce.spec.ECParameterSpec
 */
package com.daimler.cebas.users.control.crypto;

import com.daimler.cebas.certificates.CertificateCryptoEngine;
import com.daimler.cebas.certificates.control.crypto.CustomPKCS8EncodedKeySpec;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.common.CryptoTools;
import com.daimler.cebas.common.PublicKeyCryptoAlgorithms;
import com.daimler.cebas.common.control.HexUtil;
import com.daimler.cebas.common.control.SecurityProviders;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.exceptions.UserCryptoException;
import com.daimler.cebas.users.control.factories.UserFactory;
import com.daimler.cebas.users.entity.User;
import com.daimler.cebas.users.entity.UserKeyPair;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.spec.ECParameterSpec;

@CEBASControl
public class UserCryptoEngine {
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(UserCryptoEngine.class.getSimpleName());
    private static final String ED_DSA = PublicKeyCryptoAlgorithms.EdDSA.name();
    private static final String ECDH = PublicKeyCryptoAlgorithms.ECDH.name();
    private static final String CLASS_NAME = UserCryptoEngine.class.getSimpleName();
    private Logger logger;

    public UserCryptoEngine(Logger logger) {
        this.logger = logger;
    }

    public String encryptMachineNameUsingPasswordHash(String secret, String machineName, User user) {
        String METHOD_NAME = "encryptMachineNameUsingPasswordHash";
        String decryptedSalt = this.getAESDecryptedSalt(secret, user);
        String decodedPassword = UserCryptoEngine.getBase64Decoded(user.getUserPassword().getBytes(StandardCharsets.UTF_8));
        String passwordHash = this.hashPassword(decodedPassword, decryptedSalt, this.getHashIterationCount(user));
        return this.encryptMachineName(passwordHash, machineName);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean matchPublicKeys(String containerKey, Certificate certificate, UserKeyPair userKeyPair) {
        byte[] decodedPrivateKey = this.decodePrivateKeyToByteArray(containerKey, userKeyPair.getPrivateKey());
        PrivateKey privateKey = CertificateCryptoEngine.generatePrivateKey((byte[])decodedPrivateKey);
        try {
            PublicKey calculatedPublicKey = CertificateCryptoEngine.calculateEDDSAFromEdDSAPrivate((PrivateKey)privateKey);
            String publicKeyFromPrivate = this.getPublicKeyDataHex(calculatedPublicKey);
            boolean bl = certificate.getSubjectPublicKey().equals(publicKeyFromPrivate);
            return bl;
        }
        catch (GeneralSecurityException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            boolean bl = false;
            return bl;
        }
        finally {
            CryptoTools.destroyPrivateKey((PrivateKey)privateKey);
        }
    }

    public byte[] encryptAES(String encryptionKey, byte[] text) {
        String METHOD_NAME = "encryptAES";
        try {
            int textLength = text.length;
            PaddedBufferedBlockCipher encryptCipher = new PaddedBufferedBlockCipher((BlockCipher)new CBCBlockCipher((BlockCipher)new AESEngine()));
            encryptCipher.init(true, (CipherParameters)new KeyParameter(encryptionKey.getBytes(StandardCharsets.UTF_8)));
            byte[] buffer = new byte[encryptCipher.getOutputSize(textLength)];
            int outputLength = encryptCipher.processBytes(text, 0, textLength, buffer, 0);
            outputLength += encryptCipher.doFinal(buffer, outputLength);
            if (outputLength < buffer.length) {
                byte[] byArray = Arrays.copyOfRange(buffer, 0, outputLength);
                return byArray;
            }
            byte[] byArray = buffer;
            return byArray;
        }
        catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            throw new UserCryptoException(e.getMessage());
        }
        finally {
            Arrays.fill(text, (byte)0);
        }
    }

    public byte[] decryptAES(String decryptionKey, byte[] cipherText) {
        String METHOD_NAME = "decryptAES";
        try {
            int cipherTextLength = cipherText.length;
            PaddedBufferedBlockCipher encryptCipher = new PaddedBufferedBlockCipher((BlockCipher)new CBCBlockCipher((BlockCipher)new AESEngine()));
            encryptCipher.init(false, (CipherParameters)new KeyParameter(decryptionKey.getBytes(StandardCharsets.UTF_8)));
            byte[] buffer = new byte[encryptCipher.getOutputSize(cipherTextLength)];
            int outputLength = encryptCipher.processBytes(cipherText, 0, cipherTextLength, buffer, 0);
            outputLength += encryptCipher.doFinal(buffer, outputLength);
            if (outputLength >= buffer.length) return buffer;
            return Arrays.copyOfRange(buffer, 0, outputLength);
        }
        catch (Exception e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new UserCryptoException(e.getMessage());
        }
    }

    public String getAESEncryptedUserPassword(String secret, String password) {
        String METHOD_NAME = "getAESEncryptedUserPassword";
        byte[] encPassword = this.encryptAES(secret, password.getBytes(StandardCharsets.UTF_8));
        String encPasswordString = Base64.getEncoder().encodeToString(encPassword);
        return encPasswordString;
    }

    public String getAESEncryptedSalt(String secret, String salt) {
        String METHOD_NAME = "getAESEncryptedSalt";
        byte[] encSalt = this.encryptAES(secret, salt.getBytes(StandardCharsets.UTF_8));
        String encSaltString = Base64.getEncoder().encodeToString(encSalt);
        return encSaltString;
    }

    public String getAESDecryptedUserPassword(String secret, User user) {
        String METHOD_NAME = "getAESDecryptedUserPassword";
        String encryptedPassword = user.getUserPassword();
        byte[] decryptedPassword = this.decryptAES(secret, Base64.getDecoder().decode(encryptedPassword));
        return new String(decryptedPassword, StandardCharsets.UTF_8);
    }

    public String getAESDecryptedSalt(String secret, User user) {
        String METHOD_NAME = "getAESDecryptedSalt";
        String encryptedSalt = user.getSalt();
        byte[] decryptedSalt = this.decryptAES(secret, Base64.getDecoder().decode(encryptedSalt));
        return new String(decryptedSalt, StandardCharsets.UTF_8);
    }

    public String generateContainerKey(String secret, User user) {
        String METHOD_NAME = "generateContainerKey";
        try {
            String decryptedPassword = this.getAESDecryptedUserPassword(secret, user);
            String decryptedSalt = this.getAESDecryptedSalt(secret, user);
            return this.hashPassword(decryptedPassword, decryptedSalt, this.getHashIterationCount(user));
        }
        catch (Exception e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new UserCryptoException(e.getMessage());
        }
    }

    public String hashPassword(String decryptedPassword, String decryptedSalt, int hashIterationCount) {
        String string;
        int keyLength = 128;
        PBEKeySpec spec = new PBEKeySpec(decryptedPassword.toCharArray(), decryptedSalt.getBytes(StandardCharsets.UTF_8), hashIterationCount, keyLength);
        byte[] encodedKey = null;
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            encodedKey = factory.generateSecret(spec).getEncoded();
            string = Base64.getEncoder().encodeToString(encodedKey);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            try {
                LOG.log(Level.FINEST, e.getMessage(), e);
                throw new UserCryptoException(e.getMessage());
            }
            catch (Throwable throwable) {
                Arrays.fill(encodedKey, (byte)0);
                throw throwable;
            }
        }
        Arrays.fill(encodedKey, (byte)0);
        return string;
    }

    private int getHashIterationCount(User user) {
        if (!this.isDefaultUser(user)) return 100000;
        return 10000;
    }

    private boolean isDefaultUser(User user) {
        if (user == null) {
            return false;
        }
        if (user.getUserName() != null) return user.getUserName().equals(UserFactory.getDefaultUsername());
        return false;
    }

    public String encryptMachineName(String encryptionKey, String machineName) {
        String METHOD_NAME = "encryptMachineName";
        return Base64.getEncoder().encodeToString(this.encryptAES(encryptionKey, machineName.getBytes(StandardCharsets.UTF_8)));
    }

    public String decryptMachineName(String containerKey, String machineNameEncrypted) {
        String METHOD_NAME = "decryptMachineName";
        return new String(this.decryptAES(containerKey, Base64.getDecoder().decode(machineNameEncrypted)), StandardCharsets.UTF_8);
    }

    public String generateContainerKey(String salt, String password, User user) {
        String METHOD_NAME = "generateContainerKey";
        return this.hashPassword(password, salt, this.getHashIterationCount(user));
    }

    public static String getBase64Decoded(byte[] encoded) {
        return new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
    }

    public KeyPair generateKeyPair() {
        String METHOD_NAME = "generateKeyPair";
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ED_DSA, SecurityProviders.SNED25519.name());
            KeyPair keyPair = keyGen.generateKeyPair();
            return keyPair;
        }
        catch (Exception e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new UserCryptoException(e.getMessage());
        }
    }

    public KeyPair generateCommunicationKeyPair() {
        String METHOD_NAME = "generateCommunicationKeyPair";
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ECDH, SecurityProviders.BC.name());
            X9ECParameters ecP = CustomNamedCurves.getByName((String)"curve25519");
            ECParameterSpec ecSpec = new ECParameterSpec(ecP.getCurve(), ecP.getG(), ecP.getN(), ecP.getH(), ecP.getSeed());
            keyGen.initialize((AlgorithmParameterSpec)ecSpec, new SecureRandom());
            KeyPair keyPair = keyGen.generateKeyPair();
            return keyPair;
        }
        catch (Exception e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new UserCryptoException(e.getMessage());
        }
    }

    public String getPublicKeyDataHex(PublicKey key) {
        String METHOD_NAME = "getPublicKeyHex";
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(key.getEncoded());
        ASN1Sequence asn1seq = ASN1Sequence.getInstance((Object)pubKeySpec.getEncoded());
        SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance((Object)asn1seq);
        byte[] pubBytes = subjectPublicKeyInfo.getPublicKeyData().getBytes();
        return HexUtil.bytesToHex((byte[])pubBytes).trim();
    }

    public String getPublicKeyEncoded(PublicKey key) {
        return HexUtil.bytesToHex((byte[])key.getEncoded()).trim();
    }

    public PrivateKey getCommunicationPrivateKeyFromBytes(byte[] privateKeyBytes) {
        PrivateKey privateKey;
        PrivateKey privateKey2;
        block5: {
            CustomPKCS8EncodedKeySpec spec = null;
            privateKey2 = null;
            try {
                spec = new CustomPKCS8EncodedKeySpec(privateKeyBytes);
                KeyFactory factory = KeyFactory.getInstance(PublicKeyCryptoAlgorithms.ECDH.name());
                privateKey = privateKey2 = factory.generatePrivate((KeySpec)spec);
                if (spec == null) break block5;
            }
            catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                try {
                    LOG.log(Level.FINEST, e.getMessage(), e);
                    throw new UserCryptoException(e.getMessage());
                }
                catch (Throwable throwable) {
                    if (spec != null) {
                        spec.destroy();
                    }
                    CryptoTools.destroyPrivateKey(privateKey2);
                    throw throwable;
                }
            }
            spec.destroy();
        }
        CryptoTools.destroyPrivateKey((PrivateKey)privateKey2);
        return privateKey;
    }

    public String encodePrivateKey(String containerKey, byte[] privateKey) {
        String METHOD_NAME = "encodePrivateKey";
        return Base64.getEncoder().encodeToString(this.encryptAES(containerKey, privateKey));
    }

    public String decodePrivateKey(String containerKey, String encodedPrivateKey) {
        String METHOD_NAME = "decodePrivateKey";
        return new String(this.decryptAES(containerKey, Base64.getDecoder().decode(encodedPrivateKey)), StandardCharsets.UTF_8);
    }

    public byte[] decodePrivateKeyToByteArray(String containerKey, String encodedPrivateKey) {
        String METHOD_NAME = "decodePrivateKeyToByteArray";
        return this.decryptAES(containerKey, Base64.getDecoder().decode(encodedPrivateKey));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public UserKeyPair generateUserKeyPair(String containerKey, User user) {
        KeyPair generateKeyPair = this.generateKeyPair();
        try {
            String publicKey = this.getPublicKeyDataHex(generateKeyPair.getPublic());
            String encodedPrivateKey = this.encodePrivateKey(containerKey, generateKeyPair.getPrivate().getEncoded());
            UserKeyPair userKeyPair = new UserKeyPair(publicKey, encodedPrivateKey, user);
            return userKeyPair;
        }
        finally {
            CryptoTools.destroyPrivateKey((PrivateKey)generateKeyPair.getPrivate());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public UserKeyPair generateUserKeyPairCommunication(String containerKey, User user) {
        KeyPair generateKeyPair = this.generateCommunicationKeyPair();
        try {
            String publicKey = this.getPublicKeyEncoded(generateKeyPair.getPublic());
            String encodedPrivateKey = this.encodePrivateKey(containerKey, generateKeyPair.getPrivate().getEncoded());
            UserKeyPair userKeyPair = new UserKeyPair(publicKey, encodedPrivateKey, user);
            return userKeyPair;
        }
        finally {
            CryptoTools.destroyPrivateKey((PrivateKey)generateKeyPair.getPrivate());
        }
    }
}
