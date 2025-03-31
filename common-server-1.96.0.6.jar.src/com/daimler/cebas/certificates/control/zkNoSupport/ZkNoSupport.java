/*    */ package com.daimler.cebas.certificates.control.zkNoSupport;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.validation.CertificatesFieldsValidator;
/*    */ import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
/*    */ import com.daimler.cebas.common.control.HexUtil;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.logging.Level;
/*    */ import javax.persistence.Tuple;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ZkNoSupport
/*    */ {
/*    */   static List<ZkNoMappingResult> getZkNoMapping(String identifier, List<Tuple> tuples, Logger logger) {
/* 33 */     Boolean isZkNo = StringUtils.isEmpty(identifier) ? null : Boolean.valueOf(CertificatesFieldsValidator.isZkNo(identifier));
/* 34 */     List<ZkNoMappingResult> result = getZkNoMapping(tuples);
/*    */     
/* 36 */     if (StringUtils.isBlank(identifier)) {
/* 37 */       String mapping = "";
/*    */       try {
/* 39 */         mapping = (new ObjectMapper()).writeValueAsString(result);
/* 40 */       } catch (JsonProcessingException e) {
/* 41 */         logger.logExceptionOnFinest(e, ZkNoSupport.class.getSimpleName());
/*    */       } 
/* 43 */       logger.logWithTranslation(Level.INFO, "000547", "entireMappingReturned", new String[] { mapping }, ZkNoSupport.class
/* 44 */           .getSimpleName());
/*    */     } 
/* 46 */     if (result.isEmpty()) {
/* 47 */       String message = loggingMessage(identifier, isZkNo);
/* 48 */       logger.logWithTranslation(Level.INFO, "000548", "entireMappingNothingFound", new String[] { message }, ZkNoSupport.class
/* 49 */           .getSimpleName());
/*    */     } else {
/* 51 */       for (Tuple tuple : tuples) {
/* 52 */         logger.logWithTranslation(Level.INFO, "000549", "zkNumberMappingFound", new String[] { (String)tuple
/* 53 */               .get(1), (String)tuple.get(0) }, ZkNoSupport.class.getSimpleName());
/*    */       } 
/*    */     } 
/* 56 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   static String loggingMessage(String identifier, Boolean isZkNo) {
/* 61 */     if (isZkNo == null) {
/* 62 */       return "EMPTY";
/*    */     }
/* 64 */     return isZkNo.booleanValue() ? ("ZK Number " + identifier) : ("BSKI " + HexUtil.base64ToHex(identifier));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static List<ZkNoMappingResult> getZkNoMapping(List<Tuple> tuples) {
/* 73 */     List<ZkNoMappingResult> result = new ArrayList<>();
/* 74 */     for (Tuple tuple : tuples) {
/* 75 */       result.add(new ZkNoMappingResult(CertificateParser.hexToBase64((String)tuple.get(0)), (String)tuple.get(1)));
/*    */     }
/* 77 */     return result;
/*    */   }
/*    */   
/*    */   String getIdentifier();
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\zkNoSupport\ZkNoSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */