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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.RandomAccess;
import java.util.Stack;

import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import ead.tools.reflection.ReflectionClass;
import ead.tools.reflection.ReflectionClassLoader;
import ead.tools.reflection.ReflectionField;

public class EAdUtils {

	public static final char[] ID_CHARS = new char[] { '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
			'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z', /*
										 * '.', '_', '\'', '?', '¿', '¡', '!', 'ñ',
										 * 'Ñ', 'ç', '+', '-', ' ', '@', '^', '#',
										 * '$', '%', '(', ')',';', ',', '{', '}',
										 * '*', '·', '[', ']', '`', '´'
										 */};

	private static Stack<Object> elements = new Stack<Object>();

	private static boolean ignoreId;

	public static void shuffle(List<?> list, Random rnd) {
		if (list instanceof RandomAccess) {
			int size = list.size();
			for (int i = size; i > 1; i--)
				Collections.swap(list, i - 1, rnd.nextInt(i));
		}
	}

	public static boolean equals(Object o1, Object o2, boolean ignoreId) {
		elements.clear();
		EAdUtils.ignoreId = ignoreId;
		return equalsImpl(o1, o2);
	}

	public static boolean equalsImpl(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}

		if (o1 == null || o2 == null) {
			return false;
		}

		if (o1.getClass() != o2.getClass()) {
			return false;
		}

		if (o1 instanceof EAdSceneElement || o1 instanceof BasicElement) {
			return o1.equals(o2);
		} else if (o1 instanceof Number || o1 instanceof String
				|| o1 instanceof Boolean || o1.getClass() == BasicElement.class) {
			return o1.equals(o2);
		} else if (o1 instanceof List) {
			List<?> list1 = (List<?>) o1;
			List<?> list2 = (List<?>) o2;
			if (list1.size() != list2.size()) {
				return false;
			}
			for (int i = 0; i < list1.size(); i++) {
				if (checkStack(list1.get(i))) {
					continue;
				}
				elements.push(o1);
				if (!equalsImpl(list1.get(i), list2.get(i))) {
					return false;
				}
				elements.pop();
			}
			return true;
		} else if (o1 instanceof Map) {
			Map<?, ?> map1 = (Map<?, ?>) o1;
			Map<?, ?> map2 = (Map<?, ?>) o2;
			if (map1.size() != map2.size()) {
				return false;
			}

			Iterator<?> it1 = map1.keySet().iterator();
			Iterator<?> it2 = map2.keySet().iterator();
			while (it1.hasNext()) {
				Object v1 = it1.next();
				Object v2 = it2.next();
				if (checkStack(v1)) {
					continue;
				}
				elements.push(v1);
				if (!equalsImpl(v1, v2)) {
					return false;
				}
				elements.pop();
			}

			it1 = map1.values().iterator();
			it2 = map2.values().iterator();
			while (it1.hasNext()) {
				Object v1 = it1.next();
				Object v2 = it2.next();
				if (checkStack(v1)) {
					continue;
				}
				elements.push(v1);
				if (!equalsImpl(v1, v2)) {
					return false;
				}
				elements.pop();
			}
			return true;
		}

		ReflectionClass<?> clazz = ReflectionClassLoader.getReflectionClass(o1
				.getClass());
		while (clazz != null) {
			for (ReflectionField f : clazz.getFields()) {
				if (!f.isStatic()) {
					if (ignoreId && f.getName().equals("id")) {
						continue;
					}
					Object v1 = f.getFieldValue(o1);
					Object v2 = f.getFieldValue(o2);
					if (checkStack(v1)) {
						continue;
					}
					elements.push(v1);
					if (!equalsImpl(v1, v2)) {
						return false;
					}
					elements.pop();
				}
			}
			clazz = clazz.getSuperclass();
		}
		return true;
	}

	private static boolean checkStack(Object o) {
		return !elements.isEmpty() && elements.peek() != o
				&& elements.contains(o);
	}

	public static String generateId(String prefix, int ordinal) {
		String id = "";
		int id2 = ordinal;
		boolean oneZero = false;
		while (!oneZero) {
			id = ID_CHARS[(id2 % ID_CHARS.length)] + id;
			id2 /= ID_CHARS.length;
			if (id2 == 0) {
				oneZero = true;
			}
		}
		return prefix + id;
	}
}
