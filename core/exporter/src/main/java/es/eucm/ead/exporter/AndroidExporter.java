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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Properties;

import org.apache.maven.cli.MavenCli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.ead.engine.resources.R;
import es.eucm.ead.tools.java.utils.FileUtils;

public class AndroidExporter {

	public static final String SDK_HOME = "android_home";

	private static final Logger logger = LoggerFactory
			.getLogger("AndroidExporter");

	private PrintStream stdOut;

	private PrintStream stdErr;

	private MavenCli maven;

	public AndroidExporter(MavenCli maven) {
		this.maven = maven;
		this.stdOut = System.out;
		this.stdErr = System.err;
	}

	public PrintStream getStdOut() {
		return stdOut;
	}

	public void setStdOut(PrintStream stdOut) {
		this.stdOut = stdOut;
	}

	public PrintStream getStdErr() {
		return stdErr;
	}

	public void setStdErr(PrintStream stdErr) {
		this.stdErr = stdErr;
	}

	public AndroidExporter() {
		this(new MavenCli());
	}

	public void export(String projectFolder, String destiny,
			Properties properties, boolean installAndDeploy) {
		// Copy android-pom.xml to project/android
		InputStream apkpom = ClassLoader
				.getSystemResourceAsStream("android-pom.xml");
		InputStream manifest = ClassLoader
				.getSystemResourceAsStream("AndroidManifest.xml");
		OutputStream os = null;
		try {
			File androidFolder = FileUtils.createTempDir("eadandroid", "");
			FileUtils.deleteRecursive(androidFolder);
			androidFolder.mkdirs();

			FileUtils.copy(apkpom, new FileOutputStream(new File(androidFolder,
					"pom.xml")));
			FileUtils.copy(manifest, new FileOutputStream(new File(
					androidFolder, "AndroidManifest.xml")));

			// Copy game assets to folder
			FileUtils.copyRecursive(new File(projectFolder), androidFolder,
					androidFolder);
			// Copy engine assets to folder
			for (String s : R.RESOURCES) {
				InputStream ris = ClassLoader.getSystemResourceAsStream(s);
				OutputStream ros = null;
				try {
					String fileName = s.substring(s.lastIndexOf('/') + 1);
					String folder = s.substring(0, s.lastIndexOf('/'));
					File rf = new File(androidFolder.getAbsolutePath() + '/'
							+ folder);
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

			String[] mavenArguments;

			if (installAndDeploy) {
				mavenArguments = new String[] {
						"-Dresources=" + androidFolder.getAbsolutePath(),
						"-Dandroid.sdk.path="
								+ properties.getProperty(SDK_HOME), "clean",
						"compile", "install", "android:deploy", "android:run" };
			} else {
				mavenArguments = new String[] {
						"-Dresources=" + androidFolder.getAbsolutePath(),
						"-Dandroid.sdk.path="
								+ properties.getProperty(SDK_HOME), "clean",
						"compile", "install", };
			}

			maven.doMain(mavenArguments, androidFolder.getAbsolutePath(),
					getStdOut(), getStdErr());
			// Copy apk to destiny
			if (destiny != null) {
				File destinyFile = new File(destiny);
				if (destinyFile.isDirectory()) {
					destinyFile = new File(destinyFile, "eAdventuregame.apk");
				} else if (!destiny.endsWith(".apk")) {
					destiny += ".apk";
					destinyFile = new File(destiny);
				}
				/* FileUtils.copy(new File(androidFolder.getAbsolutePath()
						+ "/target", "game-android.apk"), destinyFile);*/
			}
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
