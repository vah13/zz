/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.StoreModificationNotAllowed
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASSession
 *  com.daimler.cebas.logs.control.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 */
package com.daimler.cebas.certificates.control;

import com.daimler.cebas.certificates.control.exceptions.StoreModificationNotAllowed;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASSession;
import com.daimler.cebas.logs.control.Logger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Autowired;

@CEBASSession
public class ImportSession {
    private Logger logger;
    private ReentrantLock lock = new ReentrantLock();
    private MetadataManager i18n;

    @Autowired
    public ImportSession(Logger logger, MetadataManager i18n) {
        this.logger = logger;
        this.i18n = i18n;
    }

    public synchronized void run() {
        if (this.isRunning()) {
            this.logger.log(Level.WARNING, "000367", this.i18n.getEnglishMessage("operationNotAllowedImportIsRunning"), ImportSession.class.getSimpleName());
            throw new StoreModificationNotAllowed(this.i18n.getMessage("operationNotAllowedImportIsRunning"));
        }
        this.logger.log(Level.INFO, "000279", "Import session started", ImportSession.class.getSimpleName());
        this.lock.lock();
    }

    public void setNotRunning() {
        if (this.lock.getHoldCount() == 0) return;
        this.logger.log(Level.INFO, "000279", "Import session stopped", ImportSession.class.getSimpleName());
        this.lock.unlock();
    }

    public boolean isRunning() {
        return this.lock.isLocked();
    }
}
