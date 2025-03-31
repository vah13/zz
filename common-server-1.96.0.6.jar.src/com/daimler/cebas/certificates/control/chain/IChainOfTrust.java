package com.daimler.cebas.certificates.control.chain;

import com.daimler.cebas.certificates.control.validation.ValidationError;
import com.daimler.cebas.certificates.entity.Certificate;
import java.util.List;

@FunctionalInterface
public interface IChainOfTrust {
  void check(List<Certificate> paramList, CertificatePrivateKeyHolder paramCertificatePrivateKeyHolder, List<ValidationError> paramList1, boolean paramBoolean1, boolean paramBoolean2);
}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\chain\IChainOfTrust.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */