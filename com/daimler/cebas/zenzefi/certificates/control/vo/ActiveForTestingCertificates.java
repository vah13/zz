/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.vo;
/*    */ 
/*    */ import com.daimler.cebas.certificates.entity.CertificateType;
/*    */ import java.util.EnumMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ public class ActiveForTestingCertificates
/*    */ {
/* 20 */   private final Map<CertificateType, List<String>> mapping = new EnumMap<>(CertificateType.class);
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
/*    */   public void add(CertificateType type, List<String> value) {
/* 36 */     this.mapping.put(type, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<CertificateType, List<String>> getData() {
/* 44 */     return this.mapping;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\control\vo\ActiveForTestingCertificates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */