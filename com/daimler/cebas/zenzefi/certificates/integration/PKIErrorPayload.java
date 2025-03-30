/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.integration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PKIErrorPayload
/*    */ {
/*    */   private String message;
/*    */   private String statusCode;
/*    */   private String code;
/*    */   
/*    */   public PKIErrorPayload() {}
/*    */   
/*    */   public PKIErrorPayload(String message, String statusCode, String code) {
/* 40 */     this.message = message;
/* 41 */     this.statusCode = statusCode;
/* 42 */     this.code = code;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 50 */     return this.message;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 58 */     return this.code;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getStatusCode() {
/* 66 */     return this.statusCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\integration\PKIErrorPayload.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */