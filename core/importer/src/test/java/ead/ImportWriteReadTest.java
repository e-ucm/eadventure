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

package ead;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA_2_3.portable.OutputStream;

import ead.common.model.elements.EAdAdventureModel;
import ead.importer.EAdventureImporter;
import ead.reader.adventure.AdventureReader;
import ead.tools.java.FileUtils;
import ead.tools.java.xml.JavaXMLParser;
import ead.writer.EAdAdventureModelWriter;

public class ImportWriteReadTest {

	private EAdventureImporter importer;
	private AdventureReader reader;
	private EAdAdventureModelWriter writer;

	@Before
	public void setUp() {
		importer = new EAdventureImporter();
		reader = new AdventureReader(new JavaXMLParser());
		writer = new EAdAdventureModelWriter();
	}

	@Test
	public void testImport() {

		File f = new File(ClassLoader.getSystemResource(
				"ead/importer/test/test.ead").getPath());

		assertTrue(f.exists());

		try {
			File eadtemp = File.createTempFile("eadTemp",
					Long.toString(System.nanoTime()));

			if (!(eadtemp.delete())) {
				throw new IOException("Could not delete temp file: "
						+ eadtemp.getAbsolutePath());
			}

			if (!(eadtemp.mkdir())) {
				throw new IOException("Could not create temp directory: "
						+ eadtemp.getAbsolutePath());
			}

			EAdAdventureModel model = importer.importGame(f.getAbsolutePath(),
					eadtemp.getAbsolutePath(), "none");

			File modelFile = File.createTempFile("tempmodel",
					Long.toString(System.nanoTime()) + ".xml");

			writer.write(model, modelFile.getAbsolutePath());
			
			

			FileUtils.deleteRecursive(eadtemp);
			assertFalse(eadtemp.exists());
			assertTrue(modelFile.delete());

		} catch (IOException e) {
			fail();
		}

	}

}
