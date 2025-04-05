/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.common.control.annotations.SafeString
 *  com.daimler.cebas.common.entity.AbstractEntity
 *  com.daimler.cebas.configuration.entity.Configuration
 *  com.daimler.cebas.users.entity.UserKeyPair
 *  com.daimler.cebas.users.entity.UserRole
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.persistence.CascadeType
 *  javax.persistence.Column
 *  javax.persistence.DiscriminatorValue
 *  javax.persistence.Entity
 *  javax.persistence.Inheritance
 *  javax.persistence.InheritanceType
 *  javax.persistence.JoinTable
 *  javax.persistence.NamedQueries
 *  javax.persistence.NamedQuery
 *  javax.persistence.OneToMany
 *  javax.persistence.Table
 */
package com.daimler.cebas.users.entity;

import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.common.control.annotations.SafeString;
import com.daimler.cebas.common.entity.AbstractEntity;
import com.daimler.cebas.configuration.entity.Configuration;
import com.daimler.cebas.users.entity.UserKeyPair;
import com.daimler.cebas.users.entity.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="r_user")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="User")
@NamedQueries(value={@NamedQuery(name="findUserByName", query="Select u from User u where upper(u.userName)=upper(:userName)"), @NamedQuery(name="findUserByDeleteExpiredCerts", query="Select u from User u JOIN u.configuration c WHERE c.configKey = 'DELETE_EXPIRED_CERTS' AND c.configValue = 'true'")})
@ApiModel
public class User
extends AbstractEntity {
    public static final String USER_NAME = "userName";
    public static final String FIND_USER_BY_NAME = "findUserByName";
    public static final String FIND_USER_BY_DELETE_EXPIRED_CERTS = "findUserByDeleteExpiredCerts";
    public static final String QUERY_BY_NAME = "Select u from User u where upper(u.userName)=upper(:userName)";
    public static final String QUERY_BY_DELETE_EXPIRED_CERTS = "Select u from User u JOIN u.configuration c WHERE c.configKey = 'DELETE_EXPIRED_CERTS' AND c.configValue = 'true'";
    public static final String R_USER_CERTIFICATES = "r_user_certificates";
    public static final String R_USER_CONFIGURATIONS = "r_user_configurations";
    public static final String R_USER = "r_user";
    private static final long serialVersionUID = -9103586832848736329L;
    public static final String F_USER_NAME = "f_user_name";
    public static final String F_USER_FIRST_NAME = "f_user_first_name";
    public static final String F_USER_LAST_NAME = "f_user_last_name";
    public static final String F_USER_ORGANISATION = "f_user_organisation";
    public static final String F_USER_CREDENTIALS_HASH = "f_user_password";
    public static final String F_USER_ROLE = "f_user_role";
    public static final String F_SALT = "f_salt";
    @ApiModelProperty(dataType="java.lang.String", required=true, value="The user name. Length between 1 and 7 characters. Must be alpha numeric, non blank.")
    @Column(name="f_user_name")
    @SafeString
    private String userName;
    @ApiModelProperty(dataType="java.lang.String", required=true, value="The first name. Length between 1 and 100 characters.")
    @Column(name="f_user_first_name")
    private String firstName;
    @ApiModelProperty(dataType="java.lang.String", required=true, value="The last name. Length between 1 and 100 characters.")
    @Column(name="f_user_last_name")
    private String lastName;
    @ApiModelProperty(dataType="java.lang.String", required=true, value="The organisation. Length between 1 and 100 characters.")
    @Column(name="f_user_organisation")
    private String organisation;
    @ApiModelProperty(dataType="java.lang.String", required=true, value="The user password. Length between 9 and 100 characters. Must contain upper case, lower case, digit, and special characters.")
    @Column(name="f_user_password")
    private String userCredentials;
    @Column(name="f_user_role")
    private UserRole role;
    @Column(name="f_salt")
    @JsonIgnore
    private String salt;
    @OneToMany(cascade={CascadeType.PERSIST, CascadeType.DETACH})
    @JoinTable(name="r_user_certificates")
    @JsonIgnore
    private List<Certificate> certificates;
    @OneToMany(cascade={CascadeType.ALL}, orphanRemoval=true, mappedBy="user")
    @JsonIgnore
    private List<UserKeyPair> keyPairs;
    @OneToMany(cascade={CascadeType.ALL}, orphanRemoval=true)
    @JoinTable(name="r_user_configurations")
    private List<Configuration> configuration;

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOrganisation() {
        return this.organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getUserPassword() {
        return this.userCredentials;
    }

    public void setUserPassword(String userPassword) {
        this.userCredentials = userPassword;
    }

    public UserRole getRole() {
        return this.role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setConfigurations(List<Configuration> configuration) {
        this.configuration = configuration;
    }

    @JsonIgnore
    public List<Configuration> getConfigurations() {
        if (this.configuration != null) return this.configuration;
        this.configuration = new ArrayList<Configuration>();
        return this.configuration;
    }

    public List<Certificate> getCertificates() {
        if (this.certificates != null) return this.certificates;
        this.certificates = new ArrayList<Certificate>();
        return this.certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    public String getSalt() {
        return this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<UserKeyPair> getKeyPairs() {
        if (this.keyPairs != null) return this.keyPairs;
        this.keyPairs = new ArrayList<UserKeyPair>();
        return this.keyPairs;
    }
}
