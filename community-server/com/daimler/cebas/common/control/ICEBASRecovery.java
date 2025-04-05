/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.common.control;

public interface ICEBASRecovery {
    public void checkForRecovery();

    public void doBackup();

    public void doRestore();

    public void scheduleBackup();

    public void scheduleCouplingBackup();

    public boolean isRestoreExecuted();

    public boolean backupExists();

    public boolean canAccessFiles();
}
