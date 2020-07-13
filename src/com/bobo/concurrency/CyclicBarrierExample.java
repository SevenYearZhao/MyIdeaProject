package com.bobo.concurrency;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @author bobo
 * @date 2020-07-13
 * 循环屏障/栅栏
 * CyclicBarrier使等待的线程达到一定数目后再让它们继续执行。
 */

public class CyclicBarrierExample {
    static CyclicBarrier cyclicBarrier = new CyclicBarrier(10);

    private static class MyTask extends Thread {
        public MyTask(String threadName) {
            super(threadName);
        }

        @Override
        public void run() {
            try {
                int sleepTime = new Random().nextInt(10);
                System.out.println(Thread.currentThread().getName() + " 需要：" + sleepTime + " 秒才能到达战场");
                TimeUnit.SECONDS.sleep(sleepTime);
                System.out.println(Thread.currentThread().getName() + "到达战场");
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName() + "结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new MyTask("t-" + i).start();
        }
    }
}
