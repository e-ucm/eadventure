package es.eucm.eadventure.common.interfaces;


public interface ReflectionProvider {

	Class<?>[] getInterfaces(Class<?> object);

	boolean isAssignableFrom(Class<?> class1, Class<?> class2);
	
	Class<?> getSuperclass(Class<?> c);

}
