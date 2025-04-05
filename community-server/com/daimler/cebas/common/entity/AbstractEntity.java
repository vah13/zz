/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.users.control.Session
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  io.swagger.annotations.ApiModelProperty
 *  javax.persistence.Column
 *  javax.persistence.Id
 *  javax.persistence.MappedSuperclass
 *  javax.persistence.PrePersist
 *  javax.persistence.PreUpdate
 *  javax.persistence.Temporal
 *  javax.persistence.TemporalType
 */
package com.daimler.cebas.common.entity;

import com.daimler.cebas.users.control.Session;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@MappedSuperclass
public abstract class AbstractEntity
implements Serializable {
    public static final String F_ID = "f_id";
    public static final String F_CREATE_USER = "f_create_user";
    public static final String F_UPDATE_USER = "f_update_user";
    public static final String F_CREATE_TIMESTAMP = "f_create_timestamp";
    public static final String F_UPDATE_TIMESTAMP = "f_update_timestamp";
    public static final String ID = "id";
    public static final String ENTITY_ID = "entityId";
    public static final String CREATE_USER = "createUser";
    public static final String UPDATE_USER = "updateUser";
    public static final String CREATE_TIMESTAMP = "createTimestamp";
    public static final String UPDATE_TIMESTAMP = "updateTimestamp";
    public static final int ID_LENGTH = 36;
    public static final int USER_LENGTH = 50;
    protected static final String PACKAGE_NAME = "com.daimler.cebas.zenzefi.common";
    private static final long serialVersionUID = -2274627895272084434L;
    @Id
    @Column(name="f_id", unique=true, length=36)
    private String entityId;
    @Column(name="f_create_user", length=50)
    @JsonIgnore
    private String createUser;
    @Column(name="f_create_timestamp")
    @Temporal(value=TemporalType.TIMESTAMP)
    @ApiModelProperty(dataType="java.lang.String")
    private Date createTimestamp;
    @Column(name="f_update_user", length=50)
    @JsonIgnore
    private String updateUser;
    @Column(name="f_update_timestamp")
    @Temporal(value=TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date updateTimestamp;

    @PrePersist
    public void onPrePersist() {
        this.entityId = UUID.randomUUID().toString();
        this.createTimestamp = new Date();
        this.createUser = Session.getUserId();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updateTimestamp = new Date();
        this.updateUser = Session.getUserId();
    }

    @JsonProperty(value="id")
    public String getEntityId() {
        return this.entityId;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return this.updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTimestamp() {
        return this.updateTimestamp;
    }

    public Date getCreateTimestamp() {
        return this.createTimestamp;
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.getEntityId()).toHashCode();
    }

    public boolean equals(Object obj) {
        boolean result = obj != null && obj.getClass() == this.getClass() && obj == this;
        return result && new EqualsBuilder().append(this.entityId, ((AbstractEntity)obj).getEntityId()).isEquals();
    }
}
