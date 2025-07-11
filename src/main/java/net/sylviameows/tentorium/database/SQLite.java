package net.sylviameows.tentorium.database;

import net.sylviameows.tentorium.TentoriumCore;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQLite extends Database {
    String dbname;
    public SQLite(TentoriumCore core){
        super(core);
        dbname = "scores";
    }

    public String CREATE_TABLE_STRING =
            "CREATE TABLE IF NOT EXISTS tentorium (" +
            "`uuid` varchar(36) NOT NULL," +
            "`name` varchar(32) NOT NULL," +
            "`ffa_kills` int(11) NOT NULL," +
            "`kb_kills` int(11) NOT NULL," +
            "`spleef_wins` int(11) NOT NULL," +
            "`tnt_wins` int(11) NOT NULL," +
            "PRIMARY KEY (`uuid`)" +
            ");";


    @Override
    public Connection getSQLConnection() {
        File dataFolder = new File(core.getDataFolder(), dbname+".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.getParentFile().mkdirs();
                dataFolder.createNewFile();
            } catch (IOException e) {
                DatabaseError.logExecute(core, e);
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            DatabaseError.logExecute(core, ex);
        } catch (ClassNotFoundException ex) {
            DatabaseError.logExecute(core, ex);
        }
        return null;
    }

    @Override
    public void load() {
        connection = getSQLConnection();
        if (connection == null) {
            core.getLogger().severe("Failed to establish SQLite connection. Plugin cannot start.");
            return;
        }
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(CREATE_TABLE_STRING);
            s.close();
        } catch (SQLException e) {
            DatabaseError.logExecute(core, e);
        }
        initialize();
    }
}
