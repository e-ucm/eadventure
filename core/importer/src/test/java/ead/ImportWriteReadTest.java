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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.model.elements.EAdAdventureModel;
import ead.importer.EAdventureImporter;
import ead.reader.adventure.AdventureReader;
import ead.reader.adventure.ObjectFactory;
import ead.tools.java.FileUtils;
import ead.tools.java.reflection.JavaReflectionClassLoader;
import ead.tools.java.reflection.JavaReflectionProvider;
import ead.tools.java.xml.JavaXMLParser;
import ead.tools.reflection.ReflectionClassLoader;
import ead.writer.DataPrettifier;
import ead.writer.EAdAdventureModelWriter;

public class ImportWriteReadTest {

	private static final Logger logger = LoggerFactory
			.getLogger("ImportWriteReadTest");

	private EAdventureImporter importer;
	private AdventureReader reader;
	private EAdAdventureModelWriter writer;

	@Before
	public void setUp() {
		importer = new EAdventureImporter();
		reader = new AdventureReader(new JavaXMLParser());
		writer = new EAdAdventureModelWriter();
		ObjectFactory.init(new JavaReflectionProvider());
		ReflectionClassLoader.init(new JavaReflectionClassLoader());
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

			File modelAfterImport = new File(eadtemp, "data.xml");
			
			File modelFile = File.createTempFile("tempmodel",
					Long.toString(System.nanoTime()) + ".xml");
			File modelFile2 = File.createTempFile("tempmodel",
					Long.toString(System.nanoTime()) + ".xml");
			File modelFileAfterRead = File.createTempFile("tempmodel",
					Long.toString(System.nanoTime()) + ".xml");
			File modelFileAfterReadPretty = File.createTempFile("tempmodel",
					Long.toString(System.nanoTime()) + ".xml");
			File modelFilePretty = File.createTempFile("tempmodel",
					Long.toString(System.nanoTime()) + ".xml");

			writer.write(model, modelFile.getAbsolutePath());
			writer.write(model, modelFile2.getAbsolutePath());

			assertTrue(equals(modelAfterImport, modelFile));
			assertTrue(equals(modelFile, modelFile2));

//			EAdAdventureModel model2 = reader
//					.readXML(getString(modelAfterImport));
			

			writer.write(model, modelFileAfterRead.getAbsolutePath());

			DataPrettifier.prettify(modelFileAfterRead,
					modelFileAfterReadPretty);
			DataPrettifier.prettify(modelFile, modelFilePretty);

//			assertTrue(equals(modelFileAfterReadPretty, modelFilePretty));

			FileUtils.deleteRecursive(eadtemp);
//			assertFalse(eadtemp.exists());
//			assertTrue(modelFile.delete());
//			assertTrue(modelFile2.delete());
//			assertTrue(modelFilePretty.delete());
//			assertTrue(modelFileAfterRead.delete());
//			assertTrue(modelFileAfterReadPretty.delete());

		} catch (IOException e) {
			fail();
		}

	}

	public boolean equals(File f1, File f2) {
		try {
			return FileUtils.isFileBinaryEqual(f1, f2);
		} catch (IOException e) {
			return false;
		}
//		String s1 = getString(f1);
//		String s2 = getString(f2);
//		logger.debug("Comparing {} and {}", new Object[] { s1, s2 });
//		return s1.equals(s2);
	}

	public String getString(File f) {
		String s = "";
		BufferedReader reader = null;
		try {
			char[] c = new char[(int) f.length()];
			FileReader fileReader = new FileReader(f);
			fileReader.read(c);
			fileReader.close();
			StringBuilder builder = new StringBuilder( );
			builder.append(c);			
		} catch (FileNotFoundException e) {
			fail();
		} catch (IOException e) {
			fail();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					fail();
				}
			}
		}
		return s;
	}

}
