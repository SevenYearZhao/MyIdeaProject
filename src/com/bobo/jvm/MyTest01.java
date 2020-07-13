package com.bobo.jvm;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyTest01 {

    public static void main(String[] args) {
        int i = 8;
        i = i++;
        System.out.println(i);
    }
}
