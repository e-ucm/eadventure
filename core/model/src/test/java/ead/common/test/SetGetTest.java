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

package ead.common.test;

import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.interfaces.features.Identified;
import org.junit.Test;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import static junit.framework.Assert.fail;
import static org.reflections.ReflectionUtils.getFields;
import static org.reflections.ReflectionUtils.getMethods;
import static org.reflections.ReflectionUtils.withAnnotation;
import static org.reflections.ReflectionUtils.withName;
import static org.reflections.ReflectionUtils.withParameters;
import static org.reflections.ReflectionUtils.withParametersCount;

/**
 * This test ensures that all elements of the model have 
 * proper setters and getters for attributes annotated as 'param'
 * (this is needed for GWT compatibility.)
 */
public class SetGetTest {

	@Test
	public void testSetGest() {
		Reflections reflections = new Reflections("es.eucm.ead.model");
		for (Class<?> c : reflections.getSubTypesOf(Identified.class)) {
			for (Field f : getFields(c, withAnnotation(Param.class))) {
				String input = f.getName();
				String output = input.substring(0, 1).toUpperCase()
						+ input.substring(1);
				Set<Method> s = getMethods(c, withName("get" + output),
						withParametersCount(0));
				Set<Method> s2 = getMethods(c, withName("is" + output),
						withParametersCount(0));
				if (s.size() != 1 && s2.size() != 1) {
					fail("No get method for field " + f.getName()
							+ " in class " + c);
				}
				s = getMethods(c, withName("set" + output), withParameters(f
						.getType()));
				if (s.size() != 1) {
					fail("No set method for field " + f.getName()
							+ " in class " + c);
				}
			}
		}
	}
}
