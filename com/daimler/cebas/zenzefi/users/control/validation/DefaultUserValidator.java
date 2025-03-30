/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control.validation;
/*     */ 
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.crypto.UserCryptoEngine;
/*     */ import com.daimler.cebas.users.control.exceptions.UserValidationException;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import com.daimler.cebas.zenzefi.users.control.UserService;
/*     */ import com.daimler.cebas.zenzefi.users.control.validation.IUserValidator;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.logging.Level;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.annotation.Profile;
/*     */ import org.springframework.web.context.annotation.RequestScope;
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
/*     */ 
/*     */ @CEBASControl
/*     */ @RequestScope
/*     */ @Profile({"!AFTERSALES"})
/*     */ public class DefaultUserValidator
/*     */   implements IUserValidator
/*     */ {
/*  34 */   private static final String CLASS_NAME = UserService.class.getName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MetadataManager i18n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public DefaultUserValidator(Logger logger, MetadataManager i18n) {
/*  56 */     this.logger = logger;
/*  57 */     this.i18n = i18n;
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
/*     */   public void validateUser(User user) {
/*  70 */     String METHOD_NAME = "validateUser";
/*  71 */     this.logger.entering(CLASS_NAME, "validateUser");
/*     */     
/*  73 */     int isValidationOK = 0;
/*  74 */     if (!validateFirstName(user)) {
/*  75 */       isValidationOK++;
/*     */     }
/*  77 */     if (!validateLastName(user)) {
/*  78 */       isValidationOK += 2;
/*     */     }
/*  80 */     if (!validateUserName(user)) {
/*  81 */       isValidationOK += 4;
/*     */     }
/*  83 */     if (!validateOrganisation(user)) {
/*  84 */       isValidationOK += 8;
/*     */     }
/*  86 */     if (!validatePassword(user)) {
/*  87 */       isValidationOK += 16;
/*     */     }
/*     */     
/*  90 */     if (isValidationOK != 0) {
/*  91 */       String message = this.i18n.getMessage("userInvalidForRegistration", new String[] { "ErrorCode=" + isValidationOK });
/*     */       
/*  93 */       UserValidationException userValidationException = new UserValidationException(user, message, "userInvalidForRegistration");
/*     */       
/*  95 */       this.logger.log(Level.WARNING, "000662X", message, userValidationException
/*  96 */           .getClass().getSimpleName());
/*  97 */       throw userValidationException;
/*     */     } 
/*  99 */     this.logger.exiting(CLASS_NAME, "validateUser");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validateFirstName(User user) {
/* 110 */     if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
/* 111 */       user.setFirstName("Invalid");
/* 112 */       return false;
/*     */     } 
/*     */     
/* 115 */     if (user.getFirstName().length() < 1 || user
/* 116 */       .getFirstName().length() > 100) {
/* 117 */       user.setFirstName("Invalid");
/* 118 */       return false;
/*     */     } 
/* 120 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validateLastName(User user) {
/* 131 */     if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
/* 132 */       user.setLastName("Invalid");
/* 133 */       return false;
/*     */     } 
/*     */     
/* 136 */     if (user.getLastName().length() < 1 || user
/* 137 */       .getLastName().length() > 100) {
/* 138 */       user.setLastName("Invalid");
/* 139 */       return false;
/*     */     } 
/* 141 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validateUserName(User user) {
/* 152 */     if (StringUtils.isEmpty(user.getUserName()) || 
/* 153 */       !user.getUserName().matches("[a-zA-Z0-9]+")) {
/* 154 */       user.setUserName("Invalid");
/* 155 */       return false;
/*     */     } 
/* 157 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validateOrganisation(User user) {
/* 168 */     if (user.getOrganisation() == null || user.getOrganisation().trim().isEmpty()) {
/* 169 */       user.setOrganisation("Invalid");
/* 170 */       return false;
/*     */     } 
/*     */     
/* 173 */     if (user.getOrganisation().length() < 1 || user
/* 174 */       .getOrganisation().length() > 100) {
/* 175 */       user.setOrganisation("Invalid");
/* 176 */       return false;
/*     */     } 
/* 178 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validatePassword(User user) {
/* 189 */     if (user.getUserPassword() == null || user.getUserPassword().trim().isEmpty()) {
/* 190 */       user.setUserPassword("Invalid");
/* 191 */       return false;
/*     */     } 
/*     */     
/* 194 */     String decodedPassword = UserCryptoEngine.getBase64Decoded(user.getUserPassword().getBytes(StandardCharsets.UTF_8));
/*     */     
/* 196 */     if (decodedPassword.length() < 10 || decodedPassword
/* 197 */       .length() > 100) {
/* 198 */       user.setUserPassword("Invalid");
/* 199 */       return false;
/*     */     } 
/*     */     
/* 202 */     if (!decodedPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!#\\$%&\\*@\\-\"'()+,/;:=?_>\\.])[A-Za-z\\_\\d\\W][^\\s]{9,}")) {
/* 203 */       user.setUserPassword("Invalid");
/* 204 */       return false;
/*     */     } 
/* 206 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\validation\DefaultUserValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */