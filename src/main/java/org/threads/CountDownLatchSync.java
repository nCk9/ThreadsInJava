package org.threads;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Processor implements Runnable{
    private final CountDownLatch latch;

    public Processor(CountDownLatch latch_){
        latch = latch_;
    }

    @Override
    public void run(){
        System.out.println("reducing CountDownLatch count!");
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        latch.countDown();
    }

}

public class CountDownLatchSync {
    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(3); //initial count is 3
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for(int i=0 ;i<3; i++){
            executorService.submit(new Processor(countDownLatch));
        }

        try{
            countDownLatch.await();
        }catch (InterruptedException exception){
            //either re-interrupt or throw some exception
            Thread.currentThread().interrupt();
        }
        executorService.shutdown();
        if(executorService.awaitTermination(100, TimeUnit.MILLISECONDS)) {
            System.out.println("Processing completed");
        }
    }
}
