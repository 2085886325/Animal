package com.project.animal;

import org.junit.jupiter.api.Test;

public class ThreadLocalTest {

    @Test
    public void testThreadLocalGetAndSet(){
        ThreadLocal<Object> threadLocal = new ThreadLocal<>();

        new Thread(()->{
            threadLocal.set("绿色");
            System.out.println(Thread.currentThread().getName()+": "+threadLocal.get());
            System.out.println(Thread.currentThread().getName()+": "+threadLocal.get());
            System.out.println(Thread.currentThread().getName()+": "+threadLocal.get());
        },"name：萧炎").start();

        new Thread(()->{
            threadLocal.set("红色");
            System.out.println(Thread.currentThread().getName()+": "+threadLocal.get());
            System.out.println(Thread.currentThread().getName()+": "+threadLocal.get());
            System.out.println(Thread.currentThread().getName()+": "+threadLocal.get());
        },"name：药尘").start();

    }
}
