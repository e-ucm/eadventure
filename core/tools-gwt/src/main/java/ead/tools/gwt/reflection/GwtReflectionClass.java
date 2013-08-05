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

package ead.tools.gwt.reflection;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Constructor;
import com.gwtent.reflection.client.Field;
import com.gwtent.reflection.client.TypeOracle;

import es.eucm.ead.tools.reflection.ReflectionClass;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;
import es.eucm.ead.tools.reflection.ReflectionConstructor;
import es.eucm.ead.tools.reflection.ReflectionField;

public class GwtReflectionClass<T> implements ReflectionClass<T> {

	private ClassType<T> clazz;

	private ReflectionConstructor<T> constructor;

	private Map<String, ReflectionField> fields;

	private ReflectionClass<?> superClass;

	private boolean allFieldsAdded;

	private List<ReflectionClass<?>> interfaces;

	public GwtReflectionClass(Class<T> clazz) {
		this.clazz = TypeOracle.Instance.getClassType(clazz);
		this.fields = new HashMap<String, ReflectionField>();
		this.allFieldsAdded = false;
	}

	@Override
	public ReflectionConstructor<T> getConstructor() {
		if (constructor == null) {
			Constructor<T> c = clazz.findConstructor();
			constructor = new GwtReflectionConstructor<T>(c);
		}

		return constructor;
	}

	@Override
	public ReflectionField getField(String name) {
		if (!fields.containsKey(name)) {
			fields.put(name, new GwtReflectionField(clazz.getField(name)));
		}
		return fields.get(name);
	}

	@Override
	public Collection<ReflectionField> getFields() {
		if (!allFieldsAdded) {
			allFieldsAdded = true;
			for (Field f : clazz.getFields()) {
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
		if (clazz.getDeclaringClass() == Object.class) {
			return null;
		}

		if (superClass == null) {
			superClass = new GwtReflectionClass(clazz.getSuperclass()
					.getDeclaringClass());
		}
		return superClass;
	}

	@Override
	public Class<?> getType() {
		return clazz.getDeclaringClass();
	}

	@Override
	public <S extends Annotation> boolean hasAnnotation(Class<S> annotation) {
		return clazz.getAnnotation(annotation) != null;
	}

	@Override
	public List<ReflectionClass<?>> getInterfaces() {
		if (interfaces == null) {
			interfaces = new ArrayList<ReflectionClass<?>>();
			for (ClassType<?> c : clazz.getImplementedInterfaces()) {
				interfaces.add(ReflectionClassLoader.getReflectionClass(c
						.getDeclaringClass()));
			}
		}
		return interfaces;
	}

}
