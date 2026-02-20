package org.threads;

class Counter{
    int count;
    Counter(){
        count = 0;
    }
//how synchronized works is:
    //every object in java has an associated monitor to it. A monitor/mutex is mechanism to control concurrent access to an object.

    public synchronized void incrementCount(){// the thread calling this objects' methods gets the object's monitor
        count++;
    } //that thread releases the monitor of the object which called the method.

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
        Thread t3 = new Thread(()->{
           for (int i=0; i<1000; i++)
                incrementor.incrementCount();
        });

        t2.start();
        t1.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        System.out.println("Final count = " + incrementor.getCount());
    }
}
