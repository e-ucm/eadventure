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

package ead.editor.model;

import ead.editor.model.ModelQuery.QueryPart;
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author mfreire
 */
public class ModelQueryTest {

	public void testParser(String queryString, boolean expectedValid,
			String... expectedParts) {
		if (!expectedValid) {
			assertFalse(ModelQuery.isValid(queryString));
			return;
		}

		assertTrue(ModelQuery.isValid(queryString));

		ArrayList<QueryPart> parts = new ModelQuery(queryString)
				.getQueryParts();
		for (int i = 0, j = 0; i < parts.size() && j < expectedParts.length; i++, j += 2) {
			assertEquals(expectedParts[j], parts.get(i).getField());
			assertEquals(expectedParts[j + 1], parts.get(i).getValue());
		}
		assertEquals(expectedParts.length, parts.size() * 2);
	}

	@Test
	public void testSimpleQueries() {
		System.out.println("simpleQueries");
		testParser("a simple test", true, "", "a", "", "simple", "", "test");
		testParser("one:two three:four", true, "one", "two", "three", "four");
	}

	@Test
	public void testQuotedQueries() {
		System.out.println("quotedQueries");
		testParser("a \"simple\" test", true, "", "a", "", "simple", "", "test");
		testParser("two \"three:four\":five", true, "", "two", "three:four",
				"five");
		testParser("two \"three four\":\"five six\"", true, "", "two",
				"three four", "five six");
	}

	@Test
	public void testInvalidQueries() {
		System.out.println("invalidQueries");
		testParser("", false);
		testParser("a simple \"test", false);
		testParser("one:two three:four:five", false);
	}
}
