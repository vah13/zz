/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASService
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.system.control.performance.PerformanceAuditRepository
 *  com.daimler.cebas.system.control.vo.PerformanceAuditSummary
 *  com.daimler.cebas.system.entity.PerformanceAuditEntry
 *  com.daimler.cebas.users.control.Session
 *  org.springframework.beans.factory.annotation.Autowired
 */
package com.daimler.cebas.system.control;

import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASService;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.system.control.performance.PerformanceAuditRepository;
import com.daimler.cebas.system.control.vo.PerformanceAuditSummary;
import com.daimler.cebas.system.entity.PerformanceAuditEntry;
import com.daimler.cebas.users.control.Session;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@CEBASService
public class SystemService {
    private static final String CLASS_NAME = SystemService.class.getSimpleName();
    protected Logger logger;
    protected MetadataManager i18n;
    protected Session session;
    private PerformanceAuditRepository performanceAuditRepository;

    @Autowired
    public SystemService(Logger logger, MetadataManager i18n, Session session, PerformanceAuditRepository performanceAuditRepository) {
        this.logger = logger;
        this.i18n = i18n;
        this.session = session;
        this.performanceAuditRepository = performanceAuditRepository;
    }

    public List<PerformanceAuditSummary> getPerformanceAuditEntrySummaries() {
        String METHOD_NAME = "getDatabasePerformanceEntries";
        this.logger.entering(CLASS_NAME, "getDatabasePerformanceEntries");
        this.logger.exiting(CLASS_NAME, "getDatabasePerformanceEntries");
        this.logger.log(Level.INFO, "000530", "Loading existing performance audit entries.", CLASS_NAME);
        return this.getDatabasePerformanceEntries().stream().map(p -> new PerformanceAuditSummary(p)).collect(Collectors.toList());
    }

    private List<PerformanceAuditEntry> getDatabasePerformanceEntries() {
        String METHOD_NAME = "getDatabasePerformanceEntries";
        this.logger.entering(CLASS_NAME, "getDatabasePerformanceEntries");
        this.logger.exiting(CLASS_NAME, "getDatabasePerformanceEntries");
        return this.performanceAuditRepository.getPerformanceEntries();
    }

    public String composeMessageForCurrentUser() {
        String currentUserName = this.session.isDefaultUser() ? this.i18n.getMessage("defaultUser") : this.session.getCurrentUser().getUserName();
        return currentUserName;
    }
}
