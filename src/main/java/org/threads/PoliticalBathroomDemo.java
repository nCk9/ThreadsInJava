package org.threads;

class SharedBathroom {
    private int occupancyCount = 0;
    private String currentPartyName = "NONE";

    private int waitingDemocrats = 0;
    private int waitingRepublicans = 0;

    private int consecutiveCount = 0;
    private final int STARVATION_LIMIT = 10;

    // 1. Entering the Bathroom
    public synchronized void enterBathroom(String partyName) throws InterruptedException {
        // Register that this person is waiting
        if (partyName.equals("Democrat")) {
            waitingDemocrats++;
        } else {
            waitingRepublicans++;
        }

        // The Wait Loop: Check if it's safe to enter
        while (!canEnter(partyName)) {
            this.wait(); // Not safe! Release the lock and go to sleep
        }

        // It is safe to enter! De-register from waiting
        if (partyName.equals("Democrat")) {
            waitingDemocrats--;
        } else {
            waitingRepublicans--;
        }

        // Update the bathroom state
        if (occupancyCount == 0) {
            currentPartyName = partyName; // Claim the bathroom for the party
        }

        occupancyCount++;
        consecutiveCount++;

        System.out.println(partyName + " entered. Occupancy: " + occupancyCount +
                ", Consecutive " + partyName + "s: " + consecutiveCount);
    }

    // 2. Leaving the Bathroom
    public synchronized void leaveBathroom() {
        String leavingParty = currentPartyName;
        occupancyCount--;

        System.out.println(leavingParty + " left. Occupancy: " + occupancyCount);

        // If the bathroom is now empty, reset the state
        if (occupancyCount == 0) {
            currentPartyName = "NONE";
            consecutiveCount = 0; // Reset the streak!
        }

        // Wake up EVERYONE waiting so they can check their while loops
        this.notifyAll();
    }

    // 3. The Core Logic Helper
    private boolean canEnter(String partyName) {
        // Rule 1: If it's empty, anyone can enter
        if (occupancyCount == 0) {
            return true;
        }

        // Rule 2: If the OTHER party is inside, you cannot enter
        if (!currentPartyName.equals(partyName)) {
            return false;
        }

        // Rule 3: If YOUR party is inside, check the starvation limit
        if (currentPartyName.equals(partyName)) {
            boolean opposingPartyWaiting = partyName.equals("Democrat") ?
                    (waitingRepublicans > 0) : (waitingDemocrats > 0);

            // If we hit the limit AND the other side is waiting, we must stop entering
            if (consecutiveCount >= STARVATION_LIMIT && opposingPartyWaiting) {
                return false;
            }

            // Otherwise, come on in!
            return true;
        }

        return false;
    }
}

public class PoliticalBathroomDemo {
    public static void main(String[] args) {
        SharedBathroom bathroom = new SharedBathroom();

        // Create a task representing a person
        Runnable personTask = () -> {
            String partyName = Thread.currentThread().getName().contains("Democrat") ? "Democrat" : "Republican";
            try {
                bathroom.enterBathroom(partyName);
                Thread.sleep((long) (Math.random() * 1000)); // Simulate time spent inside
                bathroom.leaveBathroom();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        // Spin up a bunch of threads
        for (int i = 0; i < 15; i++) {
            new Thread(personTask, "Republican-" + i).start();
        }
        for (int i = 0; i < 15; i++) {
            new Thread(personTask, "Democrat-" + i).start();
        }
    }
}