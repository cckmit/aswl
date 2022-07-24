package com.aswl.as.asos;

public class Test8 {

    public static void main(String[] args) {


//        TestThread t1=new TestThread("线程1");
//        TestThread t2=new TestThread("线程2");
//        new Thread(t1).start();
//        new Thread(t2).start();

        TestThread1 tt1=new TestThread1("线程3");

        tt1.start();

        try
        {
            Thread.sleep(100);
            System.out.println(TestThread.value);
            tt1.interrupt();
        }catch (Exception e)
        {
            System.out.println("线程报错中断");
        }





    }



}
