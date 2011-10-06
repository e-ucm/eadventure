package es.eucm.eadventure.editor.view.impl;

import java.util.HashMap;
import java.util.Map;

import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.ProviderFactory;
import es.eucm.eadventure.editor.view.generics.InterfaceElement;

public abstract class AbstractProviderFactory<T> implements ProviderFactory<T> {

	private Map<Class<? extends InterfaceElement>, ComponentProvider<? extends InterfaceElement, T>> map;
	
	public AbstractProviderFactory() {
		map = new HashMap<Class<? extends InterfaceElement>, ComponentProvider<? extends InterfaceElement, T>>();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <S extends InterfaceElement> ComponentProvider<S, T> getProvider(
			S option) {
		ComponentProvider<? extends InterfaceElement, T> provider = map.get(option.getClass());
		return (ComponentProvider<S, T>) provider;
	}
	
	protected void addToMap(Class<? extends InterfaceElement> element,
			ComponentProvider<? extends InterfaceElement, T> provider) {
		map.put(element, provider);
	}



}
