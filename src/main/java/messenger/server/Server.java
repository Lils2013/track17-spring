package messenger.server;

import messenger.client.Client;
import messenger.core.User;
import messenger.core.messages.*;
import messenger.core.net.Protocol;
import messenger.core.net.ProtocolException;
import messenger.core.net.Session;
import messenger.core.net.StringProtocol;
import messenger.core.store.MessageStoreImpl;
import messenger.core.store.UserStoreImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.lessons.lesson9.DbManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Основной класс для сервера сообщений
 */
public class Server {

    public static final int DEFAULT_MAX_CONNECT = 4;
    public static Logger log = LoggerFactory.getLogger(Server.class);
    public static List<Session> sessions;
    public static Map<String, String> loginInfo;
    public static long id;
    public static CommandSwitch commandSwitch;

    // Засетить из конфига
    private int port;
    private Protocol protocol;
    private int maxConnection = DEFAULT_MAX_CONNECT;
    public static final String PATH_TO_DB = "/home/alexander/java/track17-spring/messenger.sqlite";

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.setPort(19000);
        ServerSocket ssock = null;
        try {
            ssock = new ServerSocket(server.getPort());
        } catch (IOException e) {
            log.error("Failed to open the socket: ", e);
        }
        Server.sessions = new ArrayList<>();
        Server.loginInfo = new HashMap<>();
        Server.commandSwitch = new CommandSwitch(new TextCommand(),
                new LoginCommand(), new LogoutCommand(), new UnknownCommand(),
                new InfoCommand(), new ChatListCommand(), new ChatCreateCommand(),
                new ChatHistoryCommand(), new RegisterCommand()
        );
        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_MAX_CONNECT);
        DbManager dbManager = new DbManager();
        dbManager.setUrl(Session.PATH_TO_DB);
        dbManager.init();
        MessageStoreImpl.MESSAGESTORE.setDbManager(dbManager);
        UserStoreImpl.USERSTORE.setDbManager(dbManager);
        System.out.println("Listening");

        while (true) {
            Socket sock = null;
            try {
                sock = ssock.accept();
            } catch (IOException e) {
                log.error("Failed to accept the socket: ", e);
            }
            Session session = null;
            try {
                session = new Session(null, sock, new StringProtocol());
            } catch (IOException e) {
                log.error("Failed to create stream: ", e);
            }
            executor.submit(session);
            System.out.println("Connected");
        }
    }

    public void stop() {
    }

}