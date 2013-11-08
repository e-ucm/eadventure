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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AndroidExporter {

	public static final String SDK_HOME = "android_home";

	public static final String PACKAGE_NAME = "package_name";

	public static final String ICON = "icon";

	static private Logger logger = LoggerFactory
			.getLogger(AndroidExporter.class);
	public static final String TITLE = "title";

	private PrintStream stdOut;

	private PrintStream stdErr;

	public AndroidExporter() {
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

	public void export(String projectFolder, String destination,
			Properties properties, boolean installAndDeploy) {
		// Copy android-pom.xml to android folder
		InputStream apkpom = ClassLoader
				.getSystemResourceAsStream("android-pom.xml");

		InputStream manifest = generateManifest(properties);
		OutputStream os = null;
		try {
			File androidFolder = FileUtils.createTempDir("eadandroid", "");
			FileUtils.deleteRecursive(androidFolder);
			androidFolder.mkdirs();
			addTitle(androidFolder, properties);
			addIcon(androidFolder, properties);

			logger.info("Finished adding title & icon to {}", androidFolder
					.getAbsolutePath());

			FileUtils.copy(apkpom, new FileOutputStream(new File(androidFolder,
					"pom.xml")));
			FileUtils.copy(manifest, new FileOutputStream(new File(
					androidFolder, "AndroidManifest.xml")));

			logger.info("Added manifest and pom; now copying game assets...");

			// Copy game assets to folder
			FileUtils.copyRecursive(new File(projectFolder), androidFolder,
					null);
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

			logger.info("... game assets & engine assets copied over");

			String[] mavenArguments;

			if (installAndDeploy) {
				mavenArguments = new String[] {
						"-Dresources=" + androidFolder.getAbsolutePath(),
						"-DfailIfNoTests=false",
						"-Dandroid.extractDuplicates=true",
						"-Dandroid.sdk.path="
								+ properties.getProperty(SDK_HOME), "-X",
						"clean", "compile", "install", "android:deploy",
						"android:run" };
			} else {
				mavenArguments = new String[] {
						"-Dresources=" + androidFolder.getAbsolutePath(),
						"-DfailIfNoTests=false",
						"-Dandroid.extractDuplicates=true",
						"-Dandroid.sdk.path="
								+ properties.getProperty(SDK_HOME), "-X",
						"clean", "compile", "install", };
			}

			logger.info("Starting maven build of game .apk...");

			MavenCli maven = new MavenCli();
			maven.doMain(mavenArguments, androidFolder.getAbsolutePath(),
					getStdOut(), getStdErr());

			logger.info("... finished. Now copying .apk to final destination");

			// Copy apk to destination
			if (destination != null) {
				File destinationFile = new File(destination);
				if (destinationFile.isDirectory()) {
					destinationFile = new File(destinationFile,
							"eAdventuregame.apk");
				} else if (!destination.endsWith(".apk")) {
					destination += ".apk";
					destinationFile = new File(destination);
				}
				FileUtils.copy(new File(androidFolder.getAbsolutePath()
						+ "/target", "game-android.apk"), destinationFile);
			}
		} catch (Exception e) {
			logger.error("Error exporting to apk", e);
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

	private void addIcon(File androidFolder, Properties properties) {
		String icon = properties.getProperty(ICON);
		if (icon != null) {
			File iconFile = new File(icon);
			if (iconFile.exists()) {
				File drawable = new File(androidFolder, "res/drawable/");
				drawable.mkdirs();
				try {
					FileUtils.copy(iconFile, new File(drawable, "icon.png"));
				} catch (IOException e) {
					logger.error("Error copying {} to {}/icon.png",
							new Object[] { iconFile, drawable }, e);
				}
			}
		} else {
			logger.warn("File {} for icon not found; making one up", icon);
			File drawable = new File(androidFolder, "res/drawable/");
			drawable.mkdirs();
			try {
				int w = 36;
				int h = w;
				BufferedImage bi = new BufferedImage(w, h,
						BufferedImage.TYPE_INT_ARGB);
				Graphics g = bi.getGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, w, h);
				g.setColor(Color.red);
				g.drawLine(0, 0, w, h);
				g.drawLine(0, h, w, 0);
				ImageIO.write(bi, "png", new File(drawable, "icon.png"));
			} catch (IOException e) {
				logger.error("Error making up icon into {}/icon.png", drawable,
						e);
			}
		}
	}

	private void addTitle(File androidFolder, Properties properties) {
		String title = properties.getProperty(AndroidExporter.TITLE,
				"eAdventure game");
		File values = new File(androidFolder, "res/values/");
		values.mkdirs();
		File strings = new File(values, "strings.xml");
		Map<String, String> substitutions = new HashMap<String, String>();
		substitutions.put("{title}", title);
		OutputStream os = null;
		try {
			os = new FileOutputStream(strings);
			FileUtils.substituteText(ClassLoader
					.getSystemResourceAsStream("strings.xml"), os,
					substitutions);
		} catch (FileNotFoundException e) {
			logger.error("Error generating strings.xml", e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					logger.error("Error generation strings.xml", e);
				}
			}
		}

	}

	private InputStream generateManifest(Properties properties) {
		Map<String, String> substitutions = new HashMap<String, String>();
		substitutions.put("{package}", properties.getProperty(PACKAGE_NAME,
				"es.eucm.ead.android.game"));
		InputStream manifest = ClassLoader
				.getSystemResourceAsStream("AndroidManifest.xml");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		FileUtils.substituteText(manifest, os, substitutions);
		String result = os.toString();
		InputStream is = new ByteArrayInputStream(result.getBytes());
		return is;
	}
}
