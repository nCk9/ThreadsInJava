package org.DiningPhilosopherProblem;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosopherReentrant {
    public static void main(String[] args) {
        int numPhilosophers = 5;
        Lock[] chopsticks = new ReentrantLock[numPhilosophers];

        for (int i = 0; i < numPhilosophers; i++) {
            chopsticks[i] = new ReentrantLock();
        }

        for (int i = 0; i < numPhilosophers; i++) {
            // Logic: Philosopher i sits between chopstick i and (i+1)%num
            Lock left = chopsticks[i];
            Lock right = chopsticks[(i + 1) % numPhilosophers];

            // Breaking the circular wait: last philosopher picks right-to-left
            if (i == numPhilosophers - 1) {
                new Thread(new Philosopher(i, right, left)).start();
            } else {
                new Thread(new Philosopher(i, left, right)).start();
            }
        }
    }

    static class Philosopher implements Runnable {
        private final int id;
        private final Lock leftLock;
        private final Lock rightLock;

        public Philosopher(int id, Lock left, Lock right) {
            this.id = id;
            this.leftLock = left;
            this.rightLock = right;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("Philosopher " + id + " is thinking...");
                    Thread.sleep(((long) (Math.random() * 1000)));

                    // Pick up left chopstick
                    leftLock.lock();
                    try {
                        System.out.println("Philosopher " + id + " picked up left chopstick.");

                        // Pick up right chopstick
                        rightLock.lock();
                        try {
                            System.out.println("Philosopher " + id + " is EATING.");
                            Thread.sleep(((long) (Math.random() * 1000)));
                        } finally {
                            rightLock.unlock(); // Always unlock in finally
                            System.out.println("Philosopher " + id + " put down right chopstick.");
                        }
                    } finally {
                        leftLock.unlock();
                        System.out.println("Philosopher " + id + " put down left chopstick.");
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Philosopher " + id + " was interrupted.");
            }
        }
    }
}

