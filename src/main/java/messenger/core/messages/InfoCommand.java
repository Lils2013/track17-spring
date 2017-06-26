package messenger.core.messages;

import messenger.core.User;
import messenger.core.net.Session;
import messenger.core.store.UserStoreImpl;

import java.io.IOException;

/**
 * Created by alexander on 14.05.17.
 */
public class InfoCommand implements Command {

    @Override
    public void execute(Session session, Message message) throws CommandException {
        InfoMessage infoMessage = (InfoMessage) message;
        StatusMessage status = new StatusMessage();
        status.setType(Type.MSG_STATUS);
        InfoResultMessage infoResultMessage = new InfoResultMessage();
        infoResultMessage.setType(Type.MSG_INFO_RESULT);
        if (session.getUser() == null) {
            status.setStatus("you need to log in");
        } else {
            if (infoMessage.getUserId() == 0) {
                infoResultMessage.setLogin(session.getUser().getName());
                infoResultMessage.setUserId(session.getUser().getId());
                status.setStatus("success");
                try {
                    session.send(infoResultMessage);
                } catch (IOException e) {
                    throw new CommandException(e);
                }
            } else {
                User user = UserStoreImpl.USERSTORE.getUserById(infoMessage.getUserId());
                if (user == null) {
                    status.setStatus("no such user exists");

                } else {
                    status.setStatus("success");
                    infoResultMessage.setLogin(user.getName());
                    infoResultMessage.setUserId(user.getId());
                    try {
                        session.send(infoResultMessage);
                    } catch (IOException e) {
                        throw new CommandException(e);
                    }
                }
            }
        }
        try {
            session.send(status);
        } catch (IOException e) {
            throw new CommandException(e);
        }
    }
}
