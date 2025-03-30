/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.security;
/*    */ 
/*    */ import com.daimler.cebas.common.control.CEBASException;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import java.io.IOException;
/*    */ import javax.servlet.FilterChain;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.stereotype.Component;
/*    */ import org.springframework.web.filter.HiddenHttpMethodFilter;
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
/*    */ @Component("hiddenHttpMethodFilter")
/*    */ public class ZenzefiHiddenHttpMethodFilter
/*    */   extends HiddenHttpMethodFilter
/*    */ {
/*    */   @Autowired
/*    */   private Logger zenzefiLogger;
/*    */   
/*    */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/*    */     try {
/* 33 */       super.doFilterInternal(request, response, filterChain);
/* 34 */     } catch (IOException e) {
/* 35 */       this.logger.info(e.getMessage());
/* 36 */       this.zenzefiLogger.logWithExceptionByInfo("000322X", new CEBASException(e.getMessage()));
/* 37 */       if (e.getCause() != null && e.getMessage().contains("Stream is closed")) {
/* 38 */         response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
/*    */       } else {
/* 40 */         throw e;
/*    */       } 
/* 42 */     } catch (IllegalStateException ise) {
/* 43 */       this.logger.warn(ise.getMessage());
/* 44 */       this.zenzefiLogger.logWithException("000322X", ise.getMessage(), new CEBASException(ise.getMessage()));
/* 45 */       if (ise.getCause() != null && ise.getCause() instanceof io.undertow.server.handlers.form.MultiPartParserDefinition.FileTooLargeException) {
/* 46 */         response.setStatus(HttpStatus.REQUEST_ENTITY_TOO_LARGE.value());
/*    */       } else {
/* 48 */         throw ise;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\security\ZenzefiHiddenHttpMethodFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */