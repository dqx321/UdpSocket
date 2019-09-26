package com.example.Util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author CIDI
 */
public class ThreadPoolUtils {

    //核心线程数
    private static int coreThreadSize = 5;
    //最大线程数
    private static int maxThreadSize = 50;
    private static ExecutorService executorService;

    public static void InitThreadPool() {
        if (executorService == null) {
            synchronized (ExecutorService.class) {
                if (executorService == null) {
                    executorService = new ThreadPoolExecutor(coreThreadSize, maxThreadSize, Integer.MAX_VALUE, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10, true), new ThreadPoolExecutor.DiscardOldestPolicy());
                }
            }
        }
    }

    public static void InitThreadPool(int coreThreadSize, int maxThreadSize) {
        if (executorService == null) {
            synchronized (ExecutorService.class) {
                if (executorService == null) {
                    executorService = new ThreadPoolExecutor(coreThreadSize, maxThreadSize, 1000 * 6, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10, true), new ThreadPoolExecutor.DiscardOldestPolicy());
                }
            }
        }
    }

    //提交线程任务
    public static void execute(Runnable taskRunable) {
        InitThreadPool();
        executorService.execute(taskRunable);
    }

    //清空所有线程
    public static void clearAllThread() {
        executorService.shutdownNow();
    }

}
