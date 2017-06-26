package messenger.core.messages;

import messenger.core.net.ProtocolException;
import messenger.core.net.Session;
import messenger.core.store.MessageStoreImpl;
import messenger.server.Server;

import java.io.IOException;

public class TextCommand implements Command {

    @Override
    public void execute(Session session, Message message) throws CommandException {
        StatusMessage status = new StatusMessage();
        status.setType(Type.MSG_STATUS);
        TextMessage text = (TextMessage) message;
        try {
            if (session.getUser() == null) {
                status.setStatus("you need to log in");
            } else {
                if (MessageStoreImpl.MESSAGESTORE.getChatsByUserId(session.getUser().getId())
                        .contains(text.getChatId())) {
                    text.setSenderId(session.getUser().getId());
                    MessageStoreImpl.MESSAGESTORE.addMessage(text.getChatId(),text);
                    for (Session sess : Server.sessions) {
                        if (sess.getSocket() != session.getSocket()) {
                            if (MessageStoreImpl.MESSAGESTORE.getChatsByUserId(sess.getUser().getId())
                                    .contains(text.getChatId())) {
                                sess.send(text);
                            }
                        }
                    }
                    status.setStatus("success");
                } else {
                    status.setStatus("sender not in chat");
                }
            }
            session.send(status);
        } catch (IOException e) {
            throw new CommandException(e);
        }
    }
}
