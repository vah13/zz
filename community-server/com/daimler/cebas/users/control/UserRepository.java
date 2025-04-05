/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.AbstractRepository
 *  com.daimler.cebas.common.control.annotations.CEBASRepository
 *  com.daimler.cebas.users.entity.User
 *  javax.persistence.TypedQuery
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 */
package com.daimler.cebas.users.control;

import com.daimler.cebas.common.control.AbstractRepository;
import com.daimler.cebas.common.control.annotations.CEBASRepository;
import com.daimler.cebas.users.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import javax.persistence.TypedQuery;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@CEBASRepository
public class UserRepository
extends AbstractRepository {
    private static final String CLASS_NAME = UserRepository.class.getName();
    private static final Logger LOG = Logger.getLogger(UserRepository.class.getName());

    @Transactional(propagation=Propagation.REQUIRED)
    public User findUserById(String userId) {
        String METHOD_NAME = "findUserById";
        LOG.entering(CLASS_NAME, "findUserById");
        LOG.exiting(CLASS_NAME, "findUserById");
        return (User)this.em.find(User.class, (Object)userId);
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public Optional<User> findUserByName(String userName) {
        String METHOD_NAME = "findUserByName";
        LOG.entering(CLASS_NAME, "findUserByName");
        TypedQuery query = this.em.createNamedQuery("findUserByName", User.class);
        query.setParameter("userName", (Object)userName);
        List resultList = query.getResultList();
        User user = null;
        if (!resultList.isEmpty()) {
            user = (User)resultList.get(0);
        }
        LOG.exiting(CLASS_NAME, "findUserByName");
        return Optional.ofNullable(user);
    }

    public List<User> findUsersWithDeleteExpiredCertsTrue() {
        String METHOD_NAME = "findUsersWithDeleteExpiredCertsTrue";
        LOG.entering(CLASS_NAME, "findUsersWithDeleteExpiredCertsTrue");
        TypedQuery query = this.em.createNamedQuery("findUserByDeleteExpiredCerts", User.class);
        LOG.exiting(CLASS_NAME, "findUsersWithDeleteExpiredCertsTrue");
        return query.getResultList();
    }
}
