/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.validation.CertificatesFieldsValidator
 *  com.daimler.cebas.certificates.control.zkNoSupport.ZkNoMappingResult
 *  com.daimler.cebas.certificates.entity.parsing.CertificateParser
 *  com.daimler.cebas.common.control.HexUtil
 *  com.daimler.cebas.logs.control.Logger
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  javax.persistence.Tuple
 */
package com.daimler.cebas.certificates.control.zkNoSupport;

import com.daimler.cebas.certificates.control.validation.CertificatesFieldsValidator;
import com.daimler.cebas.certificates.control.zkNoSupport.ZkNoMappingResult;
import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
import com.daimler.cebas.common.control.HexUtil;
import com.daimler.cebas.logs.control.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.Tuple;
import org.apache.commons.lang3.StringUtils;

public interface ZkNoSupport {
    public static List<ZkNoMappingResult> getZkNoMapping(String identifier, List<Tuple> tuples, Logger logger) {
        Boolean isZkNo = StringUtils.isEmpty(identifier) ? null : Boolean.valueOf(CertificatesFieldsValidator.isZkNo((String)identifier));
        List<ZkNoMappingResult> result = ZkNoSupport.getZkNoMapping(tuples);
        if (StringUtils.isBlank(identifier)) {
            String mapping = "";
            try {
                mapping = new ObjectMapper().writeValueAsString(result);
            }
            catch (JsonProcessingException e) {
                logger.logExceptionOnFinest(e, ZkNoSupport.class.getSimpleName());
            }
            logger.logWithTranslation(Level.INFO, "000547", "entireMappingReturned", new String[]{mapping}, ZkNoSupport.class.getSimpleName());
        }
        if (result.isEmpty()) {
            String message = ZkNoSupport.loggingMessage(identifier, isZkNo);
            logger.logWithTranslation(Level.INFO, "000548", "entireMappingNothingFound", new String[]{message}, ZkNoSupport.class.getSimpleName());
            return result;
        }
        Iterator<Tuple> iterator = tuples.iterator();
        while (iterator.hasNext()) {
            Tuple tuple = iterator.next();
            logger.logWithTranslation(Level.INFO, "000549", "zkNumberMappingFound", new String[]{(String)tuple.get(1), (String)tuple.get(0)}, ZkNoSupport.class.getSimpleName());
        }
        return result;
    }

    public static String loggingMessage(String identifier, Boolean isZkNo) {
        if (isZkNo != null) return isZkNo != false ? "ZK Number " + identifier : "BSKI " + HexUtil.base64ToHex((String)identifier);
        return "EMPTY";
    }

    public static List<ZkNoMappingResult> getZkNoMapping(List<Tuple> tuples) {
        ArrayList<ZkNoMappingResult> result = new ArrayList<ZkNoMappingResult>();
        Iterator<Tuple> iterator = tuples.iterator();
        while (iterator.hasNext()) {
            Tuple tuple = iterator.next();
            result.add(new ZkNoMappingResult(CertificateParser.hexToBase64((String)((String)tuple.get(0))), (String)tuple.get(1)));
        }
        return result;
    }

    public String getIdentifier();
}
