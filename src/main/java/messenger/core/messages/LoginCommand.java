package messenger.core.messages;

import messenger.core.User;
import messenger.core.net.ProtocolException;
import messenger.core.net.Session;
import messenger.core.store.UserStoreImpl;
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
        Server.log.info(login.getLogin() + " is trying to login");
        try {
            User result = UserStoreImpl.USERSTORE.getUser(login.getLogin(),login.getPassword());
            if (result == null) {
                status.setStatus("failed to login");
            } else {
                session.setUser(result);
                status.setStatus("logged in");
                InfoResultMessage info = new InfoResultMessage();
                info.setType(Type.MSG_INFO_RESULT);
                info.setUserId(result.getId());
                info.setLogin(result.getName());
                session.send(info);
            }
            session.send(status);
        } catch (IOException e) {
            throw new CommandException(e);
        }
    }
}
