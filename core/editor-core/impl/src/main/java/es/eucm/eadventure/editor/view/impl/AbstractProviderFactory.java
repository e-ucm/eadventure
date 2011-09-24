package es.eucm.eadventure.editor.view.impl;

import java.util.HashMap;
import java.util.Map;


import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.ProviderFactory;
import es.eucm.eadventure.editor.view.generics.InterfaceElement;

public abstract class AbstractProviderFactory<T> implements ProviderFactory<T> {

	private Map<Class<? extends InterfaceElement>, Class<? extends ComponentProvider<? extends InterfaceElement, T>>> map;
	
	public AbstractProviderFactory() {
		map = new HashMap<Class<? extends InterfaceElement>, Class<? extends ComponentProvider<? extends InterfaceElement, T>>>();
	}
	
	@Override
	public <S extends InterfaceElement> ComponentProvider<S, T> getProvider(
			S option) {
		Class<? extends ComponentProvider<? extends InterfaceElement, T>> clazz = map.get(option.getClass());
		return getInstance(clazz);
	}
	
	protected void addToMap(Class<? extends InterfaceElement> element, Class<? extends ComponentProvider<? extends InterfaceElement, T>> provider) {
		map.put(element, provider);
	}

	abstract protected <S extends ComponentProvider<? extends InterfaceElement, T>> S getInstance(Class<? extends ComponentProvider<? extends InterfaceElement, T>> clazz); 
	
}
