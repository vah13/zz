/*    */ package com.daimler.cebas.users.control;
/*    */ 
/*    */ import com.daimler.cebas.common.control.AbstractRepository;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASRepository;
/*    */ import com.daimler.cebas.users.entity.User;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.logging.Logger;
/*    */ import javax.persistence.TypedQuery;
/*    */ import org.springframework.transaction.annotation.Propagation;
/*    */ import org.springframework.transaction.annotation.Transactional;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CEBASRepository
/*    */ public class UserRepository
/*    */   extends AbstractRepository
/*    */ {
/* 22 */   private static final String CLASS_NAME = UserRepository.class.getName();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   private static final Logger LOG = Logger.getLogger(UserRepository.class.getName());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Transactional(propagation = Propagation.REQUIRED)
/*    */   public User findUserById(String userId) {
/* 38 */     String METHOD_NAME = "findUserById";
/* 39 */     LOG.entering(CLASS_NAME, "findUserById");
/* 40 */     LOG.exiting(CLASS_NAME, "findUserById");
/* 41 */     return (User)this.em.find(User.class, userId);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Transactional(propagation = Propagation.REQUIRED)
/*    */   public Optional<User> findUserByName(String userName) {
/* 53 */     String METHOD_NAME = "findUserByName";
/* 54 */     LOG.entering(CLASS_NAME, "findUserByName");
/*    */     
/* 56 */     TypedQuery<User> query = this.em.createNamedQuery("findUserByName", User.class);
/*    */     
/* 58 */     query.setParameter("userName", userName);
/* 59 */     List<User> resultList = query.getResultList();
/* 60 */     User user = null;
/* 61 */     if (!resultList.isEmpty()) {
/* 62 */       user = resultList.get(0);
/*    */     }
/* 64 */     LOG.exiting(CLASS_NAME, "findUserByName");
/* 65 */     return Optional.ofNullable(user);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<User> findUsersWithDeleteExpiredCertsTrue() {
/* 74 */     String METHOD_NAME = "findUsersWithDeleteExpiredCertsTrue";
/* 75 */     LOG.entering(CLASS_NAME, "findUsersWithDeleteExpiredCertsTrue");
/* 76 */     TypedQuery<User> query = this.em.createNamedQuery("findUserByDeleteExpiredCerts", User.class);
/*    */     
/* 78 */     LOG.exiting(CLASS_NAME, "findUsersWithDeleteExpiredCertsTrue");
/* 79 */     return query.getResultList();
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\ceba\\users\control\UserRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */