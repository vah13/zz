/*    */ package com.daimler.cebas.certificates.control.chain.events;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.vo.ImportResult;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Collectors;
/*    */ import org.springframework.context.ApplicationEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DeleteCertsFromKnownBackends
/*    */   extends ApplicationEvent
/*    */ {
/*    */   private transient List<ImportResult> rootAndBackendsResult;
/*    */   
/*    */   public DeleteCertsFromKnownBackends(List<ImportResult> rootAndBackendsResult, Object source) {
/* 19 */     super(source);
/* 20 */     this.rootAndBackendsResult = rootAndBackendsResult;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<ImportResult> getBackends() {
/* 25 */     return (List<ImportResult>)this.rootAndBackendsResult.stream().filter(result -> Objects.nonNull(result.getAuthorityKeyIdentifier())).collect(Collectors.toList());
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\chain\events\DeleteCertsFromKnownBackends.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */