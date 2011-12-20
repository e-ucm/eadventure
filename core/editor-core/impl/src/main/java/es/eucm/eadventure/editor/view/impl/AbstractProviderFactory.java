/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.eadventure.editor.view.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

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
		Stack<Class<?>> stack = new Stack<Class<?>>();
		stack.push(option.getClass());
		ComponentProvider<? extends InterfaceElement, T> provider = null;
		while (provider == null && !stack.isEmpty()) {
			Class<?> clazz = stack.pop();
			provider = map.get(clazz);
			if (clazz != null) {
				if (clazz.getInterfaces() != null)
					for (Class<?> c : clazz.getInterfaces())
						stack.push(c);
				stack.push(clazz.getSuperclass());
			}
		}
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
