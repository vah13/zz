/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package com.daimler.cebas.certificates.integration.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class NonVsmIdentifier {
    @JsonProperty(value="ski")
    private String subjectKeyIdentifier;
    @JsonProperty(value="cn")
    private String subject;
    @JsonProperty(value="ecuUniqueIds")
    private List<String> ecuUniqueIds;

    public NonVsmIdentifier() {
    }

    public NonVsmIdentifier(String subjectKeyIdentifier, String subject, List<String> ecuUniqueIds) {
        this.subjectKeyIdentifier = subjectKeyIdentifier;
        this.subject = subject;
        this.ecuUniqueIds = ecuUniqueIds;
    }

    public String getSubjectKeyIdentifier() {
        return this.subjectKeyIdentifier;
    }

    public void setSubjectKeyIdentifier(String subjectKeyIdentifier) {
        this.subjectKeyIdentifier = subjectKeyIdentifier;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getEcuUniqueIds() {
        return this.ecuUniqueIds;
    }

    public void setEcuUniqueIds(List<String> ecuUniqueIds) {
        this.ecuUniqueIds = ecuUniqueIds;
    }

    public String toString() {
        return "NonVsmIdentifier{subjectKeyIdentifier='" + this.subjectKeyIdentifier + '\'' + ", subject='" + this.subject + '\'' + ", ecuUniqueIds=" + this.ecuUniqueIds + '}';
    }
}
