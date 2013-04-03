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

package ead.tools.java.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.tools.reflection.ReflectionClass;
import ead.tools.reflection.ReflectionConstructor;
import ead.tools.reflection.ReflectionField;

public class JavaReflectionClass<T> implements ReflectionClass<T> {

	private static Logger logger = LoggerFactory
			.getLogger(JavaReflectionClass.class);

	private Class<T> clazz;

	private ReflectionConstructor<T> constructor;

	private Map<String, ReflectionField> fields;

	private ReflectionClass<?> superClass;

	private boolean allFieldsAdded;

	public JavaReflectionClass(Class<T> clazz) {
		this.clazz = clazz;
		this.fields = new LinkedHashMap<String, ReflectionField>();
		this.allFieldsAdded = false;
	}

	@Override
	public ReflectionConstructor<T> getConstructor() {
		if (constructor == null) {
			try {
				Constructor<T> c = clazz.getConstructor();
				constructor = new JavaReflectionConstructor<T>(c);
			} catch (SecurityException e) {
				logger.error("error building constructor for {}", clazz, e);
			} catch (NoSuchMethodException e) {
				logger.error("no constructor for {}", clazz, e);
			}
		}
		return constructor;
	}

	@Override
	public ReflectionField getField(String name) {
		if (!fields.containsKey(name)) {
			try {
				fields.put(name, new JavaReflectionField(clazz
						.getDeclaredField(name)));
			} catch (SecurityException e) {
				logger.debug("error accessing field {} for {}", new Object[] {
						clazz, name });
			} catch (NoSuchFieldException e) {
				logger.debug("no such field for {} for {}", new Object[] {
						clazz, name });
			}
		}
		return fields.get(name);
	}

	@Override
	public Collection<ReflectionField> getFields() {
		if (!allFieldsAdded) {
			allFieldsAdded = true;
			for (Field f : clazz.getDeclaredFields()) {
				if (!fields.containsKey(f.getName())) {
					getField(f.getName());
				}
			}
		}
		return fields.values();
	}

	@SuppressWarnings( { "rawtypes", "unchecked" })
	@Override
	public ReflectionClass<?> getSuperclass() {
		if (clazz == Object.class) {
			return null;
		}

		if (superClass == null) {
			superClass = new JavaReflectionClass(clazz.getSuperclass());
		}
		return superClass;
	}

	@Override
	public Class<?> getType() {
		return clazz;
	}

	@Override
	public <S extends Annotation> boolean hasAnnotation(Class<S> annotation) {
		return clazz.getAnnotation(annotation) != null;
	}

}
