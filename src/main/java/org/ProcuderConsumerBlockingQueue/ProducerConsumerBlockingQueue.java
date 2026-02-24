package org.ProcuderConsumerBlockingQueue;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerConsumerBlockingQueue {

    public static void main(String[] args) throws InterruptedException {
        int capacity = 10;
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(10); //ArrayBlockingQueue is bounded, we must specify capacity
        AtomicInteger producerCount = new AtomicInteger(0);
        AtomicInteger consumerCount = new AtomicInteger(0);
        String POISON_PILL = "__STOP__";
        //using blocking queue makes our lives easier. We don't have to think about blocking procedure calls like wait()/sleep() etc., it will handle on our behalf

        Thread producer = new Thread(() -> {
            //while the thread isn't interrupted, keep on trying to push elements into the queue
            int i=0;
            try {
                while(!Thread.currentThread().isInterrupted()){
                //if you do this try/catch/finally inside while loop, there's a possibility that the poison pill never gets delivered at all.
                    String event = "Event "+ i + " pushing into the queue!";
                        blockingQueue.put(event); //this throws InterruptedException; its a blocking call. Alternatively we can use offer (non-blocking/partially-blocking)
                        System.out.println("Produced event -> " + event);
                        i = producerCount.getAndIncrement();
                    }
            }catch(InterruptedException e){
                //this is called re-interrupt pattern. Why we need re-interruption? because while throwing the interrupted exception the thread resets the interrupted flag to false
                Thread.currentThread().interrupt();
            }finally {
                boolean delivered = false;
                //why we needed while loop here:  lets say we had used 'put' instead of offer and the queue is full already, then 'put' will lead to blocking that thread forever (no progress). If we use offer, we have a timeout option. Doing that timeout thing again and again requires a while loop.
                //this is how we ensure that the poison pill gets delivered no matter what!
                while(!delivered){
                    try{
                        delivered = blockingQueue.offer(POISON_PILL, 100, TimeUnit.MILLISECONDS); // it stays in the blocking state for 100 milliseconds, then returns false if still not able to publish data to queue
                    }catch (InterruptedException ignored){
                        System.out.println("Retrying poison pill enqueue after interrupt!");
                    }
                }
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                while(true) {
                    String retrievedEvent = blockingQueue.take(); //again, a blocking call if there's no event to consume
                    System.out.println("Consumed event -> " + retrievedEvent);
                    if (retrievedEvent.equals(POISON_PILL)) {
                        System.out.println("Consumer received poison pill, Exiting...");
                        break;
                    }
                    consumerCount.getAndIncrement();
                }
            }catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        Thread.sleep(2000);

        producer.interrupt(); //interrupting producer thread from the main thread.
        producer.join();
        consumer.join();
        System.out.println("Total produced events: " + producerCount.get());
        System.out.println("Total consumed events: " + consumerCount.get());
    }
}
