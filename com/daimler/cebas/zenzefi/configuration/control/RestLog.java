/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.configuration.control;
/*    */ 
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import java.io.IOException;
/*    */ import java.util.UUID;
/*    */ import javax.servlet.Filter;
/*    */ import javax.servlet.FilterChain;
/*    */ import javax.servlet.FilterConfig;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.ServletRequest;
/*    */ import javax.servlet.ServletResponse;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.slf4j.MDC;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class RestLog
/*    */   implements Filter
/*    */ {
/*    */   private static final String MDC_CORRELATION = "correlationId";
/*    */   private MetadataManager requestMetadata;
/*    */   private Session session;
/*    */   
/*    */   @Autowired
/*    */   public RestLog(MetadataManager requestMetadata, Session session) {
/* 33 */     this.requestMetadata = requestMetadata;
/* 34 */     this.session = session;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void init(FilterConfig filterConfig) throws ServletException {}
/*    */ 
/*    */   
/*    */   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
/*    */     try {
/* 44 */       if (servletRequest instanceof HttpServletRequest) {
/* 45 */         HttpServletRequest request = (HttpServletRequest)servletRequest;
/* 46 */         handleCorrelationId(request);
/* 47 */         this.session.initMdc();
/*    */       } 
/* 49 */       chain.doFilter(servletRequest, servletResponse);
/*    */     } finally {
/* 51 */       MDC.remove("correlationId");
/* 52 */       this.session.removeUserFromMDC();
/*    */     } 
/*    */   }
/*    */   
/*    */   private void handleCorrelationId(HttpServletRequest r) {
/* 57 */     String correlationId = r.getHeader("X-Correlation-ID");
/* 58 */     if (StringUtils.isEmpty(correlationId)) {
/* 59 */       correlationId = UUID.randomUUID().toString();
/*    */     }
/* 61 */     MDC.put("correlationId", correlationId);
/* 62 */     this.requestMetadata.setCorrelationId(correlationId);
/*    */   }
/*    */   
/*    */   public void destroy() {}
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\configuration\control\RestLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */