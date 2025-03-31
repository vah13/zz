package com.daimler.cebas.certificates.control.config;

import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import java.util.List;

public interface CertificatesImportConfiguration {
  boolean isCertificateImportNotAllowed(Certificate paramCertificate);
  
  String[] certificateImportNotAllowedInvalidFields(CertificateType paramCertificateType, String paramString1, String paramString2);
  
  boolean shouldReplaceDuringImport(CertificateType paramCertificateType);
  
  boolean isImportWithoutSignatureCheck();
  
  void logCertificateImportNotAllowed(Certificate paramCertificate);
  
  List<CertificateType> availableCertificateTypesForImport();
}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\config\CertificatesImportConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */