package org.ProducerConsumer;


public class Producer implements Runnable{

    SharedQueue sharedQueue;
    int messageCount;

    Producer(SharedQueue sharedQueue_, int messageCount_){
        sharedQueue = sharedQueue_;
        messageCount = messageCount_;
    }
    String generateMessage(){
        Integer randInt = (int) (Math.random()*1000);
        return ("heyyyyyyyy"+ randInt.toString());
    }

    @Override
    public void run() {
        for (int i = 0; i < messageCount; i++) {
            Message payload = new Message();
            String message = generateMessage();
            payload.setMessage(message);
            try {
                sharedQueue.produce(payload);
            } catch (QueueSuffocateException | InterruptedException e) {
                System.out.println("Exception occurred during producing message to queue!");
            }
            System.out.println("Produced message is: " + payload.getMessage());
        }
    }
}
