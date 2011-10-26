package es.eucm.eadventure.engine.core.platform.impl;

import java.util.Stack;

import com.google.inject.Singleton;
import com.gwtent.reflection.client.ClassHelper;

import es.eucm.eadventure.common.interfaces.ReflectionProvider;

@Singleton
public class PlayNReflectionProvider implements ReflectionProvider {

	@Override
	public Class<?>[] getInterfaces(Class<?> object) {
		return ClassHelper.AsClass(object).getInterfaces();
	}
	
	@Override
	public boolean isAssignableFrom(Class<?> class1, Class<?> class2) {
		Stack<Class<?>> stack = new Stack<Class<?>>();
		stack.push(class2);
		while (!stack.isEmpty()) {
			Class<?> temp = stack.pop();
			if (class1 == temp)
				return true;
			Class<?> temp2 = ClassHelper.AsClass(temp).getSuperclass();
			if (temp2 != null)
				stack.push(temp2);
			for (Class<?> newClass : ClassHelper.AsClass(temp).getInterfaces())
				stack.push(newClass);
			if (stack.peek() == Object.class)
				stack.pop();
			}
		return false;
	}

	@Override
	public Class<?> getSuperclass(Class<?> c) {
		return ClassHelper.AsClass(c).getSuperclass();
	}

}
