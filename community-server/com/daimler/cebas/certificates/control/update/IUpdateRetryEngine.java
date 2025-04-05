/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.certificates.control.update;

public interface IUpdateRetryEngine {
    public Integer retry(String var1);

    public void resetRetries();

    public long getNextRetryTimeStamp();

    public void setDidFailAllRetries(boolean var1);

    public Integer getNextRetryTimeInterval();

    public void setNextRetryTimeInterval();

    public boolean didFailAllRetries();

    public String getRetryUrl();

    public Integer getMaxRetries();

    public Integer getCurrentRetry();
}
