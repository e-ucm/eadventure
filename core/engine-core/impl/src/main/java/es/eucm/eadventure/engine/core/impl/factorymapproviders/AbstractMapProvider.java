package es.eucm.eadventure.engine.core.impl.factorymapproviders;

import java.util.HashMap;
import java.util.Map;

import es.eucm.eadventure.common.interfaces.MapProvider;

public abstract class AbstractMapProvider<S, T> implements MapProvider<S, T> {

	protected Map<S, T> factoryMap;
	
	public AbstractMapProvider() {
		factoryMap = new HashMap<S, T>();
	}
	
	@Override
	public Map<S, T> getMap() {
		return factoryMap;
	}
	
	
}
