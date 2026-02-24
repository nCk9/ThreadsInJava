package org.threads;

class OddEvenCounter{
    private int counter = 0;
    private int limit;

    public OddEvenCounter(int N){
        limit = N;
    }

    public synchronized void printOdd() throws InterruptedException {

        while(counter%2 == 0){
            wait();
        }

        if (counter >= limit) {
            notifyAll();
            Thread.currentThread().interrupt();
        }
        counter++;
        System.out.println(counter);
        notifyAll();
    }

    public synchronized void printEven() throws InterruptedException {
        while(counter%2 == 1){
            wait();
        }

        if (counter >= limit) {
            notifyAll();
            Thread.currentThread().interrupt();
        }
        counter++;
        System.out.println(counter);
        notifyAll();
    }

}

public class IncreasingEvenOddNumbers {

    public static void main(String[] args) throws InterruptedException {
        int N = 100;
        OddEvenCounter oddEvenCounter = new OddEvenCounter(N);
        Thread odds = new Thread(() -> {
            while(true){
                try {
                    oddEvenCounter.printOdd();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        Thread evens = new Thread(() -> {
            while (true){
                try {
                    oddEvenCounter.printEven();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        evens.start();
        odds.start();
        evens.join();
        odds.join();
    }
}
