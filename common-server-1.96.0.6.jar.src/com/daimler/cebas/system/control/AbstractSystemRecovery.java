/*     */ package com.daimler.cebas.system.control;
/*     */ 
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.ICEBASRecovery;
/*     */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.io.File;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.FileLock;
/*     */ import java.nio.channels.OverlappingFileLockException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.time.LocalTime;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.sql.DataSource;
/*     */ import org.h2.tools.Restore;
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
/*     */ public abstract class AbstractSystemRecovery
/*     */   implements ICEBASRecovery
/*     */ {
/*     */   protected static final String BACKUP_EXTENSION = "_backup.zip";
/*     */   private static final String STORAGE_EXTENSION = ".mv.db";
/*     */   private static final String STORAGE_LOGS_EXTENSION = "Logs.db";
/*     */   private static final String BACKUP_TO = "BACKUP TO '";
/*     */   private static final String USER = "h2";
/*     */   private static final String SPRING_DATASOURCE_PASS = "spring.datasource.password";
/*     */   private static final String SPRING_DATASOURCE_URL = "spring.datasource.url";
/*     */   private static final String SPRING_JPA_SCHEMA = "spring.jpa.properties.hibernate.default_schema";
/*     */   private static final String DB_DRIVER = "org.h2.Driver";
/*     */   private static final String PROGRAM_DATA_PATH = "program.data.path";
/*     */   private static final String DATASOURCE_NAME = "datasource.name";
/*     */   protected AbstractConfigurator configurator;
/*     */   private DataSource dbSource;
/*  64 */   protected AtomicBoolean scheduleBackUpRequested = new AtomicBoolean(false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean restoreExecuted;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ScheduledFuture<?> scheduledTimeForBackupCheck;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractSystemRecovery(AbstractConfigurator configurator, DataSource source) {
/*  84 */     this.configurator = configurator;
/*  85 */     this.dbSource = source;
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkForRecovery() {
/*  90 */     Connection connection = null;
/*     */     try {
/*  92 */       log(Level.INFO, "000427", "Checking Database state");
/*     */       
/*  94 */       String dataSourceName = getDataSourceName();
/*  95 */       String directory = getDirectory();
/*  96 */       String dbPath = directory + dataSourceName + ".mv.db";
/*  97 */       String dbLogsPath = directory + dataSourceName + "Logs.db";
/*     */       
/*  99 */       if (Files.exists(Paths.get(dbPath, new String[0]), new java.nio.file.LinkOption[0])) {
/*     */         
/* 101 */         connection = checkConnection();
/* 102 */         log(Level.INFO, "000427", "Database state is ok");
/*     */       }
/* 104 */       else if (Files.exists(Paths.get(dbLogsPath, new String[0]), new java.nio.file.LinkOption[0])) {
/* 105 */         log(Level.INFO, "000428", "No database was found, but logs exist. System will try to restore backup");
/*     */         
/* 107 */         doRestore();
/*     */       } else {
/* 109 */         log(Level.INFO, "000429", "No database was found");
/*     */       }
/*     */     
/* 112 */     } catch (ClassNotFoundException|SQLException e) {
/* 113 */       if (canAccessFiles()) {
/* 114 */         log(Level.INFO, "000430", "Database cannot be opened. Restore backup will be executed", e);
/*     */         
/* 116 */         doRestore();
/*     */       } else {
/* 118 */         log(Level.INFO, "000674", "Database cannot be opened. Restore can't be executed, files are locked by another process", e);
/*     */       } 
/*     */     } finally {
/*     */       
/* 122 */       closeConnection(connection, "Connection could not be closed after checking database state");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void doBackup() {
/* 128 */     try(Connection connection = this.dbSource.getConnection(); Statement stat = connection.createStatement()) {
/* 129 */       String dataSourceName = getDataSourceName();
/* 130 */       String directory = getDirectory();
/* 131 */       String path = directory + dataSourceName;
/* 132 */       Path directoryPath = Paths.get(directory, new String[0]);
/* 133 */       String successMessage = "Recovery file created";
/* 134 */       if (backupExists()) {
/* 135 */         successMessage = "Recovery file created and has been overwritten";
/*     */       }
/* 137 */       if (Files.isDirectory(directoryPath, new java.nio.file.LinkOption[0])) {
/* 138 */         log(Level.INFO, "000431", "Backup started: " + LocalTime.now());
/* 139 */         stat.execute("BACKUP TO '" + path + "_backup.zip'");
/* 140 */         log(Level.INFO, "000431", successMessage);
/* 141 */         log(Level.INFO, "000431", "Backup ended: " + LocalTime.now());
/*     */       } else {
/* 143 */         log(Level.SEVERE, "000432X", "Backup failed: The path is invalid");
/*     */       } 
/* 145 */     } catch (SQLException e) {
/* 146 */       log(Level.SEVERE, "000433X", "Backup failed: " + e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void doRestore() {
/* 152 */     String dataSourceName = getDataSourceName();
/* 153 */     String directory = getDirectory();
/* 154 */     Path directoryPath = Paths.get(directory, new String[0]);
/* 155 */     if (Files.isDirectory(directoryPath, new java.nio.file.LinkOption[0])) {
/* 156 */       String zipFileName = directory + dataSourceName + "_backup.zip";
/* 157 */       if (Files.exists(Paths.get(zipFileName, new String[0]), new java.nio.file.LinkOption[0])) {
/* 158 */         Connection connection = null;
/*     */         try {
/* 160 */           Restore.execute(zipFileName, directory, dataSourceName);
/* 161 */           this.restoreExecuted = true;
/* 162 */           log(Level.INFO, "000434", "Database recovered from recovery-file");
/*     */           
/* 164 */           connection = checkConnection();
/* 165 */         } catch (ClassNotFoundException e) {
/* 166 */           log(Level.INFO, "000435X", "Database broken and can not be recovered from recovery-file", e);
/*     */           
/* 168 */           throw new CEBASException("Class not found: " + e.getMessage(), "000435X");
/* 169 */         } catch (SQLException|org.h2.message.DbException e) {
/* 170 */           log(Level.INFO, "000435X", "Database broken and can not be recovered from recovery-file", e);
/*     */           
/* 172 */           throw new CEBASException("SQL exception: " + e.getMessage(), "000435X");
/*     */         } finally {
/* 174 */           closeConnection(connection, "Connection could not be closed after checking the restore state");
/*     */         } 
/*     */       } else {
/*     */         
/* 178 */         log(Level.SEVERE, "000436", "Restoring failed: no backup zip available");
/*     */       } 
/*     */     } else {
/* 181 */       log(Level.SEVERE, "000437", "Restored failed: The path is invalid");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void scheduleBackup() {
/* 187 */     if (!this.scheduleBackUpRequested.get()) {
/* 188 */       this.scheduleBackUpRequested.set(true);
/* 189 */       log(Level.INFO, "000439", "Backup was marked for execution");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void scheduleCouplingBackup();
/*     */ 
/*     */   
/*     */   public boolean isRestoreExecuted() {
/* 198 */     return this.restoreExecuted;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean backupExists() {
/* 203 */     String dataSourceName = getDataSourceName();
/* 204 */     String directory = getDirectory();
/* 205 */     String pathString = directory + dataSourceName + "_backup.zip";
/* 206 */     return Files.exists(Paths.get(pathString, new String[0]), new java.nio.file.LinkOption[0]);
/*     */   }
/*     */   
/*     */   protected String getDirectory() {
/* 210 */     return this.configurator.readProperty("program.data.path");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canAccessFiles() {
/* 215 */     String dataSourceName = getDataSourceName();
/* 216 */     String directory = getDirectory();
/* 217 */     String dbPath = directory + dataSourceName + ".mv.db";
/*     */     try {
/* 219 */       File dbFile = new File(dbPath);
/*     */       
/* 221 */       if (!dbFile.exists()) {
/* 222 */         return true;
/*     */       }
/* 224 */       try(RandomAccessFile rw = new RandomAccessFile(dbFile, "rw"); 
/* 225 */           FileChannel fileChannel = rw.getChannel(); 
/* 226 */           FileLock lock = fileChannel.tryLock()) {
/*     */         
/* 228 */         return (lock != null && lock.isValid());
/*     */       } 
/* 230 */     } catch (OverlappingFileLockException|java.io.IOException e) {
/* 231 */       log(Level.SEVERE, "000440", e.getMessage());
/* 232 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String getDataSourceName() {
/* 237 */     return this.configurator.readProperty("datasource.name");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void doScheduledBackup();
/*     */ 
/*     */   
/*     */   protected Connection getConnection() throws SQLException {
/* 246 */     String dataSource = this.configurator.readProperty("spring.datasource.url");
/* 247 */     String password = this.configurator.readProperty("spring.datasource.password");
/* 248 */     return DriverManager.getConnection(dataSource, "h2", password);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Connection checkConnection() throws ClassNotFoundException, SQLException {
/* 259 */     Class.forName("org.h2.Driver");
/*     */     
/* 261 */     Connection connection = getConnection();
/* 262 */     DatabaseMetaData metaData = connection.getMetaData();
/*     */     
/* 264 */     if (!hasValidTables(metaData)) {
/* 265 */       closeConnection(connection, "Empty DB - table check: connection could not be closed.");
/* 266 */       throw new SQLException("Invalid DB file detected, no tables can be found.");
/*     */     } 
/*     */     
/* 269 */     if (!hasValidSchema(metaData)) {
/* 270 */       closeConnection(connection, "Empty DB - schema check: connection could not be closed.");
/* 271 */       throw new SQLException("Invalid DB file detected, no valid schemas can be found.");
/*     */     } 
/*     */     
/* 274 */     return connection;
/*     */   }
/*     */   
/*     */   private boolean hasValidTables(DatabaseMetaData metaData) throws SQLException {
/* 278 */     ResultSet meta = metaData.getTables(null, null, "%", new String[] { "TABLE" });
/* 279 */     return meta.next();
/*     */   }
/*     */   
/*     */   private boolean hasValidSchema(DatabaseMetaData metaData) throws SQLException {
/* 283 */     String schema = this.configurator.readProperty("spring.jpa.properties.hibernate.default_schema");
/* 284 */     ResultSet schemas = metaData.getSchemas();
/* 285 */     while (schemas.next()) {
/* 286 */       if (schema.equals(schemas.getString(1))) {
/* 287 */         return true;
/*     */       }
/*     */     } 
/* 290 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void closeConnection(Connection connection, String errorMessage) {
/* 301 */     if (connection != null) {
/*     */       try {
/* 303 */         connection.close();
/* 304 */       } catch (SQLException e) {
/* 305 */         log(Level.SEVERE, "000441X", errorMessage);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void log(Level l, String id, String message, Exception e) {
/* 318 */     Logger logger = Logger.getLogger(AbstractSystemRecovery.class.getSimpleName());
/* 319 */     logger.log(l, message, e);
/* 320 */     log(l, id, message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void log(Level l, String id, String message) {
/* 331 */     getLogger().log(l, id, message, AbstractSystemRecovery.class.getSimpleName());
/*     */   }
/*     */   
/*     */   protected abstract Logger getLogger();
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\AbstractSystemRecovery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */