package es.eucm.eadventure.common.interfaces;

import es.eucm.eadventure.common.model.variables.EAdVarDef;

public interface ReflectionProvider {

	Class<?>[] getInterfaces(Class<?> object);

	boolean isAssignableFrom(Class<?> varDef, Object value);

}
