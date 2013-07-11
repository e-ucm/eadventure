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

package ead.tools;

import java.util.ArrayList;
import java.util.List;

// We borrow the libgdx pool implementation to be available in the ead tools and to avoid adding the whole libgdx library

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

/**
 * A pool of objects that can be reused to avoid allocation.
 * 
 * @author Nathan Sweet
 */
abstract public class Pool<T> {
	/** The maximum number of objects that will be pooled. */
	public final int max;
	/** The highest number of free objects. Can be reset any time. */
	public int peak;

	private final List<T> freeObjects;

	/** Creates a pool with an initial capacity of 16 and no maximum. */
	public Pool() {
		this(16, Integer.MAX_VALUE);
	}

	/** Creates a pool with the specified initial capacity and no maximum. */
	public Pool(int initialCapacity) {
		this(initialCapacity, Integer.MAX_VALUE);
	}

	/**
	 * @param max
	 *            The maximum number of free objects to store in this pool.
	 */
	public Pool(int initialCapacity, int max) {
		freeObjects = new ArrayList<T>();
		this.max = max;
	}

	abstract protected T newObject();

	/**
	 * Returns an object from this pool. The object may be new (from
	 * {@link #newObject()}) or reused (previously {@link #free(Object) freed}).
	 */
	public T obtain() {
		return freeObjects.size() == 0 ? newObject() : freeObjects.remove(0);
	}

	/**
	 * Puts the specified object in the pool, making it eligible to be returned
	 * by {@link #obtain()}. If the pool already contains {@link #max} free
	 * objects, the specified object is reset but not added to the pool.
	 */
	public void free(T object) {
		if (object == null)
			throw new IllegalArgumentException("object cannot be null.");
		if (freeObjects.size() < max) {
			freeObjects.add(object);
			peak = Math.max(peak, freeObjects.size());
		}
		if (object instanceof Poolable)
			((Poolable) object).reset();
	}

	/**
	 * Puts the specified objects in the pool. Null objects within the array are
	 * silently ignored.
	 * 
	 * @see #free(Object)
	 */
	public void freeAll(List<T> objects) {
		if (objects == null)
			throw new IllegalArgumentException("object cannot be null.");
		for (int i = 0; i < objects.size(); i++) {
			T object = objects.get(i);
			if (object == null)
				continue;
			if (freeObjects.size() < max)
				freeObjects.add(object);
			if (object instanceof Poolable)
				((Poolable) object).reset();
		}
		peak = Math.max(peak, freeObjects.size());
	}

	/** Removes all free objects from this pool. */
	public void clear() {
		freeObjects.clear();
	}

	/** The number of objects available to be obtained. */
	public int getFree() {
		return freeObjects.size();
	}

	/**
	 * Objects implementing this interface will have {@link #reset()} called
	 * when passed to {@link #free(Object)}.
	 */
	static public interface Poolable {
		/**
		 * Resets the object for reuse. Object references should be nulled and
		 * fields may be set to default values.
		 */
		public void reset();
	}
}
