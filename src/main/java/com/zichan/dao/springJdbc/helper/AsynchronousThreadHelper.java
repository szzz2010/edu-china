package com.zichan.dao.springJdbc.helper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsynchronousThreadHelper {

	public static Future<?> runNewThread(String cm, Class<?>[] classes, Object... args) {
		ExecutorService threadPool = Executors.newSingleThreadExecutor();
		Future<?> future = threadPool.submit(new Callable<Object>() {
			public Object call() {
				Object obj = null;
				try {
					obj = InvokeMethod.invoke(cm, classes, args);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return obj;
			}
		});
		threadPool.shutdown();
		return future;
	}
	
	public static void runNewThread2(String cm, Class<?>[] classes, Object... args) {
		Thread thread = new Thread() {
			public void run() {
				try {
					InvokeMethod.invoke(cm, classes, args);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}
	
}
