package org.DiningPhilosopherProblem;

import java.util.Random;

public class DiningPhilosopher {
    public static void main(String[] args) {
        final int NUM_PHILOSOPHERS = 5;
        Object[] chopsticks = new Object[NUM_PHILOSOPHERS];

        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            chopsticks[i] = new Object();
        }

        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            Object leftChopstick = chopsticks[i];
            Object rightChopstick = chopsticks[(i + 1) % NUM_PHILOSOPHERS];

            // Asymmetric pick-up to prevent deadlock
            if (i == NUM_PHILOSOPHERS - 1) {
                new Thread(new Philosopher(i, rightChopstick, leftChopstick)).start();
            } else {
                new Thread(new Philosopher(i, leftChopstick, rightChopstick)).start();
            }
        }
    }
}

class Philosopher implements Runnable {
    private final int id;
    private final Object leftChopstick;
    private final Object rightChopstick;
    private final Random random = new Random();

    public Philosopher(int id, Object left, Object right) {
        this.id = id;
        this.leftChopstick = left;
        this.rightChopstick = right;
    }

    private void performAction(String action) throws InterruptedException {
        System.out.println("Philosopher " + id + " is " + action);
        Thread.sleep(random.nextInt(1000));
    }

    @Override
    public void run() {
        try {
            while (true) {
                performAction("thinking");
                synchronized (leftChopstick) {
                    performAction("picked up left chopstick");
                    synchronized (rightChopstick) {
                        performAction("eating"); // Critical section
                    }
                    performAction("put down right chopstick");
                }
                performAction("put down left chopstick");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

