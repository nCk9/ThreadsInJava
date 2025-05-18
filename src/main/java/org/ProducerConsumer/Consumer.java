package org.ProducerConsumer;

public class Consumer implements Runnable{
    SharedQueue sharedQueue;
    int messageCount;
    Consumer(SharedQueue sharedQueue_, int messageCount_){
        sharedQueue = sharedQueue_;
        messageCount = messageCount_;
    }


    public void run() {
        for(int i=0; i<messageCount; i++){
            try {
                Message consumedMessage = sharedQueue.consume();
                System.out.println("Consumed message is: " + consumedMessage.getMessage());
            } catch (QueueEmptyException | InterruptedException e) {
                System.out.println("Exception thrown while reading from queue! Please validate.");
            }
        }
    }
}
