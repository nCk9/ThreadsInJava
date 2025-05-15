package org.ProducerConsumer;

public class QueueSuffocateException extends Exception{

    QueueSuffocateException(String exceptionMessage){
        super(exceptionMessage);
    }
}
