package com.mec.mec_rmi.test;

import com.mec.mec_rmi.core.RmiClient;
import com.mec.mec_rmi.core.RmiResourceFactory;
import com.mec.mec_rmi.core.RmiServer;

public class RmiTest {
		
	public static void main(String[] args) {
		try {
			RmiResourceFactory.registery(Test.class, ITest.class);
			
			RmiServer server = new RmiServer(54188);
			server.startup();
			
			RmiClient client = new RmiClient();
			client.setServerIp("127.0.0.1");
			client.setPort(54188);
			
			ITest test = client.getProxy(ITest.class);
			System.out.println(test.fun1("123"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
