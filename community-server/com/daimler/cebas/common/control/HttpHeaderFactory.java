/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.http.HttpHeaders
 *  org.springframework.http.MediaType
 */
package com.daimler.cebas.common.control;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HttpHeaderFactory {
    private static final String BEARER = "Bearer ";
    private static final String NO_CACHE_NO_STORE = "no-cache, no-store";
    private static final String CACHE_CONTROL = "Cache-Control";

    private HttpHeaderFactory() {
    }

    public static HttpHeaders buildHeaderCacheControl() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(CACHE_CONTROL, NO_CACHE_NO_STORE);
        return responseHeaders;
    }

    public static HttpHeaders buildHeaderCacheControlWithPagination(long page, long pageSize, long maxRows) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(CACHE_CONTROL, NO_CACHE_NO_STORE);
        long startingIndex = page * pageSize;
        long endIndex = startingIndex + pageSize;
        responseHeaders.set("Content-Range", "items " + startingIndex + "-" + endIndex + "/" + maxRows);
        return responseHeaders;
    }

    public static HttpHeaders buildDefaultPKIRequestHeader(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", BEARER + accessToken);
        return headers;
    }

    public static HttpHeaders buildDefaultPKIRequestHeader(String accessToken, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", BEARER + accessToken);
        headers.setContentType(mediaType);
        return headers;
    }

    public static HttpHeaders buildDefaultPKIRequestHeaderWithNoAuthCode(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return headers;
    }
}
