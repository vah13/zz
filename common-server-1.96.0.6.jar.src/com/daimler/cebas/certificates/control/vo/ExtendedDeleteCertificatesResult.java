/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExtendedDeleteCertificatesResult
/*    */   extends DeleteCertificatesResult
/*    */ {
/*    */   private String zkNo;
/*    */   private String backendEcuPackageTs;
/*    */   @JsonIgnore
/*    */   private DeleteCertificatesResult deleteCertificatesResult;
/*    */   
/*    */   public ExtendedDeleteCertificatesResult(DeleteCertificatesResult result, String backendZkNo, String backendEcuPackageTs) {
/* 18 */     super(result.getCertificateId(), result.isCertificate(), result
/* 19 */         .getCertificateType(), result.isSuccess(), result.getMessage(), result
/* 20 */         .getSerialNo(), result.getSubjectKeyIdentifier(), result
/* 21 */         .getAuthKeyIdentifier());
/* 22 */     this.zkNo = backendZkNo;
/* 23 */     this.publicKey = result.getPublicKey();
/* 24 */     this.backendEcuPackageTs = backendEcuPackageTs;
/* 25 */     this.deleteCertificatesResult = result;
/*    */   }
/*    */   
/*    */   public String getZkNo() {
/* 29 */     return this.zkNo;
/*    */   }
/*    */   
/*    */   public String getBackendEcuPackageTs() {
/* 33 */     return this.backendEcuPackageTs;
/*    */   }
/*    */   
/*    */   public DeleteCertificatesResult getDeleteCertificatesResult() {
/* 37 */     return this.deleteCertificatesResult;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\ExtendedDeleteCertificatesResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */