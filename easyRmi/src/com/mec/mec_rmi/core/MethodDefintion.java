package com.mec.mec_rmi.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class MethodDefintion {
		private Class<?> klass;
		private Object obj;
		private Method method;
		
		MethodDefintion() {
		}
		
		MethodDefintion(Class<?> klass, Object obj, Method method) {
			this.klass = klass;
			this.obj = obj;
			this.method = method;
		}

		Class<?> getKlass() {
			return klass;
		}

		void setKlass(Class<?> klass) {
			this.klass = klass;
		}

		Object getObj() {
			return obj;
		}

		void setObj(Object obj) {
			this.obj = obj;
		}

		Method getMethod() {
			return method;
		}

		void setMethod(Method method) {
			this.method = method;
		}
		
		int getParaCount() {
			return method.getParameterCount();
		}
		
		Class<?>[] getParaTypes() {
			return method.getParameterTypes();
		}
		
		Object invoke(Object[] params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			System.out.println(method);
			return method.invoke(obj, params);
		}
		
		Object invoke() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			return method.invoke(obj);
		}
}
