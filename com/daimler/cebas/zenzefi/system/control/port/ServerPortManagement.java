/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.system.control.port;
/*     */ 
/*     */ import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.prefs.BackingStoreException;
/*     */ import java.util.prefs.Preferences;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASControl
/*     */ public class ServerPortManagement
/*     */ {
/*  20 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.system.control.port.ServerPortManagement.class
/*  21 */     .getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  27 */   private static final Logger LOG = Logger.getLogger(CertificateParser.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZENZEFI_ADDRESS_NAME = "/com/daimler/zenzefi";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZENZEFI_PORT_ID = "port";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String PREFERENCES_FIRST_NODE = "com";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Value("${addPortToPrefs}")
/*     */   private String addPortToPrefs;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Value("${server.port}")
/*     */   private String serverPortSystemValue;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   protected Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   protected MetadataManager i18n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setZenZefiPort() {
/*  73 */     String METHOD_NAME = "setZenZefiPort";
/*  74 */     this.logger.entering(CLASS_NAME, "setZenZefiPort");
/*  75 */     if (Boolean.valueOf(this.addPortToPrefs).booleanValue()) {
/*  76 */       saveServerPortInPreferences();
/*     */     } else {
/*  78 */       logInfoWithMessageId("000314", "serverConfiguredNotToWritePort");
/*     */     } 
/*     */     
/*  81 */     this.logger.exiting(CLASS_NAME, "setZenZefiPort");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeZenZefiPort() {
/*  88 */     String METHOD_NAME = "removeZenZefiPort";
/*  89 */     this.logger.entering(CLASS_NAME, "removeZenZefiPort");
/*  90 */     if (Boolean.valueOf(this.addPortToPrefs).booleanValue()) {
/*  91 */       Preferences.userRoot().node("/com/daimler/zenzefi")
/*  92 */         .remove("port");
/*  93 */       Preferences.systemRoot().node("/com/daimler/zenzefi")
/*  94 */         .remove("port");
/*  95 */       removePortFromUserRoot();
/*  96 */       removePortFromSystemRoot();
/*     */     } else {
/*  98 */       logInfoWithMessageId("000315", "serverConfiguredNotToDeletePort");
/*     */     } 
/*     */     
/* 101 */     this.logger.exiting(CLASS_NAME, "removeZenZefiPort");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removePortFromSystemRoot() {
/*     */     try {
/* 109 */       Preferences.systemRoot().node("com").removeNode();
/* 110 */     } catch (BackingStoreException e) {
/* 111 */       logWarningWithErrorMessage("000146X", "deletePortFromSystemPreferences", e
/*     */           
/* 113 */           .getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removePortFromUserRoot() {
/*     */     try {
/* 122 */       Preferences.userRoot().node("com").removeNode();
/* 123 */     } catch (BackingStoreException e) {
/* 124 */       logWarningWithErrorMessage("000145X", "deletePortFromUserPreferences", e
/*     */           
/* 126 */           .getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void saveServerPortInPreferences() {
/* 137 */     Preferences userPrefs = Preferences.userRoot().node("/com/daimler/zenzefi");
/* 138 */     userPrefs.putInt("port", 
/* 139 */         Integer.parseInt(this.serverPortSystemValue));
/*     */     
/* 141 */     Preferences systemPrefs = Preferences.systemRoot().node("/com/daimler/zenzefi");
/* 142 */     systemPrefs.putInt("port", 
/* 143 */         Integer.parseInt(this.serverPortSystemValue));
/* 144 */     flushUserPreferences(userPrefs);
/* 145 */     flushSystemPreferences(systemPrefs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void flushSystemPreferences(Preferences systemPrefs) {
/*     */     try {
/* 155 */       if (systemPrefs.nodeExists("/com/daimler/zenzefi")) {
/* 156 */         systemPrefs.flush();
/* 157 */         logInfoWithMessageId("000013", "portSetInSystemPreferences");
/*     */       }
/*     */     
/* 160 */     } catch (BackingStoreException e) {
/* 161 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 162 */       logInfoWithMessageId("000070", "portCouldNotBeSetInSystemPreferencesNodeNotAvailable");
/*     */     }
/* 164 */     catch (Exception e) {
/* 165 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 166 */       logInfoWithMessageId("000071", "portCouldNotBeSetInSystemPreferences");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void flushUserPreferences(Preferences userPrefs) {
/*     */     try {
/* 178 */       userPrefs.flush();
/* 179 */       logInfoWithMessageId("000012", "portSetInUserPreferences");
/*     */     }
/* 181 */     catch (BackingStoreException e) {
/* 182 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 183 */       logWarningWithErrorMessage("000012X", "portCouldNotBeSetInUserPreferences", e
/*     */           
/* 185 */           .getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void logInfoWithMessageId(String logId, String messageId) {
/* 196 */     this.logger.log(Level.INFO, logId, this.i18n.getEnglishMessage(messageId), CLASS_NAME);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void logWarningWithErrorMessage(String logId, String messageId, String errorMessage) {
/* 209 */     this.logger.log(Level.WARNING, logId, this.i18n.getEnglishMessage(messageId, new String[] { errorMessage }), CLASS_NAME);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\system\control\port\ServerPortManagement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */