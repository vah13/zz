/*    */ package com.daimler.cebas.certificates.control.exceptions;
/*    */ 
/*    */ import com.daimler.cebas.common.control.CEBASException;
/*    */ 
/*    */ public class EmptyPermissionsException
/*    */   extends CEBASException
/*    */ {
/*    */   private static final long serialVersionUID = 5754067865813931069L;
/*    */   
/*    */   public EmptyPermissionsException() {
/* 11 */     super("EmptyPermissionsException");
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 16 */     return EmptyPermissionsException.class.getSimpleName() + ": No permissions retrieved from PKI.";
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\exceptions\EmptyPermissionsException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */