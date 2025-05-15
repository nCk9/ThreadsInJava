package org.ProducerConsumer;

public class Consumer {
    Message consumeMessage(SharedQueue sharedQueue) throws QueueEmptyException {
        Message consumedMessage = sharedQueue.consume();
        System.out.println("Consumed message is: " + consumedMessage.getMessage());
        return consumedMessage;
    }
}
