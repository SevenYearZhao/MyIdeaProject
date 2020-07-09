package com.bobo.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author bobo
 * @date 2020-07-09
 * wait notify 实现生产者消费者
 */

public class ProducerConsumerWaitNotify {
    private static class Storage {
        private int MAX_CAPACITY = 10;

        public Storage(int size) {
            this.MAX_CAPACITY = size;
        }

        private List<Object> list = new ArrayList<>(MAX_CAPACITY);

        public synchronized void push(Object o) {
            while (list.size() >= MAX_CAPACITY) {
                System.out.println(Thread.currentThread().getName() + " 当前容量已满，清稍等！");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.add(o);
            System.out.println(Thread.currentThread().getName() + " add success:" + list.size());
            notifyAll();
        }

        public synchronized void pull() {
            while (list.size() == 0) {
                System.out.println(Thread.currentThread().getName() + "仓库已空，请等待！");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.remove(0);
            System.out.println(Thread.currentThread().getName() + " 已出库：" + list.size());
            notifyAll();
        }
    }

    private static class Producer extends Thread {

        private Storage storage;

        public Producer(String threadName, Storage storage) {
            super(threadName);
            this.storage = storage;
        }

        @Override
        public void run() {
            while (true){
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                storage.push(new Object());
            }
        }
    }

    private static class Consumer extends Thread {
        private Storage storage;

        public Consumer(String threadName, Storage storage) {
            this.storage = storage;
        }

        @Override
        public void run() {
            while (true){
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                storage.pull();
            }
        }
    }

    public static void main(String[] args) {
        int count = 5;
        Storage storage = new Storage(count);
        for (int i = 0; i < count; i++) {
            new Producer("producer " + i, storage).start();
            new Consumer("consumer " + i, storage).start();
        }
    }
}
