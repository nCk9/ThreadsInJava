package org.threads;

class Counter{
    int count;
    Counter(){
        count = 0;
    }

    public synchronized void incrementCount(){
        count++;
    }

    public synchronized int getCount(){
        return count;
    }
}

public class Synchronization {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("Welcome to synchronization demo!");
        Counter incrementor = new Counter();
        Runnable run1 = new Runnable(){
            public void run(){
                for(int i = 0; i<1000; i++)
                    incrementor.incrementCount();
            }
        };
        Thread t1 = new Thread(run1);

        Runnable run2 = () -> {
            for(int i=0; i<1000; i++)
                incrementor.incrementCount();
        };
        Thread t2 = new Thread(run2);
        t2.start();
        t1.start();
        t1.join();
        t2.join();
        System.out.println("Final count = " + incrementor.getCount());
    }
}
