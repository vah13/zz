/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.AbstractRepository
 *  com.daimler.cebas.common.control.annotations.CEBASRepository
 *  com.daimler.cebas.system.entity.PerformanceAuditEntry
 *  javax.persistence.criteria.CriteriaBuilder
 *  javax.persistence.criteria.CriteriaDelete
 *  javax.persistence.criteria.Expression
 *  javax.persistence.criteria.Root
 */
package com.daimler.cebas.system.control.performance;

import com.daimler.cebas.common.control.AbstractRepository;
import com.daimler.cebas.common.control.annotations.CEBASRepository;
import com.daimler.cebas.system.entity.PerformanceAuditEntry;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

@CEBASRepository
public class PerformanceAuditRepository
extends AbstractRepository {
    public List<PerformanceAuditEntry> getPerformanceEntries() {
        return this.findAll(PerformanceAuditEntry.class);
    }

    public void deleteEntriesOlderThanDate(Date maxDate) {
        CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        CriteriaDelete deleteStatement = criteriaBuilder.createCriteriaDelete(PerformanceAuditEntry.class);
        Root root = deleteStatement.from(PerformanceAuditEntry.class);
        deleteStatement.where((Expression)criteriaBuilder.lessThan((Expression)root.get("createTimestamp"), (Comparable)maxDate));
        this.em.createQuery(deleteStatement).executeUpdate();
    }
}
