package com.e1858.building.net;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 处理线程
 * 
 * @author momo
 *         2014-12-23上午9:58:34
 */
public class ThreadPool {
	private static ExecutorService	executor;

	private ThreadPool() {}

	public static void execute(Runnable run) {
		if (null == executor) {
			executor = Executors.newCachedThreadPool();
		}
		executor.execute(run);
	}

	public static <T> Future<T> submit(Callable<T> task) {
		if (null == executor) {
			executor = Executors.newCachedThreadPool();
		}
		return executor.submit(task);
	}

	public static void shutdown() {
		if (null != executor) {
			executor.shutdown();
		}
	}
}
