package messenger.core.net;

import messenger.core.messages.Message;

/**
 * TODO: реализовать здесь свой протокол
 */
public class BinaryProtocol implements Protocol {



    @Override
    public Message decode(byte[] bytes) throws ProtocolException {
        return null;
    }

    @Override
    public byte[] encode(Message msg) throws ProtocolException {
        return new byte[0];
    }
}
