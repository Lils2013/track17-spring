package messenger.core.messages;

import messenger.core.User;
import messenger.core.net.ProtocolException;
import messenger.core.net.Session;
import messenger.server.Server;

import java.io.IOException;
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
