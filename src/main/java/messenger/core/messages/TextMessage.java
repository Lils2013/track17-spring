package messenger.core.messages;

import java.util.Objects;

/**
 * Простое текстовое сообщение
 */
public class TextMessage extends Message {
    private String text;
    private long chatId;

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        TextMessage message = (TextMessage) other;
        return Objects.equals(text, message.text) && (chatId == message.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), text, chatId);
    }

    @Override
    public String toString() {
        return "TextMessage{" +
                "text=" + text +
                " chat=" + chatId +
                "}";
    }
}
