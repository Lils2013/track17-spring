package messenger.core.messages;

import messenger.core.net.ProtocolException;
import messenger.core.net.Session;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexander on 20.04.17.
 */
public class CommandSwitch {

    private TextCommand textCommand;
    private LoginCommand loginCommand;
    private LogoutCommand logoutCommand;
    private UnknownCommand unknownCommand;
    private InfoCommand infoCommand;
    public Map<Type, Command> map = new HashMap<>();

    public CommandSwitch(TextCommand textCommand, LoginCommand loginCommand, LogoutCommand logoutCommand,
                         UnknownCommand unknownCommand, InfoCommand infoCommand) {
        this.textCommand = textCommand;
        this.loginCommand = loginCommand;
        this.logoutCommand = logoutCommand;
        this.infoCommand = infoCommand;
        this.unknownCommand = unknownCommand;
        map.put(Type.MSG_TEXT,this.textCommand);
        map.put(Type.MSG_LOGIN,this.loginCommand);
        map.put(Type.MSG_LOGOUT,this.logoutCommand);
        map.put(Type.MSG_INFO,this.infoCommand);
    }

    public Map<Type, Command> getMap() {
        return map;
    }
    public TextCommand getTextCommand() {
        return textCommand;
    }

    public LoginCommand getLoginCommand() {
        return loginCommand;
    }

    public LogoutCommand getLogoutCommand() {
        return logoutCommand;
    }

    public UnknownCommand getUnknownCommand() {
        return unknownCommand;
    }

    public InfoCommand getInfoCommand() {
        return infoCommand;
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

    public void info(Session session, Message message) throws CommandException {
        infoCommand.execute(session,message);
    }
}
