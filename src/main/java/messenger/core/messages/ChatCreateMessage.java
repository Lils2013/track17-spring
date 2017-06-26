package messenger.core.messages;

import java.util.List;
import java.util.Objects;

/**
 * Простое текстовое сообщение
 */
public class ChatCreateMessage extends Message {

    private long[] ids;

    public long[] getIds() {
        return ids;
    }

    public void setIds(long[] ids) {
        this.ids = ids;
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
        ChatCreateMessage message = (ChatCreateMessage) other;
        return Objects.equals(ids, message.ids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ids);
    }

    @Override
    public String toString() {
        return "ChatCreate message";
    }
}
