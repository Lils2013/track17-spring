package messenger.core.messages;

import messenger.core.User;
import messenger.core.net.Session;
import messenger.core.store.UserStoreImpl;

import java.io.IOException;

import static messenger.core.messages.Type.MSG_INFO_RESULT;

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
        infoResultMessage.setType(MSG_INFO_RESULT);
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
                    e.printStackTrace();
                }
            } else {
                UserStoreImpl userStore = new UserStoreImpl();
                User user = userStore.getUserById(infoMessage.getUserId());
                if (user == null) {
                    status.setStatus("no such user exists");

                } else {
                    status.setStatus("success");
                    infoResultMessage.setLogin(user.getName());
                    infoResultMessage.setUserId(user.getId());
                    try {
                        session.send(infoResultMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        try {
            session.send(status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
