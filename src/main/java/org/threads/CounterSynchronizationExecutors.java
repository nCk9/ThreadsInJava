package org.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CounterSynchronizationExecutors {

//    private static AtomicInteger counter = new AtomicInteger(0);
    private static int counter = 0;

    public static synchronized void incrementCount(){
        counter++;
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i=0; i<1000; i++){
            executorService.submit(CounterSynchronizationExecutors::incrementCount);
        }
        executorService.shutdown();
        boolean flag = executorService.awaitTermination(3000, TimeUnit.MILLISECONDS);
        System.out.println("the counter has reached to: " + counter);
        if(flag){
            System.out.println("termination of all threads successful!");
        }else{
            System.out.println("awaitTermination() timed out!");
        }

    }
}
