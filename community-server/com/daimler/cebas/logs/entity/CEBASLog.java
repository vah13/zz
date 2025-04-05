/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.logs.control.LogCryptoEngine
 *  com.daimler.cebas.logs.entity.LogsViews$Donwload
 *  com.daimler.cebas.logs.entity.LogsViews$Management
 *  com.daimler.cebas.logs.entity.LogsViews$Persisted
 *  com.fasterxml.jackson.annotation.JsonView
 */
package com.daimler.cebas.logs.entity;

import com.daimler.cebas.logs.control.LogCryptoEngine;
import com.daimler.cebas.logs.entity.LogsViews;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="log")
public class CEBASLog {
    @XmlTransient
    @JsonView(value={LogsViews.Persisted.class, LogsViews.Management.class})
    private long id;
    @JsonView(value={LogsViews.Donwload.class})
    private String createUser;
    @JsonView(value={LogsViews.Donwload.class, LogsViews.Persisted.class, LogsViews.Management.class})
    private String type;
    @JsonView(value={LogsViews.Donwload.class, LogsViews.Persisted.class, LogsViews.Management.class})
    private String message;
    @JsonView(value={LogsViews.Donwload.class, LogsViews.Persisted.class})
    private String checksum;
    @JsonView(value={LogsViews.Donwload.class, LogsViews.Persisted.class, LogsViews.Management.class})
    private long createTimestamp;
    @JsonView(value={LogsViews.Donwload.class, LogsViews.Management.class})
    private boolean valid;

    public CEBASLog() {
    }

    public CEBASLog(String createUser, String type, String message) {
        this.createUser = createUser;
        this.type = type;
        this.message = message;
        this.createTimestamp = new Date().getTime();
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChecksum() {
        return this.checksum;
    }

    public boolean isValid() {
        return this.valid;
    }

    public void setValid(boolean isValid) {
        this.valid = isValid;
    }

    @XmlElement
    public long getCreateTimestamp() {
        return this.createTimestamp;
    }

    public void setId(long id) {
        this.id = id;
        this.checksum = LogCryptoEngine.hashWithSHA512((String)(this.id + this.createTimestamp + this.type + this.message));
    }

    public long getId() {
        return this.id;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public boolean validateLogChecksum() {
        return this.getChecksum().equals(LogCryptoEngine.hashWithSHA512((String)(this.id + this.createTimestamp + this.type + this.getMessage())));
    }

    public String toString() {
        return "CEBASLog [id=" + this.id + ", createUser=" + this.createUser + ", type=" + this.type + ", message=" + this.message + ", checksum=" + this.checksum + ", createTimestamp=" + this.createTimestamp + ", valid=" + this.valid + "]";
    }
}
