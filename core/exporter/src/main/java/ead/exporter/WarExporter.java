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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class WarExporter implements Exporter {

	private static final String DEFAULT_WAR_PATH = "resources/engine.war";
	private static final byte[] BUFFER = new byte[4096 * 1024];
	private ArrayList<String> assets = new ArrayList<String>();

	private String name = "game";

	private String warPath = DEFAULT_WAR_PATH;

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public void setWarPath(String path) {
		this.warPath = path;
	}

	@Override
	public void setIcon(File icon) {
		// WAR hasn't got icon :(
	}

	public static void copy(InputStream input, OutputStream output)
			throws IOException {
		int bytesRead;
		while ((bytesRead = input.read(BUFFER)) != -1) {
			output.write(BUFFER, 0, bytesRead);
		}
	}

	private void readAssets(BufferedReader reader) {
		assets.clear();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				assets.add(line);
			}
		} catch (IOException e) {

		}
	}

	@Override
	public void export(String gameBaseDir, String outputfolder) {
		File parent = new File(gameBaseDir);
		// Copy the war into destination
		File output = new File(outputfolder);
		if (!output.exists()) {
			output.mkdirs();
		}
		File gameWar = new File(output, name + ".war");

		ZipOutputStream os = null;
		try {
			os = new ZipOutputStream(new FileOutputStream(gameWar));
			copyWar(os);
			addFolder(parent, parent, gameWar.getAbsolutePath(), os);
			os.putNextEntry(new ZipEntry("assets/assets.txt"));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					os));
			for (String a : assets) {
				writer.write(a);
				writer.newLine();
			}
			writer.flush();
			os.closeEntry();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e1) {

				}
			}
		}

	}

	private void copyWar(ZipOutputStream gameWar) {

		try {
			File f = new File(warPath);
			ZipFile is = new ZipFile(f);
			Enumeration<? extends ZipEntry> entries = is.entries();
			while (entries.hasMoreElements()) {
				ZipEntry e = entries.nextElement();
				if (!e.getName().equals("assets/assets.txt")) {
					gameWar.putNextEntry(e);
					if (!e.isDirectory()) {
						copy(is.getInputStream(e), gameWar);
					}
					gameWar.closeEntry();
				}
			}

			readAssets(new BufferedReader(new InputStreamReader(is
					.getInputStream(is.getEntry("assets/assets.txt")))));
			is.close();

		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} finally {

		}

	}

	public boolean isImage(String fileName) {
		return fileName.endsWith(".png") || fileName.endsWith(".jpg");
	}

	public void addFolder(File parent, File folder, String outputWarPath,
			ZipOutputStream os) {
		for (File f : folder.listFiles()) {
			String fileEntry = f.getAbsolutePath().substring(
					parent.getAbsolutePath().length() + 1).replace('\\', '/');
			if (f.isDirectory()) {
				assets.add("d:" + fileEntry);
				addFolder(parent, f, outputWarPath, os);
			} else {
				FileInputStream is = null;
				try {
					os.putNextEntry(new ZipEntry("assets/" + fileEntry));
					String prefix = isImage(fileEntry) ? "i:" : "t:";
					assets.add(prefix + fileEntry);
					is = new FileInputStream(f);
					copy(is, os);
					os.closeEntry();

				} catch (FileNotFoundException e) {
					e.printStackTrace();

				} catch (IOException e) {
					e.printStackTrace();

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

}
