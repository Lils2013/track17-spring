package messenger.core.store;

import messenger.core.User;
import messenger.core.net.Session;
import messenger.server.Server;
import track.lessons.lesson9.DbManager;
import track.util.Util;

import java.sql.*;
import java.util.Objects;

import static messenger.core.net.Session.PATH_TO_DB;

/**
 * Created by alexander on 14.05.17.
 */
public class UserStoreImpl  {

    public static final UserStoreImpl USERSTORE = new UserStoreImpl();
    private DbManager dbManager;

    public UserStoreImpl() {
    }

    public void setDbManager(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    public User getUser(String login, String pass) {
        final String sql = "SELECT * FROM user WHERE user.login = \'" + login + "\';";
        Statement stmt = null;
        ResultSet rs = null;
        User user = null;
        try {
            Connection connection = dbManager.getConnection();
                    //DriverManager.getConnection("jdbc:sqlite:" + Session.PATH_TO_DB);
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                // Column index starts with 1
                Long id = (long) rs.getInt("id");
                String logindb = rs.getString("login");
                String passdb = rs.getString("password");
                Server.log.info(String.format("ID: %d, login: %s", id, logindb));
                if (Objects.equals(passdb, pass)) {
                    user = new User();
                    user.setName(login);
                    user.setId(id);
                }
            }
        } catch (SQLException e) {
            Server.log.error("Failed to execute statement: " + sql, e);
        } finally {
            Util.closeQuietly(rs, stmt);
        }
        return user;
    }

    public User getUserById(Long id) {
        final String sql = "SELECT * FROM user WHERE user.id = \'" + id + "\';";
        Statement stmt = null;
        ResultSet rs = null;
        User user = null;
        try {
            Connection connection = dbManager.getConnection();
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                user = new User();
                user.setName(rs.getString("login"));
                user.setId(id);
            }
        } catch (SQLException e) {
            Server.log.error("Failed to execute statement: " + sql, e);
        } finally {
            Util.closeQuietly(rs, stmt);
        }
        return user;
    }

    public void addUser(String login, String pass) {
        Statement stmt = null;
        ResultSet rs = null;
        String sql = null;
        try {
            Connection connection = dbManager.getConnection();
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            sql = "INSERT INTO user (login, password) VALUES ('" +
                    login + "', '" + pass + "');";
            stmt.executeUpdate(sql);
            connection.commit();
        } catch (SQLException e) {
            Server.log.error("Failed to execute statement: " + sql, e);
        } finally {
            Util.closeQuietly(rs, stmt);
        }
    }
}
