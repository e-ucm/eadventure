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

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import com.gwtent.reflection.client.ClassHelper;
import com.gwtent.reflection.client.ReflectionRequiredException;

import ead.tools.reflection.ReflectionClassLoader;
import ead.tools.reflection.ReflectionProvider;

@Singleton
public class GwtReflectionProvider implements ReflectionProvider {

	private static final Logger logger = LoggerFactory
			.getLogger("ReflectionProvider");

	public GwtReflectionProvider() {
		ReflectionClassLoader.init(new GwtReflectionClassLoader());
	}

	@Override
	public Class<?>[] getInterfaces(Class<?> object) {
		return ClassHelper.AsClass(object).getInterfaces();
	}

	@Override
	public boolean isAssignableFrom(Class<?> class1, Class<?> class2) {
		if (class1 == Object.class || class1 == class2)
			return true;
		// Special cases
		if (class2 == String.class) {
			return false;
		}

		if (class2 == Float.class || class2 == Integer.class
				|| class2 == Long.class || class2 == Double.class) {
			return class1 == Number.class;
		}

		Stack<Class<?>> stack = new Stack<Class<?>>();
		stack.push(class2);

		while (!stack.isEmpty()) {
			Class<?> temp = stack.pop();
			if (class1 == temp)
				return true;
			if (temp != null) {
				try {
					Class<?> temp2 = ClassHelper.AsClass(temp).getSuperclass();
					if (temp2 != null)
						stack.push(temp2);
					for (Class<?> newClass : ClassHelper.AsClass(temp)
							.getInterfaces())
						stack.push(newClass);
				} catch (ReflectionRequiredException e) {
					logger.error(
							"Reflection required for {} assignable-from {}",
							new Object[] { class1, class2 }, e);
				}
			}

			if (!stack.isEmpty() && stack.peek() == Object.class)
				stack.pop();
		}
		return false;
	}

	@Override
	public Class<?> getSuperclass(Class<?> c) {
		return ClassHelper.AsClass(c).getSuperclass();
	}

	@Override
	public Class<?> getClassforName(String string) {
		try {
			return ClassHelper.forName(string);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

}
