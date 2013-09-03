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

import com.badlogic.gdx.backends.gwt.preloader.DefaultAssetFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class WarExporter {

	static private Logger logger = LoggerFactory.getLogger(WarExporter.class);

	private static final String DEFAULT_WAR_PATH = "/home/eva/repositories/eadventure/core/engine-html/target/eadengine.war";
	private static final byte[] BUFFER = new byte[4096 * 1024];
	private ArrayList<String> assets = new ArrayList<String>();

	private String name = "game";

	private String warPath = DEFAULT_WAR_PATH;

	private DefaultAssetFilter assetFilter;

	public WarExporter() {
		assetFilter = new DefaultAssetFilter() {
			@Override
			public AssetType getType(String file) {
				if (isText(file)) {
					return AssetType.Text;
				}
				return super.getType(file);
			}

			private boolean isText(String file) {
				return file.endsWith(".scene") || file.endsWith(".chapter");
			}
		};
	}

	public void setWarPath(String path) {
		this.warPath = path;
	}

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
			addFolder(parent, parent, os);
			os.putNextEntry(new ZipEntry("assets/assets.txt"));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					os));
			for (String a : assets) {
				writer.write(a);
				writer.newLine();
			}
			writer.flush();
			os.closeEntry();
		} catch (Exception e) {
			logger.error("Error exporting war", e);
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

	public void addFolder(File parent, File folder, ZipOutputStream os) {
		for (File f : folder.listFiles()) {

			String fileEntry = f.getAbsolutePath().substring(
					parent.getAbsolutePath().length() + 1).replace('\\', '/');

			// Create line for assets.txt
			String path = fileEntry.replace('\\', '/');
			if (path.startsWith("/"))
				path = path.substring(1);
			String assetLine = (f.isDirectory() ? "d" : assetFilter
					.getType(path).code)
					+ ":";
			assetLine += path + ":";
			assetLine += (f.isDirectory() ? 0 : f.length()) + ":";
			String mimetype = URLConnection.guessContentTypeFromName(f
					.getName());
			assetLine += (mimetype == null ? "application/unknown" : mimetype);
			assets.add(assetLine);

			// Continue copying
			if (f.isDirectory()) {
				addFolder(parent, f, os);
			} else {
				FileInputStream is = null;
				try {
					os.putNextEntry(new ZipEntry("assets/" + fileEntry));
					is = new FileInputStream(f);
					copy(is, os);
					os.closeEntry();

				} catch (Exception e) {
					logger.error("Error copying asset to zip", e);
				} finally {

					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							logger.error("Error copying asset to zip", e);
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

	private void readAssets(BufferedReader reader) {
		assets.clear();
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				assets.add(line);
			}
		} catch (IOException e) {

		}
	}
}
