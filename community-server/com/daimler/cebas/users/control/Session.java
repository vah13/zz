/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.validation.SystemIntegrityCheckResult
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.common.control.StartupStatus
 *  com.daimler.cebas.common.control.annotations.CEBASSession
 *  com.daimler.cebas.common.entity.AbstractEntity
 *  com.daimler.cebas.configuration.control.AbstractConfigurator
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.security.EncryptedString
 *  com.daimler.cebas.system.control.startup.CeBASStartupProperty
 *  com.daimler.cebas.users.control.UserRepository
 *  com.daimler.cebas.users.control.crypto.UserCryptoEngine
 *  com.daimler.cebas.users.control.exceptions.ApplicationNotStartedException
 *  com.daimler.cebas.users.control.exceptions.UserException
 *  com.daimler.cebas.users.control.factories.UserFactory
 *  com.daimler.cebas.users.entity.User
 *  com.daimler.cebas.users.entity.UserKeyPair
 *  javax.annotation.PostConstruct
 *  org.jboss.logging.MDC
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.core.env.Environment
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 */
package com.daimler.cebas.users.control;

import com.daimler.cebas.certificates.control.validation.SystemIntegrityCheckResult;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.common.control.StartupStatus;
import com.daimler.cebas.common.control.annotations.CEBASSession;
import com.daimler.cebas.common.entity.AbstractEntity;
import com.daimler.cebas.configuration.control.AbstractConfigurator;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.security.EncryptedString;
import com.daimler.cebas.system.control.startup.CeBASStartupProperty;
import com.daimler.cebas.users.control.UserRepository;
import com.daimler.cebas.users.control.crypto.UserCryptoEngine;
import com.daimler.cebas.users.control.exceptions.ApplicationNotStartedException;
import com.daimler.cebas.users.control.exceptions.UserException;
import com.daimler.cebas.users.control.factories.UserFactory;
import com.daimler.cebas.users.entity.User;
import com.daimler.cebas.users.entity.UserKeyPair;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@CEBASSession
public class Session {
    private static final String CLASS_NAME = Session.class.getName();
    private static final String MDC_USERID = "userId";
    @Autowired
    private Logger logger;
    public static final String NAME = "Session";
    @Autowired
    private Environment env;
    private String defaultUser;
    private String currentUser;
    private String currentWindowsUser;
    private User backendAuthenticatedUser;
    private String token;
    private Date tokenExpirationDate;
    private boolean onlineLogout;
    private boolean checkLocalPassword;
    private String containerKey;
    @Autowired
    private UserFactory userFactory;
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserCryptoEngine cryptoEngine;
    @Autowired
    private AbstractConfigurator configurator;
    private String secret;
    private EncryptedString refreshToken;
    private SystemIntegrityCheckResult systemIntegrityCheckResult;
    private static String userName = UserFactory.getDefaultUsername();
    private boolean transitionValid;

    @PostConstruct
    private void init() {
        this.secret = this.configurator.readProperty(CeBASStartupProperty.SECRET.getProperty());
        this.systemIntegrityCheckResult = new SystemIntegrityCheckResult();
        this.refreshToken = new EncryptedString();
    }

    @Transactional(propagation=Propagation.MANDATORY)
    public boolean userExists(String userName) {
        String METHOD_NAME = "userExists";
        this.logger.entering(CLASS_NAME, "userExists");
        Optional optional = this.repository.findUserByName(userName);
        boolean exists = optional.isPresent();
        this.logger.exiting(CLASS_NAME, "userExists");
        return exists;
    }

    @Transactional(propagation=Propagation.MANDATORY)
    public <T extends User> T createDefaultUser(Class<T> type) {
        String METHOD_NAME = "createDefaultUser";
        this.logger.entering(CLASS_NAME, "createDefaultUser");
        this.logger.exiting(CLASS_NAME, "createDefaultUser");
        return (T)((User)this.repository.create((AbstractEntity)this.userFactory.getDefaultUser(type)));
    }

    public synchronized void setDefaultUser() {
        User user;
        String METHOD_NAME = "setDefaultUser";
        this.logger.entering(CLASS_NAME, "setDefaultUser");
        if (this.defaultUser == null) {
            Optional defaultUserOptional = this.repository.findUserByName(UserFactory.getDefaultUsername());
            if (!defaultUserOptional.isPresent()) throw new CEBASException("Default user is not yet initialized");
            user = (User)defaultUserOptional.get();
        } else {
            user = this.repository.findUserById(this.defaultUser);
        }
        this.currentUser = this.defaultUser;
        Session.setUserId(UserFactory.getDefaultUsername());
        this.updateMdc();
        this.containerKey = this.cryptoEngine.generateContainerKey(this.cryptoEngine.getAESDecryptedSalt(this.secret, user), this.cryptoEngine.getAESDecryptedUserPassword(this.secret, user), user);
        this.logger.exiting(CLASS_NAME, "setDefaultUser");
    }

    public synchronized User getDefaultUser() {
        User user = this.repository.findUserByName(UserFactory.getDefaultUsername()).orElse(null);
        this.defaultUser = user != null ? user.getEntityId() : null;
        return user;
    }

    public synchronized void setCurrentUser(User user, String passwordContainerKey) {
        String METHOD_NAME = "setCurrentUser";
        this.logger.entering(CLASS_NAME, "setCurrentUser");
        this.currentUser = user.getEntityId();
        Session.setUserId(user.getUserName());
        this.updateMdc();
        this.containerKey = this.cryptoEngine.generateContainerKey(this.cryptoEngine.getAESDecryptedSalt(this.secret, user), new String(Base64.getDecoder().decode(passwordContainerKey), StandardCharsets.UTF_8), user);
        this.logger.exiting(CLASS_NAME, "setCurrentUser");
    }

    public synchronized String getContainerKey() {
        String METHOD_NAME = "getContainerKey";
        this.logger.entering(CLASS_NAME, "getContainerKey");
        this.logger.exiting(CLASS_NAME, "getContainerKey");
        return this.containerKey;
    }

    public synchronized User getCurrentUser() {
        User user;
        if (!StartupStatus.isApplicationStarted()) {
            throw new ApplicationNotStartedException("Application is started up yet - can not get the current user.");
        }
        if (this.currentUser == null) {
            Optional findUserByName = this.repository.findUserByName(UserFactory.getDefaultUsername());
            if (!findUserByName.isPresent()) throw new UserException("Error getting current user.");
            user = (User)findUserByName.get();
        } else {
            user = this.repository.findUserById(this.currentUser);
        }
        return user;
    }

    public synchronized String getCurrentWindowsUser() {
        return this.currentWindowsUser;
    }

    public synchronized void setCurrentWindowsUser(String windowsUser) {
        this.currentWindowsUser = windowsUser;
    }

    public List<User> getAllUsers() {
        String METHOD_NAME = "getAllUsers";
        this.logger.entering(CLASS_NAME, "getAllUsers");
        this.logger.exiting(CLASS_NAME, "getAllUsers");
        return this.repository.findAll(User.class);
    }

    public synchronized boolean isDefaultUser() {
        String METHOD_NAME = "isDefaultUser";
        this.logger.entering(CLASS_NAME, "isDefaultUser");
        this.logger.exiting(CLASS_NAME, "isDefaultUser");
        return StringUtils.equals(this.currentUser, this.defaultUser) || StringUtils.equals(userName, UserFactory.getDefaultUsername());
    }

    public UserCryptoEngine getCryptoEngine() {
        String METHOD_NAME = "getCryptoEngine";
        this.logger.entering(CLASS_NAME, "getCryptoEngine");
        this.logger.exiting(CLASS_NAME, "getCryptoEngine");
        return this.cryptoEngine;
    }

    public SystemIntegrityCheckResult getSystemIntegrityCheckResult() {
        String METHOD_NAME = "getSystemIntegrityCheckResult";
        this.logger.entering(CLASS_NAME, "getSystemIntegrityCheckResult");
        this.logger.exiting(CLASS_NAME, "getSystemIntegrityCheckResult");
        return this.systemIntegrityCheckResult;
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public Optional<UserKeyPair> getCorrelatedKeyPair(Certificate certificate) {
        HashMap<String, Certificate> map = new HashMap<String, Certificate>();
        map.put("certificate", certificate);
        List findWithNamedQuery = this.repository.findWithNamedQuery("findUserKeyPairByCertificate", map, -1);
        return findWithNamedQuery.stream().findFirst();
    }

    public synchronized User getBackendAuthenticatedUser() {
        return this.backendAuthenticatedUser;
    }

    public synchronized void setBackendAuthenticatedUser(User backendAuthenticatedUser) {
        this.backendAuthenticatedUser = backendAuthenticatedUser;
    }

    public synchronized String getToken() {
        return this.token;
    }

    public synchronized void setToken(String token) {
        this.token = token;
    }

    public synchronized Date getTokenExpirationDate() {
        return this.tokenExpirationDate;
    }

    public synchronized void setTokenExpirationDate(Date tokenExpirationDate) {
        this.tokenExpirationDate = tokenExpirationDate;
    }

    public synchronized Boolean isOnlineLogout() {
        return this.onlineLogout;
    }

    public synchronized void setOnlineLogout(boolean onlineLogout) {
        this.onlineLogout = onlineLogout;
    }

    public synchronized boolean isCheckLocalPassword() {
        return this.checkLocalPassword;
    }

    public synchronized void setCheckLocalPassword(boolean checkLocalPassword) {
        this.checkLocalPassword = checkLocalPassword;
    }

    public static synchronized String getUserId() {
        return userName;
    }

    private static synchronized void setUserId(String userId) {
        userName = userId;
    }

    public synchronized void setTransitionValid(boolean transitionValid) {
        this.transitionValid = transitionValid;
    }

    public synchronized boolean isTransitionValid() {
        return this.transitionValid;
    }

    public String getRefreshToken() {
        return this.refreshToken.getValue();
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken.setValue(refreshToken);
    }

    public String[] activeProfiles() {
        return this.env.getActiveProfiles();
    }

    public void removeUserFromMDC() {
        MDC.remove((String)MDC_USERID);
    }

    public void updateMdc() {
        this.setUserToMdc(false);
    }

    public void initMdc() {
        this.setUserToMdc(true);
    }

    private void setUserToMdc(boolean create) {
        Object userId = MDC.get((String)MDC_USERID);
        if (!create) {
            if (userId == null) return;
            if (!StringUtils.isNotBlank((String)userId)) return;
        }
        if (this.isDefaultUser()) {
            MDC.put((String)MDC_USERID, (Object)"Default User");
        } else {
            MDC.put((String)MDC_USERID, (Object)Session.getUserId());
        }
    }
}
