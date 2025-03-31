/*      */ package com.daimler.cebas.certificates.control.repository;
/*      */ 
/*      */ import com.daimler.cebas.certificates.control.vo.DateFilter;
/*      */ import com.daimler.cebas.certificates.entity.Certificate;
/*      */ import com.daimler.cebas.certificates.entity.CertificateState;
/*      */ import com.daimler.cebas.certificates.entity.CertificateType;
/*      */ import com.daimler.cebas.common.control.AbstractRepository;
/*      */ import com.daimler.cebas.common.control.annotations.CEBASRepository;
/*      */ import com.daimler.cebas.common.control.vo.ExtendedFilterObject;
/*      */ import com.daimler.cebas.common.control.vo.FilterObject;
/*      */ import com.daimler.cebas.users.entity.User;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.logging.Logger;
/*      */ import java.util.stream.Collectors;
/*      */ import javax.persistence.Query;
/*      */ import javax.persistence.Tuple;
/*      */ import javax.persistence.TypedQuery;
/*      */ import javax.persistence.criteria.CriteriaBuilder;
/*      */ import javax.persistence.criteria.CriteriaQuery;
/*      */ import javax.persistence.criteria.CriteriaUpdate;
/*      */ import javax.persistence.criteria.Expression;
/*      */ import javax.persistence.criteria.Order;
/*      */ import javax.persistence.criteria.Path;
/*      */ import javax.persistence.criteria.Predicate;
/*      */ import javax.persistence.criteria.Root;
/*      */ import javax.persistence.criteria.Selection;
/*      */ import org.springframework.transaction.annotation.Propagation;
/*      */ import org.springframework.transaction.annotation.Transactional;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @CEBASRepository
/*      */ public class CertificateRepository
/*      */   extends AbstractRepository
/*      */ {
/*      */   private static final String EMPTY = "";
/*      */   private static final String PERCENT = "%";
/*   69 */   private static final String CLASS_NAME = CertificateRepository.class.getName();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   74 */   private static final Logger LOG = Logger.getLogger(CertificateRepository.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addCertificateToUserStore(User user, Certificate certificate) {
/*   85 */     String METHOD_NAME = "addCertificateToUserStore";
/*   86 */     LOG.entering(CLASS_NAME, "addCertificateToUserStore");
/*   87 */     user.getCertificates().add(certificate);
/*   88 */     LOG.exiting(CLASS_NAME, "addCertificateToUserStore");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Transactional(propagation = Propagation.SUPPORTS)
/*      */   public Optional<Certificate> findCertificate(String certificateId) {
/*  100 */     String METHOD_NAME = "findCertificate";
/*  101 */     LOG.entering(CLASS_NAME, "findCertificate");
/*  102 */     Certificate certificate = (Certificate)this.em.find(Certificate.class, certificateId);
/*  103 */     LOG.exiting(CLASS_NAME, "findCertificate");
/*  104 */     return Optional.ofNullable(certificate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Transactional(propagation = Propagation.REQUIRED)
/*      */   public <T> Optional<T> findCertificate(Class<T> type, String certificateId) {
/*  116 */     String METHOD_NAME = "findCertificate";
/*  117 */     LOG.entering(CLASS_NAME, "findCertificate");
/*  118 */     T certificate = (T)this.em.find(type, certificateId);
/*  119 */     LOG.exiting(CLASS_NAME, "findCertificate");
/*  120 */     return Optional.ofNullable(certificate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends Certificate> List<T> filter(Class<T> type, Map<String, FilterObject> filters, int pageNumber, int pageSize) {
/*  138 */     String METHOD_NAME = "filter";
/*  139 */     LOG.entering(CLASS_NAME, "filter");
/*  140 */     List<Predicate> predicates = new ArrayList<>();
/*  141 */     CriteriaBuilder cb = this.em.getCriteriaBuilder();
/*  142 */     CriteriaQuery<T> q = cb.createQuery(type);
/*  143 */     Root<? extends T> c = q.from(type);
/*  144 */     filters.entrySet().forEach(entry -> evaluateFilters(predicates, cb, c, entry));
/*  145 */     q.select((Selection)c).where(predicates.<Predicate>toArray(new Predicate[predicates.size()]));
/*  146 */     TypedQuery<T> query = this.em.createQuery(q);
/*  147 */     if (pageSize > 0) {
/*  148 */       query.setFirstResult(pageNumber * pageSize);
/*  149 */       query.setMaxResults(pageSize);
/*      */     } 
/*  151 */     LOG.exiting(CLASS_NAME, "filter");
/*  152 */     return query.getResultList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Certificate> findActiveForTesting(User user, CertificateType type) {
/*  164 */     TypedQuery<Certificate> createQuery = this.em.createQuery("SELECT c FROM Certificate c WHERE c.user= ?1 AND c.state =?2 AND c.activeForTesting =?3 AND c.type =?4", Certificate.class);
/*      */ 
/*      */     
/*  167 */     createQuery.setParameter(1, user);
/*  168 */     createQuery.setParameter(2, CertificateState.ISSUED);
/*  169 */     createQuery.setParameter(3, Boolean.valueOf(true));
/*  170 */     createQuery.setParameter(4, type);
/*  171 */     return createQuery.getResultList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends Certificate> List<T> findActiveForTesting(Class<T> theClass, User user, CertificateType type) {
/*  183 */     Map<String, Object> params = new HashMap<>();
/*  184 */     params.put("user", user);
/*  185 */     params.put("state", CertificateState.ISSUED);
/*  186 */     params.put("type", type);
/*  187 */     params.put("activeForTesting", Boolean.valueOf(true));
/*  188 */     return findWithQuery(theClass, params, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends Certificate> List<T> findNotActiveForTesting(Class<T> theClass, User user, CertificateType type) {
/*  200 */     Map<String, Object> params = new HashMap<>();
/*  201 */     params.put("user", user);
/*  202 */     params.put("state", CertificateState.ISSUED);
/*  203 */     params.put("type", type);
/*  204 */     params.put("activeForTesting", Boolean.valueOf(false));
/*  205 */     return findWithQuery(theClass, params, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> findActiveForTestingIds(User user, CertificateType type) {
/*  217 */     TypedQuery<String> createQuery = this.em.createQuery("SELECT c.entityId FROM Certificate c WHERE c.user= ?1 AND c.state =?2 AND c.activeForTesting =?3 AND c.type =?4", String.class);
/*      */ 
/*      */     
/*  220 */     createQuery.setParameter(1, user);
/*  221 */     createQuery.setParameter(2, CertificateState.ISSUED);
/*  222 */     createQuery.setParameter(3, Boolean.valueOf(true));
/*  223 */     createQuery.setParameter(4, type);
/*  224 */     return createQuery.getResultList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean checkUniqueAuthKeyAndSN(User user, String authorityKeyId, String serialNo, Certificate parent) {
/*  236 */     TypedQuery<Long> query = this.em.createQuery("SELECT count(c.entityId) FROM Certificate c WHERE c.user= ?1 AND c.authorityKeyIdentifier =?2 AND c.serialNo =?3 AND c.parent =?4", Long.class);
/*      */     
/*  238 */     query.setParameter(1, user);
/*  239 */     query.setParameter(2, authorityKeyId);
/*  240 */     query.setParameter(3, serialNo);
/*  241 */     query.setParameter(4, parent);
/*      */     
/*  243 */     return (((Long)query.getSingleResult()).longValue() == 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean checkUniqueSignatureAndSPK(User user, String signature, String subjectPublicKey) {
/*  255 */     TypedQuery<Long> query = this.em.createQuery("SELECT count(c.entityId) FROM Certificate c WHERE c.user= ?1 AND c.signature =?2 AND c.subjectPublicKey =?3", Long.class);
/*  256 */     query.setParameter(1, user);
/*  257 */     query.setParameter(2, signature);
/*  258 */     query.setParameter(3, subjectPublicKey);
/*  259 */     return (((Long)query.getSingleResult()).longValue() == 0L);
/*      */   }
/*      */   
/*      */   public Certificate getCertificateBySignatureAndSPK(User user, String signature, String subjectPublicKey) {
/*  263 */     TypedQuery<Certificate> query = this.em.createQuery("SELECT c FROM Certificate c WHERE c.user = :user AND c.signature = :signature AND c.subjectPublicKey = :subjectPublicKey", Certificate.class);
/*  264 */     query.setParameter("user", user);
/*  265 */     query.setParameter("signature", signature);
/*  266 */     query.setParameter("subjectPublicKey", subjectPublicKey);
/*  267 */     return (Certificate)query.getSingleResult();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Certificate> getVSMCertIdsCreatedBefore(User user, Date date, boolean appFilter) {
/*  277 */     CertificateType type = CertificateType.ECU_CERTIFICATE;
/*      */ 
/*      */ 
/*      */     
/*  281 */     TypedQuery<Certificate> query = appFilter ? this.em.createQuery("SELECT c FROM Certificate c WHERE c.user = :user AND c.state = :state AND c.type = :type AND c.createTimestamp < :date AND c.specialECU = '1' AND c not in (SELECT kp.certificate from UserKeyPair kp)", Certificate.class) : this.em.createQuery("SELECT c FROM Certificate c WHERE c.user = :user AND c.state = :state AND c.type = :type AND c.createTimestamp < :date AND c.specialECU = '1'", Certificate.class);
/*  282 */     setFieldsForCertificatesDeletion(query, user, date, type);
/*  283 */     return query.getResultList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Certificate> getExpiredCertsAddedInAdHocMode(User user, CertificateType certificateType, Date date) {
/*  294 */     TypedQuery<Certificate> query = this.em.createQuery("SELECT c FROM Certificate c WHERE c.user = :user AND c.state = :state AND c.type = :type AND c.validTo < :date AND c.enrollmentId is not NULL", Certificate.class);
/*      */     
/*  296 */     setFieldsForCertificatesDeletion(query, user, date, certificateType);
/*  297 */     return query.getResultList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Certificate> getExpiredECUCertificates(User user) {
/*  307 */     TypedQuery<Certificate> query = this.em.createQuery("SELECT c FROM Certificate c WHERE c.user = :user AND c.type = :type AND (c.validFrom > :currentDate OR c.validTo < :currentDate)", Certificate.class);
/*      */ 
/*      */     
/*  310 */     query.setParameter("user", user);
/*  311 */     query.setParameter("type", CertificateType.ECU_CERTIFICATE);
/*  312 */     query.setParameter("currentDate", new Date());
/*      */     
/*  314 */     return query.getResultList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getExpiredNonECUCertificates(User user) {
/*  324 */     TypedQuery<String> query = this.em.createQuery("SELECT c.entityId FROM Certificate c WHERE c.user = :user AND c.type <> :type AND (c.validFrom > :currentDate OR c.validTo < :currentDate)", String.class);
/*      */ 
/*      */     
/*  327 */     query.setParameter("user", user);
/*  328 */     query.setParameter("type", CertificateType.ECU_CERTIFICATE);
/*  329 */     query.setParameter("currentDate", new Date());
/*      */     
/*  331 */     return query.getResultList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> void setNotActiveForTestingIds(Class<T> type, List<String> ids) {
/*  340 */     CriteriaBuilder cb = this.em.getCriteriaBuilder();
/*  341 */     CriteriaUpdate<T> update = cb.createCriteriaUpdate(type);
/*  342 */     Root<T> root = update.from(type);
/*  343 */     update.set(root.get("activeForTesting"), Boolean.valueOf(false)).where((Expression)root.get("entityId").in(ids));
/*  344 */     this.em.createQuery(update).executeUpdate();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Transactional(propagation = Propagation.SUPPORTS)
/*      */   public <T extends com.daimler.cebas.common.entity.AbstractEntity> List<Tuple> findTupleWithQuery(Class<T> type, Map<String, Object> queryParameters, List<String> columns, int resultLimit) {
/*  362 */     String METHOD_NAME = "findTupleWithQuery";
/*  363 */     LOG.entering(CLASS_NAME, "findTupleWithQuery");
/*  364 */     Set<Map.Entry<String, Object>> rawParameters = queryParameters.entrySet();
/*  365 */     CriteriaBuilder builder = this.em.getCriteriaBuilder();
/*  366 */     CriteriaQuery<Tuple> createQuery = builder.createTupleQuery();
/*  367 */     Root<T> root = createQuery.from(type);
/*      */     
/*  369 */     List<Predicate> predicates = new ArrayList<>();
/*  370 */     for (Map.Entry<String, Object> entry : rawParameters) {
/*  371 */       if (entry.getValue() instanceof String) {
/*  372 */         Predicate like = builder.like((Expression)root.get(entry.getKey()), (String)entry.getValue());
/*  373 */         predicates.add(like); continue;
/*      */       } 
/*  375 */       Predicate equals = builder.equal((Expression)root.get(entry.getKey()), entry.getValue());
/*  376 */       predicates.add(equals);
/*      */     } 
/*      */ 
/*      */     
/*  380 */     CriteriaQuery<Tuple> query = createQuery.where(predicates.<Predicate>toArray(new Predicate[predicates.size()])).orderBy(new Order[] { builder.asc((Expression)root.get("createTimestamp")) });
/*      */     
/*  382 */     List<Selection<?>> selectionList = (List<Selection<?>>)columns.stream().map(c -> root.get(c)).collect(Collectors.toList());
/*  383 */     query.multiselect(selectionList);
/*      */     
/*  385 */     TypedQuery<Tuple> typedQuery = this.em.createQuery(query);
/*  386 */     if (resultLimit > 0) {
/*  387 */       typedQuery.setMaxResults(resultLimit);
/*      */     }
/*  389 */     LOG.exiting(CLASS_NAME, "findTupleWithQuery");
/*  390 */     return typedQuery.getResultList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Long countFilter(Map<String, FilterObject> filters) {
/*  401 */     String METHOD_NAME = "countFilter";
/*  402 */     LOG.entering(CLASS_NAME, "countFilter");
/*  403 */     List<Predicate> predicates = new ArrayList<>();
/*  404 */     CriteriaBuilder qb = this.em.getCriteriaBuilder();
/*  405 */     CriteriaQuery<Long> cq = qb.createQuery(Long.class);
/*  406 */     Root<Certificate> c = cq.from(Certificate.class);
/*  407 */     filters.entrySet().forEach(entry -> evaluateFiltersOnCount(predicates, qb, c, entry));
/*  408 */     Path path = c.get("entityId");
/*  409 */     cq.select((Selection)qb.count((Expression)path)).where(predicates.<Predicate>toArray(new Predicate[predicates.size()]));
/*      */     
/*  411 */     LOG.exiting(CLASS_NAME, "countFilter");
/*  412 */     return (Long)this.em.createQuery(cq).getSingleResult();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Certificate> findDiagnosticCertificatesForBackends(User user, List<String> backendSubjectKeyIdentifiers, String userRole, String targetECU, String targetVIN) {
/*  434 */     CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
/*  435 */     CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
/*      */     
/*  437 */     List<Predicate> predicates = new ArrayList<>();
/*  438 */     Root<Certificate> root = criteriaQuery.from(Certificate.class);
/*  439 */     predicates.add(criteriaBuilder.equal((Expression)root.get("user"), user));
/*  440 */     predicates.add(criteriaBuilder.equal((Expression)root.get("state"), CertificateState.ISSUED));
/*  441 */     predicates.add(criteriaBuilder.equal((Expression)root.get("type"), CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE));
/*  442 */     predicates.add(root.get("authorityKeyIdentifier").in(backendSubjectKeyIdentifiers));
/*  443 */     predicates.add(criteriaBuilder.equal((Expression)root.get("userRole"), userRole));
/*  444 */     predicates.add(criteriaBuilder.equal((Expression)root.get("targetECU"), targetECU));
/*  445 */     predicates.add(criteriaBuilder.equal((Expression)root.get("targetVIN"), targetVIN));
/*      */     
/*  447 */     criteriaQuery.select((Selection)root).where(predicates.<Predicate>toArray(new Predicate[predicates.size()]));
/*  448 */     return this.em.createQuery(criteriaQuery).getResultList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Certificate> findDiagnosticCertificateForCentralAuthentication(User user, String backendSubjectKeyIdentifier, List<String> excludedUserRoles) {
/*  466 */     CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
/*  467 */     CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
/*  468 */     List<Predicate> predicates = new ArrayList<>();
/*  469 */     Root<Certificate> root = criteriaQuery.from(Certificate.class);
/*  470 */     predicates.add(criteriaBuilder.equal((Expression)root.get("user"), user));
/*  471 */     predicates.add(criteriaBuilder.equal((Expression)root.get("state"), CertificateState.ISSUED));
/*  472 */     predicates.add(criteriaBuilder.equal((Expression)root.get("type"), CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE));
/*  473 */     predicates.add(criteriaBuilder.equal((Expression)root.get("authorityKeyIdentifier"), backendSubjectKeyIdentifier));
/*  474 */     predicates.add(criteriaBuilder.equal((Expression)root.get("targetECU"), ""));
/*  475 */     predicates.add(criteriaBuilder.not((Expression)root.get("userRole").in(excludedUserRoles)));
/*  476 */     criteriaQuery.select((Selection)root).where(predicates.<Predicate>toArray(new Predicate[predicates.size()]));
/*  477 */     return this.em.createQuery(criteriaQuery).getResultList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends Certificate> List<String> getColumnData(User user, Class<T> theClass, String column) {
/*  492 */     CriteriaBuilder cb = this.em.getCriteriaBuilder();
/*  493 */     CriteriaQuery<String> query = cb.createQuery(String.class);
/*  494 */     Root<T> root = query.from(theClass);
/*  495 */     Path path = root.get("user");
/*  496 */     query.select((Selection)root.get(column)).where((Expression)cb.equal((Expression)path, user));
/*  497 */     TypedQuery<String> typedQuery = this.em.createQuery(query);
/*  498 */     return typedQuery.getResultList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends Certificate> List<CertificateType> getColumnTypeData(User user, Class<T> theClass, String column) {
/*  514 */     CriteriaBuilder cb = this.em.getCriteriaBuilder();
/*  515 */     CriteriaQuery<CertificateType> query = cb.createQuery(CertificateType.class);
/*  516 */     Root<T> root = query.from(theClass);
/*  517 */     Path path = root.get("user");
/*  518 */     query.select((Selection)root.get(column)).where((Expression)cb.equal((Expression)path, user));
/*  519 */     TypedQuery<CertificateType> typedQuery = this.em.createQuery(query);
/*  520 */     return typedQuery.getResultList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends Certificate> List<boolean[]> getColumnBooleanArrayData(User user, Class<T> theClass, String column) {
/*  536 */     CriteriaBuilder cb = this.em.getCriteriaBuilder();
/*  537 */     CriteriaQuery<boolean[]> query = cb.createQuery(boolean[].class);
/*  538 */     Root<T> root = query.from(theClass);
/*  539 */     Path path = root.get("user");
/*  540 */     query.select((Selection)root.get(column)).where((Expression)cb.equal((Expression)path, user));
/*  541 */     TypedQuery<boolean[]> typedQuery = this.em.createQuery(query);
/*  542 */     return typedQuery.getResultList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setFieldsForCertificatesDeletion(TypedQuery<Certificate> query, User user, Date date, CertificateType type) {
/*  553 */     query.setParameter("user", user);
/*  554 */     query.setParameter("state", CertificateState.ISSUED);
/*  555 */     query.setParameter("type", type);
/*  556 */     query.setParameter("date", date);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Predicate getLikeLower(CriteriaBuilder cb, String value, Expression<String> expression) {
/*  568 */     return cb.like(cb.lower(expression), "%" + value.toLowerCase() + "%");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Predicate getLikeUpper(CriteriaBuilder cb, String value, Expression<String> expression) {
/*  580 */     return cb.like(cb.upper(expression), "%" + value.toUpperCase() + "%");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Predicate getInListPredicate(CriteriaBuilder cb, Object value, Expression<String> expression) {
/*      */     Predicate inList;
/*  594 */     if (((List)value).contains((Object)null)) {
/*  595 */       ((List)value).remove((Object)null);
/*  596 */       inList = ((List)value).isEmpty() ? expression.isNull() : cb.or((Expression)expression.isNull(), (Expression)expression.in(new Object[] { value }));
/*      */     } else {
/*  598 */       inList = expression.in(new Object[] { value });
/*      */     } 
/*  600 */     return inList;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <T extends Certificate> void evaluateFilters(List<Predicate> predicates, CriteriaBuilder cb, Root<? extends T> c, Map.Entry<String, FilterObject> entry) {
/*  617 */     Object value = ((FilterObject)entry.getValue()).getFilterValue();
/*  618 */     if (value instanceof String) {
/*  619 */       Path path = c.get(entry.getKey());
/*  620 */       String filterValue = (String)((FilterObject)entry.getValue()).getFilterValue();
/*  621 */       if (StringUtils.isEmpty(filterValue)) {
/*  622 */         predicates.add(cb.equal((Expression)path, ""));
/*      */       } else {
/*  624 */         Predicate likeLower = getLikeLower(cb, filterValue, (Expression<String>)path);
/*  625 */         Predicate likeUpper = getLikeUpper(cb, filterValue, (Expression<String>)path);
/*  626 */         predicates.add(cb.or((Expression)likeLower, (Expression)likeUpper));
/*      */       } 
/*      */     } 
/*  629 */     determineUserFilter(predicates, cb, c, entry, value);
/*  630 */     determineCertificateTypeFilter(predicates, cb, c, entry, value);
/*  631 */     determineCertificateFilter(predicates, cb, c, entry, value);
/*  632 */     determineListFilter(predicates, cb, c, entry, value);
/*  633 */     determineDateFilter(predicates, cb, c, entry, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void evaluateFiltersOnCount(List<Predicate> predicates, CriteriaBuilder cb, Root<Certificate> c, Map.Entry<String, FilterObject> entry) {
/*  650 */     Object value = ((FilterObject)entry.getValue()).getFilterValue();
/*  651 */     if (value instanceof String) {
/*  652 */       Path path = c.get(entry.getKey());
/*  653 */       String filterValue = (String)((FilterObject)entry.getValue()).getFilterValue();
/*  654 */       Predicate likeLower = getLikeLower(cb, filterValue, (Expression<String>)path);
/*  655 */       Predicate likeUpper = getLikeUpper(cb, filterValue, (Expression<String>)path);
/*  656 */       predicates.add(cb.or((Expression)likeLower, (Expression)likeUpper));
/*      */     } 
/*  658 */     determineUserFilter(predicates, cb, c, entry, value);
/*  659 */     determineCertificateTypeFilter(predicates, cb, c, entry, value);
/*  660 */     determineUserFilter(predicates, cb, c, entry, value);
/*  661 */     determineListFilter(predicates, cb, c, entry, value);
/*  662 */     determineDateFilter(predicates, cb, c, entry, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <T extends Certificate> void determineDateFilter(List<Predicate> predicates, CriteriaBuilder cb, Root<? extends T> c, Map.Entry<String, FilterObject> entry, Object value) {
/*  681 */     if (value instanceof DateFilter) {
/*  682 */       DateFilter dateFilter = (DateFilter)value;
/*  683 */       Date startDate = dateFilter.getStart();
/*  684 */       Date endDate = dateFilter.getEnd();
/*  685 */       Path path = c.get(entry.getKey());
/*  686 */       if (startDate != null && endDate != null) {
/*  687 */         predicates.add(cb.between((Expression)path, startDate, endDate));
/*  688 */       } else if (startDate != null) {
/*  689 */         predicates.add(cb.greaterThanOrEqualTo((Expression)path, startDate));
/*  690 */       } else if (endDate != null) {
/*  691 */         predicates.add(cb.lessThanOrEqualTo((Expression)path, endDate));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <T extends Certificate> void determineListFilter(List<Predicate> predicates, CriteriaBuilder cb, Root<? extends T> c, Map.Entry<String, FilterObject> entry, Object value) {
/*  712 */     FilterObject filterObject = entry.getValue();
/*  713 */     if (filterObject instanceof ExtendedFilterObject) {
/*  714 */       ExtendedFilterObject extendedFilterObject = (ExtendedFilterObject)filterObject;
/*  715 */       Path path = c.get(entry.getKey());
/*  716 */       Predicate inList = getInListPredicate(cb, value, (Expression<String>)path);
/*  717 */       Predicate likeLower = getLikeLower(cb, extendedFilterObject.getOrSomethingElse(), (Expression<String>)path);
/*  718 */       Predicate likeUpper = getLikeUpper(cb, extendedFilterObject.getOrSomethingElse(), (Expression<String>)path);
/*  719 */       predicates.add(cb.or(new Predicate[] { likeLower, likeUpper, inList }));
/*      */     }
/*  721 */     else if (value instanceof List && !filterObject.isMatch()) {
/*  722 */       Path path = c.get(entry.getKey());
/*  723 */       Predicate inList = getInListPredicate(cb, value, (Expression<String>)path);
/*  724 */       predicates.add(inList);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <T extends Certificate> void determineCertificateFilter(List<Predicate> predicates, CriteriaBuilder cb, Root<? extends T> c, Map.Entry<String, FilterObject> entry, Object value) {
/*  744 */     if (value instanceof Certificate) {
/*  745 */       Path path = c.get(entry.getKey());
/*  746 */       predicates.add(cb.equal((Expression)path, ((FilterObject)entry.getValue()).getFilterValue()));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <T extends Certificate> void determineCertificateTypeFilter(List<Predicate> predicates, CriteriaBuilder cb, Root<? extends T> c, Map.Entry<String, FilterObject> entry, Object value) {
/*  766 */     if (value instanceof CertificateType) {
/*  767 */       Path path = c.get(entry.getKey());
/*  768 */       predicates.add(cb.equal((Expression)path, ((FilterObject)entry.getValue()).getFilterValue()));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <T extends Certificate> void determineUserFilter(List<Predicate> predicates, CriteriaBuilder cb, Root<? extends T> c, Map.Entry<String, FilterObject> entry, Object value) {
/*  788 */     if (value instanceof User) {
/*  789 */       Path path = c.get(entry.getKey());
/*  790 */       predicates.add(cb.equal((Expression)path, ((FilterObject)entry.getValue()).getFilterValue()));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> findVSMCertificates() {
/*  800 */     String METHOD_NAME = "findVSMCertificates";
/*  801 */     LOG.entering(CLASS_NAME, "findVSMCertificates");
/*  802 */     TypedQuery<String> query = this.em.createNamedQuery("ECU_WITH_PK", String.class);
/*  803 */     LOG.exiting(CLASS_NAME, "findVSMCertificates");
/*  804 */     return query.getResultList();
/*      */   }
/*      */   
/*      */   public void updateCertificatesWithStatus(List<String> ids, String status) {
/*  808 */     String METHOD_NAME = "updateCertificatesWithStatus";
/*  809 */     LOG.entering(CLASS_NAME, "updateCertificatesWithStatus");
/*  810 */     Query q = this.em.createNamedQuery("UPDATE_STATUS");
/*  811 */     int u = q.setParameter("ids", ids).setParameter("status", status).executeUpdate();
/*  812 */     LOG.info("updateCertificatesWithStatus set status to: " + status + " for " + u + " entries.");
/*  813 */     LOG.exiting(CLASS_NAME, "updateCertificatesWithStatus");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*      */   public void updateEnrollmentId(User user, CertificateType type, String subjectKeyIdentifier, String enrollmentId) {
/*  830 */     Query query = this.em.createQuery("UPDATE SigModulCertificate c SET c.enrollmentId = :enrollmentId where c.user = :user and c.type = :type and c.subjectKeyIdentifier = :subjectKeyIdentifier");
/*  831 */     query.setParameter("user", user);
/*  832 */     query.setParameter("type", type);
/*  833 */     query.setParameter("subjectKeyIdentifier", subjectKeyIdentifier);
/*  834 */     query.setParameter("enrollmentId", enrollmentId);
/*  835 */     query.executeUpdate();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*      */   public void updateEnrollmentId(User user, String entityId, String enrollmentId) {
/*  850 */     Query query = this.em.createQuery("UPDATE SigModulCertificate c SET c.enrollmentId = :enrollmentId where c.user = :user and c.entityId = :entityId");
/*  851 */     query.setParameter("user", user);
/*  852 */     query.setParameter("entityId", entityId);
/*  853 */     query.setParameter("enrollmentId", enrollmentId);
/*  854 */     query.executeUpdate();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updatePkiKnown(String entityId, Boolean pkiKnown) {
/*  864 */     Query query = this.em.createQuery("UPDATE ZenZefiCertificate c SET c.pkiKnown = :pkiKnown where c.entityId = :entityId");
/*  865 */     query.setParameter("entityId", entityId);
/*  866 */     query.setParameter("pkiKnown", pkiKnown);
/*  867 */     query.executeUpdate();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updatePkiKnownForUnknownBackends(User user, List<String> pkiKnownBackCertIds) {
/*  877 */     Query query = this.em.createQuery("UPDATE ZenZefiCertificate c SET c.pkiKnown = false where c.user = :user and c.type = :type and c.entityId not in :entityId");
/*  878 */     query.setParameter("user", user);
/*  879 */     query.setParameter("type", CertificateType.BACKEND_CA_CERTIFICATE);
/*  880 */     query.setParameter("entityId", pkiKnownBackCertIds);
/*  881 */     query.executeUpdate();
/*      */   }
/*      */   
/*      */   public List<Certificate> getIdentical(Certificate certificate) {
/*  885 */     Query q = this.em.createNamedQuery("getPossibleIdenticals");
/*  886 */     q.setParameter("signature", certificate.getSignature());
/*  887 */     q.setParameter("subject", certificate.getSubject());
/*  888 */     q.setParameter("type", certificate.getType());
/*  889 */     q.setParameter("user", certificate.getUser());
/*  890 */     return q.getResultList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isECUUnique(Certificate certificate) {
/*  909 */     Query q = this.em.createNamedQuery("findECUIdenticals");
/*  910 */     q.setParameter("ski", certificate.getSubjectKeyIdentifier());
/*  911 */     q.setParameter("aki", certificate.getAuthorityKeyIdentifier());
/*  912 */     q.setParameter("subject", certificate.getSubject());
/*  913 */     q.setParameter("uniqueECUID", certificate.getUniqueECUID());
/*  914 */     q.setParameter("certType", certificate.getType());
/*  915 */     q.setParameter("specialECU", certificate.getSpecialECU());
/*  916 */     q.setParameter("publicKey", certificate.getSubjectPublicKey());
/*  917 */     q.setParameter("user", certificate.getUser());
/*  918 */     Long identicals = (Long)q.getSingleResult();
/*  919 */     return (identicals.longValue() == 0L);
/*      */   }
/*      */   
/*      */   public Optional<Certificate> findParent(Certificate certificate) {
/*  923 */     Query q = this.em.createNamedQuery("findParentUsingAKI");
/*  924 */     q.setParameter("ski", certificate.getAuthorityKeyIdentifier());
/*  925 */     q.setParameter("user", certificate.getUser());
/*  926 */     List<Certificate> resultList = q.getResultList();
/*  927 */     if (resultList.isEmpty()) {
/*  928 */       return Optional.empty();
/*      */     }
/*  930 */     return Optional.of(resultList.get(0));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends Certificate> List<T> findByType(Class<T> theClass, User user, CertificateType type) {
/*  942 */     Map<String, Object> params = new HashMap<>();
/*  943 */     params.put("user", user);
/*  944 */     params.put("state", CertificateState.ISSUED);
/*  945 */     params.put("type", type);
/*  946 */     return findWithQuery(theClass, params, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends Certificate> List<T> findByTypeAndParent(Class<T> theClass, User user, CertificateType type, Certificate parent) {
/*  958 */     Map<String, Object> params = new HashMap<>();
/*  959 */     params.put("user", user);
/*  960 */     params.put("state", CertificateState.ISSUED);
/*  961 */     params.put("type", type);
/*  962 */     params.put("authorityKeyIdentifier", parent.getSubjectKeyIdentifier());
/*  963 */     return findWithQuery(theClass, params, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends Certificate> List<T> findCertificatesWithTargetVinAndTargetEcu(User user, CertificateType certificateType, String backendCertSkid, String patternTargetEcu, String patternTargetVin, Class<T> type) {
/*  986 */     Query query = this.em.createNativeQuery("SELECT * FROM SIGMODUL.R_CERTIFICATE c WHERE c.F_USER_ID = :user AND c.F_STATE = :state AND c.F_CERTIFICATE_TYPE = :type AND c.F_AUTHORITY_KEY_IDENTIFIER = :authorityKeyIdentifier AND c.F_TARGET_ECU regexp :targetECU AND c.F_TARGET_VIN regexp :targetVIN", type);
/*      */ 
/*      */     
/*  989 */     query.setParameter("user", user.getEntityId());
/*  990 */     query.setParameter("state", CertificateState.ISSUED.name());
/*  991 */     query.setParameter("type", certificateType.name());
/*  992 */     query.setParameter("authorityKeyIdentifier", backendCertSkid);
/*  993 */     query.setParameter("targetECU", patternTargetEcu);
/*  994 */     query.setParameter("targetVIN", patternTargetVin);
/*      */     
/*  996 */     return query.getResultList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> List<T> findCSRsWithTargetVinAndTargetEcu(User user, CertificateType csrType, String backendSubjectKeyIdentifier, String patternTargetEcu, String patternTargetVin, Class<T> type) {
/* 1019 */     Query query = this.em.createNativeQuery("SELECT * FROM SIGMODUL.R_CERTIFICATE c WHERE c.F_USER_ID = :user AND c.F_STATE = :state AND c.F_CERTIFICATE_TYPE = :type AND c.F_AUTHORITY_KEY_IDENTIFIER = :authorityKeyIdentifier AND c.F_TARGET_ECU regexp :targetECU AND c.F_TARGET_VIN regexp :targetVIN", type);
/*      */ 
/*      */     
/* 1022 */     query.setParameter("user", user.getEntityId());
/* 1023 */     query.setParameter("state", CertificateState.SIGNING_REQUEST.name());
/* 1024 */     query.setParameter("type", csrType.name());
/* 1025 */     query.setParameter("authorityKeyIdentifier", backendSubjectKeyIdentifier);
/* 1026 */     query.setParameter("targetECU", patternTargetEcu);
/* 1027 */     query.setParameter("targetVIN", patternTargetVin);
/*      */     
/* 1029 */     return query.getResultList();
/*      */   }
/*      */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\repository\CertificateRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */