package com.aswl.as.asos;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class TestSumTask extends RecursiveTask<Integer> {

    private final static int THRESHOLD = 10;// 阀值，当数组长度小于10就不再拆分
    int[] array = null;
    int start;
    int end;

    public TestSumTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start <= THRESHOLD) {
            // 直接求和
            int sum = 0;
            for (int i = start; i <= end; i++) {
                sum += this.array[i];
            }
            return sum;
        } else {
            // 拆分
            int mid = (start + end) / 2;
            TestSumTask left = new TestSumTask(array, start, mid);
            TestSumTask right = new TestSumTask(array, mid + 1, end);
//            invokeAll(left);
//            invokeAll(right);
            invokeAll(left, right);
            return left.join() + right.join();
        }
    }


    public static void main(String[] args) {
        // 定义数组
        int[] arr = new int[100];
        for (int i = 0; i < 100; i++) {
            arr[i] = i + 1;
        }

        ForkJoinPool pool = new ForkJoinPool();
        TestSumTask innerFind = new TestSumTask(arr, 0, arr.length - 1);
//        pool.invoke(innerFind);// 同步调用
        pool.execute(innerFind);
        System.out.println("完成,结果是:" + innerFind.join());
    }


}
