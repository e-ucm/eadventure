package es.eucm.eadventure.editor.view.impl;

import java.util.HashMap;
import java.util.Map;

import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.ProviderFactory;
import es.eucm.eadventure.editor.view.generics.InterfaceElement;

/**
 * Abstract (i.e. platform independent) implementation of
 * {@link ProviderFactory}
 * 
 * @param <T>
 *            The platform dependent type of interface elements
 */
public abstract class AbstractProviderFactory<T> implements ProviderFactory<T> {

	/**
	 * Map with types of {@link InterfaceElement} and actual instances of
	 * content providers
	 */
	private Map<Class<? extends InterfaceElement>, ComponentProvider<? extends InterfaceElement, T>> map;

	/**
	 * Default constructor
	 */
	public AbstractProviderFactory() {
		map = new HashMap<Class<? extends InterfaceElement>, ComponentProvider<? extends InterfaceElement, T>>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.editor.view.ProviderFactory#getProvider(es.eucm.eadventure
	 * .editor.view.generics.InterfaceElement)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <S extends InterfaceElement> ComponentProvider<S, T> getProvider(
			S option) {
		ComponentProvider<? extends InterfaceElement, T> provider = map
				.get(option.getClass());
		return (ComponentProvider<S, T>) provider;
	}

	/**
	 * Add an element to the map of types of {@link InterfaceElement} and an
	 * instance of their corresponding {@link ComponentProvider}
	 * 
	 * @param element
	 *            The {@link InterfaceElement} type or class
	 * @param provider
	 *            The instance of the {@link ComponentProvider}
	 */
	protected void addToMap(Class<? extends InterfaceElement> element,
			ComponentProvider<? extends InterfaceElement, T> provider) {
		map.put(element, provider);
	}

}
