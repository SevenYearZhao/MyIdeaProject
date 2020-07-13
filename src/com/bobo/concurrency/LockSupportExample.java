package com.bobo.concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author bobo
 * @date 2020-07-10
 * 1/wait notify 和 condition的await signal都必须先wait后notify，先await后signal
 * 2/lockSupport支持先unpark后park，其原理是：每一个线程都有一个permit许可，默认为0。
 *      当调用park时，如permit为0则阻塞，permit为1则置为0立即返回。
 *      当调用unpark时，如permit为0时若没有阻塞则置为1，若阻塞则permit置为1后唤醒当前线程后再将permit置为0
 *                      当permit为1时，则还是1不会累加。
 */

public class LockSupportExample {

    /**
     * 测试unpark可以先于park执行
     */
    public void m1() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("park");
                LockSupport.park();
                System.out.println("end");
            }
        });
        t1.start();
        System.out.println("start");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LockSupport.unpark(t1);
        System.out.println("unpark");
    }

    public void m2(){
        Thread t2=new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("isInterrupted:"+Thread.currentThread().isInterrupted());
                LockSupport.park();
                System.out.println("isInterrupted:"+Thread.currentThread().isInterrupted());
            }
        });
        t2.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("开始中断线程");
        t2.interrupt();

    }

    public static void main(String[] args) {
        LockSupportExample example = new LockSupportExample();
//        example.m1();
        example.m2();
    }
}
