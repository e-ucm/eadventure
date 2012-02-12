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

package ead.common.util;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.interfaces.EAdRuntimeException;

/**
 * Abstract factory that gets an element of a certain class. This factory uses a
 * map that is intended to be injected though guice. When no element is found of
 * the class given as a parameter, an element of one of its interfaces is
 * searched as well.
 *
 * @param <T>
 *            The return type of the factory.
 */
public abstract class AbstractFactory<T> implements Factory<T> {

	protected Map<Class<?>, T> map;

	protected ReflectionProvider reflectionProvider;

	protected static final Logger logger = LoggerFactory.getLogger("AbstractFactory");

	public AbstractFactory(MapProvider<Class<?>, T> mapProvider,
			ReflectionProvider interfacesProvider) {
		if (mapProvider != null)
			this.map = mapProvider.getMap();
		this.reflectionProvider = interfacesProvider;
	}

	@Override
	public void setMap(MapProvider<Class<?>, T> mapProvider) {
		this.map = mapProvider.getMap();
	}

	@Override
	public T get(Class<?> object) {
		T element = map.get(object);
		if (element == null) {
			logger.info("No element in factory for object " + object
					+ " " + this.getClass());
			Class<?> c = reflectionProvider.getSuperclass(object);
			while (element == null && c != null) {
				element = map.get(c);
				c = reflectionProvider.getSuperclass(c);
			}

			if (element == null) {
				Class<?>[] interfaces = reflectionProvider
						.getInterfaces(object);
				for (Class<?> i : interfaces) {
					element = map.get(i);
					if (element != null) {
						logger.info("Using super class in factory for object of class "
								+ object.getClass() + " using " + i);
						map.put(object, element);
						return element;
					}
				}
			}
			throw new EAdRuntimeException(
					"No element in factory for object (or for any of its interfaces) "
							+ object);
		}
		return element;
	}

}
