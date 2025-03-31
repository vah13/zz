/*     */ package com.daimler.cebas.common.control;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.time.ZoneOffset;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.Calendar;
/*     */ import java.util.concurrent.Callable;
/*     */ import org.springframework.boot.system.ApplicationHome;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StartupInfoBuilder
/*     */ {
/*     */   private static final String USER_DIR_PROPERTY = "user.dir";
/*     */   private static final String USER_NAME_PROPERTY = "user.name";
/*     */   private static final String OS_NAME_PROPERTY = "os.name";
/*     */   private static final String EMPTY = "";
/*     */   private static final String SPACE = " ";
/*     */   private static final String JAVA_VM_VERSION = "java.vm.version";
/*     */   private static final String JAVA_VM_VENDOR = "java.vm.vendor";
/*     */   private static final String JAVA_VM_NAME = "java.vm.name";
/*     */   private static final int MegaBytes = 10241024;
/*     */   private final Class<?> sourceClass;
/*     */   
/*     */   public StartupInfoBuilder(Class<?> sourceClass) {
/*  34 */     this.sourceClass = sourceClass;
/*     */   }
/*     */   
/*     */   public String getStartupMessage() {
/*  38 */     StringBuilder message = new StringBuilder();
/*  39 */     message.append("Started ");
/*  40 */     message.append(getApplicationName());
/*  41 */     message.append(getVersion(this.sourceClass));
/*  42 */     message.append(getOn());
/*  43 */     message.append(getOS());
/*  44 */     message.append(getPid());
/*  45 */     message.append(getContext());
/*  46 */     return message.toString();
/*     */   }
/*     */   
/*     */   public String getConfigurationsInfo() {
/*  50 */     StringBuilder info = new StringBuilder();
/*  51 */     info.append(getApplicationName() + " will start with following user configurations:\n");
/*  52 */     info.append(getJavaVmVersion() + "\n");
/*  53 */     info.append(getJavaVmVendor() + "\n");
/*  54 */     info.append(getJavaVmName() + "\n");
/*  55 */     info.append(getMaxHeapSize() + "\n");
/*  56 */     info.append(getOSVersion() + "\n");
/*  57 */     info.append(getSystemTime() + "\n");
/*  58 */     info.append(getUTCTime());
/*  59 */     return info.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getApplicationName() {
/*  66 */     return (this.sourceClass != null) ? 
/*  67 */       ClassUtils.getShortName(this.sourceClass) : "application";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getVersion(Class<?> source) {
/*  78 */     return getValue(" v", () -> source.getPackage().getImplementationVersion(), "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getOn() {
/*  88 */     return getValue(" on ", () -> InetAddress.getLocalHost().getHostName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getOS() {
/*  97 */     return getValue(" using OS: ", () -> System.getProperty("os.name"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getPid() {
/* 106 */     return getValue(" with PID ", () -> System.getProperty("PID"));
/*     */   }
/*     */   
/*     */   private String getJavaVmVersion() {
/* 110 */     return getValue(" Java VM Version: ", () -> System.getProperty("java.vm.version"));
/*     */   }
/*     */   
/*     */   private String getJavaVmVendor() {
/* 114 */     return getValue(" Java VM Vendor: ", () -> System.getProperty("java.vm.vendor"));
/*     */   }
/*     */   
/*     */   private String getJavaVmName() {
/* 118 */     return getValue(" Java VM Name: ", () -> System.getProperty("java.vm.name"));
/*     */   }
/*     */   
/*     */   private String getMaxHeapSize() {
/* 122 */     return getValue(" Maximum Heap Size: ", () -> (Runtime.getRuntime().maxMemory() / 10241024L) + " MB");
/*     */   }
/*     */   
/*     */   private String getOSVersion() {
/* 126 */     return getValue(" OS Version: ", () -> System.getProperty("os.name"));
/*     */   }
/*     */ 
/*     */   
/*     */   private String getSystemTime() {
/* 131 */     Calendar cal = Calendar.getInstance();
/* 132 */     SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
/* 133 */     return getValue(" System Time: ", () -> time.format(cal.getTime()));
/*     */   }
/*     */   
/*     */   private String getUTCTime() {
/* 137 */     ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
/* 138 */     return getValue(" UTC Time: ", () -> utc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
/*     */   }
/*     */   
/*     */   private String getContext() {
/* 142 */     String startedBy = getValue("started by ", () -> System.getProperty("user.name"));
/*     */     
/* 144 */     String in = getValue("in ", () -> System.getProperty("user.dir"));
/*     */     
/* 146 */     ApplicationHome home = new ApplicationHome(this.sourceClass);
/*     */     
/* 148 */     String path = (home.getSource() == null) ? "" : home.getSource().getAbsolutePath();
/* 149 */     if (path == null) {
/* 150 */       return "";
/*     */     }
/* 152 */     if (StringUtils.hasLength(startedBy) && StringUtils.hasLength(path)) {
/* 153 */       startedBy = " " + startedBy;
/*     */     }
/* 155 */     if (StringUtils.hasLength(in) && StringUtils.hasLength(startedBy)) {
/* 156 */       in = " " + in;
/*     */     }
/* 158 */     return " (" + path + startedBy + in + ")";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getValue(String prefix, Callable<Object> call) {
/* 169 */     return getValue(prefix, call, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getValue(String prefix, Callable<Object> call, String defaultValue) {
/*     */     try {
/* 183 */       Object value = call.call();
/* 184 */       if (value != null && StringUtils.hasLength(value.toString())) {
/* 185 */         return prefix + value;
/*     */       }
/* 187 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 190 */     return defaultValue;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\StartupInfoBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */