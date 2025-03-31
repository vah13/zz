/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import com.daimler.cebas.certificates.entity.CertificateType;
/*    */ 
/*    */ public enum RootOrBackend
/*    */ {
/*  7 */   ROOT(CertificateType.ROOT_CA_CERTIFICATE), BACKEND(CertificateType.BACKEND_CA_CERTIFICATE);
/*    */   
/*    */   private CertificateType type;
/*    */   
/*    */   RootOrBackend(CertificateType type) {
/* 12 */     this.type = type;
/*    */   }
/*    */   
/*    */   public static CertificateType[] toType(RootOrBackend o) {
/* 16 */     if (o != null) {
/* 17 */       return new CertificateType[] { o.type };
/*    */     }
/* 19 */     return new CertificateType[] { ROOT.type, BACKEND.type };
/*    */   }
/*    */   
/*    */   public static RootOrBackend fromType(CertificateType o) {
/* 23 */     if (o == ROOT.type) {
/* 24 */       return ROOT;
/*    */     }
/* 26 */     return BACKEND;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\RootOrBackend.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */