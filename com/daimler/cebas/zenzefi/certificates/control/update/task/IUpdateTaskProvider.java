package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;

import com.daimler.cebas.certificates.control.update.task.AbstractUpdateEcuLinkMarkersTask;
import com.daimler.cebas.certificates.control.update.task.UpdateBackendsTask;
import com.daimler.cebas.zenzefi.certificates.control.update.task.CollectTimeAndSecocisCSRsTask;
import com.daimler.cebas.zenzefi.certificates.control.update.task.CreateCSRsUnderBackendTask;
import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadBackendsTask;
import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadCertificatesUnderBackendTask;
import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadEnhancedCertificatesTask;
import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadLinkCertificatesTask;
import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadNonVsmECUCertificateTask;
import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadPermissionsTask;
import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadTimeAndSecocisTask;
import com.daimler.cebas.zenzefi.certificates.control.update.task.UpdateRenewalPeriodTask;
import com.daimler.cebas.zenzefi.certificates.control.update.task.ZenzefiCreateEnhancedCSRsTask;

public interface IUpdateTaskProvider {
  CreateCSRsUnderBackendTask getCreateCSRsUnderBackendTask();
  
  ZenzefiCreateEnhancedCSRsTask getCreateEnhancedCSRsTask();
  
  DownloadBackendsTask getDownloadBackendsTask();
  
  DownloadCertificatesUnderBackendTask getDownloadCertificatesTask();
  
  DownloadEnhancedCertificatesTask getDownloadEnhancedCertificatesTask();
  
  DownloadPermissionsTask getDownloadPermissionsTask();
  
  CollectTimeAndSecocisCSRsTask getCollectTimeAndSecocisCSR();
  
  DownloadTimeAndSecocisTask getDownloadTimeAndSecocisTask();
  
  UpdateBackendsTask getUpdateBackendTask();
  
  UpdateRenewalPeriodTask getUpdateRenewalPeriodTask();
  
  DownloadLinkCertificatesTask getDownloadLinkCertificatesTask();
  
  DownloadNonVsmECUCertificateTask getDownloadNonVsmEcuCertificateTask();
  
  AbstractUpdateEcuLinkMarkersTask getAbstractUpdateEcuLinkMarkersTask();
}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\IUpdateTaskProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */