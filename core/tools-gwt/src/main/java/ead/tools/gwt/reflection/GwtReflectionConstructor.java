package ead.tools.gwt.reflection;

import com.gwtent.reflection.client.Constructor;

import ead.tools.reflection.ReflectionConstructor;

public class GwtReflectionConstructor<T> implements ReflectionConstructor<T>{
	
	private Constructor<T> constructor;
	
	public GwtReflectionConstructor( Constructor<T> constructor ){
		this.constructor = constructor;
	}

	@Override
	public T newInstance() {
		return constructor.newInstance();
	}

}
