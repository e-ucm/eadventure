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

package es.eucm.eadventure.common.interfaces;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.eucm.eadventure.common.interfaces.EAdRuntimeException;
import es.eucm.eadventure.common.interfaces.Factory;
import es.eucm.eadventure.common.interfaces.MapProvider;

/**
 * Abstract factory that gets an element of a certain class. This factory uses a map that is
 * intended to be injected though guice.
 * When no element is found of the class given as a parameter, an element of one of its interfaces
 * is searched as well.
 * 
 * @param <T> The return type of the factory.
 */
public abstract class AbstractFactory<T> implements Factory<T> {

	private Map<Class<?>, T> map;

	private static final Logger logger = Logger.getLogger("AbstractFactory");
	
	public AbstractFactory(MapProvider<Class<?>, T> mapProvider) {
		this.map = mapProvider.getMap();
	}
	
	@Override
	public T get(Class<?> object) {
		T element = map.get(object);
		if (element == null) {
			logger.log(Level.INFO, "No element in factory for object " + object + " " + this.getClass());
			//TODO Not supported by GWT
			
			Class<?>[] interfaces = object.getInterfaces();
			for (Class<?> i : interfaces) {
				element = map.get(i);
				if (element != null) {
					logger.info("Using super class in factory for object of class " + object.getClass() + " using " + i);
					map.put(object, element);
					return element;
				}
			}
			
			throw new EAdRuntimeException("No element in factory for object (or for any of its interfaces) " + object);
		}
		return element;
	}

}
