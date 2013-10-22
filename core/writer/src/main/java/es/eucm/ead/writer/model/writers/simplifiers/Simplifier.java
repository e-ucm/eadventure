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

package es.eucm.ead.writer.model.writers.simplifiers;

import es.eucm.ead.model.assets.drawable.basics.EAdShape;
import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.assets.drawable.basics.animation.FramesAnimation;
import es.eucm.ead.model.assets.drawable.compounds.StateDrawable;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.conditions.Condition;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.operations.Operation;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.tools.EAdUtils;
import es.eucm.ead.tools.reflection.ReflectionClass;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;
import es.eucm.ead.writer.model.writers.simplifiers.assets.FramesAnimationSimplifier;
import es.eucm.ead.writer.model.writers.simplifiers.assets.ImagesSimplifier;
import es.eucm.ead.writer.model.writers.simplifiers.assets.ShapesSimplifier;
import es.eucm.ead.writer.model.writers.simplifiers.assets.StateDrawablesSimplifier;
import es.eucm.ead.writer.model.writers.simplifiers.elements.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simplifier {

	static private Logger logger = LoggerFactory.getLogger(Simplifier.class);

	private Map<Class<?>, List<Object>> objectsLists;

	private Map<Class<?>, ObjectSimplifier<?>> simplifiers;

	private int simplifications;

	private FieldsSimplifier fieldsSimplifier;

	public Simplifier() {
		objectsLists = new HashMap<Class<?>, List<Object>>();
		simplifications = 0;
		simplifiers = new HashMap<Class<?>, ObjectSimplifier<?>>();
		// Simplifiers
		fieldsSimplifier = new FieldsSimplifier();

		simplifiers.put(Condition.class, new ConditionsSimplifier());
		simplifiers.put(ElementField.class, fieldsSimplifier);
		simplifiers.put(Operation.class, new OperationsSimplifier());
		simplifiers.put(SceneElementDef.class, new SceneElementDefSimplifier());
		simplifiers.put(FramesAnimation.class, new FramesAnimationSimplifier());
		simplifiers.put(Image.class, new ImagesSimplifier());
		simplifiers.put(EAdShape.class, new ShapesSimplifier());
		simplifiers.put(StateDrawable.class, new StateDrawablesSimplifier());
	}

	public void clear() {
		simplifications = 0;
		objectsLists.clear();
		for (ObjectSimplifier<?> o : simplifiers.values()) {
			o.clear();
		}
		fieldsSimplifier.clear();
	}

	public Object simplify(Object o) {
		Object oldObject = o;
		if (o instanceof SceneElement || o.getClass() == BasicElement.class) {
			// SceneElement can not be simplified
			// BaiscElement are already simplified
			return o;
		}

		Class<?> oldClass = o.getClass();
		Class<?> newClass = null;
		while (oldClass != newClass) {
			Object newObject = simplifyImpl(o);
			if (newObject == null) {
				logger.warn("Error simplifiying asset: {}", o);
				return null;
			}
			newClass = newObject.getClass();
			oldClass = o.getClass();
			o = newObject;
		}

		List<Object> list = objectsLists.get(o.getClass());
		if (list == null) {
			list = new ArrayList<Object>();
			objectsLists.put(o.getClass(), list);
		}
		o = simplifyCheckEquals(o, list, generalEq);
		if (oldObject != o) {
			simplifications++;
		}
		return o;
	}

	@SuppressWarnings( { "rawtypes", "unchecked" })
	public Object simplifyImpl(Object o) {
		ReflectionClass<?> clazz = ReflectionClassLoader.getReflectionClass(o
				.getClass());

		while (clazz != null) {
			ObjectSimplifier simplifier = simplifiers.get(clazz.getType());
			if (simplifier != null) {
				o = simplifier.simplify(o);
			}
			for (ReflectionClass<?> i : clazz.getInterfaces()) {
				simplifier = simplifiers.get(i.getType());
				if (simplifier != null) {
					o = simplifier.simplify(o);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return o;
	}

	public int getSimplifications() {
		return simplifications;
	}

	public interface CheckEquals<T> {

		boolean equals(T o1, T o2);
	}

	private CheckEquals<Object> generalEq = new CheckEquals<Object>() {

		@Override
		public boolean equals(Object o1, Object o2) {
			if (o1 == o2) {
				return true;
			}
			boolean equals = EAdUtils.equals(o1, o2, true);

			return equals;
		}

	};

	private Object simplifyCheckEquals(Object o, List<Object> list,
			CheckEquals<Object> generalEq2) {
		Object copy = find(o, list, generalEq2);
		if (copy == null) {
			copy = o;
			list.add(o);
		} else {
			simplifications++;
		}
		return copy;
	}

	public Object find(Object o2, List<Object> list,
			CheckEquals<Object> generalEq2) {
		for (Object o : list) {
			if (generalEq2.equals(o2, o)) {
				return o;
			}
		}
		return null;
	}

	public Map<Object, Map<String, ElementField>> getFields() {
		return fieldsSimplifier.getFields();
	}

	public Map<Class<?>, List<Object>> getIdentified() {
		return objectsLists;
	}

}
