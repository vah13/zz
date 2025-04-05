/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.configuration.control.gracefulshutdown.ShutdownHttpFilter
 *  com.daimler.cebas.system.control.websocket.IWebsocketSessions
 *  com.daimler.cebas.users.control.Session
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Qualifier
 *  org.springframework.boot.web.context.WebServerInitializedEvent
 *  org.springframework.boot.web.server.WebServer
 *  org.springframework.boot.web.server.WebServerException
 *  org.springframework.context.ConfigurableApplicationContext
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.context.event.EventListener
 *  org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
 *  org.springframework.stereotype.Component
 */
package com.daimler.cebas.configuration.control.gracefulshutdown;

import com.daimler.cebas.configuration.control.gracefulshutdown.ShutdownHttpFilter;
import com.daimler.cebas.system.control.websocket.IWebsocketSessions;
import com.daimler.cebas.users.control.Session;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class ShutdownHook
implements Runnable {
    protected final Log LOG = LogFactory.getLog(this.getClass());
    private final List<WebServer> embeddedContainers = new ArrayList<WebServer>();
    private ConfigurableApplicationContext applicationContext;
    private volatile boolean alreadyExecuted = false;
    private int shutdownTimeout = 60000;
    @Autowired
    private ShutdownHttpFilter filter;
    @Autowired
    private Optional<IWebsocketSessions> sessions;
    @Autowired
    private Session session;
    @Autowired
    @Qualifier(value="taskScheduler")
    private ThreadPoolTaskScheduler taskScheduler;

    public void init(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.alreadyExecuted = false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        this.session.initMdc();
        if (this.alreadyExecuted) {
            this.LOG.info((Object)"Shutdown - was already executed. Skipping.");
            return;
        }
        this.LOG.info((Object)("Shutdown - started. Existing HTTP sessions and scheduled jobs can finish within: " + this.shutdownTimeout + " miliseconds."));
        ExecutorService shutdownExecutor = null;
        try {
            shutdownExecutor = Executors.newSingleThreadExecutor();
            Future<Void> httpFuture = shutdownExecutor.submit(() -> {
                this.session.initMdc();
                this.shutdownHttpFilter();
                this.stopWebsocketSessions();
                this.drainScheduler(this.taskScheduler);
                this.shutdownHTTPConnector();
                this.shutdownScheduler(this.taskScheduler);
                return null;
            });
            try {
                httpFuture.get(this.shutdownTimeout, TimeUnit.MILLISECONDS);
            }
            catch (ExecutionException | TimeoutException e) {
                this.LOG.warn((Object)"HTTP graceful shutdown failed", (Throwable)e);
            }
            catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                this.LOG.warn((Object)"HTTP graceful shutdown failed", (Throwable)interrupted);
            }
        }
        finally {
            this.alreadyExecuted = true;
            this.shutdownExecutor(shutdownExecutor);
            this.shutdownApplication();
        }
    }

    private void drainScheduler(ThreadPoolTaskScheduler scheduler) {
        this.LOG.info((Object)"Shutdown - execute all scheduled tasks.");
        try {
            ScheduledThreadPoolExecutor executor = scheduler.getScheduledThreadPoolExecutor();
            executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
            executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
            executor.setRemoveOnCancelPolicy(true);
            executor.shutdown();
            executor.awaitTermination(this.shutdownTimeout, TimeUnit.MILLISECONDS);
        }
        catch (SecurityException e) {
            this.LOG.warn((Object)"Shutdown - drainScheduler failed", (Throwable)e);
        }
        catch (InterruptedException e) {
            this.LOG.warn((Object)"Shutdown - drainScheduler failed", (Throwable)e);
            Thread.currentThread().interrupt();
        }
    }

    private void stopWebsocketSessions() {
        if (!this.sessions.isPresent()) return;
        this.LOG.info((Object)"Shutdown - Websocket sessions");
        this.sessions.get().close();
    }

    private void shutdownHttpFilter() {
        this.LOG.info((Object)"Shutdown - HTTP filter.");
        try {
            this.filter.shutdown();
        }
        catch (InterruptedException e) {
            this.LOG.warn((Object)"Shutdown - shutdownHttpFilter failed", (Throwable)e);
            Thread.currentThread().interrupt();
        }
    }

    private void shutdownScheduler(ThreadPoolTaskScheduler scheduler) {
        this.LOG.info((Object)"Shutdown - Schedulers.");
        try {
            scheduler.shutdown();
        }
        catch (SecurityException e) {
            this.LOG.warn((Object)"Shutdown - shutdown taskScheduler failed", (Throwable)e);
        }
    }

    private void shutdownHTTPConnector() {
        this.LOG.info((Object)"Shutdown - HTTP Connector.");
        try {
            Iterator<WebServer> iterator = this.embeddedContainers.iterator();
            while (iterator.hasNext()) {
                WebServer embeddedServletContainer = iterator.next();
                embeddedServletContainer.stop();
            }
            return;
        }
        catch (WebServerException e) {
            this.LOG.warn((Object)"Shutdown - shutdownHTTPConnector failed", (Throwable)e);
        }
    }

    private void shutdownExecutor(ExecutorService shutdownExecutor) {
        if (shutdownExecutor == null) return;
        try {
            shutdownExecutor.shutdownNow();
        }
        catch (SecurityException e) {
            this.LOG.warn((Object)"Executor shutdown failed", (Throwable)e);
        }
    }

    private void shutdownApplication() {
        this.LOG.info((Object)"Shutdown - ApplicationContext");
        this.applicationContext.close();
    }

    @EventListener
    public synchronized void onContainerInitialized(WebServerInitializedEvent event) {
        this.embeddedContainers.add(event.getSource());
    }
}
