/*     */ package com.daimler.cebas.common.control;
/*     */ 
/*     */ import com.daimler.cebas.common.control.annotations.CEBASRepository;
/*     */ import com.daimler.cebas.common.entity.AbstractEntity;
/*     */ import com.daimler.cebas.common.entity.EmptyPredicate;
/*     */ import com.daimler.cebas.common.entity.InPredicate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ import javax.persistence.EntityManager;
/*     */ import javax.persistence.PersistenceContext;
/*     */ import javax.persistence.Query;
/*     */ import javax.persistence.TypedQuery;
/*     */ import javax.persistence.criteria.CriteriaBuilder;
/*     */ import javax.persistence.criteria.CriteriaDelete;
/*     */ import javax.persistence.criteria.CriteriaQuery;
/*     */ import javax.persistence.criteria.Expression;
/*     */ import javax.persistence.criteria.Order;
/*     */ import javax.persistence.criteria.Path;
/*     */ import javax.persistence.criteria.Predicate;
/*     */ import javax.persistence.criteria.Root;
/*     */ import javax.persistence.criteria.Selection;
/*     */ import org.springframework.data.domain.Pageable;
/*     */ import org.springframework.transaction.annotation.Propagation;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ 
/*     */ 
/*     */ @CEBASRepository
/*     */ public abstract class AbstractRepository
/*     */ {
/*  34 */   private static final String CLASS_NAME = AbstractRepository.class.getName();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  39 */   private static final Logger LOG = Logger.getLogger(AbstractRepository.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PersistenceContext
/*     */   protected EntityManager em;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends AbstractEntity> List<T> findAll(Class<T> type) {
/*  55 */     String METHOD_NAME = "findAll";
/*  56 */     LOG.entering(CLASS_NAME, "findAll");
/*  57 */     CriteriaBuilder builder = this.em.getCriteriaBuilder();
/*  58 */     CriteriaQuery<T> createQuery = builder.createQuery(type);
/*  59 */     Root<T> root = createQuery.from(type);
/*  60 */     CriteriaQuery<T> selectAll = createQuery.select((Selection)root);
/*  61 */     TypedQuery<T> query = this.em.createQuery(selectAll);
/*  62 */     LOG.exiting(CLASS_NAME, "findAll");
/*  63 */     return query.getResultList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends AbstractEntity> Optional<T> find(Class<T> type, String id) {
/*  76 */     String METHOD_NAME = "find";
/*  77 */     LOG.entering(CLASS_NAME, "find");
/*  78 */     LOG.exiting(CLASS_NAME, "find");
/*  79 */     return Optional.ofNullable((T)this.em.find(type, id));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends AbstractEntity> List<T> findMultiple(Class<T> type, List<String> idList) {
/*  92 */     String METHOD_NAME = "findMultiple";
/*  93 */     LOG.entering(CLASS_NAME, "findMultiple");
/*  94 */     CriteriaBuilder builder = this.em.getCriteriaBuilder();
/*  95 */     CriteriaQuery<T> criteriaQuery = builder.createQuery(type);
/*  96 */     Root<T> root = criteriaQuery.from(type);
/*  97 */     Path<Object> expresion = root.get("entityId");
/*  98 */     CriteriaQuery<T> select = criteriaQuery.where((Expression)expresion.in(idList));
/*  99 */     TypedQuery<T> query = this.em.createQuery(select);
/* 100 */     List<T> resultList = query.getResultList();
/* 101 */     LOG.exiting(CLASS_NAME, "findMultiple");
/* 102 */     return resultList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends AbstractEntity> void delete(Class<T> type, String id) {
/* 114 */     String METHOD_NAME = "delete";
/* 115 */     LOG.entering(CLASS_NAME, "delete");
/* 116 */     CriteriaBuilder builder = this.em.getCriteriaBuilder();
/* 117 */     CriteriaDelete<T> createCriteriaDelete = builder.createCriteriaDelete(type);
/* 118 */     Root<T> e = createCriteriaDelete.from(type);
/* 119 */     createCriteriaDelete.where((Expression)builder.equal((Expression)e.get("entityId"), id));
/* 120 */     this.em.createQuery(createCriteriaDelete).executeUpdate();
/* 121 */     LOG.exiting(CLASS_NAME, "delete");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends AbstractEntity> List<T> findWithNamedQuery(String namedQueryName, Map<String, Object> parameters, int resultLimit) {
/* 138 */     String METHOD_NAME = "findWithNamedQuery";
/* 139 */     LOG.entering(CLASS_NAME, "findWithNamedQuery");
/* 140 */     Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
/* 141 */     Query query = this.em.createNamedQuery(namedQueryName);
/* 142 */     if (resultLimit > 0) {
/* 143 */       query.setMaxResults(resultLimit);
/*     */     }
/* 145 */     for (Map.Entry<String, Object> entry : rawParameters) {
/* 146 */       query.setParameter(entry.getKey(), entry.getValue());
/*     */     }
/* 148 */     LOG.exiting(CLASS_NAME, "findWithNamedQuery");
/* 149 */     return query.getResultList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.SUPPORTS)
/*     */   public <T extends AbstractEntity> List<T> findWithQuery(Class<T> type, Map<String, Object> parameters, int resultLimit) {
/* 165 */     return findWithPageableQuery(type, parameters, resultLimit, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.SUPPORTS)
/*     */   public <T extends AbstractEntity> List<T> findWithPageableQuery(Class<T> type, Map<String, Object> parameters, int resultLimit, Pageable pageable) {
/* 182 */     String METHOD_NAME = "findWithQuery";
/* 183 */     LOG.entering(CLASS_NAME, "findWithQuery");
/* 184 */     TypedQuery<T> typedQuery = getTypedQuery(type, parameters);
/* 185 */     if (pageable != null) {
/* 186 */       typedQuery.setFirstResult((int)pageable.getOffset());
/* 187 */       typedQuery.setMaxResults(pageable.getPageSize());
/* 188 */     } else if (resultLimit > 0) {
/* 189 */       typedQuery.setMaxResults(resultLimit);
/*     */     } 
/* 191 */     LOG.exiting(CLASS_NAME, "findWithQuery");
/* 192 */     return typedQuery.getResultList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.SUPPORTS)
/*     */   public <T extends AbstractEntity> List<T> findWithQuery(Class<T> type, Map<String, Object> parameters, int pageSize, int pageNumber) {
/* 213 */     String METHOD_NAME = "findWithQuery";
/* 214 */     LOG.entering(CLASS_NAME, "findWithQuery");
/* 215 */     TypedQuery<T> typedQuery = getTypedQuery(type, parameters);
/* 216 */     typedQuery.setFirstResult(pageNumber);
/* 217 */     typedQuery.setMaxResults(pageSize);
/* 218 */     LOG.exiting(CLASS_NAME, "findWithQuery");
/* 219 */     return typedQuery.getResultList();
/*     */   }
/*     */   
/*     */   public <T extends AbstractEntity> TypedQuery<T> getTypedQuery(Class<T> type, Map<String, Object> parameters) {
/* 223 */     Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
/* 224 */     CriteriaBuilder builder = this.em.getCriteriaBuilder();
/* 225 */     CriteriaQuery<T> createQuery = builder.createQuery(type);
/* 226 */     Root<T> root = createQuery.from(type);
/* 227 */     List<Predicate> predicates = new ArrayList<>();
/* 228 */     for (Map.Entry<String, Object> entry : rawParameters) {
/* 229 */       if (entry.getValue() instanceof String) {
/* 230 */         Predicate like = builder.like((Expression)root.get(entry.getKey()), (String)entry.getValue());
/* 231 */         predicates.add(like); continue;
/* 232 */       }  if (entry.getValue() instanceof EmptyPredicate) {
/* 233 */         if (((EmptyPredicate)entry.getValue()).canBeEmpty()) {
/* 234 */           predicates.add(builder.equal((Expression)root.get(entry.getKey()), "")); continue;
/*     */         } 
/* 236 */         predicates.add(builder.notEqual((Expression)root.get(entry.getKey()), "")); continue;
/*     */       } 
/* 238 */       if (entry.getValue() instanceof InPredicate) {
/* 239 */         Predicate in = root.get(entry.getKey()).in(((InPredicate)entry.getValue()).getPredicates());
/* 240 */         predicates.add(in); continue;
/*     */       } 
/* 242 */       Predicate equals = builder.equal((Expression)root.get(entry.getKey()), entry.getValue());
/* 243 */       predicates.add(equals);
/*     */     } 
/*     */ 
/*     */     
/* 247 */     CriteriaQuery<T> query = createQuery.select((Selection)root).where(predicates.<Predicate>toArray(new Predicate[predicates.size()])).orderBy(new Order[] { builder.asc((Expression)root.get("createTimestamp")) });
/* 248 */     return this.em.createQuery(query);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.SUPPORTS)
/*     */   public <T extends AbstractEntity> Number countWithQuery(Class<T> type, Map<String, Object> parameters) {
/* 262 */     String METHOD_NAME = "countWithQuery";
/* 263 */     LOG.entering(CLASS_NAME, "countWithQuery");
/* 264 */     Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
/* 265 */     CriteriaBuilder builder = this.em.getCriteriaBuilder();
/* 266 */     CriteriaQuery<Long> createQuery = builder.createQuery(Long.class);
/* 267 */     Root<T> root = createQuery.from(type);
/* 268 */     List<Predicate> predicates = new ArrayList<>();
/* 269 */     for (Map.Entry<String, Object> entry : rawParameters) {
/* 270 */       Predicate equals = builder.equal((Expression)root.get(entry.getKey()), entry.getValue());
/* 271 */       predicates.add(equals);
/*     */     } 
/* 273 */     Path path = root.get("entityId");
/*     */     
/* 275 */     CriteriaQuery<Long> query = createQuery.select((Selection)builder.count((Expression)path)).where(predicates.<Predicate>toArray(new Predicate[predicates.size()]));
/* 276 */     LOG.exiting(CLASS_NAME, "countWithQuery");
/* 277 */     return (Number)this.em.createQuery(query).getSingleResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.SUPPORTS)
/*     */   public <T extends AbstractEntity> void detach(T type) {
/* 288 */     String METHOD_NAME = "detach";
/* 289 */     LOG.entering(CLASS_NAME, "detach");
/* 290 */     this.em.detach(type);
/* 291 */     LOG.exiting(CLASS_NAME, "detach");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends AbstractEntity> T update(T type) {
/* 302 */     String METHOD_NAME = "update";
/* 303 */     LOG.entering(CLASS_NAME, "update");
/* 304 */     AbstractEntity abstractEntity = (AbstractEntity)this.em.merge(type);
/* 305 */     LOG.exiting(CLASS_NAME, "update");
/* 306 */     return (T)abstractEntity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends AbstractEntity> void refresh(T type) {
/* 316 */     String METHOD_NAME = "refresh";
/* 317 */     LOG.entering(CLASS_NAME, "refresh");
/* 318 */     this.em.refresh(type);
/* 319 */     LOG.exiting(CLASS_NAME, "refresh");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends AbstractEntity> void deleteUnManagedEntity(T type) {
/* 329 */     String METHOD_NAME = "deleteUnManagedEntity";
/* 330 */     LOG.entering(CLASS_NAME, "deleteUnManagedEntity");
/* 331 */     Object ref = this.em.getReference(type.getClass(), type.getEntityId());
/* 332 */     this.em.remove(ref);
/* 333 */     LOG.exiting(CLASS_NAME, "deleteUnManagedEntity");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends AbstractEntity> void deleteManagedEntity(T entity) {
/* 343 */     String METHOD_NAME = "deleteManagedEntity";
/* 344 */     LOG.entering(CLASS_NAME, "deleteManagedEntity");
/*     */     
/* 346 */     this.em.remove(entity);
/*     */     
/* 348 */     LOG.exiting(CLASS_NAME, "deleteManagedEntity");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRED)
/*     */   public <T extends AbstractEntity> T create(T type) {
/* 360 */     String METHOD_NAME = "create";
/* 361 */     LOG.entering(CLASS_NAME, "create");
/* 362 */     this.em.persist(type);
/* 363 */     this.em.flush();
/* 364 */     this.em.refresh(type);
/* 365 */     LOG.exiting(CLASS_NAME, "create");
/* 366 */     return type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearContext() {
/* 373 */     String METHOD_NAME = "clearContext";
/* 374 */     LOG.entering(CLASS_NAME, "clearContext");
/* 375 */     LOG.exiting(CLASS_NAME, "clearContext");
/* 376 */     this.em.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() {
/* 383 */     this.em.flush();
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\AbstractRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */