/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.integration;
/*     */ 
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.configuration.control.mdc.MdcDecoratorCompletableFuture;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.entity.UserRole;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.util.ZenZefiPkiAndOidcPropertiesManager;
/*     */ import com.daimler.cebas.zenzefi.system.control.RefreshTokenClient;
/*     */ import com.daimler.cebas.zenzefi.users.entity.UserOrganisation;
/*     */ import com.daimler.cebas.zenzefi.users.entity.ZenZefiUser;
/*     */ import com.daimler.cebas.zenzefi.users.integration.AuthenticationServiceEsi;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Qualifier;
/*     */ import org.springframework.context.annotation.Profile;
/*     */ import org.springframework.security.core.Authentication;
/*     */ import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
/*     */ import org.springframework.security.oauth2.core.oidc.user.OidcUser;
/*     */ import org.springframework.web.client.RestTemplate;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASControl
/*     */ @Profile({"!AFTERSALES"})
/*     */ public class ZenZefiAuthenticationServiceEsi
/*     */   implements AuthenticationServiceEsi
/*     */ {
/*     */   private static final String OIDC_USER_ID = "sub";
/*     */   private static final String OIDC_FIRSTNAME = "given_name";
/*     */   private static final String OIDC_LASTNAME = "family_name";
/*     */   private static final String OIDC_NAME = "family_name";
/*     */   private static final String OIDC_OBJECT_CLASS = "object_class";
/*     */   private static final String OIDC_CLASS_DCX_SUPPLIER_PERSON = "dcxSupplierPerson";
/*     */   private RefreshTokenClient tokenClient;
/*     */   private Session session;
/*     */   private RestTemplate restTemplate;
/*     */   
/*     */   @Autowired
/*     */   public ZenZefiAuthenticationServiceEsi(@Qualifier("noInterceptor") RestTemplate restTemplate, Logger logger, ZenZefiPkiAndOidcPropertiesManager pkiAndOAuthPropertiesManager, Session session, RefreshTokenClient tokenClient) {
/*  70 */     this.session = session;
/*  71 */     this.tokenClient = tokenClient;
/*  72 */     this.restTemplate = restTemplate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZenZefiUser backendLogin(HttpServletRequest request) {
/*  83 */     OAuth2AuthenticationToken userPrincipal = (OAuth2AuthenticationToken)request.getUserPrincipal();
/*     */     
/*  85 */     if (userPrincipal != null && userPrincipal.getPrincipal() instanceof OidcUser) {
/*  86 */       OidcUser oidcUser = (OidcUser)userPrincipal.getPrincipal();
/*  87 */       Map<String, Object> attributes = oidcUser.getAttributes();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  92 */       return createUserFromDetails(attributes);
/*     */     } 
/*  94 */     throw new IllegalStateException("User not found");
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
/*     */   public void revokeToken(Authentication authentication) {
/* 106 */     revokeToken(this.session.getToken());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void revokeToken(String accessToken) {
/* 116 */     MdcDecoratorCompletableFuture.runAsync(() -> this.tokenClient.performRevokeTokenRequest(this.restTemplate))
/* 117 */       .exceptionally(e -> null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ZenZefiUser createUserFromDetails(Map<String, Object> details) {
/* 159 */     ZenZefiUser user = new ZenZefiUser();
/*     */     
/* 161 */     user.setUserName(getUserName(details));
/* 162 */     user.setFirstName(getFirstName(details));
/* 163 */     user.setLastName(getLastName(details));
/* 164 */     user.setRole(UserRole.REGULAR);
/* 165 */     user.setOrganisation(getOrganisation(details));
/* 166 */     return user;
/*     */   }
/*     */ 
/*     */   
/*     */   private String getOrganisation(Map<String, Object> details) {
/* 171 */     Object object = details.get("object_class");
/* 172 */     String organisation = UserOrganisation.NO_INFORMATION.name();
/* 173 */     if (object instanceof List) {
/* 174 */       List<?> list = (List)object;
/* 175 */       for (Object item : list) {
/* 176 */         if ("dcxSupplierPerson".equals(item)) {
/* 177 */           organisation = UserOrganisation.SUPPLIER.name();
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 182 */     return organisation;
/*     */   }
/*     */ 
/*     */   
/*     */   private String getUserName(Map<String, Object> details) {
/* 187 */     String userID = (String)details.get("sub");
/* 188 */     if (userID == null) {
/* 189 */       throw new CEBASException("User id coming from backend was evaluated to null. Login can't be performed");
/*     */     }
/* 191 */     return userID.toUpperCase();
/*     */   }
/*     */   
/*     */   private String getFirstName(Map<String, Object> details) {
/* 195 */     String firstName = (String)details.get("given_name");
/* 196 */     if (StringUtils.isEmpty(firstName) && !StringUtils.isEmpty((String)details.get("family_name"))) {
/* 197 */       firstName = ((String)details.get("family_name")).split(" ")[0];
/*     */     }
/* 199 */     return firstName;
/*     */   }
/*     */   
/*     */   private String getLastName(Map<String, Object> details) {
/* 203 */     String lastName = (String)details.get("family_name");
/* 204 */     if (StringUtils.isEmpty(lastName) && !StringUtils.isEmpty((String)details.get("family_name"))) {
/* 205 */       String[] strings = ((String)details.get("family_name")).split(" ");
/* 206 */       if (strings.length > 1) {
/* 207 */         lastName = strings[1];
/*     */       }
/*     */     } 
/* 210 */     return lastName;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\integration\ZenZefiAuthenticationServiceEsi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */