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
import java.lang.reflect.Field;

import ead.tools.reflection.ReflectionClass;
import ead.tools.reflection.ReflectionClassLoader;
import ead.tools.reflection.ReflectionField;

public class JavaReflectionField implements ReflectionField {

	private Field field;

	public JavaReflectionField(Field field) {
		this.field = field;
		field.setAccessible(true);
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return field.getAnnotation(annotationClass);
	}

	@Override
	public String getName() {
		return field.getName();
	}

	@Override
	public ReflectionClass<?> getType() {
		return ReflectionClassLoader.getReflectionClass(field.getType());
	}

	@Override
	public Object getFieldValue(Object object) {
		try {
			return field.get(object);
		} catch (IllegalArgumentException e) {

		} catch (IllegalAccessException e) {

		}
		return null;
	}

	@Override
	public void setFieldValue(Object object, Object value) {
		try {
			field.set(object, value);
		} catch (IllegalArgumentException e) {

		} catch (IllegalAccessException e) {

		}
	}

}
