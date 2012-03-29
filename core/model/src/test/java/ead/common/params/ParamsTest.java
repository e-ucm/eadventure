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

package ead.common.params;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ead.common.EqualsHashCodeTest;

public abstract class ParamsTest<T extends EAdParam> extends
		EqualsHashCodeTest<T> {

	public abstract T buildParam(String data);

	public abstract T defaultValue();

	@Test
	public void testToStringDataParse() {
		for (int i = 0; i < objects.length; i++) {
			String data = objects[i].toStringData();
			T c = buildParam(data);
			assertTrue(c.equals(objects[i]));
			assertTrue(objects[i].equals(c));
		}
	}

	@Test
	public void testParseCorruptedData(){
		String[] strings = new String[]{ null, "0;2;4;a", ";;;", "0:2;4.03f;0f", "dakjfaosidfyipu43676r21", "jo09ua87/77;;", ":0:2", "21;78;20:20", "ljasfasdfa", "123456789!"};
		T defaultValue = defaultValue();
		for ( String s: strings ){
			T c = buildParam(s);
			assertTrue(defaultValue.equals(c));
		}
	}
}
