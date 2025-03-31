package com.daimler.cebas.certificates.control.config.handlers;

import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
import com.daimler.cebas.certificates.control.validation.ValidationError;
import java.util.List;

@FunctionalInterface
public interface IAddUnderBackendHandler<T extends com.daimler.cebas.certificates.entity.Certificate> {
  boolean addUnderBackend(CertificatePrivateKeyHolder paramCertificatePrivateKeyHolder, List<ValidationError> paramList, T paramT);
}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\config\handlers\IAddUnderBackendHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */