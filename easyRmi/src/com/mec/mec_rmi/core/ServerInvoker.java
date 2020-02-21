package com.mec.mec_rmi.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

public class ServerInvoker implements Runnable{
		private Socket socket;
		private ObjectOutputStream oos;
		private ObjectInputStream ois;
		
		public ServerInvoker() {
		}
		
		public ServerInvoker(Socket socket) throws IOException {
			this.socket = socket;
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		}
		
		private Object[] getParams(Map<String, Object> params) {
			Object[] objs = new Object[params.size()];
			
			for (int index = 0; index < params.size(); index++) {
				objs[index] = params.get("arg"+ index);
			}
			
			return objs;
		}

		@Override
		public void run() {
			try {
//				System.out.println("正式开始接受客户端的调用具体函数!!!!!!!!!!!!");
				int methodId = ois.readInt();
//				System.out.println("服务器接收到methodid:" + methodId);
				
				MethodDefintion definition = RmiResourceFactory.getDefinition(methodId);
				Method method = definition.getMethod();
				Object result = null;
				if (method.getParameterCount() != 0) {
					@SuppressWarnings("unchecked")
					Map<String, Object> paramMap = (Map<String, Object>) ois.readObject();
//					System.out.println("服务器接收到函数参数");
					Object[] params = getParams(paramMap);
					
					result = definition.invoke(params);
				} else {
					result = definition.invoke();
				}
				
//				System.out.println("远程调用结束!!!");
				if (method.getReturnType() != void.class) {
					System.out.println("result" + result);
					oos.writeObject(result);
				}
				
				ois.close();
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					socket = null;
				}
			}
		}
		
		
}
