package messenger.core.messages;

import messenger.core.User;
import messenger.core.net.ProtocolException;
import messenger.core.net.Session;
import messenger.server.Server;
import track.util.Util;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

/**
 * Created by alexander on 20.04.17.
 */
public class LoginCommand implements Command {

    @Override
    public void execute(Session session, Message message) throws CommandException {
        StatusMessage status = new StatusMessage();
        status.setType(Type.MSG_STATUS);
        LoginMessage login = (LoginMessage) message;
        Server.log.info(login.getLogin() + "is trying to login");
        final String sql = "SELECT * FROM user WHERE user.login = \'" + login.getLogin() + "\';";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + Session.PATH_TO_DB);
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                // Column index starts with 1
                Integer id = rs.getInt("id");
                String logindb = rs.getString("login");
                System.out.println(String.format("ID: %d, login: %s", id, logindb));
            }
            connection.close();
        } catch (SQLException e) {
            Server.log.error("Failed to execute statement: " + sql, e);
        } finally {
            Util.closeQuietly(rs, stmt);
        }
        try {
            if (Server.loginInfo.containsKey(login.getLogin())) {
                if (Objects.equals(Server.loginInfo.get(login.getLogin()), login.getPassword())) {
                    User user = new User();
                    user.setName(login.getLogin());
                    user.setId(Server.id++);
                    session.setUser(user);
                    status.setStatus("logged in");
                } else {
                    status.setStatus("wrong password");
                }
            } else {
                Server.loginInfo.put(login.getLogin(), login.getPassword());
                status.setStatus("registered");
            }
            session.send(status);
        } catch (IOException e) {
            throw new CommandException(e);
        }
    }
}
