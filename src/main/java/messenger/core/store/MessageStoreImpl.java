package messenger.core.store;

import messenger.core.User;
import messenger.core.net.Session;
import messenger.server.Server;
import track.util.Util;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by alexander on 15.05.17.
 */
public class MessageStoreImpl {

    List<Long> getChatsByUserId(Long userId) {
        final String sql = "SELECT * FROM chat_membership WHERE user_id = \'" + userId + "\';";
        Statement stmt = null;
        ResultSet rs = null;
        User user = null;
        List<Long> list = new LinkedList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + Session.PATH_TO_DB);
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                // Column index starts with 1
                Long id = (long) rs.getInt("id");
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
}
