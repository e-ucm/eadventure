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

package es.eucm.ead.tests.importer;

import es.eucm.ead.importer.AdventureConverter;
import es.eucm.ead.model.elements.EAdAdventureModel;
import es.eucm.ead.reader.AdventureReader;
import es.eucm.ead.tools.EAdUtils;
import es.eucm.ead.tools.java.JavaTextFileReader;
import es.eucm.ead.tools.java.reflection.JavaReflectionProvider;
import es.eucm.ead.tools.java.utils.FileUtils;
import es.eucm.ead.tools.java.xml.SaxXMLParser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class TestImporter {

	private AdventureConverter converter;

	private AdventureReader reader;

	private final String ROOT = "src/test/resources/";

	private final String[] GAMES = new String[] { "Minimum", "DamaBoba",
			"PrimerosAuxilios" };

	private final File TEMP = new File("temp/");

	@Before
	public void setUp() {
	}

	@Test
	public void testImports() {
		for (String g : GAMES) {
			TEMP.mkdirs();
			converter = new AdventureConverter();
			converter.setEnableTranslations(false);
			reader = new AdventureReader(new JavaReflectionProvider(),
					new SaxXMLParser(), new JavaTextFileReader());
			String destiny = converter
					.convert(ROOT + g, TEMP.getAbsolutePath());
			EAdAdventureModel modelConverted = converter.getModel();
			reader.setPath(destiny + "/");
			EAdAdventureModel modelRead = reader.readFullModel();
			EAdUtils.NotEqualHandler notEqualHandler = new EAdUtils.NotEqualHandler() {
				@Override
				public void notEqual(Object o1, Object o2) {
					System.out.println(o1 + " is not equal to " + o2);
				}
			};
			assertTrue(EAdUtils.equals(modelConverted, modelConverted, false,
					notEqualHandler));
			assertTrue(EAdUtils.equals(modelRead, modelRead, false,
					notEqualHandler));
			assertTrue(EAdUtils.equals(modelConverted, modelRead, false,
					notEqualHandler));

			try {
				FileUtils.deleteRecursive(TEMP);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
