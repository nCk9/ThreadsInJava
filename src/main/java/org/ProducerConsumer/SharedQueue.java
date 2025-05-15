package org.ProducerConsumer;

import com.sun.xml.internal.ws.message.PayloadElementSniffer;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;
import sun.lwawt.macosx.CSystemTray;

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

    void produce(Message payload) throws QueueSuffocateException {
        if(size == qCapacity) {
            throw new QueueSuffocateException("Queue capacity exhausted, wait for consumer to consume messages!");
        }

        rear = (front + size)%qCapacity;
        sharedQueue.set(rear, payload);
        size++;
    }

    Message consume() throws QueueEmptyException {
        if(size == 0){
            throw new QueueEmptyException("No new messages to consumer, wait for producer to produce messages!");
        }
        Message readMessage = sharedQueue.get(front);
        front = (front+1)%qCapacity;
        size--;
        return readMessage;
    }

}
