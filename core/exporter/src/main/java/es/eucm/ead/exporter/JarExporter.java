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

package es.eucm.ead.exporter;

import es.eucm.ead.tools.java.utils.FileUtils;
import org.apache.maven.cli.MavenCli;

import java.io.*;

/**
 * Maven wrapper able to export projects to jar/exe
 */
public class JarExporter {

	public JarExporter() {
	}

	/**
	 * @param projectFolder folder containing a folder (name as parameter 'resourcesFolder') with all the assets of the game
	 * @param destination       destination file to ouput the .jar
	 */
	public void export(String projectFolder, String destination, PrintStream out) {
		// Copy desktop-pom.xml to desktop folder
		InputStream jarpom = ClassLoader
				.getSystemResourceAsStream("desktop-pom.xml");
		OutputStream os = null;
		try {
			File desktopFolder = FileUtils.createTempDir("eaddesktop", "");
			desktopFolder.mkdirs();

			// Copy pom to temp folder
			FileUtils.copy(jarpom, new FileOutputStream(new File(desktopFolder,
					"pom.xml")));

			// Generate the jar with maven
			MavenCli maven = new MavenCli();
			maven.doMain(new String[] { "-X", "-Dresources=" + projectFolder,
					"clean", "compile", "install", "assembly:single" },
					desktopFolder.getAbsolutePath(), out, out);

			// Copy jar to destination
			File destinationFile = new File(destination);
			if (destinationFile.isDirectory()) {
				destinationFile = new File(destinationFile,
						"eAdventuregame.jar");
			} else if (!destination.endsWith(".jar")) {
				destination += ".jar";
				destinationFile = new File(destination);
			}

			// Copy to destination file
			if (destinationFile != null) {
				FileUtils.copy(new File(desktopFolder.getAbsolutePath()
						+ "/target",
						"game-desktop-1.0-jar-with-dependencies.jar"),
						destinationFile);
			}
		} catch (Exception e) {

		} finally {
			if (jarpom != null) {
				try {
					jarpom.close();
				} catch (IOException e) {
				}
			}

			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
		}

	}
}
