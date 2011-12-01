package es.eucm.eadventure.engine.core.platform.impl;

import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Singleton;
import com.gwtent.reflection.client.ClassHelper;
import com.gwtent.reflection.client.ReflectionRequiredException;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.ReflectionProvider;
import es.eucm.eadventure.common.model.EAdElement;

@Singleton
public class PlayNReflectionProvider implements ReflectionProvider {

	private static final Logger logger = Logger.getLogger("ReflectionProvider");
	
	@Override
	public Class<?>[] getInterfaces(Class<?> object) {
		return ClassHelper.AsClass(object).getInterfaces();
	}
	
	@Override
	public boolean isAssignableFrom(Class<?> class1, Class<?> class2) {
		if ( class1 == Object.class )
			return true;
		
		Stack<Class<?>> stack = new Stack<Class<?>>();
		stack.push(class2);
		
		while (!stack.isEmpty()) {
			Class<?> temp = stack.pop();
			if (class1 == temp)
				return true;
			if (temp != null) {
				try {
					Class<?> temp2 = ClassHelper.AsClass(temp).getSuperclass();
					if (temp2 != null)
						stack.push(temp2);
					for (Class<?> newClass : ClassHelper.AsClass(temp).getInterfaces())
						stack.push(newClass);
				} catch (ReflectionRequiredException e)  {
					
				}
			}
			
			if (!stack.isEmpty() && stack.peek() == Object.class)				
				stack.pop();
			}
		return false;
	}

	@Override
	public Class<?> getSuperclass(Class<?> c) {
		return ClassHelper.AsClass(c).getSuperclass();
	}
	
	@Override
	public Class<?> getRuntimeClass(EAdElement element) {
		Class<?> clazz = element.getClass();
		
		Element annotation = null;
		while (annotation == null && clazz != null ) {
			annotation = ClassHelper.AsClass(clazz).getAnnotation(Element.class);
			clazz = clazz.getSuperclass();
		}
		
		if ( annotation == null ){
			logger.log(Level.SEVERE, "No element annotation for class " + element.getClass());
			return null;
		}
		return annotation.runtime();
		
	}

}
