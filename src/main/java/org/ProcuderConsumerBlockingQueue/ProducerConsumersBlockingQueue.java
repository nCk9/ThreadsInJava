package org.ProcuderConsumerBlockingQueue;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerConsumersBlockingQueue {

    private static final int capacity = 10;
    private static final BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(capacity);
    private static final int numberOfConsumers = 50;
    private static AtomicInteger producedMessages = new AtomicInteger(0);
    private static AtomicInteger consumedMessages = new AtomicInteger(0);
    private static final String POISON_PILL = "__STOP__";
    private static final AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        Thread producer = new Thread(new Producer());
        producer.start();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i=0; i<numberOfConsumers; i++){
            executorService.submit(new Consumer());
        }

        Thread.sleep(1000);
        producer.interrupt();
        executorService.shutdown();

        producer.join();
        if(executorService.awaitTermination(1000, TimeUnit.MILLISECONDS)){
            System.out.println("Graceful shutdown complete!!");
            System.out.println("Total messages produced :" + producedMessages.get());
            System.out.println("Total messages consumed :" + consumedMessages.get());
            System.out.println("Total consumers terminated: " + count.get());
        }else{
            System.out.println("Still awaiting termination!");
        }

    }

    public static class Producer implements Runnable{
        @Override
        public void run(){
            String message = "Event pushed -> ";
            try {
                while(!Thread.currentThread().isInterrupted()) {
                    blockingQueue.put(message); //blocking
                    producedMessages.incrementAndGet();
                }
            }catch(InterruptedException exception){
                Thread.currentThread().interrupt(); //resetting the interrupt flag
            }finally {
                //delivering the poison pill for each consumer to initiate termination
                ArrayList<Boolean> delivered = new ArrayList<>(numberOfConsumers);
                for(int i=0; i<numberOfConsumers; i++) {
                    delivered.add(false);
                    //the following while ensures that the POISON PILL is delivered for each consumer no mater what!
                    while (!delivered.get(i)) {
                        try {
                            //offer will block for 100 ms and then return false if not able to push the item, otherwise true
                            delivered.set(i, blockingQueue.offer(POISON_PILL, 100, TimeUnit.MILLISECONDS));
                        }catch(InterruptedException exception){
                            //control will reach here only if some interrupt happens when offer is in blocking state
                            System.out.println("Retrying pushing event after an interrupt..");
                        }
                    }
                }
            }
        }
    }

    public static class Consumer implements Runnable{
        @Override
        public void run(){
            try{
                while (true) {
                    String consumedMessage = blockingQueue.take(); //blocking call
                    if (consumedMessage.equals(POISON_PILL)) {
                        System.out.println("Consumer " + Thread.currentThread().getId() + " received poison pill, terminating further consumption!");
                        count.getAndIncrement();
                        break;
                    }
                    consumedMessages.getAndIncrement();
                }
            }catch (InterruptedException exception){
                Thread.currentThread().interrupt();
            }
        }
    }
}

