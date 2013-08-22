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
 * 
 */
public class JarExporter {

	private MavenCli maven;

	public JarExporter(MavenCli maven) {
		this.maven = maven;
	}

	public JarExporter() {
		this(new MavenCli());
	}

	/**
	 *
	 * @param projectFolder folder containing a folder (name as parameter 'resourcesFolder') with all the assets of the game
	 * @param resourcesFolder name of folder inside 'projectFolder' containing all the assets of the game
	 * @param destiny destiny file to ouput the .jar
	 */
	public void export(String projectFolder, String resourcesFolder,
			String destiny) {
		// Copy desktop-pom.xml to project/desktop
		InputStream jarpom = ClassLoader
				.getSystemResourceAsStream("desktop-pom.xml");
		OutputStream os = null;
		try {
			File desktopFolder = new File(projectFolder + "/desktop");
			desktopFolder.mkdirs();
			FileUtils.copy(jarpom, new FileOutputStream(new File(desktopFolder,
					"pom.xml")));

			maven.doMain(new String[] { "-Dresources=" + resourcesFolder,
					"clean", "compile", "assembly:single" }, desktopFolder
					.getAbsolutePath(), System.out, System.err);
			// Copy jar to destiny
			File destinyFile = new File(destiny);
			if (destinyFile.isDirectory()) {
				destinyFile = new File(destinyFile, "eAdventuregame.jar");
			} else if (!destiny.endsWith(".jar")) {
				destiny += ".jar";
				destinyFile = new File(destiny);
			}
			FileUtils.copy(new File(
					desktopFolder.getAbsolutePath() + "/target",
					"game-desktop-1.0-jar-with-dependencies.jar"), destinyFile);
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
