/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.update.IUpdateRetryEngine
 *  com.daimler.cebas.certificates.control.update.UpdateDetails
 *  com.daimler.cebas.certificates.control.update.UpdateStatus
 *  com.daimler.cebas.certificates.control.update.UpdateSteps
 *  com.daimler.cebas.certificates.control.update.UpdateType
 */
package com.daimler.cebas.certificates.control.update;

import com.daimler.cebas.certificates.control.update.IUpdateRetryEngine;
import com.daimler.cebas.certificates.control.update.UpdateDetails;
import com.daimler.cebas.certificates.control.update.UpdateStatus;
import com.daimler.cebas.certificates.control.update.UpdateSteps;
import com.daimler.cebas.certificates.control.update.UpdateType;

public interface IUpdateSession
extends IUpdateRetryEngine {
    public void run();

    public void run(String var1);

    public void updateStep(UpdateSteps var1, String var2, UpdateType var3);

    public void updateStep(UpdateSteps var1, String var2, UpdateType var3, String[] var4);

    public void updateStep(UpdateSteps var1, String var2, UpdateType var3, boolean var4);

    public void addStepResult(UpdateSteps var1, Object var2);

    public void setNotRunning();

    public void handleInvalidToken(String var1, UpdateType var2);

    public void resume();

    public boolean isRunning();

    public boolean isPaused();

    public boolean isNotRunning();

    public UpdateDetails getCurrentDetails();

    public UpdateSteps getCurrentStep();

    public UpdateStatus getStatus();

    public boolean isDetailsEmpty();

    public UpdateType getUpdateType();

    public long getLastStopTimestamp();

    public <T> T getStepResult(UpdateSteps var1, Class<T> var2);
}
