/*     */ package com.daimler.cebas.certificates.control.update;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UpdateDetails
/*     */ {
/*     */   private UpdateSteps step;
/*     */   private String message;
/*     */   private String[] messageArguments;
/*     */   private boolean error;
/*     */   
/*     */   public UpdateDetails(UpdateSteps step, String message) {
/*  39 */     this.step = step;
/*  40 */     this.message = message;
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
/*     */   public UpdateDetails(UpdateSteps step, String message, String[] messageArguments) {
/*  56 */     this.step = step;
/*  57 */     this.message = message;
/*  58 */     this.messageArguments = messageArguments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UpdateDetails(UpdateSteps step, String message, boolean error) {
/*  69 */     this(step, message);
/*  70 */     this.error = error;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/*  79 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UpdateSteps getStep() {
/*  88 */     return this.step;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isError() {
/*  97 */     return this.error;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getMessageArguments() {
/* 106 */     return this.messageArguments;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\contro\\update\UpdateDetails.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */