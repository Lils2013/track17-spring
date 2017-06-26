package messenger.core.messages;

import messenger.core.User;
import messenger.core.net.Session;
import messenger.core.store.UserStoreImpl;
import messenger.server.Server;

import java.io.IOException;

/**
 * Created by alexander on 20.04.17.
 */
public class RegisterCommand implements Command {

    @Override
    public void execute(Session session, Message message) throws CommandException {
        StatusMessage status = new StatusMessage();
        status.setType(Type.MSG_STATUS);
        RegisterMessage login = (RegisterMessage) message;
        Server.log.info(login.getLogin() + " is trying to register");
        try {
            User result = UserStoreImpl.USERSTORE.getUser(login.getLogin(),login.getPassword());
            if (result == null) {
                UserStoreImpl.USERSTORE.addUser(login.getLogin(), login.getPassword());
                status.setStatus("succesfully registered");
            } else {
                status.setStatus("user exists");
            }
            session.send(status);
        } catch (IOException e) {
            throw new CommandException(e);
        }
    }
}
