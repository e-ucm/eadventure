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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.maven.Maven;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionResult;
import org.codehaus.plexus.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApkExporter implements Exporter {

	private static final Logger logger = LoggerFactory.getLogger("ApkExporter");

	private static final String STRING_FILE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "<resources>"
			+ "<string name=\"app_name\">{app_name}</string>"
			+ "</resources>";

	private Maven maven;

	private boolean runInDevice;

	private File icon = null;

	private String appName = null;

	private String packageName = null;

	public ApkExporter(Maven maven) {
		this.maven = maven;
		this.runInDevice = false;
	}

	public void setName(String appName) {
		this.appName = appName;
		this.packageName = cleanAppName(appName);
	}

	public void setIcon(File iconFile) {
		this.icon = iconFile;
	}

	/**
	 * Sets if the generated apk must be deployed and run into a plugged device.
	 * By default is set to {@code false}
	 * 
	 * @param runInDevice
	 */
	public void setRunInDevice(boolean runInDevice) {
		this.runInDevice = runInDevice;
	}

	@Override
	public void export(String gameBaseDir, String outputfolder) {

		if (packageName == null) {
			logger.error("app name is not set or is invalid. Apk exportation failed.");
			return;
		}

		// Create a temp folder to generate de apk
		File gameFolder = new File(gameBaseDir);
		String tempFolder = System.getProperty("java.io.tmpdir");
		File apkTemp = new File(tempFolder + File.separator
				+ "eAdventureApkTemp" + Math.round(Math.random() * 1000));
		apkTemp.mkdirs();

		File manifestFile = createManifest(apkTemp);
		File apkAssets = createAssetsFolder(apkTemp, gameFolder);
		File apkResources = createResourcesFolder(apkTemp);
		
		// Copy native libs folder
		try {
			FileUtils.copyDirectoryStructure(new File("../../resources/nativelibs"), apkTemp);
		} catch (IOException e) {
			
		}

		// Copy and load pom file
		File pomFile = createPomFile(apkTemp);
		MavenExecutionRequest request = new DefaultMavenExecutionRequest();

		// Goals
		File f = new File(apkTemp, "/target/classes");
		f.mkdirs();
		ArrayList<String> goals = new ArrayList<String>();
		goals.add("clean");
		goals.add("install");
		if (runInDevice) {
			goals.add("android:deploy");
			goals.add("android:run");
		}
		request.setGoals(goals);

		// Properties
		Properties userProperties = new Properties();
		userProperties.setProperty("game.basedir", gameBaseDir);
		userProperties.setProperty("game.outputfolder", outputfolder);
		userProperties.setProperty("game.name", appName);
		userProperties.setProperty("ead.packagename", packageName);
		userProperties.setProperty("eadmanifestdir",
				manifestFile.getAbsolutePath());
		userProperties.setProperty("ead.tempfile", apkTemp.getAbsolutePath());
		userProperties.setProperty("ead.assets", apkAssets.getAbsolutePath());
		userProperties.setProperty("ead.resources",
				apkResources.getAbsolutePath());
		request.setUserProperties(userProperties);

		// Set files
		request.setBaseDirectory(apkTemp);
		request.setPom(pomFile);

		// Execute maven
		request.setLoggingLevel(org.codehaus.plexus.logging.Logger.LEVEL_ERROR);

		MavenExecutionResult result = maven.execute(request);
		for (Throwable e : result.getExceptions()) {
			logger.warn("{}", e);
		}

		// Copy apk to destination
		File apk = new File(apkTemp, "target/" + packageName + ".apk");
		File apkDst = new File(outputfolder, packageName + ".apk");

		try {
			FileUtils.copyFile(apk, apkDst);
		} catch (IOException e1) {

		}

		// Delete temp folder
		try {
			FileUtils.deleteDirectory(apkTemp);
		} catch (IOException e) {
			logger.warn("Apk assets temp folder was not deleted {}", e);
		}

	}

	private File createPomFile(File apkTemp) {
		File pomFile = new File(apkTemp, "pom.xml");
		InputStream is = null;
		OutputStream os = null;

		try {
			is = ClassLoader.getSystemResourceAsStream("pom/androidpom.xml");
			os = new FileOutputStream(pomFile);
			WarExporter.copy(is, os);
		} catch (IOException e) {
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e1) {

				}
			}

			if (os != null) {
				try {
					os.close();
				} catch (IOException e1) {

				}
			}
		}

		return pomFile;
	}

	private File createResourcesFolder(File apkTemp) {
		File apkResources = new File(apkTemp, "res/");
		apkResources.mkdir();
		File values = new File(apkResources, "values/");
		values.mkdirs();
		File stringsFile = new File(values, "strings.xml");

		BufferedWriter writer = null;
		InputStream iconIs = null;
		OutputStream iconOut = null;
		try {
			stringsFile.createNewFile();
			writer = new BufferedWriter(new FileWriter(stringsFile));
			writer.write(STRING_FILE.replace("{app_name}", appName));

			if (icon == null) {
				iconIs = ClassLoader
						.getSystemResourceAsStream("drawable/logo.png");
			} else {
				iconIs = new FileInputStream(icon);
			}

			File drawable = new File(apkResources, "drawable/");
			drawable.mkdir();
			File resIcon = new File(drawable, "ic_launcher.png");
			resIcon.createNewFile();
			iconOut = new FileOutputStream(resIcon);

			WarExporter.copy(iconIs, iconOut);
		} catch (IOException e) {

		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {

				}
			}

			if (iconIs != null) {
				try {
					iconIs.close();
				} catch (IOException e) {

				}
			}

			if (iconOut != null) {
				try {
					iconOut.close();
				} catch (IOException e) {

				}
			}
		}

		return apkResources;
	}

	private File createAssetsFolder(File apkTemp, File gameFolder) {
		File apkAssets = new File(apkTemp, "../../resources/assets/");
		apkAssets.mkdir();

		// FIXME Assets must be somewhere accessible for all projects
		File engineAssets = new File("engineassets");

		try {
			FileUtils.copyDirectoryStructure(gameFolder, apkAssets);
			FileUtils.copyDirectoryStructure(engineAssets, apkAssets);
		} catch (IOException e) {
			logger.warn("Unexpected error exporting apk: {}", e);
			return null;
		}
		return apkAssets;
	}

	private File createManifest(File apkTemp) {
		File manifestFile = new File(apkTemp, "AndroidManifest.xml");

		BufferedWriter writer = null;
		BufferedReader reader = null;
		try {

			reader = new BufferedReader(
					new InputStreamReader(
							ClassLoader
									.getSystemResourceAsStream("manifest/AndroidManifest.xml")));
			manifestFile.createNewFile();
			writer = new BufferedWriter(new FileWriter(manifestFile));
			String line = null;

			while ((line = reader.readLine()) != null) {
				if (line.contains("{packagename}")) {
					line = line.replace("{packagename}", packageName);
				}
				if (line.contains("{app_name}")) {
					line = line.replace("{app_name}", appName);
				}
				writer.write(line);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {

				}
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {

				}
			}
		}
		return manifestFile;
	}

	public String cleanAppName(String appName) {
		appName = appName.toLowerCase();
		String appName2 = "";
		for (char c : appName.toCharArray()) {
			if (c >= 'a' && c <= 'z') {
				appName2 += c;
			}
		}
		if (appName2.length() == 0) {
			logger.warn("Invalid app name. The name should include at least one non-accented letter");
			return null;
		}
		return appName2;
	}
}
