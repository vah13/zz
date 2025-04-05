/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.certificates.control.vo;

import java.util.Date;

public class DateFilter {
    private Date dateStart;
    private Date dateEnd;

    public DateFilter(Date dateStart, Date dateEnd) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public Date getStart() {
        return this.dateStart;
    }

    public Date getEnd() {
        return this.dateEnd;
    }
}
