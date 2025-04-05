/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.InvalidInputException
 *  com.daimler.cebas.common.control.CEBASI18N
 *  com.daimler.cebas.common.control.RequestMetadata
 *  com.daimler.cebas.common.control.annotations.CEBASService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Qualifier
 *  org.springframework.web.context.request.RequestContextHolder
 *  org.springframework.web.util.HtmlUtils
 */
package com.daimler.cebas.common.control;

import com.daimler.cebas.certificates.control.exceptions.InvalidInputException;
import com.daimler.cebas.common.control.CEBASI18N;
import com.daimler.cebas.common.control.RequestMetadata;
import com.daimler.cebas.common.control.annotations.CEBASService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.util.HtmlUtils;

@CEBASService
public class MetadataManager {
    @Autowired
    @Qualifier(value="CEBASI18N")
    private CEBASI18N zenZefiI18N;
    @Autowired
    private RequestMetadata requestMetadata;

    public String getMessage(String id, String[] args) {
        return RequestContextHolder.getRequestAttributes() != null ? this.requestMetadata.getMessage(id, args) : this.zenZefiI18N.getMessage(id, args);
    }

    public String getEnglishMessage(String id, String[] args) {
        return RequestContextHolder.getRequestAttributes() != null ? this.requestMetadata.getEnglishMessage(id, args) : this.zenZefiI18N.getEnglishMessage(id, args);
    }

    public String getMessage(String id, String[] args, String locale) {
        return RequestContextHolder.getRequestAttributes() != null ? this.requestMetadata.getMessage(id, args, locale) : this.zenZefiI18N.getMessage(id, args, locale);
    }

    public String getEnglishMessage(String id) {
        return RequestContextHolder.getRequestAttributes() != null ? this.requestMetadata.getEnglishMessage(id) : this.zenZefiI18N.getEnglishMessage(id);
    }

    public String getCorrelationId() {
        return RequestContextHolder.getRequestAttributes() != null ? this.requestMetadata.getCorrelationId() : "";
    }

    public String getMessage(String id) {
        return RequestContextHolder.getRequestAttributes() != null ? this.requestMetadata.getMessage(id) : this.zenZefiI18N.getMessage(id);
    }

    public String getLocale() {
        return RequestContextHolder.getRequestAttributes() != null ? this.requestMetadata.getLocale() : this.zenZefiI18N.getLocale();
    }

    public void setLocale(String locale) {
        this.validateLocale(locale);
        this.requestMetadata.setLocale(locale);
    }

    public void setCorrelationId(String correlationId) {
        this.validateCorrelationId(this.emptyCorrelationId(correlationId));
        this.requestMetadata.setCorrelationId(this.emptyCorrelationId(correlationId));
    }

    public String emptyCorrelationId(String correlationid) {
        return correlationid == null ? "" : correlationid;
    }

    private void validateCorrelationId(String correlationId) {
        if (!StringUtils.isNotEmpty(correlationId)) return;
        if (StringUtils.containsWhitespace(correlationId)) throw new InvalidInputException("Invalid correlation id. It cannot contain spaces or html chars");
        if (correlationId.equals(HtmlUtils.htmlEscape((String)correlationId))) return;
        throw new InvalidInputException("Invalid correlation id. It cannot contain spaces or html chars");
    }

    private void validateLocale(String locale) {
        if (locale == null) throw new InvalidInputException("Invalid locale");
        if (locale.length() != 2) throw new InvalidInputException("Invalid locale");
        if (locale.equals(HtmlUtils.htmlEscape((String)locale))) return;
        throw new InvalidInputException("Invalid locale");
    }
}
