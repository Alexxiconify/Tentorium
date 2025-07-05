package net.sylviameows.tentorium.database;

import net.sylviameows.tentorium.TentoriumCore;

import java.util.logging.Level;

public class DatabaseError {
    // Error message constants
    public static final String SQL_CONNECTION_EXECUTE = "Couldn't execute SQL statement: ";
    public static final String SQL_CONNECTION_CLOSE = "Failed to close SQL connection: ";
    public static final String NO_SQL_CONNECTION = "Unable to retrieve SQL connection: ";
    public static final String NO_TABLE_FOUND = "Database Error: No Table Found";
    
    // Error logging methods
    public static void logExecute(TentoriumCore plugin, Exception ex) {
        plugin.getLogger().log(Level.SEVERE, SQL_CONNECTION_EXECUTE, ex);
    }
    
    public static void logClose(TentoriumCore plugin, Exception ex) {
        plugin.getLogger().log(Level.SEVERE, SQL_CONNECTION_CLOSE, ex);
    }
    
    public static void logConnection(TentoriumCore plugin, Exception ex) {
        plugin.getLogger().log(Level.SEVERE, NO_SQL_CONNECTION, ex);
    }
    
    public static void logTableNotFound(TentoriumCore plugin, Exception ex) {
        plugin.getLogger().log(Level.SEVERE, NO_TABLE_FOUND, ex);
    }
} 