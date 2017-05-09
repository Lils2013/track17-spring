package messenger.core.net;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;

import messenger.core.User;
import messenger.core.messages.*;
import messenger.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Session implements Runnable {
    static Logger log = LoggerFactory.getLogger(Session.class);
    private boolean stop;
    public static final String PATH_TO_DB = "/home/alexander/java/track17-spring/messenger.sqlite";
    private User user;
    private Socket socket;
    private Protocol protocol;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public Session(User user, Socket socket, Protocol protocol) throws IOException {
        this.user = user;
        this.socket = socket;
        this.protocol = protocol;
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void send(Message msg) throws IOException {
        oos.writeObject(msg);
        oos.flush();
        log.info("Message sent: {}", msg);
    }

    public void onMessage(Message msg) {
        log.info("Message received: {}", msg);
    }

    public void close() throws IOException {
        Server.sessions.remove(this);
        oos.close();
        ois.close();
        socket.close();
        Server.log.info("Session is closed");
    }

    @Override
    public void run() {
        stop = false;
        CommandSwitch executor = new CommandSwitch(new TextCommand(),
                new LoginCommand(),new LogoutCommand(), new UnknownCommand());
        Server.sessions.add(this);
        while (!Thread.currentThread().isInterrupted() && !stop) {
            try {
                Message msg = (Message) ois.readObject();
                onMessage(msg);
                if (msg.getType().equals(Type.MSG_TEXT)) {
                    executor.text(this,msg);
                } else if (msg.getType().equals(Type.MSG_LOGIN)) {
                    executor.login(this,msg);
                } else if (msg.getType().equals(Type.MSG_LOGOUT)) {
                    executor.logout(this,msg);
                } else {
                    executor.unknown(this,msg);
                }
            } catch (IOException | ClassNotFoundException | CommandException e) {
                stop = true;
                log.error("Failed to transfer data: ", e);
            }
        }
        try {
            close();
        } catch (IOException e) {
            log.error("Failed to close connection: ", e);
        }
    }
}