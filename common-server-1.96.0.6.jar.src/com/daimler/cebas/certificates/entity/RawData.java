package com.daimler.cebas.certificates.entity;

import java.io.Serializable;
import java.security.cert.X509Certificate;
import org.bouncycastle.cert.X509AttributeCertificateHolder;

public interface RawData extends Serializable {
  X509AttributeCertificateHolder getAttributesCertificateHolder();
  
  X509Certificate getCert();
  
  Object getExisting();
  
  boolean isCertificate();
  
  byte[] getOriginalBytes();
}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\entity\RawData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */