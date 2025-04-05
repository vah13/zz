/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.boot.system.ApplicationHome
 *  org.springframework.util.ClassUtils
 *  org.springframework.util.StringUtils
 */
package com.daimler.cebas.common.control;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.concurrent.Callable;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

public class StartupInfoBuilder {
    private static final String USER_DIR_PROPERTY = "user.dir";
    private static final String USER_NAME_PROPERTY = "user.name";
    private static final String OS_NAME_PROPERTY = "os.name";
    private static final String EMPTY = "";
    private static final String SPACE = " ";
    private static final String JAVA_VM_VERSION = "java.vm.version";
    private static final String JAVA_VM_VENDOR = "java.vm.vendor";
    private static final String JAVA_VM_NAME = "java.vm.name";
    private static final int MegaBytes = 10241024;
    private final Class<?> sourceClass;

    public StartupInfoBuilder(Class<?> sourceClass) {
        this.sourceClass = sourceClass;
    }

    public String getStartupMessage() {
        StringBuilder message = new StringBuilder();
        message.append("Started ");
        message.append(this.getApplicationName());
        message.append(this.getVersion(this.sourceClass));
        message.append(this.getOn());
        message.append(this.getOS());
        message.append(this.getPid());
        message.append(this.getContext());
        return message.toString();
    }

    public String getConfigurationsInfo() {
        StringBuilder info = new StringBuilder();
        info.append(this.getApplicationName() + " will start with following user configurations:\n");
        info.append(this.getJavaVmVersion() + "\n");
        info.append(this.getJavaVmVendor() + "\n");
        info.append(this.getJavaVmName() + "\n");
        info.append(this.getMaxHeapSize() + "\n");
        info.append(this.getOSVersion() + "\n");
        info.append(this.getSystemTime() + "\n");
        info.append(this.getUTCTime());
        return info.toString();
    }

    private String getApplicationName() {
        return this.sourceClass != null ? ClassUtils.getShortName(this.sourceClass) : "application";
    }

    private String getVersion(Class<?> source) {
        return this.getValue(" v", () -> source.getPackage().getImplementationVersion(), EMPTY);
    }

    private String getOn() {
        return this.getValue(" on ", () -> InetAddress.getLocalHost().getHostName());
    }

    private String getOS() {
        return this.getValue(" using OS: ", () -> System.getProperty(OS_NAME_PROPERTY));
    }

    private String getPid() {
        return this.getValue(" with PID ", () -> System.getProperty("PID"));
    }

    private String getJavaVmVersion() {
        return this.getValue(" Java VM Version: ", () -> System.getProperty(JAVA_VM_VERSION));
    }

    private String getJavaVmVendor() {
        return this.getValue(" Java VM Vendor: ", () -> System.getProperty(JAVA_VM_VENDOR));
    }

    private String getJavaVmName() {
        return this.getValue(" Java VM Name: ", () -> System.getProperty(JAVA_VM_NAME));
    }

    private String getMaxHeapSize() {
        return this.getValue(" Maximum Heap Size: ", () -> Runtime.getRuntime().maxMemory() / 10241024L + " MB");
    }

    private String getOSVersion() {
        return this.getValue(" OS Version: ", () -> System.getProperty(OS_NAME_PROPERTY));
    }

    private String getSystemTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        return this.getValue(" System Time: ", () -> time.format(cal.getTime()));
    }

    private String getUTCTime() {
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        return this.getValue(" UTC Time: ", () -> utc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private String getContext() {
        String path;
        String startedBy = this.getValue("started by ", () -> System.getProperty(USER_NAME_PROPERTY));
        String in = this.getValue("in ", () -> System.getProperty(USER_DIR_PROPERTY));
        ApplicationHome home = new ApplicationHome(this.sourceClass);
        String string = path = home.getSource() == null ? EMPTY : home.getSource().getAbsolutePath();
        if (path == null) {
            return EMPTY;
        }
        if (StringUtils.hasLength((String)startedBy) && StringUtils.hasLength((String)path)) {
            startedBy = SPACE + startedBy;
        }
        if (!StringUtils.hasLength((String)in)) return " (" + path + startedBy + in + ")";
        if (!StringUtils.hasLength((String)startedBy)) return " (" + path + startedBy + in + ")";
        in = SPACE + in;
        return " (" + path + startedBy + in + ")";
    }

    private String getValue(String prefix, Callable<Object> call) {
        return this.getValue(prefix, call, EMPTY);
    }

    private String getValue(String prefix, Callable<Object> call, String defaultValue) {
        try {
            Object value = call.call();
            if (value == null) return defaultValue;
            if (!StringUtils.hasLength((String)value.toString())) return defaultValue;
            return prefix + value;
        }
        catch (Exception exception) {
            // empty catch block
        }
        return defaultValue;
    }
}
