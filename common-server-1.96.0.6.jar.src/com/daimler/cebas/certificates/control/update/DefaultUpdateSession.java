/*     */ package com.daimler.cebas.certificates.control.update;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.exceptions.RuntimeExceptionEvent;
/*     */ import com.daimler.cebas.certificates.control.exceptions.StoreModificationNotAllowed;
/*     */ import com.daimler.cebas.certificates.control.exceptions.UpdateNotRunningException;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKICertificateRequest;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.time.Instant;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.logging.Level;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.context.event.EventListener;
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
/*     */ public class DefaultUpdateSession
/*     */   implements IUpdateSession
/*     */ {
/*  40 */   private UpdateSteps currentStep = UpdateSteps.NONE;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   private UpdateStatus status = UpdateStatus.NOT_RUNNING;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final BlockingQueue<UpdateDetails> stepDetails;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Value("${pki.update.max.retries}")
/*     */   protected Integer maxRetries;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Integer currentRetry;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String retryUrl;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   private String sessionLocale = "en";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Value("${pki.update.time.interval}")
/*     */   private String propertiesRetryTimeInterval;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Integer nextRetryTimeInterval;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long nextRetryTimestamp;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MetadataManager metadataManager;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected UpdateType updateType;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<UpdateSteps, Object> stepResults;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean didFailAllRetries;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   private long stopTimestamp = 0L;
/*     */ 
/*     */ 
/*     */   
/*     */   protected UpdateDetails lastDetailsAdded;
/*     */ 
/*     */ 
/*     */   
/*     */   private AtomicInteger invalidTokenRetries;
/*     */ 
/*     */ 
/*     */   
/* 129 */   private List<PKICertificateRequest> pkiCertificateRequests = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean ecuPackageTsReset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean linkPackageTsReset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public DefaultUpdateSession(Logger logger, MetadataManager metadataManager) {
/* 151 */     this.logger = logger;
/* 152 */     this.metadataManager = metadataManager;
/* 153 */     this.stepDetails = new LinkedBlockingQueue<>();
/* 154 */     this.stepResults = new EnumMap<>(UpdateSteps.class);
/* 155 */     this.invalidTokenRetries = new AtomicInteger(0);
/* 156 */     this.ecuPackageTsReset = false;
/* 157 */     this.linkPackageTsReset = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void run() {
/* 165 */     run("Update session is running");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void run(String message) {
/* 176 */     if (isRunning()) {
/* 177 */       this.logger.log(Level.WARNING, "000282", this.metadataManager
/* 178 */           .getEnglishMessage("updateOperationNotAllowed"), 
/* 179 */           getClass().getSimpleName());
/* 180 */       throw new StoreModificationNotAllowed(this.metadataManager
/* 181 */           .getMessage("updateOperationNotAllowed"));
/*     */     } 
/* 183 */     reset();
/* 184 */     this.status = UpdateStatus.RUNNING;
/* 185 */     this.logger.log(Level.INFO, "000279", message, DefaultUpdateSession.class.getSimpleName());
/*     */   }
/*     */   
/*     */   public void reset() {
/* 189 */     this.ecuPackageTsReset = false;
/* 190 */     this.linkPackageTsReset = false;
/* 191 */     this.invalidTokenRetries.set(0);
/* 192 */     this.nextRetryTimeInterval = Integer.valueOf(0);
/* 193 */     this.nextRetryTimestamp = 0L;
/* 194 */     setDidFailAllRetries(false);
/* 195 */     this.stepDetails.clear();
/* 196 */     this.lastDetailsAdded = null;
/* 197 */     this.currentStep = UpdateSteps.RETRIEVE_BACKEND_IDENTIFIERS;
/* 198 */     this.pkiCertificateRequests.clear();
/* 199 */     if (this.status.equals(UpdateStatus.NOT_RUNNING)) {
/* 200 */       this.stepResults.clear();
/* 201 */       this.currentRetry = Integer.valueOf(0);
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
/*     */   public void updateStep(UpdateSteps step, String details, UpdateType updateType) {
/* 218 */     this.currentStep = step;
/* 219 */     this.updateType = updateType;
/* 220 */     this.lastDetailsAdded = new UpdateDetails(step, details);
/* 221 */     this.stepDetails.add(this.lastDetailsAdded);
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
/*     */   public void updateStep(UpdateSteps step, String details, UpdateType updateType, String[] messageArguments) {
/* 238 */     this.currentStep = step;
/* 239 */     this.updateType = updateType;
/* 240 */     this.lastDetailsAdded = new UpdateDetails(step, details, messageArguments);
/* 241 */     this.stepDetails.add(this.lastDetailsAdded);
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
/*     */   public void updateStep(UpdateSteps step, String details, UpdateType updateType, boolean error) {
/* 258 */     this.currentStep = step;
/* 259 */     this.updateType = updateType;
/* 260 */     this.lastDetailsAdded = new UpdateDetails(step, details, error);
/* 261 */     this.stepDetails.add(this.lastDetailsAdded);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer retry(String retryUrl) {
/* 272 */     DefaultUpdateSession defaultUpdateSession = this; Integer integer1 = defaultUpdateSession.currentRetry, integer2 = defaultUpdateSession.currentRetry = Integer.valueOf(defaultUpdateSession.currentRetry.intValue() + 1);
/* 273 */     this.retryUrl = retryUrl;
/* 274 */     this.stepDetails.add(this.lastDetailsAdded);
/* 275 */     return this.currentRetry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNotRunning() {
/* 283 */     if (isRunning()) {
/* 284 */       this.stopTimestamp = (new Date()).getTime();
/* 285 */       this.status = UpdateStatus.NOT_RUNNING;
/* 286 */       this.currentStep = UpdateSteps.NONE;
/* 287 */       this.invalidTokenRetries.set(0);
/* 288 */       this.stepResults.clear();
/* 289 */       this.pkiCertificateRequests.clear();
/* 290 */       this.logger.log(Level.INFO, "000279", "Update session is ended", DefaultUpdateSession.class
/* 291 */           .getSimpleName());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleInvalidToken(String errorMessage, UpdateType updateType) {
/* 300 */     updateStep(getCurrentStep(), errorMessage, updateType, true);
/* 301 */     this.status = UpdateStatus.NOT_RUNNING;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resume() {
/* 309 */     this.status = UpdateStatus.RUNNING;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventListener
/*     */   public void sessionErrorEvent(RuntimeExceptionEvent event) {
/* 320 */     setNotRunning();
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
/*     */   public void addStepResult(UpdateSteps step, Object result) {
/* 333 */     this.stepResults.put(step, result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRunning() {
/* 343 */     return (this.status == UpdateStatus.RUNNING);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPaused() {
/* 353 */     return (this.status == UpdateStatus.PAUSED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNotRunning() {
/* 363 */     return (this.status == UpdateStatus.NOT_RUNNING);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 370 */     if (isNotRunning()) {
/* 371 */       throw new UpdateNotRunningException("Update session cannot be cancelled since is not running");
/*     */     }
/* 373 */     setNotRunning();
/* 374 */     this.logger.log(Level.INFO, "000376", "Update was canceled", getClass().getSimpleName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized UpdateDetails getCurrentDetails() {
/* 385 */     if (getCurrentRetry().intValue() == 0 || (getCurrentRetry().intValue() > 0 && this.stepDetails.size() > 1) || isNotRunning()) {
/* 386 */       return this.stepDetails.poll();
/*     */     }
/* 388 */     return this.stepDetails.peek();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UpdateSteps getCurrentStep() {
/* 399 */     return this.currentStep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UpdateStatus getStatus() {
/* 409 */     return this.status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getCurrentRetry() {
/* 419 */     return this.currentRetry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRetryUrl() {
/* 429 */     return this.retryUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getMaxRetries() {
/* 439 */     return this.maxRetries;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDetailsEmpty() {
/* 449 */     return this.stepDetails.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UpdateType getUpdateType() {
/* 459 */     return this.updateType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastStopTimestamp() {
/* 469 */     return this.stopTimestamp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getStepResult(UpdateSteps step, Class<T> clazz) {
/* 480 */     return (T)this.stepResults.get(step);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean didFailAllRetries() {
/* 490 */     return this.didFailAllRetries;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDidFailAllRetries(boolean didFailAllRetries) {
/* 500 */     this.didFailAllRetries = didFailAllRetries;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getNextRetryTimeInterval() {
/* 510 */     return this.nextRetryTimeInterval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNextRetryTimeInterval() {
/* 518 */     this.nextRetryTimeInterval = computeNextRetryTime();
/* 519 */     this.nextRetryTimestamp = Instant.now().plusSeconds(this.nextRetryTimeInterval.intValue()).toEpochMilli();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetRetries() {
/* 527 */     this.currentRetry = Integer.valueOf(0);
/* 528 */     this.retryUrl = "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getNextRetryTimeStamp() {
/* 537 */     return this.nextRetryTimestamp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void increseInvalidTokenRetries() {
/* 544 */     this.invalidTokenRetries.incrementAndGet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean tooManyInvalidTokenRetries() {
/* 553 */     return (this.invalidTokenRetries.get() == 0);
/*     */   }
/*     */   
/*     */   public List<PKICertificateRequest> getPkiCertificateRequests() {
/* 557 */     return this.pkiCertificateRequests;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Integer computeNextRetryTime() {
/* 566 */     Integer crt = Integer.valueOf(1);
/* 567 */     Integer multiplicationValue = Integer.valueOf(1);
/* 568 */     Integer initialIntervalTime = Integer.valueOf(this.propertiesRetryTimeInterval);
/*     */     
/* 570 */     while (crt.intValue() < this.currentRetry.intValue()) {
/* 571 */       multiplicationValue = Integer.valueOf(multiplicationValue.intValue() * 2);
/* 572 */       Integer integer1 = crt, integer2 = crt = Integer.valueOf(crt.intValue() + 1);
/*     */     } 
/* 574 */     return Integer.valueOf(initialIntervalTime.intValue() * multiplicationValue.intValue() / 1000);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUpLanguage() {
/* 581 */     this.sessionLocale = this.metadataManager.getLocale();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSessionLocale() {
/* 590 */     return this.sessionLocale;
/*     */   }
/*     */   
/*     */   public Boolean ecuResetCondition() {
/* 594 */     return Boolean.valueOf(this.ecuPackageTsReset);
/*     */   }
/*     */   
/*     */   public void resetEcuCondition(Boolean resetEcuCondition) {
/* 598 */     this.ecuPackageTsReset = resetEcuCondition.booleanValue();
/*     */   }
/*     */   
/*     */   public Boolean linkResetCondition() {
/* 602 */     return Boolean.valueOf(this.linkPackageTsReset);
/*     */   }
/*     */   
/*     */   public void resetLinkCondition(Boolean resetLinkCondition) {
/* 606 */     this.linkPackageTsReset = resetLinkCondition.booleanValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\contro\\update\DefaultUpdateSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */