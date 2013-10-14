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

import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.tools.reflection.ReflectionClass;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;
import es.eucm.ead.tools.reflection.ReflectionField;

import java.util.*;

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

	private static Stack<Object> elements1 = new Stack<Object>();
	private static Stack<Object> elements2 = new Stack<Object>();
	private static ArrayList<Object> compared1 = new ArrayList<Object>();
	private static ArrayList<Object> compared2 = new ArrayList<Object>();

	private static boolean ignoreId;

	public static void shuffle(List<?> list, Random rnd) {
		if (list instanceof RandomAccess) {
			int size = list.size();
			for (int i = size; i > 1; i--)
				Collections.swap(list, i - 1, rnd.nextInt(i));
		}
	}

	public static boolean equals(Object o1, Object o2, boolean ignoreId) {
		return equals(o1, o2, ignoreId, null);
	}

	public static boolean equals(Object o1, Object o2, boolean ignoreId,
			NotEqualHandler handler) {
		elements1.clear();
		elements2.clear();
		compared1.clear();
		compared2.clear();
		EAdUtils.ignoreId = ignoreId;
		return equalsImpl(o1, o2, handler);
	}

	private static boolean equalsImpl(Object o1, Object o2,
			NotEqualHandler handler) {
		boolean result = checkStack(o1, o2);
		boolean alreadyCompared = checkCompared(o1, o2);
		if (alreadyCompared) {
			result = true;
		} else if (result && elements1.indexOf(o1) == -1) {
			push(o1, o2);
			if (o1 == o2) {
				result = true;
			} else if (o1 == null || o2 == null) {
				result = false;
			} else if (o1.getClass() != o2.getClass()) {
				result = o1 instanceof BasicElement
						&& o2 instanceof BasicElement
						&& ((BasicElement) o1).getId().equals(
								((BasicElement) o2).getId());
			} else if (o1 instanceof Number || o1 instanceof String
					|| o1 instanceof Boolean
					|| o1.getClass() == BasicElement.class) {
				result = o1.equals(o2);
			} else if (o1 instanceof List) {
				List<?> list1 = (List<?>) o1;
				List<?> list2 = (List<?>) o2;
				result = (list1.size() == list2.size());
				for (int i = 0; i < list1.size() && result; i++) {
					result = equalsImpl(list1.get(i), list2.get(i), handler);
				}
			} else if (o1 instanceof Map) {
				Map<?, ?> map1 = (Map<?, ?>) o1;
				Map<?, ?> map2 = (Map<?, ?>) o2;
				result = map1.size() == map2.size();

				Iterator<?> it1 = map1.keySet().iterator();
				Iterator<?> it2 = map2.keySet().iterator();
				while (result && it1.hasNext()) {
					Object v1 = it1.next();
					Object v2 = it2.next();
					result = result && equalsImpl(v1, v2, handler);
				}

				it1 = map1.values().iterator();
				it2 = map2.values().iterator();
				while (result && it1.hasNext()) {
					Object v1 = it1.next();
					Object v2 = it2.next();
					result = equalsImpl(v1, v2, handler);
				}
			} else if (o1 instanceof Identified && o2 instanceof Identified) {
				ReflectionClass<?> clazz = ReflectionClassLoader
						.getReflectionClass(o1.getClass());
				while (clazz != null) {
					for (ReflectionField f : clazz.getFields()) {

						if (!result) {
							break;
						}

						if (!f.isStatic() && !f.isTransient()) {
							if (ignoreId && f.getName().equals("id")) {
								continue;
							}
							Object v1 = f.getFieldValue(o1);
							Object v2 = f.getFieldValue(o2);
							result = result && equalsImpl(v1, v2, handler);
						}
					}
					clazz = clazz.getSuperclass();
				}
			} else {
				result = o1.equals(o2);
			}
			pop();
		}
		if (!result && handler != null) {
			handler.notEqual(o1, o2);
		}
		compared1.add(o1);
		compared2.add(o2);
		return result;
	}

	private static void push(Object o1, Object o2) {
		elements1.push(o1);
		elements2.push(o2);
	}

	private static void pop() {
		elements1.pop();
		elements2.pop();
	}

	private static boolean checkStack(Object o1, Object o2) {
		int index1 = elements1.indexOf(o1);
		int index2 = elements2.indexOf(o2);
		return index1 == index2;
	}

	private static boolean checkCompared(Object o1, Object o2) {
		int index1 = compared1.indexOf(o1);
		int index2 = compared2.indexOf(o2);
		return index1 != -1 && index1 == index2;
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

	public static <T, S> EAdMap<S, T> invertMap(EAdMap<T, S> map) {
		EAdMap<S, T> map2 = new EAdMap<S, T>();
		for (Map.Entry<T, S> e : map.entrySet()) {
			map2.put(e.getValue(), e.getKey());
		}
		return map2;
	}

	public interface NotEqualHandler {
		void notEqual(Object o1, Object o2);
	}
}
