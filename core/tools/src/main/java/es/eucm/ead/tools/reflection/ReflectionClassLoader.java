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

package es.eucm.ead.tools.reflection;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * General abstract class to transform string representing classes into
 * {@link ReflectionClass}
 *
 */
public abstract class ReflectionClassLoader {

	protected static final Logger logger = LoggerFactory
			.getLogger("ReflectionClassLoader");

	private static ReflectionClassLoader reflectionClassLoader;

	private static Map<String, ReflectionClass<?>> classes = new HashMap<String, ReflectionClass<?>>();

	protected abstract ReflectionClass<?> getReflectionClassImpl(Class<?> clazz);

	protected abstract ReflectionClass<?> getReflectionClassImpl(String clazz);

	public static void init(ReflectionClassLoader loader) {
		reflectionClassLoader = loader;
	}

	public static ReflectionClass<?> getReflectionClass(Class<?> className) {
		if (!classes.containsKey(className.getName())) {
			if (reflectionClassLoader == null) {
				logger
						.error("Reflection class loader not initialized: "
								+ "init() method should be called before use getReflectionClass() method");
				return null;
			} else {
				classes.put(className.getName(), reflectionClassLoader
						.getReflectionClassImpl(className));
			}
		}
		return classes.get(className.getName());
	}

	public static ReflectionClass<?> getReflectionClass(String clazz) {
		if (!classes.containsKey(clazz)) {
			if (reflectionClassLoader == null) {
				logger
						.error("Reflection class loader not initialized: "
								+ "init() method should be called before use getReflectionClass() method");
				return null;
			} else {
				classes.put(clazz, reflectionClassLoader
						.getReflectionClassImpl(clazz));
			}
		}
		return classes.get(clazz);
	}

}
