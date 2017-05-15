package messenger.core.messages;

import java.util.Objects;

/**
 * Простое текстовое сообщение
 */
public class InfoMessage extends Message {
    private long id;

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
        InfoMessage message = (InfoMessage) other;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return "InfoMessage{" +
                "id='" + id + '\'' +
                '}';
    }
}

