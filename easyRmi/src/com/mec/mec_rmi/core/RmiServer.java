package com.mec.mec_rmi.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.my.util.core.PropertiesParser;

public class RmiServer implements Runnable{
		private final int DEFAULT_PORT = 54188;
		private final int DEFAULT_POOL_CORE_SIZE = 30;
		private final int DEFAULT_POOL_MAX_SIZE = 50;
		private final long DEFAULT_POOL_THREAD_ALIVE_TIME = 100;
		private final TimeUnit DEFAULT_POOL_UNIT = TimeUnit.MILLISECONDS;
		private final int DEFAULT_QUEUE_SIZE = 20;
		
		private int port;
		private ServerSocket serverSocket;
		private volatile boolean goon;
		private static long no;
		private ThreadPoolExecutor pool;
		private BlockingQueue<Runnable> queue;
		
		public RmiServer() {
			this(0);
		}
		
		public RmiServer(int port) {
			this.port = port;
			goon = false;
		}
		
		public void initServer() {
			initServer("/server_cfg.properties");
		}
		
		public void initServer(String cfgPath) {
			PropertiesParser.readConfig(cfgPath);
			if (checkConfig("Rmi.port") == false) {
				port = DEFAULT_PORT;
			}
		}

		private boolean checkConfig(String configUnit) {
			String stringUnit = PropertiesParser.getValueByKey(configUnit);
			if (stringUnit == null) {
				return false;
			}			
			port = Integer.valueOf(stringUnit);
			return true;
		}
 		
		public void setPort(int port) {
			this.port = port;
		}
		
		public void startup() throws IOException {
			if (goon == true || port < 1000 || port > 65535) {
				return;
			}
			goon = true;
			serverSocket = new ServerSocket(port);
			queue = new ArrayBlockingQueue<>(DEFAULT_QUEUE_SIZE);
			pool = new ThreadPoolExecutor(DEFAULT_POOL_CORE_SIZE, DEFAULT_POOL_MAX_SIZE, DEFAULT_POOL_THREAD_ALIVE_TIME, DEFAULT_POOL_UNIT, queue);
			new Thread(this, "Rmiserver" + no++).start();
//			System.out.println("服务器已经启动!!");
		}

		@Override
		public void run() {
			while (goon) {
				try {
					Socket socket = serverSocket.accept();
//					System.out.println("侦听到客户端的调用请求");
					pool.execute(new ServerInvoker(socket));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		public void shutdown() {
			goon = false;
			
			pool.shutdown();
		}
}
