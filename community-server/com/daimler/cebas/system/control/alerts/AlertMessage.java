/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.system.control.websocket.WebsocketAbstractController
 *  org.springframework.util.CollectionUtils
 */
package com.daimler.cebas.system.control.alerts;

import com.daimler.cebas.system.control.websocket.WebsocketAbstractController;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import org.springframework.util.CollectionUtils;

public class AlertMessage {
    private static Queue<String> MESSAGE_IDS;
    private static Set<String> DELETED_MESSAGE_IDS;

    public static Queue<String> getMessageIds() {
        if (null != MESSAGE_IDS) return MESSAGE_IDS;
        MESSAGE_IDS = AlertMessage.getArrayDeque();
        return MESSAGE_IDS;
    }

    public static void addMessageId(String messageId) {
        boolean added;
        if (null == MESSAGE_IDS) {
            MESSAGE_IDS = AlertMessage.getArrayDeque();
        }
        if (!(added = MESSAGE_IDS.add(messageId))) return;
        AlertMessage.sendMessageIdsViaWebSocket();
    }

    public static void removeFirstMessageId() {
        if (CollectionUtils.isEmpty(MESSAGE_IDS)) return;
        String deletedMessageId = MESSAGE_IDS.remove();
        DELETED_MESSAGE_IDS.add(deletedMessageId);
        AlertMessage.sendMessageIdsViaWebSocket();
    }

    private static void sendMessageIdsViaWebSocket() {
        WebsocketAbstractController webSocketInstance = WebsocketAbstractController.getInstance();
        if (null == webSocketInstance) return;
        webSocketInstance.updateAlertMessages();
    }

    private static Set<String> getDeletedMessageIds() {
        if (null != DELETED_MESSAGE_IDS) return DELETED_MESSAGE_IDS;
        DELETED_MESSAGE_IDS = new HashSet<String>();
        return DELETED_MESSAGE_IDS;
    }

    private static Queue<String> getArrayDeque() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    static /* synthetic */ Set access$000() {
        return AlertMessage.getDeletedMessageIds();
    }
}
