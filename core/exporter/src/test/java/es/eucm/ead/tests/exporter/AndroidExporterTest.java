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

package es.eucm.ead.tests.exporter;

import es.eucm.ead.exporter.AndroidExporter;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AndroidExporterTest {

	@Test
	public void testExport() {
		AndroidExporter apkExporter = new AndroidExporter();
		Properties properties = new Properties();
		InputStream is;
		boolean error = false;
		try {
			is = ClassLoader.getSystemResourceAsStream("test.properties");
			properties.load(is);
			error = properties.get(AndroidExporter.SDK_HOME) == null;
		} catch (Exception e) {
			System.err.println(e);
			error = true;
		} finally {
			if (error) {
				if (properties != null) {
					System.err.println("Invalid properties file found");
					for (String k : properties.stringPropertyNames()) {
						System.err.println("\t'" + k + "' : '"
								+ properties.getProperty(k) + "'");
					}
				} else {
					System.err.println("No properties file found");
				}

				fail("You need to create a text file named 'test.properties' "
						+ "with a line that reads '"
						+ AndroidExporter.SDK_HOME
						+ "'=path/to/android/sdk'"
						+ "(See /src/test/resources/test.properties.template for a template)");
			}
		}
		properties.setProperty(AndroidExporter.PACKAGE_NAME,
				"es.eucm.ead.exporter.test.game");
		properties.setProperty(AndroidExporter.TITLE, "Exporter Test Game");
		properties.setProperty(AndroidExporter.ICON,
				"src/test/resources/orangelogo.png");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream stream = new PrintStream(baos);
		apkExporter.setStdErr(stream);
		apkExporter.setStdOut(stream);
		apkExporter.export("src/test/resources/convertedproject/", null,
				properties, false);

		String result = baos.toString();
		if (result.contains("BUILD FAILURE")) {
			fail(result);
		} else {
			assertTrue(result.contains("BUILD SUCCESS"));
		}
	}
}
