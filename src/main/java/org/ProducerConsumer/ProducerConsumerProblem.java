package org.ProducerConsumer;

import java.util.Scanner;

public class ProducerConsumerProblem {
    public static void main(String[] args){
        System.out.println("Demonstrating Producer Consumer Problem.");
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Enter the capacity of the shared queue: ");
        Integer size = inputScanner.nextInt();
        SharedQueue sharedQueue = new SharedQueue(size);
        Producer producer = new Producer();
        for(int i=0; i<10; i++){
            try {
                Message messageBody = new Message();
                producer.produceMessage(sharedQueue, messageBody);
                System.out.println("Message produced successfully!");
            } catch (QueueSuffocateException e) {
                System.out.println(e.getMessage());
            }
        }
        Consumer consumer = new Consumer();
        for(int i=0; i<10; i++){
            try {
                Message message = consumer.consumeMessage(sharedQueue);
                System.out.println("Message consumed successfully!");
            }catch(QueueEmptyException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
