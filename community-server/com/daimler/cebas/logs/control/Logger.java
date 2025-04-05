/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ch.qos.logback.classic.Level
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  org.jboss.logging.MDC
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.web.context.request.RequestContextHolder
 */
package com.daimler.cebas.logs.control;

import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.common.control.MetadataManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.function.Supplier;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;

public abstract class Logger {
    private static final String LOG_FORMAT = "%s %s %s : %s";
    @Value(value="${LOGGING_LEVEL}")
    protected String configLoggingLevel;
    @Value(value="${logging.prefix}")
    private String prefix;
    private String plantId;
    private MetadataManager requestMetadata;

    @Value(value="${csr.subject:}")
    private void setCsrSubject(String csrSubject) {
        this.plantId = StringUtils.isEmpty(csrSubject) ? csrSubject : (csrSubject.contains("=") ? csrSubject.substring(csrSubject.lastIndexOf(61) + 1) : csrSubject);
    }

    @Autowired
    public Logger(MetadataManager requestMetadata) {
        this.requestMetadata = requestMetadata;
    }

    public void entering(String className, String methodName) {
        this.logEnteringToFile(className, methodName);
        this.log(Level.FINER, " ", methodName + " ENTRY", className);
    }

    public void logEnteringToFile(String className, String methodName) {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(className);
        logger.entering(className, methodName);
    }

    public void logToFileOnly(String className, String msg, Throwable thrown) {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(className);
        logger.log(Level.INFO, msg, thrown);
    }

    public void exiting(String className, String methodName) {
        this.logExitingToFile(className, methodName);
        this.log(Level.FINER, " ", methodName + " EXITING", className);
    }

    public void logExitingToFile(String className, String methodName) {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(className);
        logger.exiting(className, methodName);
    }

    public void log(Level logLevel, String logId, String message, String className) {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(className);
        if (!logger.isLoggable(logLevel)) {
            return;
        }
        this.registerLoggingParameters(logId);
        logger.log(logLevel, message);
        this.unregisterLoggingParameters();
        String formattedMessage = this.formatMessage(logId, message);
        this.add(logLevel, formattedMessage);
    }

    private void registerLoggingParameters(String logId) {
        if (!StringUtils.isEmpty(logId)) {
            MDC.put((String)"logId", (Object)(this.prefix + logId));
        }
        if (StringUtils.isEmpty(this.plantId)) return;
        MDC.put((String)"plantId", (Object)this.plantId);
    }

    private void unregisterLoggingParameters() {
        MDC.remove((String)"logId");
        MDC.remove((String)"plantId");
    }

    public String formatMessage(String logId, String message) {
        String correlationId = RequestContextHolder.getRequestAttributes() != null ? this.getLogString(this.requestMetadata.getCorrelationId()) : this.getLogString(null);
        return String.format(LOG_FORMAT, this.getLogString((String)MDC.get((String)"userId")), this.getLogString(this.prefix, logId), correlationId, message);
    }

    private String getLogString(String value) {
        return this.getLogString(null, value);
    }

    private String getLogString(String prefix, String value) {
        if (StringUtils.isBlank(value)) {
            return " - ";
        }
        if (prefix == null) return value;
        return prefix + value;
    }

    public <T extends CEBASException> Supplier<T> logWithTranslationSupplier(Level level, String logId, T exception) {
        return () -> {
            this.logWithTranslation(level, logId, exception.getMessageId(), exception.getClass().getSimpleName());
            return exception;
        };
    }

    public <T extends CEBASException> Supplier<T> logWithTranslationSupplier(Level level, String logId, String[] args, T exception) {
        return () -> {
            this.logWithTranslation(level, logId, exception.getMessageId(), args, exception.getClass().getSimpleName());
            return exception;
        };
    }

    public void logWithTranslation(Level level, String logId, String messageId, String className) {
        this.logWithTranslation(level, logId, messageId, new String[0], className);
    }

    public void logWithTranslation(Level level, String logId, String messageId, String[] args, String className) {
        String message = args.length == 0 ? this.requestMetadata.getEnglishMessage(messageId) : this.requestMetadata.getEnglishMessage(messageId, args);
        this.log(level, logId, message, className);
    }

    public CEBASException logWithException(String logId, String message, CEBASException exception) {
        this.log(Level.SEVERE, logId, message, exception.getClass().getSimpleName());
        return exception;
    }

    public Supplier<CEBASException> logWithExceptionSupplier(String logId, String message) {
        CEBASException exception = new CEBASException(message);
        return () -> this.logWithException(logId, message, exception);
    }

    public void logWithExceptionByWarning(String logId, CEBASException exception) {
        this.log(Level.WARNING, logId, exception.getMessage(), exception.getClass().getSimpleName());
    }

    public CEBASException logWithExceptionByWarning(String logId, String message, CEBASException exception) {
        this.log(Level.WARNING, logId, message, exception.getClass().getSimpleName());
        return exception;
    }

    public void logWithExceptionByInfo(String logId, CEBASException exception) {
        this.log(Level.INFO, logId, exception.getMessage(), exception.getClass().getSimpleName());
    }

    public Supplier<CEBASException> logWithExceptionSupplierByWarning(String logId, String message) {
        CEBASException exception = new CEBASException(message);
        return () -> this.logWithExceptionByWarning(logId, message, exception);
    }

    protected abstract boolean shouldLogToPersistentStores(Level var1);

    protected abstract void add(Level var1, String var2);

    public void setConfigLoggingLevel(String configLoggingLevel) {
        this.configLoggingLevel = configLoggingLevel;
    }

    public void logExceptionOnFinest(JsonProcessingException e, String className) {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(className);
        if (!logger.isLoggable(Level.FINEST)) {
            return;
        }
        logger.log(Level.FINEST, e.getMessage(), (Throwable)e);
    }

    public static ch.qos.logback.classic.Level getLogbackMappedLogLevel(String loglevel) {
        if (loglevel.equalsIgnoreCase(Level.INFO.getName())) {
            return ch.qos.logback.classic.Level.INFO;
        }
        if (loglevel.equalsIgnoreCase(Level.FINE.getName())) return ch.qos.logback.classic.Level.DEBUG;
        if (loglevel.equalsIgnoreCase(Level.FINER.getName())) {
            return ch.qos.logback.classic.Level.DEBUG;
        }
        if (!loglevel.equalsIgnoreCase(Level.FINEST.getName())) return null;
        return ch.qos.logback.classic.Level.TRACE;
    }
}
