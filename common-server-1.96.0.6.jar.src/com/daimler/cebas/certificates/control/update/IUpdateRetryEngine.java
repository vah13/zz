package com.daimler.cebas.certificates.control.update;

public interface IUpdateRetryEngine {
  Integer retry(String paramString);
  
  void resetRetries();
  
  long getNextRetryTimeStamp();
  
  void setDidFailAllRetries(boolean paramBoolean);
  
  Integer getNextRetryTimeInterval();
  
  void setNextRetryTimeInterval();
  
  boolean didFailAllRetries();
  
  String getRetryUrl();
  
  Integer getMaxRetries();
  
  Integer getCurrentRetry();
}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\contro\\update\IUpdateRetryEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */