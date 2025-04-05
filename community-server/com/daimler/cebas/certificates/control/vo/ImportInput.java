/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.ImportInputType
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.ImportInputType;
import java.io.InputStream;

public class ImportInput {
    private InputStream stream;
    private byte[] bytes;
    private String fileName;
    private ImportInputType type;
    private String password;

    public ImportInput(InputStream stream, byte[] bytes, String fileName) {
        this(stream, bytes, fileName, null, ImportInputType.CERTIFICATE_FILE);
    }

    public ImportInput(InputStream stream, byte[] bytes, String fileName, ImportInputType type) {
        this.stream = stream;
        this.bytes = bytes;
        this.fileName = fileName;
        this.type = type;
    }

    public ImportInput(InputStream stream, byte[] bytes, String fileName, String password, ImportInputType type) {
        this.stream = stream;
        this.bytes = bytes;
        this.fileName = fileName;
        this.password = password;
        this.type = type;
    }

    public InputStream getStream() {
        return this.stream;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public boolean isPKCS12() {
        return this.type == ImportInputType.PKCS12;
    }

    public ImportInputType getType() {
        return this.type;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean hasPassword() {
        return this.password != null;
    }
}
