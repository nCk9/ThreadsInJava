package org.ProducerConsumer;

import sun.lwawt.macosx.CSystemTray;

import java.util.ArrayList;
import java.util.Scanner;

public class ProducerConsumerProblem {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Demonstrating Producer Consumer Problem.");
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Enter the capacity of the shared queue: ");
        Integer size = inputScanner.nextInt();
        SharedQueue sharedQueue = new SharedQueue(size);
        int numberOfProducers = 1;
        int numberOfConsumers = 5;
        int messageCount = 10;
        ArrayList<Thread> producers = new ArrayList<>(numberOfProducers);
        ArrayList<Thread> consumers = new ArrayList<>(numberOfConsumers);
        for(int i=0; i<numberOfProducers; i++) {
            producers.add(new Thread(new Producer(sharedQueue, messageCount)));
            producers.get(i).start();
        }

        for(int i=0; i<numberOfConsumers; i++){
            consumers.add(new Thread(new Consumer(sharedQueue, messageCount)));
            consumers.get(i).start();
        }

        for(Thread producer: producers)
            producer.join();

        for(Thread consumer: consumers)
            consumer.join();

        System.out.println("Demonstration done!! thanks!");
        System.out.println("Shared queue size at the end: " + sharedQueue.size);
    }
}
