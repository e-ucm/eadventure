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

package es.eucm.eadventure.common.model.test;

import junit.framework.TestCase;

import org.junit.Test;

import es.eucm.eadventure.common.model.params.EAdColor;

public class EAdColorTest extends TestCase {

	@Override
	public void setUp() {

	}
	
	@Test
	public void testToString() {
		EAdColor black = new EAdColor(0,0,0);
		assertEquals(black.toString(), "0x000000ff");
		EAdColor color2 = new EAdColor(0,0,3);
		assertEquals(color2.toString(), "0x000003ff");
		EAdColor normalized = new EAdColor(0,354,-2);
		assertEquals(normalized.toString(), "0x00ff00ff");
		EAdColor white = new EAdColor();
		assertEquals(white.toString(), "0xffffffff");
		white = new EAdColor(255,255,255);
		assertEquals(white.toString(), "0xffffffff");
	}

	@Test
	public void testParse() {
		EAdColor black = EAdColor.valueOf("0x000000ff");
		assertEquals(black, new EAdColor(0,0,0));
		EAdColor white = EAdColor.valueOf("0xffffffff");
		assertEquals(white, new EAdColor(255,255,255));
	}


}
