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

package ead.engine.core.test;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;

import ead.engine.core.game.ValueMap;

public class ValueMapTest {

	@Inject
	protected ValueMap v;

	@Before
	public void setUp() throws Exception {
//		v.setValue(new StringVar("name"), "John");
//		v.setValue(new StringVar("surname"), "Doe");
//		v.setValue(new IntegerVar("age"), new Integer(35));
//		v.setValue(new FloatVar("height"), new Float(1.88f));
//		v.setValue(new BooleanVar("true"), Boolean.TRUE);
//		v.setValue(new BooleanVar("false"), Boolean.FALSE);
//		v.setValue(new FloatVar("zero"), 0.0f);
	}

	@Test
	public void testGetValueS() {
//		assertEquals(v.getValue(new StringVar("name")), "John");
//		assertEquals(v.getValue(new StringVar("surname")), "Doe");
//		assertEquals(v.getValue(new IntegerVar("age")), new Integer(35));
//		assertEquals(v.getValue(new FloatVar("height")), new Float(1.88f));
//		assertEquals(v.getValue(new StringVar("non-existing-var")), null);
//		
//		v.setValue(new StringVar("name"), "Peter");
//
//		assertEquals(v.getValue(new StringVar("name")), "Peter");
	}

	@Test
	public void testProcessTextVars() {
		String[] textsToProcess = new String[] {
				"A var at the end #name",
				"This text has some vars to be replace. First, the name, wich is: #name, and second,",
				"What was the surname? (Sometimes I have bad memory!) Was it #surname?",
				"Some age too, like #age (or something? I don't know well: too bad)",
				"In twitter, sometimes they used hashtags like #hashtag? #name loves twitter",
				"Some condition too, like (#true? that was true : that was false ) and (#false? that was true : that was false )",
				"(#zero? wrong choice:right choice) OK.",
				"(#height? good :bad)" };

//		String[] resultStrings = new String[] {
//				"A var at the end " + "John",
//				"This text has some vars to be replace. First, the name, wich is: "
//						+ "John" + ", and second,",
//				"What was the surname? (Sometimes I have bad memory!) Was it "
//						+ "Doe" + "?",
//				"Some age too, like " + "35"
//						+ " (or something? I don't know well: too bad)",
//				"In twitter, sometimes they used hashtags like #hashtag? "
//						+ "John" + " loves twitter",
//				"Some condition too, like  that was true  and  that was false ",
//				"right choice OK.", " good " };

		for (int i = 0; i < textsToProcess.length; i++) {
//			String textToProcess = textsToProcess[i];
//			String resultString = resultStrings[i];
//			String processedText = ((VariableMap) v).processTextVars(textToProcess);
//			assertEquals(resultString, processedText);
		}

	}
}
