/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.integration.xentry;
/*     */ 
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.zenzefi.certificates.integration.xentry.XentryClient;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XentryRequest
/*     */ {
/*     */   private static final String CANNOT_PROCESS_PAYLOAD_FOR_XENTRY = "Cannot process payload for XENTRY";
/*  26 */   private static final Logger LOG = Logger.getLogger(XentryClient.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String BODY_PROPERTY = "\"BODY\":";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String RESOURCE_PROPERTY = "\"RESOURCE\":";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ENDPOINT_PROPERTY = "\"ENDPOINT\":";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String METHOD_PROPERTY = "\"METHOD\":";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String SEND_PROPERTY = "SEND:";
/*     */ 
/*     */ 
/*     */   
/*     */   private String sendDestination;
/*     */ 
/*     */ 
/*     */   
/*     */   private String method;
/*     */ 
/*     */   
/*     */   private String endpoint;
/*     */ 
/*     */   
/*     */   private String resource;
/*     */ 
/*     */   
/*     */   private String body;
/*     */ 
/*     */   
/*  66 */   private ObjectMapper mapper = new ObjectMapper();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XentryRequest(String sendDestination, String method, String endpoint, String resource, Object payload) {
/*  80 */     this.sendDestination = sendDestination;
/*  81 */     this.method = method;
/*  82 */     this.endpoint = endpoint;
/*  83 */     this.resource = resource;
/*     */     try {
/*  85 */       this.body = this.mapper.writeValueAsString(payload);
/*  86 */     } catch (JsonProcessingException e) {
/*  87 */       LOG.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*  88 */       throw new CEBASException("Cannot process payload for XENTRY");
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XentryRequest(String sendDestination, String method, String endpoint, String resource, String payloadJson) {
/* 105 */     this.sendDestination = sendDestination;
/* 106 */     this.method = method;
/* 107 */     this.endpoint = endpoint;
/* 108 */     this.resource = resource;
/* 109 */     if (StringUtils.isEmpty(payloadJson)) {
/*     */       try {
/* 111 */         this.body = this.mapper.writeValueAsString("");
/* 112 */       } catch (JsonProcessingException e) {
/* 113 */         LOG.log(Level.WARNING, e.getMessage(), (Throwable)e);
/* 114 */         throw new CEBASException("Cannot process payload for XENTRY");
/*     */       } 
/*     */     } else {
/* 117 */       this.body = payloadJson;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toRawData() {
/* 127 */     StringBuilder builder = new StringBuilder();
/* 128 */     builder.append("SEND:" + this.sendDestination + ":");
/* 129 */     builder.append("{");
/* 130 */     builder.append("\"METHOD\":\"" + this.method + "\",");
/* 131 */     builder.append("\"ENDPOINT\":\"" + this.endpoint + "\",");
/* 132 */     builder.append("\"RESOURCE\":\"" + this.resource + "\",");
/* 133 */     builder.append("\"BODY\":" + this.body);
/* 134 */     builder.append("}");
/* 135 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\integration\xentry\XentryRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */