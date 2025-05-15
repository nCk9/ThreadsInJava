package org.ProducerConsumer;

import com.sun.xml.internal.ws.message.PayloadElementSniffer;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;
import sun.lwawt.macosx.CSystemTray;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.ArrayList;

public class SharedQueue {
    ArrayList<Message> sharedQueue;
    Integer qCapacity;

    int front, rear, size;

    SharedQueue(Integer capacity){
        sharedQueue = new ArrayList<>(capacity);
        for(int i=0; i<capacity; i++)
            sharedQueue.add(new Message());
        qCapacity = capacity;
        front = 0;
        rear = 0;
        size = 0;
        System.out.println("Shared queue size is: " + sharedQueue.size());
    }

    synchronized void produce(Message payload) throws QueueSuffocateException, InterruptedException {
        if(size == qCapacity) {
            wait();
            System.out.println("Queue capacity exhausted, waiting for consumer to consume messages!");
        }

        rear = (front + size) % qCapacity;
        sharedQueue.set(rear, payload);
        size++;
        notify();
    }

    synchronized Message consume() throws QueueEmptyException, InterruptedException {
        if(size == 0){
            wait();
            System.out.println("No new messages to consume, waiting for producer to produce messages!");
        }

        Message readMessage = sharedQueue.get(front);
            front = (front + 1) % qCapacity;
            size--;
        notify();
        return readMessage;
    }

}
