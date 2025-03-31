package com.daimler.cebas.certificates.control.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ISecOCIsInput {
  String getEcuCertificate();
  
  String getBackendCertSubjKeyId();
  
  String getDiagCertSerialNumber();
  
  String getTargetECU();
  
  String getTargetVIN();
  
  @JsonIgnore
  boolean isInvalid();
}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\ISecOCIsInput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */