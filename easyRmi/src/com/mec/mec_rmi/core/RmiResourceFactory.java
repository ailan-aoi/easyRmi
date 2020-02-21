package com.mec.mec_rmi.core;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RmiResourceFactory {
	private static Map<Integer, MethodDefintion> methodDefintions;

	static {
		methodDefintions = new ConcurrentHashMap<>();
	}

	public static void registery(Class<?> klass, Class<?> interfaces) throws Exception {
		if (!interfaces.isInterface()) {
			throw new Exception("您所输入的" + interfaces + "不是接口!!");
		}
		Method[] interfaceMethods = interfaces.getMethods();
		Object obj = klass.newInstance();
		for (Method method : interfaceMethods) {
			MethodDefintion defintion = new MethodDefintion(klass, obj, method);
			String methodString = interfaces.getName() + ":" + method.toString();
			Integer methodId = methodString.hashCode();
			System.out.println("Factory" + methodId);
			methodDefintions.put(methodId, defintion);
		}
	}

	public static MethodDefintion getDefinition(int methodId) {
			return methodDefintions.get(methodId);
		}
}
