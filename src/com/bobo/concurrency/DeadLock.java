package com.bobo.concurrency;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * 经典死锁问题
 */
public class DeadLock {
    private static Object objectA = new Object();
    private static Object objectB = new Object();

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (objectA){
                    System.out.println("thread1获取到A锁，并且开始睡觉3s");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (objectB) {
                        System.out.println("thread1 获取到B锁");
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (objectB) {
                    System.out.println("thread2 获取到了B锁,并且开始睡觉3s");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (objectA) {
                        System.out.println("thread2 获取到了A锁");
                    }
                }
            }
        }).start();
    }
}
