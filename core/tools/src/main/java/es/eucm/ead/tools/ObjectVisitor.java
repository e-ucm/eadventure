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

package es.eucm.ead.tools;

import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.tools.reflection.ReflectionClass;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;
import es.eucm.ead.tools.reflection.ReflectionField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ObjectVisitor {

	private Stack<Object> parents;

	private List<ObjectListener> listeners;

	public ObjectVisitor() {
		parents = new Stack<Object>();
		listeners = new ArrayList<ObjectListener>();
	}

	public void addListener(ObjectListener l) {
		listeners.add(l);
	}

	public void visit(Object object) {
		parents.clear();
		visitImpl(object);
	}

	private void visitImpl(Object object) {
		if (parents.contains(object)) {
			return;
		}

		parents.push(object);
		for (ObjectListener l : listeners) {
			l.visit(object);
		}

		if (object instanceof EAdList) {
			for (Object value : (EAdList) object) {
				visitImpl(value);
			}
		} else if (object instanceof EAdMap) {
			EAdMap<?> map = (EAdMap) object;
			for (Map.Entry e : map.entrySet()) {
				parents.push(e);
				visitImpl(e.getKey());
				visitImpl(e.getValue());
				parents.pop();
			}
		} else if (object instanceof Boolean || object instanceof Number
				|| object instanceof String) {
			return;
		} else if (object instanceof Object) {
			ReflectionClass<?> clazz = ReflectionClassLoader
					.getReflectionClass(object.getClass());
			while (clazz != null) {
				for (ReflectionField f : clazz.getFields()) {
					Object value = f.getFieldValue(object);
					if (!parents.contains(value)) {
						visitImpl(value);
					}
				}
				clazz = clazz.getSuperclass();
			}
		}
		parents.pop();
	}

	public static interface ObjectListener {
		void visit(Object o);
	}

}
