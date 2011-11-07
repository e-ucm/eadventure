package es.eucm.eadventure.common.interfaces;

import es.eucm.eadventure.common.model.EAdElement;


public interface ReflectionProvider {

	Class<?>[] getInterfaces(Class<?> object);

	boolean isAssignableFrom(Class<?> class1, Class<?> class2);
	
	Class<?> getSuperclass(Class<?> c);
	
	Class<?> getRuntimeClass(EAdElement element);

}
