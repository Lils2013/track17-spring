package messenger.core.messages;

import messenger.core.net.Session;
import messenger.core.store.MessageStoreImpl;

import java.io.IOException;

/**
 * Created by alexander on 16.05.17.
 */
public class ChatCreateCommand implements Command {

    @Override
    public void execute(Session session, Message message) throws CommandException {
        ChatCreateMessage chatCreateMessage = (ChatCreateMessage) message;
        long[] ids = chatCreateMessage.getIds();
        StatusMessage status = new StatusMessage();
        status.setType(Type.MSG_STATUS);
        if (session.getUser() == null) {
            status.setStatus("you need to log in");
        } else {
            if (ids.length == 1) {
                status.setStatus("chat id = " +
                        MessageStoreImpl.chatCreate(ids[0],session.getUser().getId()));
            } else {
                status.setStatus("chat id = " +
                        MessageStoreImpl.chatCreate(ids,session.getUser().getId()));
            }
        }
        try {
            session.send(status);
        } catch (IOException e) {
            throw new CommandException(e);
        }
    }
}
