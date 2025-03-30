/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update;
/*    */ 
/*    */ import com.daimler.cebas.certificates.integration.vo.PKICertificateRequest;
/*    */ import com.daimler.cebas.certificates.integration.vo.PKIEnhancedCertificateRequest;
/*    */ import java.util.List;
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
/*    */ public class HolderRequests
/*    */ {
/*    */   private List<PKICertificateRequest> underBackendRequests;
/*    */   private List<PKIEnhancedCertificateRequest> underDiagRequests;
/*    */   
/*    */   public HolderRequests(List<PKICertificateRequest> underBackendRequests, List<PKIEnhancedCertificateRequest> underDiagRequests) {
/* 37 */     this.underBackendRequests = underBackendRequests;
/* 38 */     this.underDiagRequests = underDiagRequests;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<PKICertificateRequest> getUnderBackendRequests() {
/* 47 */     return this.underBackendRequests;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<PKIEnhancedCertificateRequest> getUnderDiagRequests() {
/* 56 */     return this.underDiagRequests;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\HolderRequests.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */