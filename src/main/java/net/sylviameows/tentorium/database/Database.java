package net.sylviameows.tentorium.database;

import net.sylviameows.tentorium.TentoriumCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Level;

public abstract class Database {
    protected final TentoriumCore core;
    protected Connection connection;

    public final String table = "tentorium";
    public int tokens = 0;

    public Database(TentoriumCore core) {
        this.core = core;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize() {
        connection = getSQLConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM "+ table +" WHERE uuid = ?");
            ResultSet rs = ps.executeQuery();
            core.getLogger().info("Database connection successful!");
        } catch (SQLException exception) {
            core.getLogger().severe("Unable to retrieve connection. "+ exception);
        }
    }


    /**
     * blanket execute command.
     * @param statement use {{table}} to replace with the table
     * @param consumer what to do with each result.
     */
    public void executeQuery(String statement, Consumer<ResultSet> consumer) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();

            var parsed = statement.replace("{{table}}", table);
            ps = conn.prepareStatement(parsed);

            var rs = ps.executeQuery();
            while(rs.next()) {
                consumer.accept(rs);
            }
        } catch (SQLException ex) {
            core.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                core.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    protected void fetch(String uuid, Consumer<ResultSet> consumer) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE uuid = '"+uuid+"';");

            var rs = ps.executeQuery();
            while(rs.next()) {
                if(rs.getString("uuid").equalsIgnoreCase(uuid)){ // Tell database to search for the player you sent into the method. e.g getTokens(sam) It will look for sam.
                    consumer.accept(rs);
                }
            }
        } catch (SQLException ex) {
            core.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                core.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    protected void update(String uuid, String column, Consumer<PreparedStatement> consumer) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("UPDATE %s SET %s = ? WHERE uuid = ?".formatted(table, column));

            consumer.accept(ps);

            ps.setString(2, uuid);

            ps.executeUpdate();
            return;
        } catch (SQLException ex) {
            core.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                core.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    public void update(String uuid, String column, String value) {
        update(uuid, column, statement -> {
            try {
                statement.setString(1, value);
            } catch (SQLException ex) {
                core.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
            }
        });
    }

    public void update(String uuid, String column, Integer value) {
        update(uuid, column, statement -> {
            try {
                statement.setInt(1, value);
            } catch (SQLException ex) {
                core.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
            }
        });
    }

    public void createPlayer(Player player) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("INSERT OR IGNORE INTO "+table+"(uuid,name,ffa_kills,kb_kills,spleef_wins,tnt_wins) VALUES(?,?,0,0,0,0)");

            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, player.getName());

            ps.executeUpdate();
        } catch (SQLException ex) {
            core.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                core.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    public int fetchInt(String uuid, String column) {
        var value = new AtomicInteger(0);
        fetch(uuid, result -> {
            try {
                value.set(result.getInt(column));
            } catch (SQLException ex) {
                core.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
            }
        });
        return value.get();
    }

    public void reset() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("DROP TABLE "+table+";");

            ps.execute();
        } catch (SQLException ex) {
            core.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                core.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }

        load();
        Bukkit.getOnlinePlayers().forEach(this::createPlayer);
    }

    public void close(PreparedStatement ps,ResultSet rs){
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            Error.close(core, ex);
        }
    }
}
