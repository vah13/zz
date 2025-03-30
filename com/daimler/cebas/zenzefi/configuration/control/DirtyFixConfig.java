/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.configuration.control;
/*    */ 
/*    */ import com.fasterxml.classmate.TypeResolver;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Profile;
/*    */ import springfox.documentation.spi.service.OperationBuilderPlugin;
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
/*    */ @Configuration
/*    */ @Profile({"!AFTERSALES"})
/*    */ public class DirtyFixConfig
/*    */ {
/*    */   @Bean
/*    */   public OperationBuilderPlugin operationBuilderPluginForCorrectingActuatorEndpoints(TypeResolver typeResolver) {
/* 74 */     return (OperationBuilderPlugin)new OperationBuilderPluginForCorrectingActuatorEndpoints(typeResolver);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\configuration\control\DirtyFixConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */