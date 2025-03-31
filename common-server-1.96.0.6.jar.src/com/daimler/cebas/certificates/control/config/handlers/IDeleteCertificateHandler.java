package com.daimler.cebas.certificates.control.config.handlers;

import com.daimler.cebas.certificates.control.vo.DeleteCertificatesInfo;
import com.daimler.cebas.certificates.control.vo.ExtendedDeleteCertificatesResult;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.users.entity.User;
import java.util.List;

public interface IDeleteCertificateHandler {
  void deleteCertificate(List<String> paramList, boolean paramBoolean, User paramUser, List<DeleteCertificatesInfo> paramList1, List<Certificate> paramList2, Certificate paramCertificate);
  
  ExtendedDeleteCertificatesResult createSuccessDeleteCertificateResult(DeleteCertificatesInfo paramDeleteCertificatesInfo);
  
  String createDeleteResultMessage(DeleteCertificatesInfo paramDeleteCertificatesInfo);
  
  String createDeleteCSRResultMessage(DeleteCertificatesInfo paramDeleteCertificatesInfo);
  
  ExtendedDeleteCertificatesResult createFailDeleteCertificateByIdResult(String paramString);
  
  ExtendedDeleteCertificatesResult createFailDeleteCertificateByAuthKeyAndSnResult(String paramString1, String paramString2);
  
  ExtendedDeleteCertificatesResult createSuccessDeleteCSRResult(DeleteCertificatesInfo paramDeleteCertificatesInfo);
}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\config\handlers\IDeleteCertificateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */