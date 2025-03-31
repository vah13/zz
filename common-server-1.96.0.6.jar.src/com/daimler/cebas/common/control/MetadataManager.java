/*     */ package com.daimler.cebas.common.control;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.exceptions.InvalidInputException;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASService;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Qualifier;
/*     */ import org.springframework.web.context.request.RequestContextHolder;
/*     */ import org.springframework.web.util.HtmlUtils;
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
/*     */ @CEBASService
/*     */ public class MetadataManager
/*     */ {
/*     */   @Autowired
/*     */   @Qualifier("CEBASI18N")
/*     */   private CEBASI18N zenZefiI18N;
/*     */   @Autowired
/*     */   private RequestMetadata requestMetadata;
/*     */   
/*     */   public String getMessage(String id, String[] args) {
/*  41 */     return (RequestContextHolder.getRequestAttributes() != null) ? this.requestMetadata
/*  42 */       .getMessage(id, args) : this.zenZefiI18N
/*  43 */       .getMessage(id, args);
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
/*     */   public String getEnglishMessage(String id, String[] args) {
/*  57 */     return (RequestContextHolder.getRequestAttributes() != null) ? this.requestMetadata
/*  58 */       .getEnglishMessage(id, args) : this.zenZefiI18N
/*  59 */       .getEnglishMessage(id, args);
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
/*     */   public String getMessage(String id, String[] args, String locale) {
/*  75 */     return (RequestContextHolder.getRequestAttributes() != null) ? this.requestMetadata
/*  76 */       .getMessage(id, args, locale) : this.zenZefiI18N
/*  77 */       .getMessage(id, args, locale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEnglishMessage(String id) {
/*  88 */     return (RequestContextHolder.getRequestAttributes() != null) ? this.requestMetadata
/*  89 */       .getEnglishMessage(id) : this.zenZefiI18N
/*  90 */       .getEnglishMessage(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCorrelationId() {
/* 100 */     return (RequestContextHolder.getRequestAttributes() != null) ? this.requestMetadata
/* 101 */       .getCorrelationId() : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String id) {
/* 112 */     return (RequestContextHolder.getRequestAttributes() != null) ? this.requestMetadata
/* 113 */       .getMessage(id) : this.zenZefiI18N.getMessage(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocale() {
/* 123 */     return (RequestContextHolder.getRequestAttributes() != null) ? this.requestMetadata
/* 124 */       .getLocale() : this.zenZefiI18N.getLocale();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocale(String locale) {
/* 134 */     validateLocale(locale);
/* 135 */     this.requestMetadata.setLocale(locale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCorrelationId(String correlationId) {
/* 145 */     validateCorrelationId(emptyCorrelationId(correlationId));
/* 146 */     this.requestMetadata.setCorrelationId(emptyCorrelationId(correlationId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String emptyCorrelationId(String correlationid) {
/* 157 */     return (correlationid == null) ? "" : correlationid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateCorrelationId(String correlationId) {
/* 167 */     if (StringUtils.isNotEmpty(correlationId) && (
/* 168 */       StringUtils.containsWhitespace(correlationId) || 
/*     */       
/* 170 */       !correlationId.equals(HtmlUtils.htmlEscape(correlationId)))) {
/* 171 */       throw new InvalidInputException("Invalid correlation id. It cannot contain spaces or html chars");
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
/*     */   private void validateLocale(String locale) {
/* 183 */     if (locale == null || locale.length() != 2 || 
/* 184 */       !locale.equals(HtmlUtils.htmlEscape(locale)))
/* 185 */       throw new InvalidInputException("Invalid locale"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\MetadataManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */