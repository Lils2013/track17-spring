package messenger.server;

import messenger.client.Client;
import messenger.core.messages.Message;
import messenger.core.messages.TextMessage;
import messenger.core.messages.Type;
import messenger.core.net.Protocol;
import messenger.core.net.ProtocolException;
import messenger.core.net.Session;
import messenger.core.net.StringProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Основной класс для сервера сообщений
 */
public class Server implements Runnable {

    public static final int DEFAULT_MAX_CONNECT = 16;
    public static Logger log = LoggerFactory.getLogger(Server.class);
    public static List<Session> sessions;

    // Засетить из конфига
    private int port;
    private Protocol protocol;
    private int maxConnection = DEFAULT_MAX_CONNECT;
    private InputStream in;
    private OutputStream out;
    private Socket csocket;
    private Session csession;

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


    private Server(Socket csocket) {
        this.csocket = csocket;
    }

    public static void main(String[] args) throws Exception {
        ServerSocket ssock = new ServerSocket(19000);
        System.out.println("Listening");
        Server.sessions = new ArrayList<>();
        while (true) {
            Socket sock = ssock.accept();
            System.out.println("Connected");
            Server server = new Server(sock);
            Session thissession = new Session(sock,new StringProtocol());
            Server.sessions.add(thissession);
            server.setProtocol(new StringProtocol());
            server.csession = thissession;
            new Thread(server).start();
        }
    }

    public void run() {
        try {
            in = csocket.getInputStream();
            out = csocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final byte[] buf = new byte[1024 * 64];
        log.info("Starting listener thread...");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // Здесь поток блокируется на ожидании данных
                int read = in.read(buf);
                if (read > 0) {
                    // По сети передается поток байт, его нужно раскодировать с помощью протокола
                    Message msg = protocol.decode(Arrays.copyOf(buf, read));
                    csession.onMessage(msg);
                    if (msg.getType().equals(Type.MSG_TEXT)) {
                        TextMessage text = (TextMessage) msg;
                        Server.log.info(text.getText());
                        for (Session session : Server.sessions) {
                            if (session.getSocket() != csocket) {
                                Server.log.info(text.toString());
                                try {
                                    session.send(msg);
                                } catch (IOException | ProtocolException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    csession.onMessage(msg);
                    log.info("Message received: {}", msg);;
                }
            } catch (Exception e) {
                log.error("Failed to process connection: {}", e);
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    public void send(Message msg) throws IOException, ProtocolException {
        log.info(msg.toString());
        out.write(protocol.encode(msg));
        out.flush(); // принудительно проталкиваем буфер с данными
    }

    public void stop() {
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
