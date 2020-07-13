package com.bobo.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author bobo
 * @date 2020-07-13
 * 闭锁
 */

public class CountDownLatchExample {

    private static class MyTask extends Thread {
        private CountDownLatch camLatch, runLatch;
        private int sleepTime;

        public MyTask(String threadName, int sleepTime, CountDownLatch camLatch, CountDownLatch runLatch) {
            super(threadName);
            this.sleepTime = sleepTime;
            this.camLatch = camLatch;
            this.runLatch = runLatch;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " 等待信号枪...");
                camLatch.await();
                System.out.println(Thread.currentThread().getName() + " 开始跑...");
                TimeUnit.SECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(Thread.currentThread().getName() + " 结束");
                runLatch.countDown();
            }
        }
    }

    public static void main(String[] args) {
        int size = 5;
        CountDownLatch camLatch = new CountDownLatch(1);
        CountDownLatch runLatch = new CountDownLatch(5);
        for (int i = 0; i < size; i++) {
            new MyTask("t-" + i, i, camLatch, runLatch).start();
        }
        System.out.println("信号枪响");
        long startTime = System.currentTimeMillis();
        camLatch.countDown();
        try {
            runLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long stoptime = System.currentTimeMillis();
        System.out.println("跑步结束：" + (stoptime - startTime));

    }
}
