package org.threads;

class H2OProduction{
    private int countH = 0;
    private int countO = 0;

    H2OProduction(){

    }

    public synchronized void releaseH() throws InterruptedException {
        while(countH == 2){
            wait();
        }
        //following line will run after countH is reset to 0
        countH++;
        checkIfMoleculeComplete();
    }

    public synchronized void releaseO() throws InterruptedException {
        while(countO == 1) {
            wait();
        }
        countO++;
        checkIfMoleculeComplete();
    }

    private void checkIfMoleculeComplete() {
        if(countO == 1 && countH == 2) {
            countH = 0;
            countO = 0;
            System.out.println("Released H2O!!");
        }
        notifyAll();
    }

}
public class H2OProblemDemo {
    public static void main(String[] args) throws InterruptedException {

        H2OProduction h2OProduction = new H2OProduction();
        new Thread(() -> {
            while(true){
                try {
                    h2OProduction.releaseO();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "oxygen").start();

        new Thread(() -> {
            while(true){
                try {
                    h2OProduction.releaseH();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


    }
}
