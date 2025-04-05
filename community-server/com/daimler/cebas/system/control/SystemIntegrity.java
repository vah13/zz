/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.common.control.HostUtil
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.configuration.control.AbstractConfigurator
 *  com.daimler.cebas.configuration.entity.Configuration
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  com.daimler.cebas.users.control.crypto.UserCryptoEngine
 *  com.daimler.cebas.users.entity.User
 */
package com.daimler.cebas.system.control;

import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.common.control.HostUtil;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.configuration.control.AbstractConfigurator;
import com.daimler.cebas.configuration.entity.Configuration;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import com.daimler.cebas.users.control.crypto.UserCryptoEngine;
import com.daimler.cebas.users.entity.User;
import java.util.Optional;
import java.util.logging.Level;

public class SystemIntegrity {
    private static final String CLASS_NAME = SystemIntegrity.class.getSimpleName();

    private SystemIntegrity() {
    }

    public static void checkSystemIntegrity(UserCryptoEngine cryptoEngine, AbstractConfigurator configurator, Logger logger, Session session, MetadataManager i18n) {
        String METHOD_NAME = "checkSystemIntegrity";
        logger.entering(CLASS_NAME, "checkSystemIntegrity");
        User user = session.getDefaultUser();
        String secret = configurator.getSecret();
        String decryptedPassword = cryptoEngine.getAESDecryptedUserPassword(secret, user);
        String decryptedSalt = cryptoEngine.getAESDecryptedSalt(secret, user);
        String containerKeyDefaultUser = cryptoEngine.generateContainerKey(decryptedSalt, decryptedPassword, user);
        Optional<Configuration> machineName = user.getConfigurations().stream().filter(config -> config.getConfigKey().equals("MACHINE_NAME")).findFirst();
        CEBASException ex = new CEBASException(i18n.getMessage("systemIntegrityCheckFailedNoMachineNameConfig"), "systemIntegrityCheckFailedNoMachineNameConfig");
        Configuration machineNameConfig = machineName.orElseThrow(logger.logWithTranslationSupplier(Level.SEVERE, "000003X", ex));
        String currentMachineName = HostUtil.getMachineName((Logger)logger, (MetadataManager)i18n);
        String machineNameEncoded = machineNameConfig.getConfigValue();
        String machineNameDecoded = cryptoEngine.decryptMachineName(containerKeyDefaultUser, machineNameEncoded);
        if (!currentMachineName.equals(machineNameDecoded)) {
            CEBASException exception = new CEBASException(i18n.getEnglishMessage("systemIntegrityCheckFailedWrongMachineName"), "systemIntegrityCheckFailedWrongMachineName");
            logger.logWithTranslation(Level.SEVERE, "000086X", exception.getMessageId(), exception.getClass().getSimpleName());
            throw exception;
        }
        logger.exiting(CLASS_NAME, "checkSystemIntegrity");
    }
}
