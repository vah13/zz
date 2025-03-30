/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.system.control.websocket;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.update.UpdateDetails;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*     */ import com.daimler.cebas.system.control.alerts.AlertMessage;
/*     */ import com.daimler.cebas.system.control.websocket.WebsocketAbstractController;
/*     */ import com.daimler.cebas.users.control.vo.UserDetailsWithSession;
/*     */ import com.daimler.cebas.zenzefi.users.control.UserService;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.messaging.simp.SimpMessagingTemplate;
/*     */ import org.springframework.stereotype.Controller;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Controller
/*     */ public class WebsocketController
/*     */   extends WebsocketAbstractController
/*     */ {
/*     */   public static final String TOPIC_LOGIN = "/topic/login";
/*     */   public static final String TOPIC_LOGOUT = "/topic/logout";
/*     */   public static final String TOPIC_TRIGGER_UPDATE = "/topic/triggerUpdate";
/*     */   public static final String TOPIC_START_CERTS_UPDATE = "/topic/certsUpdateStarted";
/*     */   public static final String TOPIC_STOP_CERTS_UPDATE = "/topic/certsUpdateStopped";
/*     */   public static final String TOPIC_NEW_CERTS_UPDATE_STEP = "/topic/newCertsUpdateStep";
/*     */   public static final String TOPIC_CERTS_UPDATE_RETRY = "/topic/certsUpdateRetry";
/*     */   public static final String TOPIC_SESSION_TIMER_RESET = "/topic/sessionTimerReset";
/*     */   public static final String TOPIC_UPDATE_ALERT_MESSAGES = "/topic/updateAlertMessages";
/*     */   @Autowired
/*     */   private SimpMessagingTemplate template;
/*     */   @Autowired
/*     */   private UserService service;
/*     */   
/*     */   public void login(Map<String, String> userData) {
/*  37 */     this.template.convertAndSend("/topic/login", userData);
/*     */   }
/*     */   
/*     */   public void logout(UserDetailsWithSession currentUserDetails) {
/*  41 */     Map<String, Object> payload = new HashMap<>();
/*  42 */     addUserDataIntoPayload(currentUserDetails, payload);
/*  43 */     this.template.convertAndSend("/topic/logout", payload);
/*     */   }
/*     */   
/*     */   public void triggerClientUpdate() {
/*  47 */     this.template.convertAndSend("/topic/triggerUpdate", "Client should update data");
/*     */   }
/*     */   
/*     */   public void startCertificatesUpdate() {
/*  51 */     this.template.convertAndSend("/topic/certsUpdateStarted", "Certificates update process started");
/*     */   }
/*     */   
/*     */   public void stopCertificatesUpdate(UpdateDetails details, UpdateType type, boolean didAllRetriesFail) {
/*  55 */     Map<String, Object> payload = new HashMap<>();
/*  56 */     UserDetailsWithSession currentUserDetails = this.service.getCurrentUserDetails();
/*  57 */     payload.put("details", details);
/*  58 */     payload.put("type", type);
/*  59 */     payload.put("didFailAllRetries", Boolean.valueOf(didAllRetriesFail));
/*  60 */     addUserDataIntoPayload(currentUserDetails, payload);
/*  61 */     this.template.convertAndSend("/topic/certsUpdateStopped", payload);
/*     */   }
/*     */   
/*     */   public void newCertificatesUpdateStep(UpdateDetails details, UpdateType type) {
/*  65 */     Map<String, Object> payload = new HashMap<>();
/*  66 */     payload.put("details", details);
/*  67 */     payload.put("type", type);
/*  68 */     this.template.convertAndSend("/topic/newCertsUpdateStep", payload);
/*     */   }
/*     */   
/*     */   public void timerResetEvent() {
/*  72 */     UserDetailsWithSession currentUserDetails = this.service.getCurrentUserDetails();
/*  73 */     if (currentUserDetails.isDefaultUser()) {
/*     */       return;
/*     */     }
/*  76 */     Map<String, Object> payload = new HashMap<>();
/*  77 */     addUserDataIntoPayload(currentUserDetails, payload);
/*  78 */     this.template.convertAndSend("/topic/sessionTimerReset", payload);
/*     */   }
/*     */   
/*     */   public void certsUpdateRetry(UpdateDetails details, int currentRetry, int maxRetries, String endpoint, long nextRetryTimestamp) {
/*  82 */     Map<String, Object> payload = new HashMap<>();
/*  83 */     payload.put("details", details);
/*  84 */     payload.put("currentRetry", Integer.valueOf(currentRetry));
/*  85 */     payload.put("maxRetries", Integer.valueOf(maxRetries));
/*  86 */     payload.put("endpoint", endpoint);
/*  87 */     payload.put("nextRetryTimestamp", Long.valueOf(nextRetryTimestamp));
/*  88 */     this.template.convertAndSend("/topic/certsUpdateRetry", payload);
/*     */   }
/*     */   
/*     */   public void addUserDataIntoPayload(UserDetailsWithSession currentUserDetails, Map<String, Object> payload) {
/*  92 */     String displayName = currentUserDetails.getFirstName() + " " + currentUserDetails.getLastName() + "(ID: " + currentUserDetails.getUserName() + ")";
/*  93 */     payload.put("displayName", displayName);
/*  94 */     payload.put("checkLocalPw", Boolean.valueOf(currentUserDetails.isCheckLocalPassword()));
/*  95 */     payload.put("isDefaultUser", Boolean.valueOf(currentUserDetails.isDefaultUser()));
/*  96 */     payload.put("isNewUser", Boolean.valueOf(currentUserDetails.isNewUser()));
/*  97 */     payload.put("isAuthenticatedAgainstBackend", Boolean.valueOf(currentUserDetails.isAuthenticationAgainstBackend()));
/*  98 */     payload.put("isAuthenticated", Boolean.valueOf(!currentUserDetails.isDefaultUser()));
/*  99 */     payload.put("remainingSessionSeconds", Long.valueOf(currentUserDetails.getRemainingSessionSeconds()));
/* 100 */     payload.put("userName", currentUserDetails.getUserName());
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateAlertMessages() {
/* 105 */     Map<String, Object> payload = new HashMap<>();
/* 106 */     payload.put("ids", AlertMessage.getMessageIds().toArray());
/* 107 */     this.template.convertAndSend("/topic/updateAlertMessages", payload);
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 112 */     instance = this;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\system\control\websocket\WebsocketController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */