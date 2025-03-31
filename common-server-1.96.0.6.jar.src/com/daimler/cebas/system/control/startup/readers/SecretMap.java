/*    */ package com.daimler.cebas.system.control.startup.readers;
/*    */ 
/*    */ import com.daimler.cebas.system.control.exceptions.ConfigurationCheckException;
/*    */ import com.daimler.cebas.system.control.startup.CeBASStartupProperty;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.core.type.TypeReference;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Base64;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class SecretMap
/*    */ {
/* 27 */   private static final Log LOG = LogFactory.getLog(SecretMap.class);
/*    */   
/* 29 */   private final Map<String, String> values = new HashMap<>();
/*    */   
/*    */   public void put(CeBASStartupProperty key, String value) {
/* 32 */     this.values.put(key.name(), value);
/*    */   }
/*    */   
/*    */   public String get(CeBASStartupProperty key) {
/* 36 */     return this.values.get(key.name());
/*    */   }
/*    */   
/*    */   public boolean contains(CeBASStartupProperty key) {
/* 40 */     return this.values.containsKey(key.name());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static SecretMap fromString(String encoded) {
/* 50 */     String decoded = new String(Base64.getDecoder().decode(encoded.trim()), StandardCharsets.UTF_8);
/*    */     
/*    */     try {
/* 53 */       Map<String, String> typedMap = (Map<String, String>)(new ObjectMapper()).readValue(decoded, new TypeReference<Map<String, String>>() {
/*    */           
/*    */           });
/* 56 */       SecretMap sc = new SecretMap();
/* 57 */       sc.values.putAll(typedMap);
/* 58 */       return sc;
/* 59 */     } catch (IOException e) {
/* 60 */       LOG.error(e);
/* 61 */       throw new ConfigurationCheckException("Invalid configuration. Try to reset the AppData Folder. " + e
/* 62 */           .getMessage());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String createString(int maximumSecretSize) {
/*    */     String json;
/*    */     try {
/* 74 */       json = (new ObjectMapper()).writeValueAsString(this.values);
/* 75 */     } catch (JsonProcessingException e) {
/* 76 */       LOG.error(e);
/* 77 */       throw new ConfigurationCheckException("Invalid configuration. Try to reset the AppData Folder. " + e
/* 78 */           .getMessage());
/*    */     } 
/*    */     
/* 81 */     String encoded = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
/* 82 */     if (encoded.length() > maximumSecretSize) {
/* 83 */       throw new ConfigurationCheckException("Invalid configuration. It is not possible to store the configuration to the secret store.");
/*    */     }
/*    */     
/* 86 */     return String.format("%1$" + maximumSecretSize + "s", new Object[] { encoded });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SecretMap join(SecretMap other) {
/* 95 */     this.values.putAll(other.values);
/* 96 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\startup\readers\SecretMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */