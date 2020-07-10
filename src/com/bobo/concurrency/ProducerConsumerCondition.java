package com.bobo.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author bobo
 * @date 2020-07-10
 * condition实现生产者消费者
 */

public class ProducerConsumerCondition {

    public static class Storage {
        ReentrantLock lock = new ReentrantLock();
        Condition fullCondition = lock.newCondition();
        Condition emptyCondition = lock.newCondition();

        private int max_capacity;
        private List<Object> list = new ArrayList<>();

        public Storage(int max_capacity) {
            this.max_capacity = max_capacity;
        }

        public void push(Object o) {
            lock.lock();
            try {
                while (list.size() >= max_capacity) {
                    System.out.println(Thread.currentThread().getName() + " full please wait");
                    fullCondition.await();
                }
                list.add(o);
                System.out.println(Thread.currentThread().getName() + " push success:" + list.size());
                emptyCondition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void pull() {
            lock.lock();
            try {
                while (list.size() == 0) {
                    System.out.println(Thread.currentThread().getName() + " empty please wait");
                    emptyCondition.await();
                }
                list.remove(0);
                System.out.println(Thread.currentThread().getName() + " pull success:" + list.size());
                fullCondition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    private static class Producer extends Thread {
        private Storage storage;

        public Producer(Storage storage, String threadName) {
            super(threadName);
            this.storage = storage;
        }

        @Override
        public void run() {
            while (true) {
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
            super(threadName);
            this.storage = storage;
        }

        @Override
        public void run() {
            while (true) {
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
        int size = 5;
        Storage storage = new Storage(size);
        for (int i = 0; i < 5; i++) {
            new Producer(storage, "producer " + i).start();
            new Consumer("consumer " + i, storage).start();
        }
    }
}
