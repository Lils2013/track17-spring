package messenger.core.messages;

import messenger.core.net.ProtocolException;
import messenger.core.net.Session;

import java.io.IOException;

/**
 * Created by alexander on 20.04.17.
 */
public class LogoutCommand implements Command {

    @Override
    public void execute(Session session, Message message) throws CommandException {
        StatusMessage status = new StatusMessage();
        status.setType(Type.MSG_STATUS);
        try {
            status.setStatus("logged out");
            session.send(status);
        } catch (IOException e) {
            throw new CommandException(e);
        }
        session.setStop(true);

    }
}
