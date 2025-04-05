/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.servlet.Filter
 *  javax.servlet.FilterChain
 *  javax.servlet.FilterConfig
 *  javax.servlet.ServletException
 *  javax.servlet.ServletRequest
 *  javax.servlet.ServletResponse
 *  javax.servlet.http.HttpServletResponse
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.boot.web.servlet.FilterRegistrationBean
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.stereotype.Component
 */
package com.daimler.cebas.configuration.control.gracefulshutdown;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class ShutdownHttpFilter
implements Filter {
    private static final AtomicLongFieldUpdater<ShutdownHttpFilter> activeRequestsUpdater = AtomicLongFieldUpdater.newUpdater(ShutdownHttpFilter.class, "activeRequests");
    private volatile long activeRequests;
    private volatile boolean shutdown;
    private CountDownLatch latch;
    protected final Log LOG = LogFactory.getLog(this.getClass());
    @Value(value="${cebas.shutdown.timeout.http}")
    private int shutdownTimeout;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (this.shutdown) {
            HttpServletResponse resp = (HttpServletResponse)response;
            resp.sendError(503, "SpringBoot shutting down");
        } else {
            activeRequestsUpdater.incrementAndGet(this);
            try {
                chain.doFilter(request, response);
            }
            finally {
                if (this.shutdown) {
                    this.getCountDownLatch().countDown();
                }
                activeRequestsUpdater.decrementAndGet(this);
            }
        }
    }

    private synchronized CountDownLatch getCountDownLatch() {
        if (this.latch != null) return this.latch;
        try {
            this.latch = new CountDownLatch((int)activeRequestsUpdater.get(this));
        }
        catch (IllegalArgumentException e) {
            this.LOG.debug((Object)e.getMessage());
            this.latch = new CountDownLatch(0);
        }
        return this.latch;
    }

    public void shutdown() throws InterruptedException {
        this.shutdown = true;
        boolean allFinished = this.getCountDownLatch().await(this.shutdownTimeout, TimeUnit.MILLISECONDS);
        if (allFinished) return;
        this.LOG.info((Object)"Shutdown - Not all http threads finished during timeout. They will be canceled during shutdown.");
    }

    @Bean
    public FilterRegistrationBean myFilterBean() {
        FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
        filterRegBean.setFilter((Filter)this);
        filterRegBean.addUrlPatterns(new String[]{"/*"});
        filterRegBean.setEnabled(Boolean.TRUE.booleanValue());
        filterRegBean.setName("Graceful HTTP Filter");
        filterRegBean.setOrder(Integer.MIN_VALUE);
        filterRegBean.setAsyncSupported(Boolean.TRUE.booleanValue());
        return filterRegBean;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }
}
