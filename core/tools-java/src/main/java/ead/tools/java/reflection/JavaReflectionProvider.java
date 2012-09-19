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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import ead.common.interfaces.Element;
import ead.common.model.EAdElement;
import ead.tools.reflection.ReflectionProvider;

@Singleton
public class JavaReflectionProvider implements ReflectionProvider {

	private static final Logger logger = LoggerFactory.getLogger("ReflectionProvider");

	@Override
	public Class<?>[] getInterfaces(Class<?> object) {
		return object.getInterfaces();
	}

	@Override
	public boolean isAssignableFrom(Class<?> class1, Class<?> class2) {
		return class1 == class2 || class1.isAssignableFrom(class2);
	}

	@Override
	public Class<?> getSuperclass(Class<?> c) {
		return c.getSuperclass();
	}

	@Override
	public Class<?> getRuntimeClass(EAdElement element) {
		Class<?> clazz = element.getClass();
		while (clazz != null ) {
			if (clazz.getAnnotation(Element.class) != null) {
				return clazz;
			}
			clazz = clazz.getSuperclass();
		}
		logger.error("No element annotation for class {}", element.getClass());
		return null;
	}
}
