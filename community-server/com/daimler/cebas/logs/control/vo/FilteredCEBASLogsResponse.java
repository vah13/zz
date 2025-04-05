/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.logs.entity.CEBASLog
 */
package com.daimler.cebas.logs.control.vo;

import com.daimler.cebas.logs.entity.CEBASLog;
import java.util.List;

public class FilteredCEBASLogsResponse {
    private List<CEBASLog> filteredLogs;
    private int totalSize;

    public FilteredCEBASLogsResponse(List<CEBASLog> filteredLogs, int totalSize) {
        this.filteredLogs = filteredLogs;
        this.totalSize = totalSize;
    }

    public List<CEBASLog> getFilteredLogs() {
        return this.filteredLogs;
    }

    public int getTotalSize() {
        return this.totalSize;
    }

    public void setFilteredLogs(List<CEBASLog> filteredLogs) {
        this.filteredLogs = filteredLogs;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }
}
