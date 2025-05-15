package org.threads;

import sun.awt.windows.ThemeReader;

import java.awt.*;

//class A implements Runnable{
//    public void run(){
//        for(int i=0; i<100; i++)
//            System.out.println("hi.");
//    }
//}

public class ThreadCreation {
    public static void main(String[] args){

        Runnable objA = () -> { //lambda expression -> because Runnable is a functional interface!
            for(int i=0; i<100; i++)
                System.out.println("hi!!");
        };
//        obja.run();
        Runnable objB = new Runnable() { //anonymous classs!
            public void run(){
                for(int i=0; i<100; i++)
                    System.out.println("hellow!");
            }
        };
//        objB.run();
        Thread t1 = new Thread(objA);
        t1.start();
        try{
            Thread.sleep(2);
        }catch (IllegalArgumentException | InterruptedException e){
            System.out.println("Caught exception!");
        }

        Thread t2 = new Thread(objB);
        t2.start();

    }
}