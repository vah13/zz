/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.system.control;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NodeServer
/*    */ {
/*    */   private static final String ZENZEFI_UI_INDEX_HTML = "/#/zenzefi/ui/certificates";
/*    */   
/*    */   public static String getClientHomepage(HttpServletRequest request) {
/* 17 */     String refererHeader = request.getHeader("referer");
/* 18 */     if (isDevServer(refererHeader))
/*    */     {
/* 20 */       return "http://localhost:3000/#/zenzefi/ui/certificates";
/*    */     }
/* 22 */     return "/#/zenzefi/ui/certificates";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean isDevServer(String refHeader) {
/* 33 */     return (refHeader != null && refHeader.contains("localhost:3000"));
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\system\control\NodeServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */