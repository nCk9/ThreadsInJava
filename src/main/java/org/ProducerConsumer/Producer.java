package org.ProducerConsumer;

public class Producer {

    String generateMessage(){
        Integer randInt = (int) (Math.random()*1000);
        return ("heyyyyyyyy"+ randInt.toString());
    }

    void produceMessage(SharedQueue sharedQueue, Message payload) throws QueueSuffocateException {
        String message  = generateMessage();
        payload.setMessage(message);
        sharedQueue.produce(payload);
        System.out.println("Produced message is: " + payload.getMessage());
    }
}
