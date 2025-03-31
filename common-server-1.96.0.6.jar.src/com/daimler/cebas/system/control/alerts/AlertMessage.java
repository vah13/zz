/*    */ package com.daimler.cebas.system.control.alerts;
/*    */ 
/*    */ import com.daimler.cebas.system.control.websocket.WebsocketAbstractController;
/*    */ import java.util.ArrayDeque;
/*    */ import java.util.HashSet;
/*    */ import java.util.Queue;
/*    */ import java.util.Set;
/*    */ import org.springframework.util.CollectionUtils;
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
/*    */ public class AlertMessage
/*    */ {
/*    */   private static Queue<String> MESSAGE_IDS;
/*    */   private static Set<String> DELETED_MESSAGE_IDS;
/*    */   
/*    */   public static Queue<String> getMessageIds() {
/* 26 */     if (null == MESSAGE_IDS) {
/* 27 */       MESSAGE_IDS = getArrayDeque();
/*    */     }
/* 29 */     return MESSAGE_IDS;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void addMessageId(String messageId) {
/* 39 */     if (null == MESSAGE_IDS) {
/* 40 */       MESSAGE_IDS = getArrayDeque();
/*    */     }
/*    */     
/* 43 */     boolean added = MESSAGE_IDS.add(messageId);
/*    */     
/* 45 */     if (added) {
/* 46 */       sendMessageIdsViaWebSocket();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void removeFirstMessageId() {
/* 54 */     if (!CollectionUtils.isEmpty(MESSAGE_IDS)) {
/* 55 */       String deletedMessageId = MESSAGE_IDS.remove();
/* 56 */       DELETED_MESSAGE_IDS.add(deletedMessageId);
/* 57 */       sendMessageIdsViaWebSocket();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void sendMessageIdsViaWebSocket() {
/* 65 */     WebsocketAbstractController webSocketInstance = WebsocketAbstractController.getInstance();
/* 66 */     if (null != webSocketInstance) {
/* 67 */       webSocketInstance.updateAlertMessages();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Set<String> getDeletedMessageIds() {
/* 77 */     if (null == DELETED_MESSAGE_IDS) {
/* 78 */       DELETED_MESSAGE_IDS = new HashSet<>();
/*    */     }
/* 80 */     return DELETED_MESSAGE_IDS;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Queue<String> getArrayDeque() {
/* 89 */     return new ArrayDeque<String>()
/*    */       {
/*    */         public boolean add(String value) {
/* 92 */           return (!contains(value) && !AlertMessage.getDeletedMessageIds().contains(value) && super.add(value));
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\alerts\AlertMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */