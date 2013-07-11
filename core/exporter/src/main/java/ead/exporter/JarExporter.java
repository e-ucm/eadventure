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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Maven wrapper able to export projects to jar/exe
 * 
 */
public class JarExporter implements Exporter {

	private static final Logger logger = LoggerFactory.getLogger("JarExporter");

	private static final byte[] BUFFER = new byte[4096 * 1024];

	private String jarPath;

	private String name;

	public JarExporter() {
		this.name = "game";
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public void setJarPath(String jarPath) {
		this.jarPath = jarPath;
	}

	@Override
	public void setIcon(File icon) {

	}

	public void export(String gameFolder, String output) {
		// Create destiny file
		File gameJar = new File(output);
		ZipOutputStream os = null;
		try {
			os = new ZipOutputStream(new FileOutputStream(gameJar));
			// Copy engine jar to destination jar
			copyJar(os);

			// Copy game to jar
			File parent = new File(gameFolder);
			addFolder(parent, parent, os);
			os.putNextEntry(new ZipEntry("assets/assets.txt"));
			os.closeEntry();
		} catch (Exception e) {
			logger.error("Error exporting game", e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e1) {

				}
			}
		}
	}

	private void copyJar(ZipOutputStream os) {
		ZipFile is = null;
		try {
			// Engine jar file
			File f = new File(jarPath);
			is = new ZipFile(f);

			Enumeration<? extends ZipEntry> entries = is.entries();
			while (entries.hasMoreElements()) {
				ZipEntry e = entries.nextElement();
				os.putNextEntry(e);
				if (!e.isDirectory()) {
					copy(is.getInputStream(e), os);
				}
				os.closeEntry();
			}
		} catch (Exception e) {
			logger.error("Error exporting to jar", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {

				}
			}
		}

	}

	public void addFolder(File parent, File folder, ZipOutputStream os) {
		for (File f : folder.listFiles()) {
			String fileEntry = f.getAbsolutePath().substring(
					parent.getAbsolutePath().length() + 1).replace('\\', '/');
			if (f.isDirectory()) {
				addFolder(parent, f, os);
			} else {
				FileInputStream is = null;
				try {
					os.putNextEntry(new ZipEntry(fileEntry));
					is = new FileInputStream(f);
					copy(is, os);
					os.closeEntry();
				} catch (Exception e) {
					logger.error("Error exporting to jar", e);
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {

						}
					}
				}
			}
		}
	}

	public static void copy(InputStream input, OutputStream output)
			throws IOException {
		int bytesRead;
		while ((bytesRead = input.read(BUFFER)) != -1) {
			output.write(BUFFER, 0, bytesRead);
		}
	}

}
