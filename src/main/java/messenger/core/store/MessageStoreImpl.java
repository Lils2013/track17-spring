package messenger.core.store;

import messenger.core.User;
import messenger.core.messages.Message;
import messenger.core.net.Session;
import messenger.server.Server;
import track.util.Util;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by alexander on 15.05.17.
 */
public class MessageStoreImpl {

    public static List<Long> getChatsByUserId(Long userId) {
        final String sql = "SELECT * FROM chat_membership WHERE user_id = \'" + userId + "\';";
        Statement stmt = null;
        ResultSet rs = null;
        List<Long> list = new LinkedList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + Session.PATH_TO_DB);
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                // Column index starts with 1
                Long id = (long) rs.getInt("chat_id");
                list.add(id);
            }
            connection.close();
        } catch (SQLException e) {
            Server.log.error("Failed to execute statement: " + sql, e);
        } finally {
            Util.closeQuietly(rs, stmt);
        }
        return list;
    }

    public static long chatCreate(long[] ids, long admin) {
        Statement stmt = null;
        ResultSet rs = null;
        String sql = null;
        long lastRowId = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + Session.PATH_TO_DB);
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            sql = "INSERT INTO chat (admin_id) VALUES (" + admin + ");";
            stmt.executeUpdate(sql);
            rs = stmt.executeQuery("SELECT LAST_INSERT_ROWID() FROM chat_membership;");
            if (rs.next()) {
                // Column index starts with 1
                lastRowId = (long) rs.getInt(1);
                boolean adminNotInIds = true;
                for (int i = 0; i < ids.length; i++) {
                    sql = "INSERT INTO chat_membership (chat_id, user_id) VALUES (" +
                            lastRowId + ", " + ids[i] + ");";
                    System.out.println(sql);
                    stmt.executeUpdate(sql);
                    if (admin == ids[i]) {
                        adminNotInIds = false;
                    }
                }
                if (adminNotInIds) {
                    sql = "INSERT INTO chat_membership (chat_id, user_id) VALUES (" +
                            lastRowId + ", " + admin + ");";
                    stmt.executeUpdate(sql);
                }
            }
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            Server.log.error("Failed to execute statement: " + sql, e);
        } finally {
            Util.closeQuietly(rs, stmt);
        }
        return lastRowId;
    }

    public static long chatCreate(long id, long admin) {
        Statement stmt = null;
        ResultSet rs = null;
        String sql = null;
        long lastRowId = 0;
        if (id == admin) {
            return lastRowId;
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + Session.PATH_TO_DB);
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            rs = stmt.executeQuery("SELECT chat_id FROM chat_membership WHERE chat_id IN " +
                    "(SELECT chat_membership.chat_id FROM chat_membership WHERE user_id = " + id +
                    " INTERSECT " +
                    "SELECT chat_membership.chat_id FROM chat_membership WHERE user_id = " + admin + ") " +
                    "GROUP BY chat_id HAVING COUNT(chat_id) = 2;");
            if (rs.next()) {
                lastRowId = rs.getInt(1);
            } else {
                sql = "INSERT INTO chat (admin_id) VALUES (" + admin + ");";
                stmt.executeUpdate(sql);
                rs = stmt.executeQuery("SELECT LAST_INSERT_ROWID() FROM chat_membership;");
                if (rs.next()) {
                    lastRowId = (long) rs.getInt(1);
                }
                sql = "INSERT INTO chat_membership (chat_id, user_id) VALUES (" +
                        lastRowId + ", " + admin + ");";
                stmt.executeUpdate(sql);
                sql = "INSERT INTO chat_membership (chat_id, user_id) VALUES (" +
                        lastRowId + ", " + id + ");";
                stmt.executeUpdate(sql);
            }
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            Server.log.error("Failed to execute statement: " + sql, e);
        } finally {
            Util.closeQuietly(rs, stmt);
        }
        return lastRowId;
    }

    public static void addMessage(Long chatId, Message message) {
        Statement stmt = null;
        ResultSet rs = null;
        String sql = null;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + Session.PATH_TO_DB);
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            sql = "INSERT INTO messages (sender_id, chat_id, mes_text) VALUES (" +
                    message.getSenderId() + ", " + chatId + ", '" + message.toString() + "');";
            stmt.executeUpdate(sql);
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            Server.log.error("Failed to execute statement: " + sql, e);
        } finally {
            Util.closeQuietly(rs, stmt);
        }
    }

    public static List<String> getMessagesFromChat(Long chatId) {
        final String sql = "SELECT * FROM messages WHERE chat_id = \'" + chatId + "\';";
        Statement stmt = null;
        ResultSet rs = null;
        List<String> result = new LinkedList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + Session.PATH_TO_DB);
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                // Column index starts with 1
                result.add("TextMessage: id=" + rs.getInt("id") + ", time=" +
                        rs.getString("timestamp") +
                        ", chat_id=" + rs.getInt("chat_id") +
                        ", sender_id=" + rs.getInt("sender_id") + ", text=" +
                        rs.getString("mes_text"));

            }
            connection.close();
        } catch (SQLException e) {
            Server.log.error("Failed to execute statement: " + sql, e);
        } finally {
            Util.closeQuietly(rs, stmt);
        }
        return result;
    }

    public static void main(String[] args) {
        MessageStoreImpl ms = new MessageStoreImpl();
        long[] lol = new long[5];
        for (int i = 0; i < lol.length; i++) {
            lol[i] = (long) i;
        }
        System.out.println(ms.chatCreate(3,4));
    }
}
