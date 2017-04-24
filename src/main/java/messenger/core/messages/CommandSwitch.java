package messenger.core.messages;

import messenger.core.net.ProtocolException;
import messenger.core.net.Session;

import java.io.IOException;

/**
 * Created by alexander on 20.04.17.
 */
public class CommandSwitch {

    private TextCommand textCommand;
    private LoginCommand loginCommand;
    private LogoutCommand logoutCommand;
    private UnknownCommand unknownCommand;

    public CommandSwitch(TextCommand textCommand, LoginCommand loginCommand, LogoutCommand logoutCommand, UnknownCommand unknownCommand) {
        this.textCommand = textCommand;
        this.loginCommand = loginCommand;
        this.logoutCommand = logoutCommand;
        this.unknownCommand = unknownCommand;
    }

    public void text(Session session, Message message) throws CommandException {
        textCommand.execute(session,message);
    }

    public void login(Session session, Message message) throws CommandException {
        loginCommand.execute(session,message);
    }

    public void logout(Session session, Message message) throws CommandException {
        logoutCommand.execute(session,message);
    }

    public void unknown(Session session, Message message) throws CommandException {
        unknownCommand.execute(session,message);
    }
}
