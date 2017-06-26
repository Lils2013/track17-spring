package messenger.core.messages;

import java.util.Objects;

/**
 * Простое текстовое сообщение
 */
public class RegisterMessage extends Message {
    private String login;
    private String password;

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        RegisterMessage message = (RegisterMessage) other;
        return Objects.equals(login, message.login) && (Objects.equals(password, message.password));
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), login);
    }

    @Override
    public String toString() {
        return "RegisterMessage{" +
                "login='" + login + '\'' +
                '}';
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
