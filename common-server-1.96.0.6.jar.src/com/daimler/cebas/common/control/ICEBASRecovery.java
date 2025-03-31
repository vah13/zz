package com.daimler.cebas.common.control;

public interface ICEBASRecovery {
  void checkForRecovery();
  
  void doBackup();
  
  void doRestore();
  
  void scheduleBackup();
  
  void scheduleCouplingBackup();
  
  boolean isRestoreExecuted();
  
  boolean backupExists();
  
  boolean canAccessFiles();
}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\ICEBASRecovery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */