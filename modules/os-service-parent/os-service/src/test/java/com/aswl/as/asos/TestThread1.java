package com.aswl.as.asos;

public class TestThread1 extends Thread {

    public static int value=0;

    private static Object key=new Object();

    public TestThread1(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public void run() {

        try
        {
            while(!isInterrupted()) {
                for(int i=0;i<100000000;i++)
                {
                    System.out.println("名字为"+name+"现在的值为"+i);
                    Thread.sleep(10);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("线程内中断");
        }

    }

}
