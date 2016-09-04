package com.qwwuyu.recite.db;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 数据库操作队列
 * Created by qw on 2016/8/14.
 */
public class DBExecutor {

    /** 数据库操作线程池队列，同时只允许一个线程操作数据库 */
    private static ExecutorService executorService = Executors.newFixedThreadPool(1);

    /** 往线程池添加线程 */
    public static void addTask(Runnable task) {
        executorService.submit(task);
    }

    /** 关闭线程池 */
    public static void shutdown() {
        executorService.shutdown();
    }

}
