/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.GenerateSecureRandom
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.configuration.control.AbstractConfigurator
 *  com.daimler.cebas.configuration.entity.Configuration
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.crypto.UserCryptoEngine
 *  com.daimler.cebas.users.control.exceptions.UserException
 *  com.daimler.cebas.users.entity.User
 *  com.daimler.cebas.users.entity.UserRole
 *  org.springframework.beans.factory.annotation.Autowired
 */
package com.daimler.cebas.users.control.factories;

import com.daimler.cebas.common.control.GenerateSecureRandom;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.configuration.control.AbstractConfigurator;
import com.daimler.cebas.configuration.entity.Configuration;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.crypto.UserCryptoEngine;
import com.daimler.cebas.users.control.exceptions.UserException;
import com.daimler.cebas.users.entity.User;
import com.daimler.cebas.users.entity.UserRole;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Autowired;

@CEBASControl
public class UserFactory {
    private static final String CLASS_NAME = UserFactory.class.getSimpleName();
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(CLASS_NAME);
    private static String defaultUserName;
    public static final int SECURE_RANDOM_BYTE_LENGTH = 36;
    @Autowired
    private Logger logger;
    @Autowired
    private AbstractConfigurator configurator;
    private UserCryptoEngine cryptoEngine;

    @Autowired
    public UserFactory(UserCryptoEngine cryptoEngine) {
        this.cryptoEngine = cryptoEngine;
    }

    public <T extends User> T getDefaultUser(Class<T> type) {
        String METHOD_NAME = "getDefaultUser";
        this.logger.entering(CLASS_NAME, "getDefaultUser");
        Configuration machineNameConfig = this.configurator.getMachineNameConfiguration();
        String secret = this.configurator.getSecret();
        String salt = GenerateSecureRandom.generateSecureNumber((int)36) + machineNameConfig.getConfigValue();
        String password = GenerateSecureRandom.generateSecureNumber((int)36);
        String encryptedPassword = this.cryptoEngine.getAESEncryptedUserPassword(secret, password);
        String encryptedSalt = this.cryptoEngine.getAESEncryptedSalt(secret, salt);
        try {
            User defaultUser = (User)type.newInstance();
            defaultUser.setUserName(defaultUserName);
            defaultUser.setRole(UserRole.DEFAULT);
            defaultUser.setUserPassword(encryptedPassword);
            defaultUser.setSalt(encryptedSalt);
            defaultUser.getConfigurations().addAll(this.configurator.getDefaultUserProperties());
            String machineName = machineNameConfig.getConfigValue();
            String containerKeyDefaultUser = this.cryptoEngine.generateContainerKey(salt, password, defaultUser);
            String machineNameEncoded = this.cryptoEngine.encryptMachineName(containerKeyDefaultUser, machineName);
            machineNameConfig.setConfigValue(machineNameEncoded);
            defaultUser.getConfigurations().add(machineNameConfig);
            this.logger.exiting(CLASS_NAME, "getDefaultUser");
            return (T)defaultUser;
        }
        catch (IllegalAccessException | InstantiationException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new UserException(e.getMessage());
        }
    }

    public static void setDefaultUsername(String defaultUser) {
        defaultUserName = defaultUser;
    }

    public static String getDefaultUsername() {
        return defaultUserName;
    }
}
