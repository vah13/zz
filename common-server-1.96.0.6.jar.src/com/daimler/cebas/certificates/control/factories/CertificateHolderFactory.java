package com.daimler.cebas.certificates.control.factories;

import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
import com.daimler.cebas.users.entity.User;
import com.secunet.provider.pkcs12.AttributeCertificate;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public interface CertificateHolderFactory {
  CertificatePrivateKeyHolder createCertificateHolder(String paramString, AttributeCertificate paramAttributeCertificate, User paramUser);
  
  CertificatePrivateKeyHolder createCertificateHolder(String paramString1, X509Certificate paramX509Certificate, String paramString2, User paramUser);
  
  CertificatePrivateKeyHolder createCertificateHolder(String paramString1, X509Certificate paramX509Certificate, PrivateKey paramPrivateKey, String paramString2, User paramUser);
}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\factories\CertificateHolderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */