package org.ProducerConsumer;

public class QueueEmptyException extends Exception{

    QueueEmptyException(String exceptionMessage){
        super(exceptionMessage);
    }
}
