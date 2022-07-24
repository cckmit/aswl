package com.aswl.as.asos;

public class ThreadInterruptDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new InterruptThread("thread_1");
        thread.start();
        Thread.sleep(1);
        System.out.println(thread.getName() + "线程设置:interrupt");
        thread.interrupt();
    }
}

class InterruptThread extends Thread {

    public InterruptThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "线程开始!");
        for(int i =0; i < 1000; i++) {
            try {
                Thread.sleep(0);
                System.out.println("" + (i + 1));
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "线程捕获异常,退出循环!");
                break;
            }
        }
        System.out.println(Thread.currentThread().getName() + "线程结束!");
    }
}