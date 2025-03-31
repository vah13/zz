package com.daimler.cebas.certificates.control.hooks;

import com.daimler.cebas.certificates.entity.Certificate;
import java.util.Optional;
import java.util.function.Consumer;

public interface ICertificateHooks {
  Optional<Consumer<Certificate>> possibleHook();
  
  void exec(Certificate paramCertificate);
}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\hooks\ICertificateHooks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */