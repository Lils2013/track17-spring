package messenger.core.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import messenger.core.User;
import messenger.core.messages.Message;
import messenger.core.messages.TextMessage;
import messenger.core.messages.Type;
import messenger.server.Server;

/**
 * Здесь храним всю информацию, связанную с отдельным клиентом.
 * - объект User - описание пользователя
 * - сокеты на чтение/запись данных в канал пользователя
 */
public class Session implements ConnectionHandler {

    /**
     * Пользователь сессии, пока не прошел логин, user == null
     * После логина устанавливается реальный пользователь
     */
    private User user;

    // сокет на клиента
    private Socket socket;
    private Protocol protocol;

    /**
     * С каждым сокетом связано 2 канала in/out
     */
    private InputStream in;
    private OutputStream out;

    public Session(Socket socket, Protocol protocol) throws IOException {
        this.socket = socket;
        this.protocol = protocol;
        in = this.socket.getInputStream();
        out = this.socket.getOutputStream();
    }

    @Override
    public void send(Message msg) throws ProtocolException, IOException {
        OutputStream cout = socket.getOutputStream();
        cout.write(protocol.encode(msg));
        cout.flush(); // принудительно проталкиваем буфер с данными
    }

    @Override
    public void onMessage(Message msg) {
        System.out.println(msg);
    }

    @Override
    public void close() {
        // TODO: закрыть in/out каналы и сокет. Освободить другие ресурсы, если необходимо
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }
}
