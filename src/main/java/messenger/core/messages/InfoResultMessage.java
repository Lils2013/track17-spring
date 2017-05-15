package messenger.core.messages;

import java.util.Objects;

public class InfoResultMessage extends Message {

    private long id;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    private String login;

    public long getUserId() {
        return id;
    }

    public void setUserId(long id) {
        this.id = id;
    }

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
        InfoResultMessage message = (InfoResultMessage) other;
        return Objects.equals(id, message.id) && Objects.equals(login, message.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, login);
    }

    @Override
    public String toString() {
        return "InfoResultMessage{" +
                "id='" + id + '\'' +
                "login='" + login + '\'' +
                '}';
    }
}

