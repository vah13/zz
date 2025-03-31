package com.daimler.cebas.certificates.control.config.handlers;

import com.daimler.cebas.certificates.control.vo.ImportResult;
import com.daimler.cebas.certificates.entity.Certificate;
import java.util.List;

public interface IPkiKnownHandler<T extends Certificate> {
  T updateBackendPkiKnown(Certificate paramCertificate);
  
  boolean updateBackendPkiKnownOnIdentical(Certificate paramCertificate);
  
  void updatePkiKnownForAllUnknownBackends(List<ImportResult> paramList);
  
  boolean isPKIKnown(Certificate paramCertificate);
}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\config\handlers\IPkiKnownHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */