/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.annotations.CEBASRepository
 *  com.daimler.cebas.common.entity.AbstractEntity
 *  com.daimler.cebas.common.entity.EmptyPredicate
 *  com.daimler.cebas.common.entity.InPredicate
 *  javax.persistence.EntityManager
 *  javax.persistence.PersistenceContext
 *  javax.persistence.Query
 *  javax.persistence.TypedQuery
 *  javax.persistence.criteria.CriteriaBuilder
 *  javax.persistence.criteria.CriteriaDelete
 *  javax.persistence.criteria.CriteriaQuery
 *  javax.persistence.criteria.Expression
 *  javax.persistence.criteria.Order
 *  javax.persistence.criteria.Path
 *  javax.persistence.criteria.Predicate
 *  javax.persistence.criteria.Root
 *  javax.persistence.criteria.Selection
 *  org.springframework.data.domain.Pageable
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 */
package com.daimler.cebas.common.control;

import com.daimler.cebas.common.control.annotations.CEBASRepository;
import com.daimler.cebas.common.entity.AbstractEntity;
import com.daimler.cebas.common.entity.EmptyPredicate;
import com.daimler.cebas.common.entity.InPredicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@CEBASRepository
public abstract class AbstractRepository {
    private static final String CLASS_NAME = AbstractRepository.class.getName();
    private static final Logger LOG = Logger.getLogger(AbstractRepository.class.getName());
    @PersistenceContext
    protected EntityManager em;

    public <T extends AbstractEntity> List<T> findAll(Class<T> type) {
        String METHOD_NAME = "findAll";
        LOG.entering(CLASS_NAME, "findAll");
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery createQuery = builder.createQuery(type);
        Root root = createQuery.from(type);
        CriteriaQuery selectAll = createQuery.select((Selection)root);
        TypedQuery query = this.em.createQuery(selectAll);
        LOG.exiting(CLASS_NAME, "findAll");
        return query.getResultList();
    }

    public <T extends AbstractEntity> Optional<T> find(Class<T> type, String id) {
        String METHOD_NAME = "find";
        LOG.entering(CLASS_NAME, "find");
        LOG.exiting(CLASS_NAME, "find");
        return Optional.ofNullable(this.em.find(type, (Object)id));
    }

    public <T extends AbstractEntity> List<T> findMultiple(Class<T> type, List<String> idList) {
        String METHOD_NAME = "findMultiple";
        LOG.entering(CLASS_NAME, "findMultiple");
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = builder.createQuery(type);
        Root root = criteriaQuery.from(type);
        Path expresion = root.get("entityId");
        CriteriaQuery select = criteriaQuery.where((Expression)expresion.in(idList));
        TypedQuery query = this.em.createQuery(select);
        List resultList = query.getResultList();
        LOG.exiting(CLASS_NAME, "findMultiple");
        return resultList;
    }

    public <T extends AbstractEntity> void delete(Class<T> type, String id) {
        String METHOD_NAME = "delete";
        LOG.entering(CLASS_NAME, "delete");
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaDelete createCriteriaDelete = builder.createCriteriaDelete(type);
        Root e = createCriteriaDelete.from(type);
        createCriteriaDelete.where((Expression)builder.equal((Expression)e.get("entityId"), (Object)id));
        this.em.createQuery(createCriteriaDelete).executeUpdate();
        LOG.exiting(CLASS_NAME, "delete");
    }

    public <T extends AbstractEntity> List<T> findWithNamedQuery(String namedQueryName, Map<String, Object> parameters, int resultLimit) {
        String METHOD_NAME = "findWithNamedQuery";
        LOG.entering(CLASS_NAME, "findWithNamedQuery");
        Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
        Query query = this.em.createNamedQuery(namedQueryName);
        if (resultLimit > 0) {
            query.setMaxResults(resultLimit);
        }
        Iterator<Map.Entry<String, Object>> iterator = rawParameters.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                LOG.exiting(CLASS_NAME, "findWithNamedQuery");
                return query.getResultList();
            }
            Map.Entry<String, Object> entry = iterator.next();
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }

    @Transactional(propagation=Propagation.SUPPORTS)
    public <T extends AbstractEntity> List<T> findWithQuery(Class<T> type, Map<String, Object> parameters, int resultLimit) {
        return this.findWithPageableQuery(type, parameters, resultLimit, null);
    }

    @Transactional(propagation=Propagation.SUPPORTS)
    public <T extends AbstractEntity> List<T> findWithPageableQuery(Class<T> type, Map<String, Object> parameters, int resultLimit, Pageable pageable) {
        String METHOD_NAME = "findWithQuery";
        LOG.entering(CLASS_NAME, "findWithQuery");
        TypedQuery<T> typedQuery = this.getTypedQuery(type, parameters);
        if (pageable != null) {
            typedQuery.setFirstResult((int)pageable.getOffset());
            typedQuery.setMaxResults(pageable.getPageSize());
        } else if (resultLimit > 0) {
            typedQuery.setMaxResults(resultLimit);
        }
        LOG.exiting(CLASS_NAME, "findWithQuery");
        return typedQuery.getResultList();
    }

    @Transactional(propagation=Propagation.SUPPORTS)
    public <T extends AbstractEntity> List<T> findWithQuery(Class<T> type, Map<String, Object> parameters, int pageSize, int pageNumber) {
        String METHOD_NAME = "findWithQuery";
        LOG.entering(CLASS_NAME, "findWithQuery");
        TypedQuery<T> typedQuery = this.getTypedQuery(type, parameters);
        typedQuery.setFirstResult(pageNumber);
        typedQuery.setMaxResults(pageSize);
        LOG.exiting(CLASS_NAME, "findWithQuery");
        return typedQuery.getResultList();
    }

    public <T extends AbstractEntity> TypedQuery<T> getTypedQuery(Class<T> type, Map<String, Object> parameters) {
        Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery createQuery = builder.createQuery(type);
        Root root = createQuery.from(type);
        ArrayList<Predicate> predicates = new ArrayList<Predicate>();
        Iterator<Map.Entry<String, Object>> iterator = rawParameters.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                CriteriaQuery query = createQuery.select((Selection)root).where(predicates.toArray(new Predicate[predicates.size()])).orderBy(new Order[]{builder.asc((Expression)root.get("createTimestamp"))});
                return this.em.createQuery(query);
            }
            Map.Entry<String, Object> entry = iterator.next();
            if (entry.getValue() instanceof String) {
                Predicate like = builder.like((Expression)root.get(entry.getKey()), (String)entry.getValue());
                predicates.add(like);
                continue;
            }
            if (entry.getValue() instanceof EmptyPredicate) {
                if (((EmptyPredicate)entry.getValue()).canBeEmpty()) {
                    predicates.add(builder.equal((Expression)root.get(entry.getKey()), (Object)""));
                    continue;
                }
                predicates.add(builder.notEqual((Expression)root.get(entry.getKey()), (Object)""));
                continue;
            }
            if (entry.getValue() instanceof InPredicate) {
                Predicate in = root.get(entry.getKey()).in((Collection)((InPredicate)entry.getValue()).getPredicates());
                predicates.add(in);
                continue;
            }
            Predicate equals = builder.equal((Expression)root.get(entry.getKey()), entry.getValue());
            predicates.add(equals);
        }
    }

    @Transactional(propagation=Propagation.SUPPORTS)
    public <T extends AbstractEntity> Number countWithQuery(Class<T> type, Map<String, Object> parameters) {
        String METHOD_NAME = "countWithQuery";
        LOG.entering(CLASS_NAME, "countWithQuery");
        Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery createQuery = builder.createQuery(Long.class);
        Root root = createQuery.from(type);
        ArrayList<Predicate> predicates = new ArrayList<Predicate>();
        Iterator<Map.Entry<String, Object>> iterator = rawParameters.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                Path expression = root.get("entityId");
                CriteriaQuery query = createQuery.select((Selection)builder.count((Expression)expression)).where(predicates.toArray(new Predicate[predicates.size()]));
                LOG.exiting(CLASS_NAME, "countWithQuery");
                return (Number)this.em.createQuery(query).getSingleResult();
            }
            Map.Entry<String, Object> entry = iterator.next();
            Predicate equals = builder.equal((Expression)root.get(entry.getKey()), entry.getValue());
            predicates.add(equals);
        }
    }

    @Transactional(propagation=Propagation.SUPPORTS)
    public <T extends AbstractEntity> void detach(T type) {
        String METHOD_NAME = "detach";
        LOG.entering(CLASS_NAME, "detach");
        this.em.detach(type);
        LOG.exiting(CLASS_NAME, "detach");
    }

    public <T extends AbstractEntity> T update(T type) {
        String METHOD_NAME = "update";
        LOG.entering(CLASS_NAME, "update");
        AbstractEntity updatedT = (AbstractEntity)this.em.merge(type);
        LOG.exiting(CLASS_NAME, "update");
        return (T)updatedT;
    }

    public <T extends AbstractEntity> void refresh(T type) {
        String METHOD_NAME = "refresh";
        LOG.entering(CLASS_NAME, "refresh");
        this.em.refresh(type);
        LOG.exiting(CLASS_NAME, "refresh");
    }

    public <T extends AbstractEntity> void deleteUnManagedEntity(T type) {
        String METHOD_NAME = "deleteUnManagedEntity";
        LOG.entering(CLASS_NAME, "deleteUnManagedEntity");
        Object ref = this.em.getReference(type.getClass(), (Object)type.getEntityId());
        this.em.remove(ref);
        LOG.exiting(CLASS_NAME, "deleteUnManagedEntity");
    }

    public <T extends AbstractEntity> void deleteManagedEntity(T entity) {
        String METHOD_NAME = "deleteManagedEntity";
        LOG.entering(CLASS_NAME, "deleteManagedEntity");
        this.em.remove(entity);
        LOG.exiting(CLASS_NAME, "deleteManagedEntity");
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public <T extends AbstractEntity> T create(T type) {
        String METHOD_NAME = "create";
        LOG.entering(CLASS_NAME, "create");
        this.em.persist(type);
        this.em.flush();
        this.em.refresh(type);
        LOG.exiting(CLASS_NAME, "create");
        return type;
    }

    public void clearContext() {
        String METHOD_NAME = "clearContext";
        LOG.entering(CLASS_NAME, "clearContext");
        LOG.exiting(CLASS_NAME, "clearContext");
        this.em.clear();
    }

    public void flush() {
        this.em.flush();
    }
}
