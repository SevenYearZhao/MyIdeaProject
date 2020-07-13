package com.bobo.concurrency;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author bobo
 * @date 2020-07-13
 * Semaphore常用场景：限流
 */

public class SemaphoreExample {

    /**
     * 默认非公平
     */
    static Semaphore semaphore = new Semaphore(2);

    private static class MyTask extends Thread {

        public MyTask(String threadName) {
            super(threadName);
        }

        @Override
        public void run() {
            boolean acquireSuccess = false;
            try {
                System.out.println(Thread.currentThread().getName() + " start acquire");
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + " acquire success");
                acquireSuccess = true;
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (acquireSuccess) {
                    System.out.println(Thread.currentThread().getName() + " release");
                    semaphore.release();
                }
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new MyTask("t-" + i).start();
        }
    }
}
