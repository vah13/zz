/*    */ package com.daimler.cebas.certificates.control.zkNoSupport;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
/*    */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*    */ import com.daimler.cebas.certificates.control.update.task.UpdateBackendsTask;
/*    */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*    */ import com.daimler.cebas.certificates.integration.vo.BackendIdentifier;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.system.control.exceptions.UnauthorizedOperationException;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.springframework.transaction.annotation.Propagation;
/*    */ import org.springframework.transaction.annotation.Transactional;
/*    */ 
/*    */ 
/*    */ @CEBASControl
/*    */ public class MappingsUpdater<T extends PublicKeyInfrastructureEsi>
/*    */ {
/*    */   private PublicKeyInfrastructureEsi esi;
/*    */   private UpdateBackendsTask<T> updateBackendTask;
/*    */   private ImportCertificatesEngine importEngine;
/*    */   private Session session;
/*    */   
/*    */   public MappingsUpdater(Session session, UpdateBackendsTask<T> updateBackendTask, ImportCertificatesEngine importEngine, PublicKeyInfrastructureEsi esi) {
/* 27 */     this.esi = esi;
/* 28 */     this.updateBackendTask = updateBackendTask;
/* 29 */     this.importEngine = importEngine;
/* 30 */     this.session = session;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*    */   public boolean updateMappings() {
/* 39 */     if (this.session.isDefaultUser()) {
/* 40 */       throw new UnauthorizedOperationException("Only registered users can update mappings");
/*    */     }
/* 42 */     if (this.session.getToken() == null) {
/* 43 */       throw new UnauthorizedOperationException("Authentication with backend is required");
/*    */     }
/*    */     
/* 46 */     List<BackendIdentifier> backendIdentifiers = this.esi.getBackendIdentifiersWithoutSession();
/* 47 */     Set<String> base64Certificates = this.esi.getCertificatesChain(backendIdentifiers);
/* 48 */     this.importEngine.importCertificatesFromBase64NewTransaction(new ArrayList<>(base64Certificates), true, false);
/* 49 */     this.updateBackendTask.updateBackends(UpdateType.ZK_NO_MAPPING, backendIdentifiers);
/* 50 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\zkNoSupport\MappingsUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */