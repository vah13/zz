/*     */ package com.daimler.cebas.certificates.control;
/*     */ 
/*     */ import java.time.Duration;
/*     */ import java.time.LocalDateTime;
/*     */ import java.time.Period;
/*     */ import java.time.format.DateTimeParseException;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class DurationParser
/*     */ {
/*  21 */   private static final Pattern PATTERN = Pattern.compile("([-+]?)P(?:([-+]?[0-9]+)Y)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)W)?(?:([-+]?[0-9]+)D)?(T(?:([-+]?[0-9]+)H)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)(?:[.,]([0-9]{0,9}))?S)?)?", 2);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Period period;
/*     */ 
/*     */ 
/*     */   
/*     */   private Duration duration;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DurationParser(Period period, Duration duration) {
/*  36 */     this.period = period;
/*  37 */     this.duration = duration;
/*     */   }
/*     */   
/*     */   public Period getPeriod() {
/*  41 */     return this.period;
/*     */   }
/*     */   
/*     */   public Duration getDuration() {
/*  45 */     return this.duration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DurationParser parse(String text) {
/*  56 */     Matcher matcher = PATTERN.matcher(text);
/*  57 */     if (matcher.matches()) {
/*     */       Period period;
/*  59 */       Duration duration = Duration.ZERO;
/*     */       
/*  61 */       int timeStart = text.indexOf("T");
/*  62 */       if (timeStart > 0) {
/*  63 */         String periodText = text.substring(0, timeStart);
/*  64 */         period = (periodText.length() > 1) ? Period.parse(periodText) : Period.ZERO;
/*     */         
/*  66 */         duration = Duration.parse("P" + text.substring(timeStart));
/*     */       } else {
/*  68 */         period = Period.parse(text);
/*     */       } 
/*     */       
/*  71 */       return new DurationParser(period, duration);
/*     */     } 
/*  73 */     throw new DateTimeParseException("Text cannot be parsed to a DurationParser", text, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalDateTime plusTo(LocalDateTime dateTime) {
/*  83 */     return dateTime.plus(this.period).plus(this.duration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalDateTime minusFrom(LocalDateTime dateTime) {
/*  93 */     return dateTime.minus(this.period).minus(this.duration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 103 */     return this.period.toString() + this.duration.toString().substring(1);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\DurationParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */