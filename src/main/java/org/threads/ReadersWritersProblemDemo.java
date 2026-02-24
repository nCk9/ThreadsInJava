package org.threads;


class Db{
    private int dbVal = 0;
    private int activeReaders = 0;
    private int activeWriters = 0;
    private int waitingWriters = 0;

    Db(){

    }
    //gteKeeper pattern. Why we needed it here is reads can be concurrent and time taking (reading/writing a file might take in seconds)
    public synchronized void startWriting() throws InterruptedException {
        waitingWriters++;
        while(activeReaders > 0 || activeWriters > 0){ // this ensures that even if one writer is already writing, we'd still wait. So while writing only one can write!
            wait();
        }
        activeWriters++;
        waitingWriters--;
    }

    public synchronized void writeData(int newVal){ //synchronized not necessarily required here
        dbVal = newVal;
    }

    public synchronized void stopWriting() {
        activeWriters--;
//        if(activeWriters == 0) {
            notifyAll();
//        }
    }

    public synchronized void startReading() throws InterruptedException {
        while (activeWriters > 0 || waitingWriters > 0){
            wait();
        }
        activeReaders++;
    }

    public int readData() { //no need of synchronized here as we enable concurrent reads together.
        return dbVal;
    }

    public synchronized void stopReading(){
        activeReaders--;
        if(activeReaders == 0){
            notifyAll();
        }
    }
}

public class ReadersWritersProblemDemo {
    public static void main(String[] args) throws InterruptedException {
        Db tempDb = new Db();
        Thread writer = new Thread(() ->{
            for(int i=0; i<150; i++){
                try {
                    tempDb.startWriting();
                    tempDb.writeData(i);
                    tempDb.stopWriting();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }, "writer");

        Thread reader = new Thread(() ->{
            for(int i=0; i<100; i++) {
                try {
                    tempDb.startReading();
                    int readVal = tempDb.readData();
                    tempDb.stopReading();
                    System.out.println("The read value is: " + readVal);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "reader");

        writer.start();
        reader.start();

        Thread.sleep(1000);

        writer.join();
        reader.join();
    }
}
