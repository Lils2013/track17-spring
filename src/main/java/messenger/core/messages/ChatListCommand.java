package messenger.core.messages;

import messenger.core.net.Session;
import messenger.core.store.MessageStoreImpl;

import java.io.IOException;

/**
 * Created by alexander on 15.05.17.
 */
public class ChatListCommand implements Command {

    @Override
    public void execute(Session session, Message message) throws CommandException {
        StatusMessage status = new StatusMessage();
        status.setType(Type.MSG_STATUS);
        ChatListResultMessage chatListResultMessage = new ChatListResultMessage();
        chatListResultMessage.setType(Type.MSG_CHAT_LIST_RESULT);
        if (session.getUser() == null) {
            status.setStatus("you need to log in");
        } else {
            chatListResultMessage.setList(MessageStoreImpl.MESSAGESTORE.getChatsByUserId(session.getUser().getId()));
            status.setStatus("success");
            try {
                session.send(chatListResultMessage);
            } catch (IOException e) {
                throw new CommandException(e);
            }
        }
        try {
            session.send(status);
        } catch (IOException e) {
            throw new CommandException(e);
        }
    }
}
