/*    */ package com.daimler.cebas.certificates.control.factories;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
/*    */ import com.daimler.cebas.certificates.control.vo.ImportResult;
/*    */ import com.daimler.cebas.certificates.entity.Certificate;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.common.control.annotations.FactoryMethodPattern;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
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
/*    */ @FactoryMethodPattern
/*    */ public abstract class AbstractImportResultFactory<T extends Certificate>
/*    */ {
/*    */   protected MetadataManager i18n;
/*    */   
/*    */   @Autowired
/*    */   public AbstractImportResultFactory(MetadataManager i18n) {
/* 30 */     this.i18n = i18n;
/*    */   }
/*    */   
/*    */   public abstract ImportResult getImportResult(CertificatePrivateKeyHolder<T> paramCertificatePrivateKeyHolder, String paramString, boolean paramBoolean);
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\factories\AbstractImportResultFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */