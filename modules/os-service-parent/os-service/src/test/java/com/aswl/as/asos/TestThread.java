package com.aswl.as.asos;

public class TestThread implements Runnable {

    public static int value=0;

    private static Object key=new Object();

    public TestThread(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public void run() {
        for(int i=0;i<100000;i++)
        {
            try
            {
//                synchronized (key)
//                {
//                    value=value+1;
//                }
//                System.out.println("名字为"+name+"现在的值为"+value);
                System.out.println("名字为"+name+"现在的值为"+i);
//                Thread.sleep(1000);
            }
            catch (Exception e)
            {

            }
        }
    }

}
