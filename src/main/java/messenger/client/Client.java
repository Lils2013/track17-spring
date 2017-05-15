package messenger.client;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import messenger.core.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import track.container.Container;
import track.container.JsonConfigReader;
import track.container.config.InvalidConfigurationException;
import messenger.core.net.ConnectionHandler;
import messenger.core.net.Protocol;
import messenger.core.net.ProtocolException;

/**
 * Клиент для тестирования серверного приложения
 */
public class Client implements ConnectionHandler {

    /**
     * Механизм логирования позволяет более гибко управлять записью данных в лог (консоль, файл и тд)
     */
    static Logger log = LoggerFactory.getLogger(Client.class);

    /**
     * Протокол, хост и порт инициализируются из конфига
     */
    private Protocol protocol;
    private int port;
    private String host;
    private boolean stop;

    /**
     * Тред "слушает" сокет на наличие входящих сообщений от сервера
     */
    private Thread socketThread;

    /**
     * С каждым сокетом связано 2 канала in/out
     */
    private InputStream in;
    private OutputStream out;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void initSocket() throws IOException {
        Socket socket = new Socket(host, port);
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
        /**
         * Инициализируем поток-слушатель. Синтаксис лямбды скрывает создание анонимного класса Runnable
         */
        socketThread = new Thread(() -> {
            final byte[] buf = new byte[1024 * 64];
            log.info("Starting listener thread...");
            while (!Thread.currentThread().isInterrupted() && !stop) {
                try {
                    // По сети передается поток байт, его нужно раскодировать с помощью протокола
                    Message msg = (Message) ois.readObject();
                    onMessage(msg);
                } catch (Exception e) {
                    log.error("Failed to process connection: {}", e);
                    e.printStackTrace();
                }
            }
            try {
                socket.close();
                ois.close();
                oos.close();
            } catch (IOException e) {
                log.error("Failed to close connection: ", e);
            }
        });
        socketThread.start();
    }

    /**
     * Реагируем на входящее сообщение
     */
    @Override
    public void onMessage(Message msg) {
        log.info("Message received: {}", msg);
    }

    /**
     * Обрабатывает входящую строку, полученную с консоли
     * Формат строки можно посмотреть в вики проекта
     */
    public void processInput(String line) throws IOException, ProtocolException {
        String[] tokens = line.split(" ");
        log.info("Tokens: {}", Arrays.toString(tokens));
        String cmdType = tokens[0];
        switch (cmdType) {
            case "/login":
                LoginMessage loginMessage = new LoginMessage();
                loginMessage.setType(Type.MSG_LOGIN);
                loginMessage.setLogin(tokens[1]);
                loginMessage.setPassword(tokens[2]);
                send(loginMessage);
                break;
            case "/logout":
                LogoutMessage logoutMessage = new LogoutMessage();
                logoutMessage.setType(Type.MSG_LOGOUT);
                send(logoutMessage);
                break;
            case "/info":
                InfoMessage infoMessage = new InfoMessage();
                infoMessage.setType(Type.MSG_INFO);
                if (tokens.length == 1) {
                    infoMessage.setUserId(0);
                } else if (tokens.length == 2) {
                    infoMessage.setUserId(Integer.parseInt(tokens[1]));
                }
                send(infoMessage);
                break;
            case "/help":
                // TODO: реализация
                break;
            case "/text":
                // FIXME: пример реализации для простого текстового сообщения
                TextMessage sendMessage = new TextMessage();
                sendMessage.setType(Type.MSG_TEXT);
                sendMessage.setText(tokens[1]);
                send(sendMessage);
                break;
            // TODO: implement another types from wiki

            default:
                log.error("Invalid input: " + line);
        }
    }

    /**
     * Отправка сообщения в сокет клиент -> сервер
     */
    @Override
    public void send(Message msg) throws IOException, ProtocolException {
        log.info(msg.toString());
        oos.writeObject(msg);
        oos.flush(); // принудительно проталкиваем буфер с данными
    }

    @Override
    public void close() {
        stop = true;
    }

    public static void main(String[] args) throws Exception {

        Client client = null;
        // Пользуемся механизмом контейнера
        try {
            JsonConfigReader reader = new JsonConfigReader();
            Container context = new Container(reader.parseBeans(new File("src/main/resources/clientconfig.json")));
            client = (Client) context.getById("client");
        } catch (InvalidConfigurationException e) {
            log.error("Failed to create client", e);
            return;
        }
        try {
            client.initSocket();

            // Цикл чтения с консоли
            Scanner scanner = new Scanner(System.in);
            System.out.println("$");
            while (true) {
                String input = scanner.nextLine();
                if ("/logout".equals(input)) {
                    client.processInput(input);
                    return;
                }
                try {
                    client.processInput(input);
                } catch (ProtocolException | IOException | ArrayIndexOutOfBoundsException e) {
                    log.error("Failed to process user input", e);
                }
            }
        } catch (Exception e) {
            log.error("Application failed.", e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }
}
