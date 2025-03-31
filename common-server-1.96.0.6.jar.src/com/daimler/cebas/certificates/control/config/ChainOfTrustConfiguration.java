/*    */ package com.daimler.cebas.certificates.control.config;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.SearchEngine;
/*    */ import com.daimler.cebas.certificates.control.chain.AddUnderBackendHandlerDefault;
/*    */ import com.daimler.cebas.certificates.control.chain.UnderBackendChainOfTrust;
/*    */ import com.daimler.cebas.certificates.control.config.handlers.IAddUnderBackendHandler;
/*    */ import com.daimler.cebas.certificates.control.config.handlers.IPkiKnownHandler;
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
/*    */ public interface ChainOfTrustConfiguration
/*    */ {
/*    */   boolean shouldDoExtendedValidation();
/*    */   
/*    */   boolean shouldReplaceECUCertificate();
/*    */   
/*    */   default IAddUnderBackendHandler getAddBackendHandler(UnderBackendChainOfTrust chain, SearchEngine searchEngine) {
/* 33 */     return (IAddUnderBackendHandler)new AddUnderBackendHandlerDefault(chain, searchEngine);
/*    */   }
/*    */   
/*    */   IPkiKnownHandler getPkiKnownHandler();
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\config\ChainOfTrustConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */