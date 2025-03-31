/*     */ package com.daimler.cebas.logs.control;
/*     */ 
/*     */ import ch.qos.logback.classic.Level;
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.logging.Level;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.jboss.logging.MDC;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.web.context.request.RequestContextHolder;
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
/*     */ 
/*     */ 
/*     */ public abstract class Logger
/*     */ {
/*     */   private static final String LOG_FORMAT = "%s %s %s : %s";
/*     */   @Value("${LOGGING_LEVEL}")
/*     */   protected String configLoggingLevel;
/*     */   @Value("${logging.prefix}")
/*     */   private String prefix;
/*     */   private String plantId;
/*     */   private MetadataManager requestMetadata;
/*     */   
/*     */   @Value("${csr.subject:}")
/*     */   private void setCsrSubject(String csrSubject) {
/*  48 */     if (StringUtils.isEmpty(csrSubject)) {
/*  49 */       this.plantId = csrSubject;
/*  50 */     } else if (csrSubject.contains("=")) {
/*  51 */       this.plantId = csrSubject.substring(csrSubject.lastIndexOf('=') + 1);
/*     */     } else {
/*  53 */       this.plantId = csrSubject;
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
/*     */   @Autowired
/*     */   public Logger(MetadataManager requestMetadata) {
/*  69 */     this.requestMetadata = requestMetadata;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void entering(String className, String methodName) {
/*  79 */     logEnteringToFile(className, methodName);
/*  80 */     log(Level.FINER, " ", methodName + " ENTRY", className);
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
/*     */   public void logEnteringToFile(String className, String methodName) {
/*  92 */     java.util.logging.Logger logger = java.util.logging.Logger.getLogger(className);
/*  93 */     logger.entering(className, methodName);
/*     */   }
/*     */   
/*     */   public void logToFileOnly(String className, String msg, Throwable thrown) {
/*  97 */     java.util.logging.Logger logger = java.util.logging.Logger.getLogger(className);
/*  98 */     logger.log(Level.INFO, msg, thrown);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void exiting(String className, String methodName) {
/* 108 */     logExitingToFile(className, methodName);
/* 109 */     log(Level.FINER, " ", methodName + " EXITING", className);
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
/*     */   public void logExitingToFile(String className, String methodName) {
/* 121 */     java.util.logging.Logger logger = java.util.logging.Logger.getLogger(className);
/* 122 */     logger.exiting(className, methodName);
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
/*     */   public void log(Level logLevel, String logId, String message, String className) {
/* 137 */     java.util.logging.Logger logger = java.util.logging.Logger.getLogger(className);
/* 138 */     if (!logger.isLoggable(logLevel)) {
/*     */       return;
/*     */     }
/*     */     
/* 142 */     registerLoggingParameters(logId);
/* 143 */     logger.log(logLevel, message);
/* 144 */     unregisterLoggingParameters();
/*     */ 
/*     */     
/* 147 */     String formattedMessage = formatMessage(logId, message);
/* 148 */     add(logLevel, formattedMessage);
/*     */   }
/*     */   
/*     */   private void registerLoggingParameters(String logId) {
/* 152 */     if (!StringUtils.isEmpty(logId)) {
/* 153 */       MDC.put("logId", this.prefix + logId);
/*     */     }
/* 155 */     if (!StringUtils.isEmpty(this.plantId)) {
/* 156 */       MDC.put("plantId", this.plantId);
/*     */     }
/*     */   }
/*     */   
/*     */   private void unregisterLoggingParameters() {
/* 161 */     MDC.remove("logId");
/* 162 */     MDC.remove("plantId");
/*     */   }
/*     */ 
/*     */   
/*     */   public String formatMessage(String logId, String message) {
/*     */     String correlationId;
/* 168 */     if (RequestContextHolder.getRequestAttributes() != null) {
/* 169 */       correlationId = getLogString(this.requestMetadata.getCorrelationId());
/*     */     } else {
/* 171 */       correlationId = getLogString(null);
/*     */     } 
/*     */     
/* 174 */     return String.format("%s %s %s : %s", new Object[] { getLogString((String)MDC.get("userId")), getLogString(this.prefix, logId), correlationId, message });
/*     */   }
/*     */ 
/*     */   
/*     */   private String getLogString(String value) {
/* 179 */     return getLogString(null, value);
/*     */   }
/*     */   
/*     */   private String getLogString(String prefix, String value) {
/* 183 */     if (StringUtils.isBlank(value)) {
/* 184 */       return " - ";
/*     */     }
/* 186 */     if (prefix != null) {
/* 187 */       return prefix + value;
/*     */     }
/* 189 */     return value;
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
/*     */   public <T extends CEBASException> Supplier<T> logWithTranslationSupplier(Level level, String logId, T exception) {
/* 204 */     return () -> {
/*     */         logWithTranslation(level, logId, exception.getMessageId(), exception.getClass().getSimpleName());
/*     */         return exception;
/*     */       };
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
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends CEBASException> Supplier<T> logWithTranslationSupplier(Level level, String logId, String[] args, T exception) {
/* 226 */     return () -> {
/*     */         logWithTranslation(level, logId, exception.getMessageId(), args, exception.getClass().getSimpleName());
/*     */         return exception;
/*     */       };
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
/*     */   public void logWithTranslation(Level level, String logId, String messageId, String className) {
/* 241 */     logWithTranslation(level, logId, messageId, new String[0], className);
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
/*     */   public void logWithTranslation(Level level, String logId, String messageId, String[] args, String className) {
/* 255 */     String message = (args.length == 0) ? this.requestMetadata.getEnglishMessage(messageId) : this.requestMetadata.getEnglishMessage(messageId, args);
/* 256 */     log(level, logId, message, className);
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
/*     */   public CEBASException logWithException(String logId, String message, CEBASException exception) {
/* 271 */     log(Level.SEVERE, logId, message, exception.getClass().getSimpleName());
/* 272 */     return exception;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Supplier<CEBASException> logWithExceptionSupplier(String logId, String message) {
/* 282 */     CEBASException exception = new CEBASException(message);
/* 283 */     return () -> logWithException(logId, message, exception);
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
/*     */   public void logWithExceptionByWarning(String logId, CEBASException exception) {
/* 295 */     log(Level.WARNING, logId, exception.getMessage(), exception.getClass().getSimpleName());
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
/*     */   public CEBASException logWithExceptionByWarning(String logId, String message, CEBASException exception) {
/* 310 */     log(Level.WARNING, logId, message, exception.getClass().getSimpleName());
/* 311 */     return exception;
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
/*     */   public void logWithExceptionByInfo(String logId, CEBASException exception) {
/* 323 */     log(Level.INFO, logId, exception.getMessage(), exception.getClass().getSimpleName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Supplier<CEBASException> logWithExceptionSupplierByWarning(String logId, String message) {
/* 333 */     CEBASException exception = new CEBASException(message);
/* 334 */     return () -> logWithExceptionByWarning(logId, message, exception);
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
/*     */   public void setConfigLoggingLevel(String configLoggingLevel) {
/* 361 */     this.configLoggingLevel = configLoggingLevel;
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
/*     */   public void logExceptionOnFinest(JsonProcessingException e, String className) {
/* 373 */     java.util.logging.Logger logger = java.util.logging.Logger.getLogger(className);
/* 374 */     if (!logger.isLoggable(Level.FINEST)) {
/*     */       return;
/*     */     }
/* 377 */     logger.log(Level.FINEST, e.getMessage(), (Throwable)e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level getLogbackMappedLogLevel(String loglevel) {
/* 388 */     if (loglevel.equalsIgnoreCase(Level.INFO.getName()))
/* 389 */       return Level.INFO; 
/* 390 */     if (loglevel.equalsIgnoreCase(Level.FINE.getName()) || loglevel
/* 391 */       .equalsIgnoreCase(Level.FINER.getName()))
/* 392 */       return Level.DEBUG; 
/* 393 */     if (loglevel.equalsIgnoreCase(Level.FINEST.getName())) {
/* 394 */       return Level.TRACE;
/*     */     }
/* 396 */     return null;
/*     */   }
/*     */   
/*     */   protected abstract boolean shouldLogToPersistentStores(Level paramLevel);
/*     */   
/*     */   protected abstract void add(Level paramLevel, String paramString);
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\logs\control\Logger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */