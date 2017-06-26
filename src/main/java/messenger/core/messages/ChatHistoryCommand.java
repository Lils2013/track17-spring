package messenger.core.messages;

import messenger.core.net.Session;
import messenger.core.store.MessageStoreImpl;

import java.io.IOException;

/**
 * Created by alexander on 15.05.17.
 */
public class ChatHistoryCommand implements Command {

    @Override
    public void execute(Session session, Message message) throws CommandException {
        StatusMessage status = new StatusMessage();
        status.setType(Type.MSG_STATUS);
        ChatHistoryResultMessage chatHistoryResultMessage = new ChatHistoryResultMessage();
        chatHistoryResultMessage.setType(Type.MSG_CHAT_HIST_RESULT);
        ChatHistoryMessage hist = (ChatHistoryMessage) message;
        if (session.getUser() == null) {
            status.setStatus("you need to log in");
        } else {
            if (MessageStoreImpl.MESSAGESTORE.getChatsByUserId(session.getUser().getId()).contains(hist.getChatId())) {
                status.setStatus("success");
                chatHistoryResultMessage.setHist(MessageStoreImpl.MESSAGESTORE.getMessagesFromChat(hist.getChatId()));
                try {
                    session.send(chatHistoryResultMessage);
                } catch (IOException e) {
                    throw new CommandException(e);
                }
            } else {
                status.setStatus("you are not in this chat");
            }
        }
        try {
            session.send(status);
        } catch (IOException e) {
            throw new CommandException(e);
        }
    }
}
