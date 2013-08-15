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

package es.eucm.ead.engine.factories;

import es.eucm.ead.model.elements.EAdElement;
import es.eucm.ead.engine.gameobjects.GameObject;

/**
 * <p>
 * Interface for a game object factory.
 * </p>
 * <p>
 * This interface has the methods to provide a game object based on an element
 * in the eAdventure model and a method to provide an instance of a game object
 * of a given class.
 * </p>
 */
public interface GameObjectFactory<S extends EAdElement, T extends GameObject<?>> {

	/**
	 * Returns a {@link GameObject} in the engine's runtime model for a
	 * {@link EAdElement} in the eAdventure game model
	 * 
	 * @param <T>
	 *            The type of the {@link EAdElement}
	 * @param element
	 *            The element
	 * @return The game object of that element
	 */
	T get(S element);

	/**
	 * Adds a new relation between some model class and its engine
	 * interpretation
	 * 
	 * @param clazz1
	 *            model class
	 * @param clazz2
	 *            game object class
	 */
	void put(Class<? extends S> clazz1, Class<? extends T> clazz2);

	boolean remove(S element);

	void remove(T element);

	/**
	 * Returns how many elements are contained by the cache of this factory
	 * 
	 * @return
	 */
	int getCacheSize();

	void clean();

}
