package org.ProducerConsumer;

import sun.lwawt.macosx.CSystemTray;

import java.util.Scanner;

public class ProducerConsumerProblem {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Demonstrating Producer Consumer Problem.");
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Enter the capacity of the shared queue: ");
        Integer size = inputScanner.nextInt();
        SharedQueue sharedQueue = new SharedQueue(size);
        Producer producer = new Producer();
        Runnable pr = () -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Message messageBody = new Message();
                    producer.produceMessage(sharedQueue, messageBody);
                    System.out.println("Message produced successfully!");
                } catch (QueueSuffocateException e) {
                    System.out.println(e.getMessage());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Runnable con = () -> {
            Consumer consumer = new Consumer();
            for (int i = 0; i < 10; i++) {
                try {
                    Message message = consumer.consumeMessage(sharedQueue);
                    System.out.println("Message consumed successfully!");
                } catch (QueueEmptyException e) {
                    System.out.println(e.getMessage());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Thread producerThread = new Thread(pr);
        Thread consumerThread = new Thread(con);
        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();
        System.out.println("Demonstration done!! thanks!");
        System.out.println("Shared queue size at the end: " + sharedQueue.size);
    }
}
