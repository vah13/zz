/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.common.control.ICEBASRecovery
 *  com.daimler.cebas.configuration.control.AbstractConfigurator
 *  com.daimler.cebas.logs.control.Logger
 *  org.h2.message.DbException
 *  org.h2.tools.Restore
 */
package com.daimler.cebas.system.control;

import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.common.control.ICEBASRecovery;
import com.daimler.cebas.configuration.control.AbstractConfigurator;
import com.daimler.cebas.logs.control.Logger;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.h2.message.DbException;
import org.h2.tools.Restore;

public abstract class AbstractSystemRecovery
implements ICEBASRecovery {
    protected static final String BACKUP_EXTENSION = "_backup.zip";
    private static final String STORAGE_EXTENSION = ".mv.db";
    private static final String STORAGE_LOGS_EXTENSION = "Logs.db";
    private static final String BACKUP_TO = "BACKUP TO '";
    private static final String USER = "h2";
    private static final String SPRING_DATASOURCE_PASS = "spring.datasource.password";
    private static final String SPRING_DATASOURCE_URL = "spring.datasource.url";
    private static final String SPRING_JPA_SCHEMA = "spring.jpa.properties.hibernate.default_schema";
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String PROGRAM_DATA_PATH = "program.data.path";
    private static final String DATASOURCE_NAME = "datasource.name";
    protected AbstractConfigurator configurator;
    private DataSource dbSource;
    protected AtomicBoolean scheduleBackUpRequested = new AtomicBoolean(false);
    private boolean restoreExecuted;
    protected ScheduledFuture<?> scheduledTimeForBackupCheck;

    public AbstractSystemRecovery(AbstractConfigurator configurator, DataSource source) {
        this.configurator = configurator;
        this.dbSource = source;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void checkForRecovery() {
        Connection connection = null;
        try {
            this.log(Level.INFO, "000427", "Checking Database state");
            String dataSourceName = this.getDataSourceName();
            String directory = this.getDirectory();
            String dbPath = directory + dataSourceName + STORAGE_EXTENSION;
            String dbLogsPath = directory + dataSourceName + STORAGE_LOGS_EXTENSION;
            if (Files.exists(Paths.get(dbPath, new String[0]), new LinkOption[0])) {
                connection = this.checkConnection();
                this.log(Level.INFO, "000427", "Database state is ok");
            } else if (Files.exists(Paths.get(dbLogsPath, new String[0]), new LinkOption[0])) {
                this.log(Level.INFO, "000428", "No database was found, but logs exist. System will try to restore backup");
                this.doRestore();
            } else {
                this.log(Level.INFO, "000429", "No database was found");
            }
            this.closeConnection(connection, "Connection could not be closed after checking database state");
        }
        catch (ClassNotFoundException | SQLException e) {
            if (this.canAccessFiles()) {
                this.log(Level.INFO, "000430", "Database cannot be opened. Restore backup will be executed", e);
                this.doRestore();
            } else {
                this.log(Level.INFO, "000674", "Database cannot be opened. Restore can't be executed, files are locked by another process", e);
            }
        }
        finally {
            this.closeConnection(connection, "Connection could not be closed after checking database state");
        }
    }

    public void doBackup() {
        try (Connection connection = this.dbSource.getConnection();
             Statement stat = connection.createStatement();){
            String dataSourceName = this.getDataSourceName();
            String directory = this.getDirectory();
            String path = directory + dataSourceName;
            Path directoryPath = Paths.get(directory, new String[0]);
            String successMessage = "Recovery file created";
            if (this.backupExists()) {
                successMessage = "Recovery file created and has been overwritten";
            }
            if (Files.isDirectory(directoryPath, new LinkOption[0])) {
                this.log(Level.INFO, "000431", "Backup started: " + LocalTime.now());
                stat.execute(BACKUP_TO + path + "_backup.zip'");
                this.log(Level.INFO, "000431", successMessage);
                this.log(Level.INFO, "000431", "Backup ended: " + LocalTime.now());
            } else {
                this.log(Level.SEVERE, "000432X", "Backup failed: The path is invalid");
            }
        }
        catch (SQLException e) {
            this.log(Level.SEVERE, "000433X", "Backup failed: " + e.getMessage(), e);
        }
    }

    /*
     * Loose catch block
     */
    public void doRestore() {
        String dataSourceName = this.getDataSourceName();
        String directory = this.getDirectory();
        Path directoryPath = Paths.get(directory, new String[0]);
        if (Files.isDirectory(directoryPath, new LinkOption[0])) {
            String zipFileName = directory + dataSourceName + BACKUP_EXTENSION;
            if (Files.exists(Paths.get(zipFileName, new String[0]), new LinkOption[0])) {
                Connection connection = null;
                try {
                    Restore.execute((String)zipFileName, (String)directory, (String)dataSourceName);
                    this.restoreExecuted = true;
                    this.log(Level.INFO, "000434", "Database recovered from recovery-file");
                    connection = this.checkConnection();
                    this.closeConnection(connection, "Connection could not be closed after checking the restore state");
                }
                catch (ClassNotFoundException e) {
                    try {
                        this.log(Level.INFO, "000435X", "Database broken and can not be recovered from recovery-file", e);
                        throw new CEBASException("Class not found: " + e.getMessage(), "000435X");
                        catch (SQLException | DbException e2) {
                            this.log(Level.INFO, "000435X", "Database broken and can not be recovered from recovery-file", (Exception)e2);
                            throw new CEBASException("SQL exception: " + e2.getMessage(), "000435X");
                        }
                    }
                    catch (Throwable throwable) {
                        this.closeConnection(connection, "Connection could not be closed after checking the restore state");
                        throw throwable;
                    }
                }
            } else {
                this.log(Level.SEVERE, "000436", "Restoring failed: no backup zip available");
            }
        } else {
            this.log(Level.SEVERE, "000437", "Restored failed: The path is invalid");
        }
    }

    public void scheduleBackup() {
        if (this.scheduleBackUpRequested.get()) return;
        this.scheduleBackUpRequested.set(true);
        this.log(Level.INFO, "000439", "Backup was marked for execution");
    }

    public abstract void scheduleCouplingBackup();

    public boolean isRestoreExecuted() {
        return this.restoreExecuted;
    }

    public boolean backupExists() {
        String dataSourceName = this.getDataSourceName();
        String directory = this.getDirectory();
        String pathString = directory + dataSourceName + BACKUP_EXTENSION;
        return Files.exists(Paths.get(pathString, new String[0]), new LinkOption[0]);
    }

    protected String getDirectory() {
        return this.configurator.readProperty(PROGRAM_DATA_PATH);
    }

    /*
     * Exception decompiling
     */
    public boolean canAccessFiles() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [2[TRYBLOCK]], but top level block is 9[TRYBLOCK]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:1570)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    protected String getDataSourceName() {
        return this.configurator.readProperty(DATASOURCE_NAME);
    }

    protected abstract void doScheduledBackup();

    protected Connection getConnection() throws SQLException {
        String dataSource = this.configurator.readProperty(SPRING_DATASOURCE_URL);
        String password = this.configurator.readProperty(SPRING_DATASOURCE_PASS);
        return DriverManager.getConnection(dataSource, USER, password);
    }

    private Connection checkConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DB_DRIVER);
        Connection connection = this.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        if (!this.hasValidTables(metaData)) {
            this.closeConnection(connection, "Empty DB - table check: connection could not be closed.");
            throw new SQLException("Invalid DB file detected, no tables can be found.");
        }
        if (this.hasValidSchema(metaData)) return connection;
        this.closeConnection(connection, "Empty DB - schema check: connection could not be closed.");
        throw new SQLException("Invalid DB file detected, no valid schemas can be found.");
    }

    private boolean hasValidTables(DatabaseMetaData metaData) throws SQLException {
        ResultSet meta = metaData.getTables(null, null, "%", new String[]{"TABLE"});
        return meta.next();
    }

    private boolean hasValidSchema(DatabaseMetaData metaData) throws SQLException {
        String schema = this.configurator.readProperty(SPRING_JPA_SCHEMA);
        ResultSet schemas = metaData.getSchemas();
        do {
            if (!schemas.next()) return false;
        } while (!schema.equals(schemas.getString(1)));
        return true;
    }

    private void closeConnection(Connection connection, String errorMessage) {
        if (connection == null) return;
        try {
            connection.close();
        }
        catch (SQLException e) {
            this.log(Level.SEVERE, "000441X", errorMessage);
        }
    }

    protected void log(Level l, String id, String message, Exception e) {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AbstractSystemRecovery.class.getSimpleName());
        logger.log(l, message, e);
        this.log(l, id, message);
    }

    protected void log(Level l, String id, String message) {
        this.getLogger().log(l, id, message, AbstractSystemRecovery.class.getSimpleName());
    }

    protected abstract Logger getLogger();
}
