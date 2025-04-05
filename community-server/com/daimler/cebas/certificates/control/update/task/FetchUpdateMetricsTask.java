/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.update.DefaultUpdateSession
 *  com.daimler.cebas.certificates.control.update.UpdateDetails
 *  com.daimler.cebas.certificates.control.update.UpdateStatus
 *  com.daimler.cebas.certificates.control.vo.UpdateCertificateMetrics
 *  com.daimler.cebas.certificates.control.vo.UpdateCertificatesRetryInfo
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  org.springframework.beans.factory.annotation.Autowired
 */
package com.daimler.cebas.certificates.control.update.task;

import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
import com.daimler.cebas.certificates.control.update.UpdateDetails;
import com.daimler.cebas.certificates.control.update.UpdateStatus;
import com.daimler.cebas.certificates.control.vo.UpdateCertificateMetrics;
import com.daimler.cebas.certificates.control.vo.UpdateCertificatesRetryInfo;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import java.util.ArrayList;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;

@CEBASControl
public class FetchUpdateMetricsTask {
    private static final String EMPTY = "";
    private DefaultUpdateSession updateSession;
    protected MetadataManager i18n;

    @Autowired
    public FetchUpdateMetricsTask(DefaultUpdateSession updateSession, MetadataManager i18n) {
        this.updateSession = updateSession;
        this.i18n = i18n;
    }

    public UpdateCertificateMetrics getUpdateCertificatesMetrics() {
        UpdateCertificateMetrics updateCertificateMetrics;
        boolean isMetricsAvailble;
        UpdateStatus status = this.updateSession.getStatus();
        boolean isRunningOrNotRunningAndDetailsNotEmpty = status == UpdateStatus.RUNNING || status == UpdateStatus.NOT_RUNNING && !this.updateSession.isDetailsEmpty();
        boolean bl = isMetricsAvailble = isRunningOrNotRunningAndDetailsNotEmpty || status == UpdateStatus.PAUSED && !this.updateSession.isDetailsEmpty();
        if (isMetricsAvailble) {
            UpdateDetails currentDetails = this.updateSession.getCurrentDetails();
            if (currentDetails == null) {
                UpdateCertificatesRetryInfo composeUpdateCertificatesRetryInfo = this.composeUpdateCertificatesRetryInfo();
                return new UpdateCertificateMetrics(composeUpdateCertificatesRetryInfo.getCurrentRetry() != 0, EMPTY, EMPTY, EMPTY, composeUpdateCertificatesRetryInfo, this.updateSession.didFailAllRetries(), this.updateSession.isRunning());
            }
            updateCertificateMetrics = new UpdateCertificateMetrics(true, currentDetails.isError(), this.i18n.getMessage(this.updateSession.getStatus().getText()), this.i18n.getMessage(currentDetails.getStep().getText()), this.i18n.getMessage(currentDetails.getMessage(), this.getMessageArguments(currentDetails.getMessageArguments())), this.composeUpdateCertificatesRetryInfo(), this.updateSession.didFailAllRetries(), this.updateSession.isRunning());
        } else {
            updateCertificateMetrics = new UpdateCertificateMetrics(false, EMPTY, EMPTY, EMPTY, null, this.updateSession.didFailAllRetries(), this.updateSession.isRunning());
        }
        return updateCertificateMetrics;
    }

    private UpdateCertificatesRetryInfo composeUpdateCertificatesRetryInfo() {
        return new UpdateCertificatesRetryInfo(this.updateSession.getRetryUrl(), this.updateSession.getMaxRetries(), this.updateSession.getCurrentRetry(), this.updateSession.getNextRetryTimeInterval(), this.updateSession.getNextRetryTimeStamp());
    }

    private String[] getMessageArguments(String[] currentArgs) {
        ArrayList<String> args = new ArrayList<String>();
        args.add(0, this.updateSession.getUpdateType().name());
        if (currentArgs == null) return args.toArray(new String[args.size()]);
        Collections.addAll(args, currentArgs);
        return args.toArray(new String[args.size()]);
    }
}
