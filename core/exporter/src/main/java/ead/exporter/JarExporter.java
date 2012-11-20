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

package ead.exporter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.maven.Maven;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionResult;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.io.RawInputStreamFacade;

/**
 * Maven wrapper able to export projects to jar/exe
 * 
 */
public class JarExporter implements Exporter {

	private Maven maven;

	private String name;

	public JarExporter(Maven maven) {
		this.maven = maven;
		this.name = "game";
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setIcon(File icon) {

	}

	public void export(String gameBaseDir, String outputfolder) {
		String tempFolder = System.getProperty("java.io.tmpdir");
		File jarTemp = new File(tempFolder + File.separator
				+ "eAdventureJarTemp" + Math.round(Math.random() * 1000));
		jarTemp.mkdirs();

		// Load pom file
		File pomFile = new File(jarTemp, "pom.xml");
		try {
			FileUtils.copyStreamToFile(new RawInputStreamFacade(ClassLoader
					.getSystemResourceAsStream("pom/desktoppom.xml")), pomFile);
		} catch (IOException e1) {

		}

		MavenExecutionRequest request = new DefaultMavenExecutionRequest();

		// Goals
		ArrayList<String> goals = new ArrayList<String>();
		goals.add("package");
		request.setGoals(goals);

		// Properties
		Properties userProperties = new Properties();
		userProperties.setProperty("game.basedir", gameBaseDir);
		userProperties.setProperty("game.outputfolder", outputfolder);
		userProperties.setProperty("game.name", name);
		request.setUserProperties(userProperties);

		// Set files
		request.setBaseDirectory(jarTemp);
		request.setPom(pomFile);

		// Execute maven
		MavenExecutionResult result = maven.execute(request);
		for (Throwable e : result.getExceptions()) {
			e.printStackTrace();
		}

		// Copy to output folder
		File jarFile = new File(jarTemp, "target/desktop-game-1.0-unified.jar");
		File dstFile = new File(outputfolder, name + ".jar");
		try {
			FileUtils.copyFile(jarFile, dstFile);
		} catch (IOException e2) {

		}

		try {
			FileUtils.deleteDirectory(jarTemp);
		} catch (IOException e1) {

		}

	}

}
