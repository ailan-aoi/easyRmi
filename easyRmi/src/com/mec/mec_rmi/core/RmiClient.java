package com.mec.mec_rmi.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.mec.mec_rmi.exceptions.UnableServerUse;

public class RmiClient {
	private INodeSelector nodeSelector;
	private int port;
	private String serverIp;

	public RmiClient() {
	}

	public RmiClient(INodeSelector nodeSelector) {
			this.nodeSelector = nodeSelector;
	}

	public void setNodeSelector(INodeSelector nodeSelector) {
			this.nodeSelector = nodeSelector;
		}
		
    public void setPort(int port) {
        this.port = port;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<?> interfacess) {
        if (interfacess == null) {
            return null;
        }
        return (T) Proxy.newProxyInstance(interfacess.getClassLoader(), new Class<?>[] {interfacess}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws UnableServerUse, IOException, ClassNotFoundException {
                return rmiInvoke(interfacess, method, args);
            }
        });
    }

	private Map<String, Object> paramsMap(Object[] params) {
			Map<String, Object> arrMaps = new HashMap<>();

			for (int i = 0 ; i < params.length; i++) {
				arrMaps.put("arg" + i, params[i]);
			}
			
			return arrMaps;
	}
		
	private Object rmiInvoke(Class<?> interfacess, Method method, Object[] args) throws IOException, UnableServerUse, ClassNotFoundException {
		Object res = null;
		Socket socket = getSocket();
//		System.out.println("已经连接上服务器!!");
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		
        String methodString = interfacess.getName() + ":" + method.toString();
		int methodId = methodString.hashCode();
		oos.writeInt(methodId);
//		System.out.println("methodId" + methodId +"已经发送给服务器");
		
		if (method.getParameterCount() != 0) {
			oos.writeObject(paramsMap(args));
		}
		Class<?> returnType = method.getReturnType();
		if (returnType != void.class) {
			res = ois.readObject();
		}
		
		ois.close();
		oos.close();
		return res;
	}

	private Socket getSocket() throws IOException, UnableServerUse {
		if (nodeSelector == null) {
			return new Socket(serverIp, port);
		} else {
			while (true) {
				INode node = nodeSelector.getNode();
				if (node == null) {
					throw new UnableServerUse("没有可用的服务器");
				}
				Socket socket;
				try {
					socket = new Socket();
					socket.connect(new InetSocketAddress(
					node.getIp(), node.getPort()), nodeSelector.getDelayTime());
				} catch (IOException e) {
					nodeSelector.connectFaliure(node);
					continue;
				}

				return socket;

			}
		}
	}
		
}
