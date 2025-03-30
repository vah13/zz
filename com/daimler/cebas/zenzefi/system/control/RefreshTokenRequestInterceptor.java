/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.system.control;
/*    */ 
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.zenzefi.system.control.RefreshTokenClient;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.logging.Level;
/*    */ import org.springframework.http.HttpRequest;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.http.client.ClientHttpRequestExecution;
/*    */ import org.springframework.http.client.ClientHttpRequestInterceptor;
/*    */ import org.springframework.http.client.ClientHttpResponse;
/*    */ import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
/*    */ import org.springframework.web.client.RestTemplate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefreshTokenRequestInterceptor
/*    */   implements ClientHttpRequestInterceptor
/*    */ {
/*    */   private Logger logger;
/*    */   private RefreshTokenClient tokenClient;
/*    */   private RestTemplate restTemplate;
/*    */   
/*    */   public RefreshTokenRequestInterceptor(RefreshTokenClient tokenClient, Logger logger, RestTemplate restTemplate) {
/* 29 */     this.tokenClient = tokenClient;
/* 30 */     this.logger = logger;
/* 31 */     this.restTemplate = restTemplate;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
/*    */     ClientHttpResponse response;
/* 38 */     if (this.tokenClient.isTokenExpired()) {
/* 39 */       this.logger.log(Level.INFO, "000668", "The access token is expired. Will try to refresh the token now. uri=" + request
/* 40 */           .getURI(), 
/* 41 */           getClass().getSimpleName());
/* 42 */       response = executeWithRefreshToken(request, body, execution, null);
/*    */     } else {
/* 44 */       response = execution.execute(request, body);
/* 45 */       if (response.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
/* 46 */         this.logger.log(Level.INFO, "000668", "Remote resource responded with UNAUTHORIZED, will try to refresh the token now. uri=" + request
/*    */             
/* 48 */             .getURI(), 
/* 49 */             getClass().getSimpleName());
/* 50 */         response = executeWithRefreshToken(request, body, execution, response);
/*    */       } 
/*    */     } 
/* 53 */     return response;
/*    */   }
/*    */ 
/*    */   
/*    */   private ClientHttpResponse executeWithRefreshToken(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, ClientHttpResponse response) throws IOException {
/* 58 */     OAuth2AuthorizedClient newTokens = this.tokenClient.requestNewTokensFromIdp(this.restTemplate);
/* 59 */     if (newTokens == null) {
/* 60 */       if (response == null) {
/* 61 */         this.logger.log(Level.INFO, "000665", "The refresh token could not be obtained: Will try request now with original data. uri=" + request
/*    */             
/* 63 */             .getURI(), 
/* 64 */             getClass().getSimpleName());
/* 65 */         return execution.execute(request, body);
/*    */       } 
/* 67 */       this.logger.log(Level.INFO, "000666", "The refresh token could not be obtained: will not retry request. uri=" + request
/* 68 */           .getURI(), 
/* 69 */           getClass().getSimpleName());
/* 70 */       return response;
/*    */     } 
/*    */ 
/*    */     
/* 74 */     List<String> authList = new ArrayList<>();
/* 75 */     authList.add("Bearer " + newTokens.getAccessToken().getTokenValue());
/*    */     
/* 77 */     request.getHeaders().replace("Authorization", authList);
/*    */     
/* 79 */     response = execution.execute(request, body);
/* 80 */     if (response.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
/* 81 */       this.logger.log(Level.INFO, "000664", "After the refresh token flow the request is still UNAUTHORIZED, the remote resource does not allow access. No further refresh will be tried. uri=" + request
/*    */           
/* 83 */           .getURI(), 
/* 84 */           getClass().getSimpleName());
/*    */     }
/* 86 */     return response;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\system\control\RefreshTokenRequestInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */