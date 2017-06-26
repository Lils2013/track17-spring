package messenger.core.messages;

import java.util.Objects;

/**
 * Простое текстовое сообщение
 */
public class LogoutMessage extends Message {

    @Override
    public String toString() {
        return "Logout message";
    }
}
