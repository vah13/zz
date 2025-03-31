package com.daimler.cebas.certificates.control.update;

public interface IUpdateSession extends IUpdateRetryEngine {
  void run();
  
  void run(String paramString);
  
  void updateStep(UpdateSteps paramUpdateSteps, String paramString, UpdateType paramUpdateType);
  
  void updateStep(UpdateSteps paramUpdateSteps, String paramString, UpdateType paramUpdateType, String[] paramArrayOfString);
  
  void updateStep(UpdateSteps paramUpdateSteps, String paramString, UpdateType paramUpdateType, boolean paramBoolean);
  
  void addStepResult(UpdateSteps paramUpdateSteps, Object paramObject);
  
  void setNotRunning();
  
  void handleInvalidToken(String paramString, UpdateType paramUpdateType);
  
  void resume();
  
  boolean isRunning();
  
  boolean isPaused();
  
  boolean isNotRunning();
  
  UpdateDetails getCurrentDetails();
  
  UpdateSteps getCurrentStep();
  
  UpdateStatus getStatus();
  
  boolean isDetailsEmpty();
  
  UpdateType getUpdateType();
  
  long getLastStopTimestamp();
  
  <T> T getStepResult(UpdateSteps paramUpdateSteps, Class<T> paramClass);
}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\contro\\update\IUpdateSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */