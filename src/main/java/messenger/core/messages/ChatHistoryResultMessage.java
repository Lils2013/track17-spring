package messenger.core.messages;

import java.util.List;
import java.util.Objects;

/**
 * Простое текстовое сообщение
 */
public class ChatHistoryResultMessage extends Message {

    private List<String> hist;

    public List<String> getHist() {
        return hist;
    }

    public void setHist(List<String> hist) {
        this.hist = hist;
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
        ChatHistoryResultMessage message = (ChatHistoryResultMessage) other;
        return Objects.equals(hist, message.hist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hist);
    }

    @Override
    public String toString() {
        String result = "ChatHistoryResult : \n";
        for (String i : hist) {
            result += i + "\n";
        }
        return result;
    }
}
