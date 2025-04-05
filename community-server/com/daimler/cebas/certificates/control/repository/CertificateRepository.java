/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.DateFilter
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateState
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.common.control.AbstractRepository
 *  com.daimler.cebas.common.control.annotations.CEBASRepository
 *  com.daimler.cebas.common.control.vo.ExtendedFilterObject
 *  com.daimler.cebas.common.control.vo.FilterObject
 *  com.daimler.cebas.common.entity.AbstractEntity
 *  com.daimler.cebas.users.entity.User
 *  javax.persistence.Query
 *  javax.persistence.Tuple
 *  javax.persistence.TypedQuery
 *  javax.persistence.criteria.CriteriaBuilder
 *  javax.persistence.criteria.CriteriaQuery
 *  javax.persistence.criteria.CriteriaUpdate
 *  javax.persistence.criteria.Expression
 *  javax.persistence.criteria.Order
 *  javax.persistence.criteria.Path
 *  javax.persistence.criteria.Predicate
 *  javax.persistence.criteria.Root
 *  javax.persistence.criteria.Selection
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.util.StringUtils
 */
package com.daimler.cebas.certificates.control.repository;

import com.daimler.cebas.certificates.control.vo.DateFilter;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateState;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.common.control.AbstractRepository;
import com.daimler.cebas.common.control.annotations.CEBASRepository;
import com.daimler.cebas.common.control.vo.ExtendedFilterObject;
import com.daimler.cebas.common.control.vo.FilterObject;
import com.daimler.cebas.common.entity.AbstractEntity;
import com.daimler.cebas.users.entity.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@CEBASRepository
public class CertificateRepository
extends AbstractRepository {
    private static final String EMPTY = "";
    private static final String PERCENT = "%";
    private static final String CLASS_NAME = CertificateRepository.class.getName();
    private static final Logger LOG = Logger.getLogger(CertificateRepository.class.getName());

    public void addCertificateToUserStore(User user, Certificate certificate) {
        String METHOD_NAME = "addCertificateToUserStore";
        LOG.entering(CLASS_NAME, "addCertificateToUserStore");
        user.getCertificates().add(certificate);
        LOG.exiting(CLASS_NAME, "addCertificateToUserStore");
    }

    @Transactional(propagation=Propagation.SUPPORTS)
    public Optional<Certificate> findCertificate(String certificateId) {
        String METHOD_NAME = "findCertificate";
        LOG.entering(CLASS_NAME, "findCertificate");
        Certificate certificate = (Certificate)this.em.find(Certificate.class, (Object)certificateId);
        LOG.exiting(CLASS_NAME, "findCertificate");
        return Optional.ofNullable(certificate);
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public <T> Optional<T> findCertificate(Class<T> type, String certificateId) {
        String METHOD_NAME = "findCertificate";
        LOG.entering(CLASS_NAME, "findCertificate");
        Object certificate = this.em.find(type, (Object)certificateId);
        LOG.exiting(CLASS_NAME, "findCertificate");
        return Optional.ofNullable(certificate);
    }

    public <T extends Certificate> List<T> filter(Class<T> type, Map<String, FilterObject> filters, int pageNumber, int pageSize) {
        String METHOD_NAME = "filter";
        LOG.entering(CLASS_NAME, "filter");
        ArrayList predicates = new ArrayList();
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery q = cb.createQuery(type);
        Root c = q.from(type);
        filters.entrySet().forEach(entry -> this.evaluateFilters(predicates, cb, (Root)c, (Map.Entry<String, FilterObject>)entry));
        q.select((Selection)c).where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery query = this.em.createQuery(q);
        if (pageSize > 0) {
            query.setFirstResult(pageNumber * pageSize);
            query.setMaxResults(pageSize);
        }
        LOG.exiting(CLASS_NAME, "filter");
        return query.getResultList();
    }

    public List<Certificate> findActiveForTesting(User user, CertificateType type) {
        TypedQuery createQuery = this.em.createQuery("SELECT c FROM Certificate c WHERE c.user= ?1 AND c.state =?2 AND c.activeForTesting =?3 AND c.type =?4", Certificate.class);
        createQuery.setParameter(1, (Object)user);
        createQuery.setParameter(2, (Object)CertificateState.ISSUED);
        createQuery.setParameter(3, (Object)true);
        createQuery.setParameter(4, (Object)type);
        return createQuery.getResultList();
    }

    public <T extends Certificate> List<T> findActiveForTesting(Class<T> theClass, User user, CertificateType type) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user", user);
        params.put("state", CertificateState.ISSUED);
        params.put("type", type);
        params.put("activeForTesting", true);
        return this.findWithQuery(theClass, params, -1);
    }

    public <T extends Certificate> List<T> findNotActiveForTesting(Class<T> theClass, User user, CertificateType type) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user", user);
        params.put("state", CertificateState.ISSUED);
        params.put("type", type);
        params.put("activeForTesting", false);
        return this.findWithQuery(theClass, params, -1);
    }

    public List<String> findActiveForTestingIds(User user, CertificateType type) {
        TypedQuery createQuery = this.em.createQuery("SELECT c.entityId FROM Certificate c WHERE c.user= ?1 AND c.state =?2 AND c.activeForTesting =?3 AND c.type =?4", String.class);
        createQuery.setParameter(1, (Object)user);
        createQuery.setParameter(2, (Object)CertificateState.ISSUED);
        createQuery.setParameter(3, (Object)true);
        createQuery.setParameter(4, (Object)type);
        return createQuery.getResultList();
    }

    public boolean checkUniqueAuthKeyAndSN(User user, String authorityKeyId, String serialNo, Certificate parent) {
        TypedQuery query = this.em.createQuery("SELECT count(c.entityId) FROM Certificate c WHERE c.user= ?1 AND c.authorityKeyIdentifier =?2 AND c.serialNo =?3 AND c.parent =?4", Long.class);
        query.setParameter(1, (Object)user);
        query.setParameter(2, (Object)authorityKeyId);
        query.setParameter(3, (Object)serialNo);
        query.setParameter(4, (Object)parent);
        return (Long)query.getSingleResult() == 0L;
    }

    public boolean checkUniqueSignatureAndSPK(User user, String signature, String subjectPublicKey) {
        TypedQuery query = this.em.createQuery("SELECT count(c.entityId) FROM Certificate c WHERE c.user= ?1 AND c.signature =?2 AND c.subjectPublicKey =?3", Long.class);
        query.setParameter(1, (Object)user);
        query.setParameter(2, (Object)signature);
        query.setParameter(3, (Object)subjectPublicKey);
        return (Long)query.getSingleResult() == 0L;
    }

    public Certificate getCertificateBySignatureAndSPK(User user, String signature, String subjectPublicKey) {
        TypedQuery query = this.em.createQuery("SELECT c FROM Certificate c WHERE c.user = :user AND c.signature = :signature AND c.subjectPublicKey = :subjectPublicKey", Certificate.class);
        query.setParameter("user", (Object)user);
        query.setParameter("signature", (Object)signature);
        query.setParameter("subjectPublicKey", (Object)subjectPublicKey);
        return (Certificate)query.getSingleResult();
    }

    public List<Certificate> getVSMCertIdsCreatedBefore(User user, Date date, boolean appFilter) {
        CertificateType type = CertificateType.ECU_CERTIFICATE;
        TypedQuery query = appFilter ? this.em.createQuery("SELECT c FROM Certificate c WHERE c.user = :user AND c.state = :state AND c.type = :type AND c.createTimestamp < :date AND c.specialECU = '1' AND c not in (SELECT kp.certificate from UserKeyPair kp)", Certificate.class) : this.em.createQuery("SELECT c FROM Certificate c WHERE c.user = :user AND c.state = :state AND c.type = :type AND c.createTimestamp < :date AND c.specialECU = '1'", Certificate.class);
        this.setFieldsForCertificatesDeletion((TypedQuery<Certificate>)query, user, date, type);
        return query.getResultList();
    }

    public List<Certificate> getExpiredCertsAddedInAdHocMode(User user, CertificateType certificateType, Date date) {
        TypedQuery query = this.em.createQuery("SELECT c FROM Certificate c WHERE c.user = :user AND c.state = :state AND c.type = :type AND c.validTo < :date AND c.enrollmentId is not NULL", Certificate.class);
        this.setFieldsForCertificatesDeletion((TypedQuery<Certificate>)query, user, date, certificateType);
        return query.getResultList();
    }

    public List<Certificate> getExpiredECUCertificates(User user) {
        TypedQuery query = this.em.createQuery("SELECT c FROM Certificate c WHERE c.user = :user AND c.type = :type AND (c.validFrom > :currentDate OR c.validTo < :currentDate)", Certificate.class);
        query.setParameter("user", (Object)user);
        query.setParameter("type", (Object)CertificateType.ECU_CERTIFICATE);
        query.setParameter("currentDate", (Object)new Date());
        return query.getResultList();
    }

    public List<String> getExpiredNonECUCertificates(User user) {
        TypedQuery query = this.em.createQuery("SELECT c.entityId FROM Certificate c WHERE c.user = :user AND c.type <> :type AND (c.validFrom > :currentDate OR c.validTo < :currentDate)", String.class);
        query.setParameter("user", (Object)user);
        query.setParameter("type", (Object)CertificateType.ECU_CERTIFICATE);
        query.setParameter("currentDate", (Object)new Date());
        return query.getResultList();
    }

    public <T> void setNotActiveForTestingIds(Class<T> type, List<String> ids) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaUpdate update = cb.createCriteriaUpdate(type);
        Root root = update.from(type);
        update.set(root.get("activeForTesting"), (Object)false).where((Expression)root.get("entityId").in(ids));
        this.em.createQuery(update).executeUpdate();
    }

    @Transactional(propagation=Propagation.SUPPORTS)
    public <T extends AbstractEntity> List<Tuple> findTupleWithQuery(Class<T> type, Map<String, Object> queryParameters, List<String> columns, int resultLimit) {
        String METHOD_NAME = "findTupleWithQuery";
        LOG.entering(CLASS_NAME, "findTupleWithQuery");
        Set<Map.Entry<String, Object>> rawParameters = queryParameters.entrySet();
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery createQuery = builder.createTupleQuery();
        Root root = createQuery.from(type);
        ArrayList<Predicate> predicates = new ArrayList<Predicate>();
        for (Map.Entry<String, Object> entry : rawParameters) {
            if (entry.getValue() instanceof String) {
                Predicate like = builder.like((Expression)root.get(entry.getKey()), (String)entry.getValue());
                predicates.add(like);
                continue;
            }
            Predicate equals = builder.equal((Expression)root.get(entry.getKey()), entry.getValue());
            predicates.add(equals);
        }
        CriteriaQuery query = createQuery.where(predicates.toArray(new Predicate[predicates.size()])).orderBy(new Order[]{builder.asc((Expression)root.get("createTimestamp"))});
        List selectionList = columns.stream().map(c -> root.get(c)).collect(Collectors.toList());
        query.multiselect(selectionList);
        TypedQuery typedQuery = this.em.createQuery(query);
        if (resultLimit > 0) {
            typedQuery.setMaxResults(resultLimit);
        }
        LOG.exiting(CLASS_NAME, "findTupleWithQuery");
        return typedQuery.getResultList();
    }

    public Long countFilter(Map<String, FilterObject> filters) {
        String METHOD_NAME = "countFilter";
        LOG.entering(CLASS_NAME, "countFilter");
        ArrayList predicates = new ArrayList();
        CriteriaBuilder qb = this.em.getCriteriaBuilder();
        CriteriaQuery cq = qb.createQuery(Long.class);
        Root c = cq.from(Certificate.class);
        filters.entrySet().forEach(entry -> this.evaluateFiltersOnCount(predicates, qb, (Root<Certificate>)c, (Map.Entry<String, FilterObject>)entry));
        Path expression = c.get("entityId");
        cq.select((Selection)qb.count((Expression)expression)).where(predicates.toArray(new Predicate[predicates.size()]));
        LOG.exiting(CLASS_NAME, "countFilter");
        return (Long)this.em.createQuery(cq).getSingleResult();
    }

    public List<Certificate> findDiagnosticCertificatesForBackends(User user, List<String> backendSubjectKeyIdentifiers, String userRole, String targetECU, String targetVIN) {
        CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        ArrayList<Predicate> predicates = new ArrayList<Predicate>();
        Root root = criteriaQuery.from(Certificate.class);
        predicates.add(criteriaBuilder.equal((Expression)root.get("user"), (Object)user));
        predicates.add(criteriaBuilder.equal((Expression)root.get("state"), (Object)CertificateState.ISSUED));
        predicates.add(criteriaBuilder.equal((Expression)root.get("type"), (Object)CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE));
        predicates.add(root.get("authorityKeyIdentifier").in(backendSubjectKeyIdentifiers));
        predicates.add(criteriaBuilder.equal((Expression)root.get("userRole"), (Object)userRole));
        predicates.add(criteriaBuilder.equal((Expression)root.get("targetECU"), (Object)targetECU));
        predicates.add(criteriaBuilder.equal((Expression)root.get("targetVIN"), (Object)targetVIN));
        criteriaQuery.select((Selection)root).where(predicates.toArray(new Predicate[predicates.size()]));
        return this.em.createQuery(criteriaQuery).getResultList();
    }

    public List<Certificate> findDiagnosticCertificateForCentralAuthentication(User user, String backendSubjectKeyIdentifier, List<String> excludedUserRoles) {
        CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        ArrayList<Predicate> predicates = new ArrayList<Predicate>();
        Root root = criteriaQuery.from(Certificate.class);
        predicates.add(criteriaBuilder.equal((Expression)root.get("user"), (Object)user));
        predicates.add(criteriaBuilder.equal((Expression)root.get("state"), (Object)CertificateState.ISSUED));
        predicates.add(criteriaBuilder.equal((Expression)root.get("type"), (Object)CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE));
        predicates.add(criteriaBuilder.equal((Expression)root.get("authorityKeyIdentifier"), (Object)backendSubjectKeyIdentifier));
        predicates.add(criteriaBuilder.equal((Expression)root.get("targetECU"), (Object)EMPTY));
        predicates.add(criteriaBuilder.not((Expression)root.get("userRole").in(excludedUserRoles)));
        criteriaQuery.select((Selection)root).where(predicates.toArray(new Predicate[predicates.size()]));
        return this.em.createQuery(criteriaQuery).getResultList();
    }

    public <T extends Certificate> List<String> getColumnData(User user, Class<T> theClass, String column) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery query = cb.createQuery(String.class);
        Root root = query.from(theClass);
        Path expression = root.get("user");
        query.select((Selection)root.get(column)).where((Expression)cb.equal((Expression)expression, (Object)user));
        TypedQuery typedQuery = this.em.createQuery(query);
        return typedQuery.getResultList();
    }

    public <T extends Certificate> List<CertificateType> getColumnTypeData(User user, Class<T> theClass, String column) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery query = cb.createQuery(CertificateType.class);
        Root root = query.from(theClass);
        Path expression = root.get("user");
        query.select((Selection)root.get(column)).where((Expression)cb.equal((Expression)expression, (Object)user));
        TypedQuery typedQuery = this.em.createQuery(query);
        return typedQuery.getResultList();
    }

    public <T extends Certificate> List<boolean[]> getColumnBooleanArrayData(User user, Class<T> theClass, String column) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery query = cb.createQuery(boolean[].class);
        Root root = query.from(theClass);
        Path expression = root.get("user");
        query.select((Selection)root.get(column)).where((Expression)cb.equal((Expression)expression, (Object)user));
        TypedQuery typedQuery = this.em.createQuery(query);
        return typedQuery.getResultList();
    }

    private void setFieldsForCertificatesDeletion(TypedQuery<Certificate> query, User user, Date date, CertificateType type) {
        query.setParameter("user", (Object)user);
        query.setParameter("state", (Object)CertificateState.ISSUED);
        query.setParameter("type", (Object)type);
        query.setParameter("date", (Object)date);
    }

    private Predicate getLikeLower(CriteriaBuilder cb, String value, Expression<String> expression) {
        return cb.like(cb.lower(expression), PERCENT + value.toLowerCase() + PERCENT);
    }

    private Predicate getLikeUpper(CriteriaBuilder cb, String value, Expression<String> expression) {
        return cb.like(cb.upper(expression), PERCENT + value.toUpperCase() + PERCENT);
    }

    private Predicate getInListPredicate(CriteriaBuilder cb, Object value, Expression<String> expression) {
        Predicate inList;
        if (((List)value).contains(null)) {
            ((List)value).remove(null);
            inList = ((List)value).isEmpty() ? expression.isNull() : cb.or((Expression)expression.isNull(), (Expression)expression.in(new Object[]{value}));
        } else {
            inList = expression.in(new Object[]{value});
        }
        return inList;
    }

    private <T extends Certificate> void evaluateFilters(List<Predicate> predicates, CriteriaBuilder cb, Root<? extends T> c, Map.Entry<String, FilterObject> entry) {
        Object value = entry.getValue().getFilterValue();
        if (value instanceof String) {
            Path expression = c.get(entry.getKey());
            String filterValue = (String)entry.getValue().getFilterValue();
            if (StringUtils.isEmpty((Object)filterValue)) {
                predicates.add(cb.equal((Expression)expression, (Object)EMPTY));
            } else {
                Predicate likeLower = this.getLikeLower(cb, filterValue, (Expression<String>)expression);
                Predicate likeUpper = this.getLikeUpper(cb, filterValue, (Expression<String>)expression);
                predicates.add(cb.or((Expression)likeLower, (Expression)likeUpper));
            }
        }
        this.determineUserFilter(predicates, cb, c, entry, value);
        this.determineCertificateTypeFilter(predicates, cb, c, entry, value);
        this.determineCertificateFilter(predicates, cb, c, entry, value);
        this.determineListFilter(predicates, cb, c, entry, value);
        this.determineDateFilter(predicates, cb, c, entry, value);
    }

    private void evaluateFiltersOnCount(List<Predicate> predicates, CriteriaBuilder cb, Root<Certificate> c, Map.Entry<String, FilterObject> entry) {
        Object value = entry.getValue().getFilterValue();
        if (value instanceof String) {
            Path expression = c.get(entry.getKey());
            String filterValue = (String)entry.getValue().getFilterValue();
            Predicate likeLower = this.getLikeLower(cb, filterValue, (Expression<String>)expression);
            Predicate likeUpper = this.getLikeUpper(cb, filterValue, (Expression<String>)expression);
            predicates.add(cb.or((Expression)likeLower, (Expression)likeUpper));
        }
        this.determineUserFilter(predicates, cb, c, entry, value);
        this.determineCertificateTypeFilter(predicates, cb, c, entry, value);
        this.determineUserFilter(predicates, cb, c, entry, value);
        this.determineListFilter(predicates, cb, c, entry, value);
        this.determineDateFilter(predicates, cb, c, entry, value);
    }

    private <T extends Certificate> void determineDateFilter(List<Predicate> predicates, CriteriaBuilder cb, Root<? extends T> c, Map.Entry<String, FilterObject> entry, Object value) {
        if (!(value instanceof DateFilter)) return;
        DateFilter dateFilter = (DateFilter)value;
        Date startDate = dateFilter.getStart();
        Date endDate = dateFilter.getEnd();
        Path expression = c.get(entry.getKey());
        if (startDate != null && endDate != null) {
            predicates.add(cb.between((Expression)expression, (Comparable)startDate, (Comparable)endDate));
        } else if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo((Expression)expression, (Comparable)startDate));
        } else {
            if (endDate == null) return;
            predicates.add(cb.lessThanOrEqualTo((Expression)expression, (Comparable)endDate));
        }
    }

    private <T extends Certificate> void determineListFilter(List<Predicate> predicates, CriteriaBuilder cb, Root<? extends T> c, Map.Entry<String, FilterObject> entry, Object value) {
        FilterObject filterObject = entry.getValue();
        if (filterObject instanceof ExtendedFilterObject) {
            ExtendedFilterObject extendedFilterObject = (ExtendedFilterObject)filterObject;
            Path expression = c.get(entry.getKey());
            Predicate inList = this.getInListPredicate(cb, value, (Expression<String>)expression);
            Predicate likeLower = this.getLikeLower(cb, extendedFilterObject.getOrSomethingElse(), (Expression<String>)expression);
            Predicate likeUpper = this.getLikeUpper(cb, extendedFilterObject.getOrSomethingElse(), (Expression<String>)expression);
            predicates.add(cb.or(new Predicate[]{likeLower, likeUpper, inList}));
        } else {
            if (!(value instanceof List)) return;
            if (filterObject.isMatch()) return;
            Path expression = c.get(entry.getKey());
            Predicate inList = this.getInListPredicate(cb, value, (Expression<String>)expression);
            predicates.add(inList);
        }
    }

    private <T extends Certificate> void determineCertificateFilter(List<Predicate> predicates, CriteriaBuilder cb, Root<? extends T> c, Map.Entry<String, FilterObject> entry, Object value) {
        if (!(value instanceof Certificate)) return;
        Path expression = c.get(entry.getKey());
        predicates.add(cb.equal((Expression)expression, entry.getValue().getFilterValue()));
    }

    private <T extends Certificate> void determineCertificateTypeFilter(List<Predicate> predicates, CriteriaBuilder cb, Root<? extends T> c, Map.Entry<String, FilterObject> entry, Object value) {
        if (!(value instanceof CertificateType)) return;
        Path expression = c.get(entry.getKey());
        predicates.add(cb.equal((Expression)expression, entry.getValue().getFilterValue()));
    }

    private <T extends Certificate> void determineUserFilter(List<Predicate> predicates, CriteriaBuilder cb, Root<? extends T> c, Map.Entry<String, FilterObject> entry, Object value) {
        if (!(value instanceof User)) return;
        Path expression = c.get(entry.getKey());
        predicates.add(cb.equal((Expression)expression, entry.getValue().getFilterValue()));
    }

    public List<String> findVSMCertificates() {
        String METHOD_NAME = "findVSMCertificates";
        LOG.entering(CLASS_NAME, "findVSMCertificates");
        TypedQuery query = this.em.createNamedQuery("ECU_WITH_PK", String.class);
        LOG.exiting(CLASS_NAME, "findVSMCertificates");
        return query.getResultList();
    }

    public void updateCertificatesWithStatus(List<String> ids, String status) {
        String METHOD_NAME = "updateCertificatesWithStatus";
        LOG.entering(CLASS_NAME, "updateCertificatesWithStatus");
        Query q = this.em.createNamedQuery("UPDATE_STATUS");
        int u = q.setParameter("ids", ids).setParameter("status", (Object)status).executeUpdate();
        LOG.info("updateCertificatesWithStatus set status to: " + status + " for " + u + " entries.");
        LOG.exiting(CLASS_NAME, "updateCertificatesWithStatus");
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void updateEnrollmentId(User user, CertificateType type, String subjectKeyIdentifier, String enrollmentId) {
        Query query = this.em.createQuery("UPDATE SigModulCertificate c SET c.enrollmentId = :enrollmentId where c.user = :user and c.type = :type and c.subjectKeyIdentifier = :subjectKeyIdentifier");
        query.setParameter("user", (Object)user);
        query.setParameter("type", (Object)type);
        query.setParameter("subjectKeyIdentifier", (Object)subjectKeyIdentifier);
        query.setParameter("enrollmentId", (Object)enrollmentId);
        query.executeUpdate();
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void updateEnrollmentId(User user, String entityId, String enrollmentId) {
        Query query = this.em.createQuery("UPDATE SigModulCertificate c SET c.enrollmentId = :enrollmentId where c.user = :user and c.entityId = :entityId");
        query.setParameter("user", (Object)user);
        query.setParameter("entityId", (Object)entityId);
        query.setParameter("enrollmentId", (Object)enrollmentId);
        query.executeUpdate();
    }

    public void updatePkiKnown(String entityId, Boolean pkiKnown) {
        Query query = this.em.createQuery("UPDATE ZenZefiCertificate c SET c.pkiKnown = :pkiKnown where c.entityId = :entityId");
        query.setParameter("entityId", (Object)entityId);
        query.setParameter("pkiKnown", (Object)pkiKnown);
        query.executeUpdate();
    }

    public void updatePkiKnownForUnknownBackends(User user, List<String> pkiKnownBackCertIds) {
        Query query = this.em.createQuery("UPDATE ZenZefiCertificate c SET c.pkiKnown = false where c.user = :user and c.type = :type and c.entityId not in :entityId");
        query.setParameter("user", (Object)user);
        query.setParameter("type", (Object)CertificateType.BACKEND_CA_CERTIFICATE);
        query.setParameter("entityId", pkiKnownBackCertIds);
        query.executeUpdate();
    }

    public List<Certificate> getIdentical(Certificate certificate) {
        Query q = this.em.createNamedQuery("getPossibleIdenticals");
        q.setParameter("signature", (Object)certificate.getSignature());
        q.setParameter("subject", (Object)certificate.getSubject());
        q.setParameter("type", (Object)certificate.getType());
        q.setParameter("user", (Object)certificate.getUser());
        return q.getResultList();
    }

    public boolean isECUUnique(Certificate certificate) {
        Query q = this.em.createNamedQuery("findECUIdenticals");
        q.setParameter("ski", (Object)certificate.getSubjectKeyIdentifier());
        q.setParameter("aki", (Object)certificate.getAuthorityKeyIdentifier());
        q.setParameter("subject", (Object)certificate.getSubject());
        q.setParameter("uniqueECUID", (Object)certificate.getUniqueECUID());
        q.setParameter("certType", (Object)certificate.getType());
        q.setParameter("specialECU", (Object)certificate.getSpecialECU());
        q.setParameter("publicKey", (Object)certificate.getSubjectPublicKey());
        q.setParameter("user", (Object)certificate.getUser());
        Long identicals = (Long)q.getSingleResult();
        return identicals == 0L;
    }

    public Optional<Certificate> findParent(Certificate certificate) {
        Query q = this.em.createNamedQuery("findParentUsingAKI");
        q.setParameter("ski", (Object)certificate.getAuthorityKeyIdentifier());
        q.setParameter("user", (Object)certificate.getUser());
        List resultList = q.getResultList();
        if (!resultList.isEmpty()) return Optional.of(resultList.get(0));
        return Optional.empty();
    }

    public <T extends Certificate> List<T> findByType(Class<T> theClass, User user, CertificateType type) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user", user);
        params.put("state", CertificateState.ISSUED);
        params.put("type", type);
        return this.findWithQuery(theClass, params, -1);
    }

    public <T extends Certificate> List<T> findByTypeAndParent(Class<T> theClass, User user, CertificateType type, Certificate parent) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user", user);
        params.put("state", CertificateState.ISSUED);
        params.put("type", type);
        params.put("authorityKeyIdentifier", parent.getSubjectKeyIdentifier());
        return this.findWithQuery(theClass, params, -1);
    }

    public <T extends Certificate> List<T> findCertificatesWithTargetVinAndTargetEcu(User user, CertificateType certificateType, String backendCertSkid, String patternTargetEcu, String patternTargetVin, Class<T> type) {
        Query query = this.em.createNativeQuery("SELECT * FROM SIGMODUL.R_CERTIFICATE c WHERE c.F_USER_ID = :user AND c.F_STATE = :state AND c.F_CERTIFICATE_TYPE = :type AND c.F_AUTHORITY_KEY_IDENTIFIER = :authorityKeyIdentifier AND c.F_TARGET_ECU regexp :targetECU AND c.F_TARGET_VIN regexp :targetVIN", type);
        query.setParameter("user", (Object)user.getEntityId());
        query.setParameter("state", (Object)CertificateState.ISSUED.name());
        query.setParameter("type", (Object)certificateType.name());
        query.setParameter("authorityKeyIdentifier", (Object)backendCertSkid);
        query.setParameter("targetECU", (Object)patternTargetEcu);
        query.setParameter("targetVIN", (Object)patternTargetVin);
        return query.getResultList();
    }

    public <T> List<T> findCSRsWithTargetVinAndTargetEcu(User user, CertificateType csrType, String backendSubjectKeyIdentifier, String patternTargetEcu, String patternTargetVin, Class<T> type) {
        Query query = this.em.createNativeQuery("SELECT * FROM SIGMODUL.R_CERTIFICATE c WHERE c.F_USER_ID = :user AND c.F_STATE = :state AND c.F_CERTIFICATE_TYPE = :type AND c.F_AUTHORITY_KEY_IDENTIFIER = :authorityKeyIdentifier AND c.F_TARGET_ECU regexp :targetECU AND c.F_TARGET_VIN regexp :targetVIN", type);
        query.setParameter("user", (Object)user.getEntityId());
        query.setParameter("state", (Object)CertificateState.SIGNING_REQUEST.name());
        query.setParameter("type", (Object)csrType.name());
        query.setParameter("authorityKeyIdentifier", (Object)backendSubjectKeyIdentifier);
        query.setParameter("targetECU", (Object)patternTargetEcu);
        query.setParameter("targetVIN", (Object)patternTargetVin);
        return query.getResultList();
    }
}
