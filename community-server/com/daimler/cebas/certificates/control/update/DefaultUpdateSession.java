/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.RuntimeExceptionEvent
 *  com.daimler.cebas.certificates.control.exceptions.StoreModificationNotAllowed
 *  com.daimler.cebas.certificates.control.exceptions.UpdateNotRunningException
 *  com.daimler.cebas.certificates.control.update.IUpdateSession
 *  com.daimler.cebas.certificates.control.update.UpdateDetails
 *  com.daimler.cebas.certificates.control.update.UpdateStatus
 *  com.daimler.cebas.certificates.control.update.UpdateSteps
 *  com.daimler.cebas.certificates.control.update.UpdateType
 *  com.daimler.cebas.certificates.integration.vo.PKICertificateRequest
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.logs.control.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.context.event.EventListener
 */
package com.daimler.cebas.certificates.control.update;

import com.daimler.cebas.certificates.control.exceptions.RuntimeExceptionEvent;
import com.daimler.cebas.certificates.control.exceptions.StoreModificationNotAllowed;
import com.daimler.cebas.certificates.control.exceptions.UpdateNotRunningException;
import com.daimler.cebas.certificates.control.update.IUpdateSession;
import com.daimler.cebas.certificates.control.update.UpdateDetails;
import com.daimler.cebas.certificates.control.update.UpdateStatus;
import com.daimler.cebas.certificates.control.update.UpdateSteps;
import com.daimler.cebas.certificates.control.update.UpdateType;
import com.daimler.cebas.certificates.integration.vo.PKICertificateRequest;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.logs.control.Logger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;

public class DefaultUpdateSession
implements IUpdateSession {
    private UpdateSteps currentStep = UpdateSteps.NONE;
    private UpdateStatus status = UpdateStatus.NOT_RUNNING;
    private Logger logger;
    private final BlockingQueue<UpdateDetails> stepDetails;
    @Value(value="${pki.update.max.retries}")
    protected Integer maxRetries;
    protected Integer currentRetry;
    protected String retryUrl;
    private String sessionLocale = "en";
    @Value(value="${pki.update.time.interval}")
    private String propertiesRetryTimeInterval;
    private Integer nextRetryTimeInterval;
    protected long nextRetryTimestamp;
    protected MetadataManager metadataManager;
    protected UpdateType updateType;
    private Map<UpdateSteps, Object> stepResults;
    protected boolean didFailAllRetries;
    private long stopTimestamp = 0L;
    protected UpdateDetails lastDetailsAdded;
    private AtomicInteger invalidTokenRetries;
    private List<PKICertificateRequest> pkiCertificateRequests = new ArrayList<PKICertificateRequest>();
    private boolean ecuPackageTsReset;
    private boolean linkPackageTsReset;

    @Autowired
    public DefaultUpdateSession(Logger logger, MetadataManager metadataManager) {
        this.logger = logger;
        this.metadataManager = metadataManager;
        this.stepDetails = new LinkedBlockingQueue<UpdateDetails>();
        this.stepResults = new EnumMap<UpdateSteps, Object>(UpdateSteps.class);
        this.invalidTokenRetries = new AtomicInteger(0);
        this.ecuPackageTsReset = false;
        this.linkPackageTsReset = false;
    }

    public synchronized void run() {
        this.run("Update session is running");
    }

    public synchronized void run(String message) {
        if (this.isRunning()) {
            this.logger.log(Level.WARNING, "000282", this.metadataManager.getEnglishMessage("updateOperationNotAllowed"), this.getClass().getSimpleName());
            throw new StoreModificationNotAllowed(this.metadataManager.getMessage("updateOperationNotAllowed"));
        }
        this.reset();
        this.status = UpdateStatus.RUNNING;
        this.logger.log(Level.INFO, "000279", message, DefaultUpdateSession.class.getSimpleName());
    }

    public void reset() {
        this.ecuPackageTsReset = false;
        this.linkPackageTsReset = false;
        this.invalidTokenRetries.set(0);
        this.nextRetryTimeInterval = 0;
        this.nextRetryTimestamp = 0L;
        this.setDidFailAllRetries(false);
        this.stepDetails.clear();
        this.lastDetailsAdded = null;
        this.currentStep = UpdateSteps.RETRIEVE_BACKEND_IDENTIFIERS;
        this.pkiCertificateRequests.clear();
        if (!this.status.equals((Object)UpdateStatus.NOT_RUNNING)) return;
        this.stepResults.clear();
        this.currentRetry = 0;
    }

    public void updateStep(UpdateSteps step, String details, UpdateType updateType) {
        this.currentStep = step;
        this.updateType = updateType;
        this.lastDetailsAdded = new UpdateDetails(step, details);
        this.stepDetails.add(this.lastDetailsAdded);
    }

    public void updateStep(UpdateSteps step, String details, UpdateType updateType, String[] messageArguments) {
        this.currentStep = step;
        this.updateType = updateType;
        this.lastDetailsAdded = new UpdateDetails(step, details, messageArguments);
        this.stepDetails.add(this.lastDetailsAdded);
    }

    public void updateStep(UpdateSteps step, String details, UpdateType updateType, boolean error) {
        this.currentStep = step;
        this.updateType = updateType;
        this.lastDetailsAdded = new UpdateDetails(step, details, error);
        this.stepDetails.add(this.lastDetailsAdded);
    }

    public Integer retry(String retryUrl) {
        DefaultUpdateSession defaultUpdateSession = this;
        Integer n = defaultUpdateSession.currentRetry;
        Integer n2 = defaultUpdateSession.currentRetry = Integer.valueOf(defaultUpdateSession.currentRetry + 1);
        this.retryUrl = retryUrl;
        this.stepDetails.add(this.lastDetailsAdded);
        return this.currentRetry;
    }

    public void setNotRunning() {
        if (!this.isRunning()) return;
        this.stopTimestamp = new Date().getTime();
        this.status = UpdateStatus.NOT_RUNNING;
        this.currentStep = UpdateSteps.NONE;
        this.invalidTokenRetries.set(0);
        this.stepResults.clear();
        this.pkiCertificateRequests.clear();
        this.logger.log(Level.INFO, "000279", "Update session is ended", DefaultUpdateSession.class.getSimpleName());
    }

    public void handleInvalidToken(String errorMessage, UpdateType updateType) {
        this.updateStep(this.getCurrentStep(), errorMessage, updateType, true);
        this.status = UpdateStatus.NOT_RUNNING;
    }

    public void resume() {
        this.status = UpdateStatus.RUNNING;
    }

    @EventListener
    public void sessionErrorEvent(RuntimeExceptionEvent event) {
        this.setNotRunning();
    }

    public void addStepResult(UpdateSteps step, Object result) {
        this.stepResults.put(step, result);
    }

    public boolean isRunning() {
        return this.status == UpdateStatus.RUNNING;
    }

    public boolean isPaused() {
        return this.status == UpdateStatus.PAUSED;
    }

    public boolean isNotRunning() {
        return this.status == UpdateStatus.NOT_RUNNING;
    }

    public void cancel() {
        if (this.isNotRunning()) {
            throw new UpdateNotRunningException("Update session cannot be cancelled since is not running");
        }
        this.setNotRunning();
        this.logger.log(Level.INFO, "000376", "Update was canceled", this.getClass().getSimpleName());
    }

    public synchronized UpdateDetails getCurrentDetails() {
        if (this.getCurrentRetry() == 0) return (UpdateDetails)this.stepDetails.poll();
        if (this.getCurrentRetry() > 0) {
            if (this.stepDetails.size() > 1) return (UpdateDetails)this.stepDetails.poll();
        }
        if (!this.isNotRunning()) return (UpdateDetails)this.stepDetails.peek();
        return (UpdateDetails)this.stepDetails.poll();
    }

    public UpdateSteps getCurrentStep() {
        return this.currentStep;
    }

    public UpdateStatus getStatus() {
        return this.status;
    }

    public Integer getCurrentRetry() {
        return this.currentRetry;
    }

    public String getRetryUrl() {
        return this.retryUrl;
    }

    public Integer getMaxRetries() {
        return this.maxRetries;
    }

    public boolean isDetailsEmpty() {
        return this.stepDetails.isEmpty();
    }

    public UpdateType getUpdateType() {
        return this.updateType;
    }

    public long getLastStopTimestamp() {
        return this.stopTimestamp;
    }

    public <T> T getStepResult(UpdateSteps step, Class<T> clazz) {
        return (T)this.stepResults.get(step);
    }

    public boolean didFailAllRetries() {
        return this.didFailAllRetries;
    }

    public void setDidFailAllRetries(boolean didFailAllRetries) {
        this.didFailAllRetries = didFailAllRetries;
    }

    public Integer getNextRetryTimeInterval() {
        return this.nextRetryTimeInterval;
    }

    public void setNextRetryTimeInterval() {
        this.nextRetryTimeInterval = this.computeNextRetryTime();
        this.nextRetryTimestamp = Instant.now().plusSeconds(this.nextRetryTimeInterval.intValue()).toEpochMilli();
    }

    public void resetRetries() {
        this.currentRetry = 0;
        this.retryUrl = "";
    }

    public long getNextRetryTimeStamp() {
        return this.nextRetryTimestamp;
    }

    public void increseInvalidTokenRetries() {
        this.invalidTokenRetries.incrementAndGet();
    }

    public boolean tooManyInvalidTokenRetries() {
        return this.invalidTokenRetries.get() == 0;
    }

    public List<PKICertificateRequest> getPkiCertificateRequests() {
        return this.pkiCertificateRequests;
    }

    private Integer computeNextRetryTime() {
        Integer crt = 1;
        Integer multiplicationValue = 1;
        Integer initialIntervalTime = Integer.valueOf(this.propertiesRetryTimeInterval);
        while (crt < this.currentRetry) {
            multiplicationValue = multiplicationValue * 2;
            Integer n = crt;
            Integer n2 = crt = Integer.valueOf(crt + 1);
        }
        return initialIntervalTime * multiplicationValue / 1000;
    }

    public void setUpLanguage() {
        this.sessionLocale = this.metadataManager.getLocale();
    }

    public String getSessionLocale() {
        return this.sessionLocale;
    }

    public Boolean ecuResetCondition() {
        return this.ecuPackageTsReset;
    }

    public void resetEcuCondition(Boolean resetEcuCondition) {
        this.ecuPackageTsReset = resetEcuCondition;
    }

    public Boolean linkResetCondition() {
        return this.linkPackageTsReset;
    }

    public void resetLinkCondition(Boolean resetLinkCondition) {
        this.linkPackageTsReset = resetLinkCondition;
    }
}
