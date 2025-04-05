/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.security.jwt.vo.JWTHeader
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.ObjectMapper
 */
package com.daimler.cebas.security.jwt;

import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.security.jwt.vo.JWTHeader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class JWTEngine {
    private static final String INVALID_DER_SIGNATURE_FORMAT = "Invalid DER signature format.";
    private static final int EC_NUMBER_SIZE = 32;

    private JWTEngine() {
    }

    public static String createToken(Object content, String privateKey) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String headerJson = mapper.writeValueAsString((Object)new JWTHeader());
        String payloadJson = mapper.writeValueAsString(content);
        String header = Base64.getEncoder().encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));
        String payload = Base64.getEncoder().encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));
        String signature = null;
        try {
            signature = Base64.getEncoder().encodeToString(JWTEngine.signToken(String.join((CharSequence)".", header, payload), privateKey));
        }
        catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException | InvalidKeySpecException e) {
            throw new CEBASException(e.getMessage());
        }
        return String.join((CharSequence)".", header, payload, signature);
    }

    private static byte[] signToken(String token, String privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException, InvalidKeySpecException {
        PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey.getBytes(StandardCharsets.UTF_8)));
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PrivateKey pk = keyFactory.generatePrivate(privateSpec);
        Signature signature = Signature.getInstance("SHA256withECDSA", "BC");
        signature.initSign(pk);
        signature.update(token.getBytes(StandardCharsets.UTF_8));
        return JWTEngine.DERToJOSE(signature.sign());
    }

    private static byte[] DERToJOSE(byte[] derSignature) throws SignatureException {
        int encodedLength;
        boolean derEncoded;
        boolean bl = derEncoded = derSignature[0] == 48 && derSignature.length != 64;
        if (!derEncoded) {
            throw new SignatureException(INVALID_DER_SIGNATURE_FORMAT);
        }
        byte[] joseSignature = new byte[64];
        int offset = 1;
        if (derSignature[1] == -127) {
            // empty if block
        }
        if ((encodedLength = derSignature[++offset] & 0xFF) != derSignature.length - ++offset) {
            throw new SignatureException(INVALID_DER_SIGNATURE_FORMAT);
        }
        int n = ++offset;
        ++offset;
        byte rLength = derSignature[n];
        if (rLength > 33) {
            throw new SignatureException(INVALID_DER_SIGNATURE_FORMAT);
        }
        int rPadding = 32 - rLength;
        System.arraycopy(derSignature, offset + Math.max(-rPadding, 0), joseSignature, Math.max(rPadding, 0), rLength + Math.min(rPadding, 0));
        offset += rLength + 1;
        byte sLength = derSignature[offset++];
        if (sLength > 33) {
            throw new SignatureException(INVALID_DER_SIGNATURE_FORMAT);
        }
        int sPadding = 32 - sLength;
        System.arraycopy(derSignature, offset + Math.max(-sPadding, 0), joseSignature, 32 + Math.max(sPadding, 0), sLength + Math.min(sPadding, 0));
        return joseSignature;
    }
}
