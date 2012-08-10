package ead.tools.java.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import ead.tools.reflection.ReflectionConstructor;

public class JavaReflectionConstructor<T> implements ReflectionConstructor<T>{
	
	private Constructor<T> constructor;
	
	public JavaReflectionConstructor( Constructor<T> constructor ){
		this.constructor = constructor;
	}

	@Override
	public T newInstance() {
		try {
			return constructor.newInstance();
		} catch (IllegalArgumentException e) {
			
		} catch (InstantiationException e) {
			
		} catch (IllegalAccessException e) {
			
		} catch (InvocationTargetException e) {

		}
		return null;
	}

}
