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

import es.eucm.ead.engine.resources.R;
import es.eucm.ead.tools.java.utils.FileUtils;
import org.apache.maven.cli.MavenCli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class AndroidExporter {

	private static final Logger logger = LoggerFactory
			.getLogger("AndroidExporter");

	private MavenCli maven;

	public AndroidExporter(MavenCli maven) {
		this.maven = maven;
	}

	public AndroidExporter() {
		this(new MavenCli());
	}

	public void export(String projectFolder, String resourcesFolder,
			String destiny, Properties properties) {
		// Copy android-pom.xml to project/android
		InputStream apkpom = ClassLoader
				.getSystemResourceAsStream("android-pom.xml");
		InputStream manifest = ClassLoader
				.getSystemResourceAsStream("AndroidManifest.xml");
		OutputStream os = null;
		try {
			File androidFolder = new File(projectFolder + "/android");
			FileUtils.deleteRecursive(androidFolder);
			androidFolder.mkdirs();
			File eadresourcesFolder = new File(androidFolder + "/eadresources");
			eadresourcesFolder.mkdirs();

			FileUtils.copy(apkpom, new FileOutputStream(new File(androidFolder,
					"pom.xml")));
			FileUtils.copy(manifest, new FileOutputStream(new File(
					androidFolder, "AndroidManifest.xml")));

			// Copy game assets to folder
			FileUtils.copyRecursive(new File(resourcesFolder),
					eadresourcesFolder, eadresourcesFolder);
			// Copy engine assets to folder
			for (String s : R.RESOURCES) {
				InputStream ris = ClassLoader.getSystemResourceAsStream(s);
				OutputStream ros = null;
				try {
					String fileName = s.substring(s.lastIndexOf('/') + 1);
					String folder = s.substring(0, s.lastIndexOf('/'));
					File rf = new File(eadresourcesFolder.getAbsolutePath()
							+ '/' + folder);
					rf.mkdirs();
					ros = new FileOutputStream(new File(rf, fileName));
					FileUtils.copy(ris, ros);
				} catch (Exception e) {
					logger.error("Problem copying resources {}", s, e);
				} finally {
					if (ris != null) {
						ris.close();
					}
					if (ros != null) {
						ros.close();
					}
				}
			}

			maven.doMain(new String[] {
					"-Dresources=" + eadresourcesFolder.getAbsolutePath(),
					"-Dandroid.sdk.path="
							+ properties.getProperty("sdk_folder"), "clean",
					"compile", "install", "android:deploy", "android:run" },
					androidFolder.getAbsolutePath(), System.out, System.err);
			// Copy apk to destiny
			File destinyFile = new File(destiny);
			if (destinyFile.isDirectory()) {
				destinyFile = new File(destinyFile, "eAdventuregame.apk");
			} else if (!destiny.endsWith(".apk")) {
				destiny += ".apk";
				destinyFile = new File(destiny);
			}
			FileUtils.copy(new File(
					androidFolder.getAbsolutePath() + "/target",
					"game-android.apk"), destinyFile);
		} catch (Exception e) {
			logger.error("Error exporting apk", e);
		} finally {
			if (apkpom != null) {
				try {
					apkpom.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}

			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}

			if (manifest != null) {
				try {
					manifest.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}
	}
}
