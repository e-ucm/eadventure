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

import com.badlogic.gdx.utils.Pool;
import com.google.inject.Singleton;
import es.eucm.ead.engine.gameobjects.GameObject;
import es.eucm.ead.model.elements.EAdElement;
import es.eucm.ead.tools.GenericInjector;
import es.eucm.ead.tools.reflection.ReflectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class GOFactoryImpl<S extends EAdElement, T extends GameObject<?>>
		implements GameObjectFactory<S, T> {

	private static final Logger logger = LoggerFactory
			.getLogger("GameObjectFactoryImpl");

	private ReflectionProvider reflectionProvider;

	private GenericInjector injector;

	private boolean useCache;

	protected Map<S, T> cache;

	protected Map<Class<?>, Pool<T>> pools;

	private Map<Class<? extends S>, Class<? extends T>> classMap;

	public GOFactoryImpl(boolean useCache,
			ReflectionProvider reflectionProvider, GenericInjector injector) {
		this.useCache = useCache;
		if (useCache) {
			cache = new HashMap<S, T>();
		}
		this.reflectionProvider = reflectionProvider;
		this.injector = injector;
		// Pools
		this.pools = new HashMap<Class<?>, Pool<T>>();
	}

	public void setClassMap(Map<Class<? extends S>, Class<? extends T>> classMap) {
		this.classMap = classMap;
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	@Override
	public T get(S element) {
		if (element == null)
			return null;

		GameObject temp = null;
		if (useCache) {
			temp = cache.get(element);

			if (temp != null)
				return (T) temp;
		}

		Class<?> elementClass = element.getClass();
		Class<?> runtimeClass = classMap.get(elementClass);
		while (elementClass != null && runtimeClass == null) {
			runtimeClass = classMap.get(elementClass);
			elementClass = reflectionProvider.getSuperclass(elementClass);
		}

		if (runtimeClass == null) {
			logger.error("No game element mapped for class {}", element
					.getClass());
		} else {
			temp = (GameObject) getInstance(runtimeClass);
			if (temp == null) {
				logger.error("No instance for game object of class {}", element
						.getClass());
			} else {
				temp.setElement(element);
				if (useCache) {
					cache.put(element, (T) temp);
				}
			}
		}
		return (T) temp;
	}

	public boolean remove(S element) {
		if (cache != null) {
			return (cache.remove(element) != null);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public void remove(T gameObject) {
		if (remove((S) gameObject.getElement())) {
			Pool<T> pool = pools.get(gameObject.getClass());
			if (pool != null) {
				pool.free((T) gameObject);
				gameObject.release();
			}
		}
	}

	public void clean() {
		pools.clear();
		if (cache != null)
			cache.clear();
	}

	public void put(Class<? extends S> clazz1, Class<? extends T> clazz2) {
		classMap.put(clazz1, clazz2);
	}

	private T getInstance(Class<?> clazz) {
		Pool<T> pool = pools.get(clazz);
		if (pool == null) {
			pool = new GameObjectPool(clazz);
			pools.put(clazz, pool);
		}
		return pool.obtain();
	}

	public class GameObjectPool extends Pool<T> {

		private Class<?> clazz;

		public GameObjectPool(Class<?> clazz) {
			this.clazz = clazz;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected T newObject() {
			return (T) injector.getInstance(clazz);
		}

	}

	public int getCacheSize() {
		return cache != null ? cache.size() : 0;
	}
}
