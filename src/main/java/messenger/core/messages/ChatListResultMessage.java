package messenger.core.messages;

import java.util.List;
import java.util.Objects;

public class ChatListResultMessage extends Message {

    private List<Long> list;

    public List<Long> getList() {
        return list;
    }

    public void setList(List<Long> list) {
        this.list = list;
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
        ChatListResultMessage message = (ChatListResultMessage) other;
        return Objects.equals(list, message.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), list);
    }

    @Override
    public String toString() {

        String result = "InfoResultMessage{" + "ids='";
        for (long i : list) {
            result += i + " ";
        }
        return result + '\'' + '}';
    }
}

