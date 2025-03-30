/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.vo;
/*    */ 
/*    */ import com.daimler.cebas.zenzefi.certificates.entity.TestingUseCaseType;
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
/*    */ public class CertificatesUseCaseHolder
/*    */ {
/* 20 */   private final Map<TestingUseCaseType, List<String>> map = new EnumMap<>(TestingUseCaseType.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void add(TestingUseCaseType type, List<String> ids) {
/* 29 */     this.map.put(type, ids);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<TestingUseCaseType, List<String>> getData() {
/* 38 */     return this.map;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\control\vo\CertificatesUseCaseHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */