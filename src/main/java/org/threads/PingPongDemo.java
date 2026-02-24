package org.threads;

//actors-> ping, pong, console (the shared resource)
class ConsoleLog{
    private String lastPrinted = "PONG";

    ConsoleLog(){}

    public synchronized void printPing() throws InterruptedException {
        while(!lastPrinted.equals("PONG"))
            wait();
        System.out.println("PING");
        lastPrinted = "PING";
        notifyAll();
    }

    public synchronized void printPong() throws InterruptedException {
        while(!lastPrinted.equals("PING"))
            wait();
        System.out.println("PONG");
        lastPrinted = "PONG";
        notifyAll();
    }
}

public class PingPongDemo {
    public static void main(String[] args){
        ConsoleLog consoleLog = new ConsoleLog();
        new Thread(() -> {
            while(true){
                try {
                    consoleLog.printPing();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "ping").start();

        new Thread(() -> {
            while(true){
                try {
                    consoleLog.printPong();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "pong").start();
    }
}
