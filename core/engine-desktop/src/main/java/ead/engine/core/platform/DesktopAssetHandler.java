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

package ead.engine.core.platform;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import ead.common.resources.assets.AssetDescriptor;
import ead.common.util.EAdURI;
import ead.engine.core.platform.assets.RuntimeAsset;

/**
 * <p>
 * Desktop implementation of the engine asset handler.
 * </p>
 * <p>
 * This asset handler extracts all the files from the adventure zip file and
 * extracts them into the system temporary directory. A map is used to identify
 * the temporary files with their original descriptors.
 * </p>
 * 
 * <p>
 * Desktop implementation of the engine asset handler.
 * </p>
 * <p>
 * This asset handler extracts all the files from the adventure zip file and
 * extracts them into the system temporary directory. A map is used to identify
 * the temporary files with their original descriptors.
 * </p>
 * 
 */
@Singleton
public class DesktopAssetHandler extends JavaAbstractAssetHandler {

	/**
	 * Map of original file names to file references in the temporary directory
	 */
	private Map<String, File> fileMap;
	/**
	 * The location of resource in the system
	 */
	private File resourceLocation;
	/**
	 * The logger of the class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger("DesktopAssetHandler");

	@Inject
	public DesktopAssetHandler(
			Injector injector,
			Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>> classMap,
			FontHandler fontHandler) {
		super(injector, classMap, fontHandler);
		fileMap = new HashMap<String, File>();
		logger.info("New instance");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.AssetHandler#initialize()
	 */
	@Override
	public void initialize() {
		logger.info("Initilizing asset handler");
		if (resourceLocation == null) {
			logger.error("No game location: ");
			setLoaded(true);
		} else if (resourceLocation.isFile()) {
			extractZipFile();
		} else if (resourceLocation.isDirectory()) {
			mapDirectory();
		}
	}

	private void mapDirectory() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				addFilesInDir("@", resourceLocation);

				File directory = resourceLocation;
				directory.listFiles();

				setLoaded(true);
			}

			private void addFilesInDir(String current, File directory) {
				for (File f : directory.listFiles()) {
					if (f.isFile()) {
						fileMap.put(current + f.getName(), f);
					} else if (f.isDirectory()) {
						addFilesInDir(current + f.getName() + "/", f);
					}
				}
			}
		};
		Thread t = new Thread(runnable);
		t.start();
	}

	/**
	 * Extract content of the adventure zip file into the temporary folder of
	 * the system
	 */
	private void extractZipFile() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					ZipFile zipFile = new ZipFile(resourceLocation);

					Enumeration<? extends ZipEntry> entries = zipFile.entries();

					while (entries.hasMoreElements()) {
						ZipEntry entry = entries.nextElement();

						if (!entry.isDirectory()) {
							String temp[] = entry.getName().split("\\.");
							File tempFile = File.createTempFile(entry.getName()
									.replace("/", "_"), "."
									+ temp[temp.length - 1]);
							copyInputStream(zipFile.getInputStream(entry),
									tempFile);
							fileMap.put("@" + entry.getName(), tempFile);
							logger.info("Temp file: @{} --> {}",
									entry.getName(), tempFile.getAbsolutePath());
						}
					}

					setLoaded(true);
				} catch (Exception e) {
					logger.error("error extracting zip '{}'", resourceLocation,
							e);
				}
			}
		};
		Thread t = new Thread(runnable);
		t.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.AssetHandler#terminate()
	 */
	@Override
	public void terminate() {
		// TODO Should consider removing files from temporary directory
	}

	/**
	 * Loads a file as an input stream
	 * 
	 * @param path
	 *            file location, with '@' substituted for location root
	 * @return The file as an input stream
	 */
	public InputStream getResourceAsStream(String path) {
		// TODO improve: localization!
		InputStream is = null;
		if (fileMap.containsKey(path)) {
			File file = fileMap.get(path);
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				logger.error("error loading resource {} (from file '{}')",
						new Object[] { path, file.getAbsolutePath() }, e);
			}
		} else {
			String location = path.replaceAll("@", "ead/resources/");
			is = ClassLoader.getSystemResourceAsStream(location);
			if (is == null) {
				logger.error(
						"resource not found {} (from classpath-location '{}')",
						new Object[] { path, location });
			}
		}

		return is;
	}

	public void setResourcesLocation(EAdURI uri) {
		if (uri != null)
			this.resourceLocation = new File(uri.getPath());
	}

	/**
	 * Copy the contents of an {@link InputStream} into a {@link File}
	 * 
	 * @param in
	 *            The {@link InputStream} with the data
	 * @param tempFile
	 *            The {@link File} where data is to be written
	 * @throws IOException
	 *             while writing to the file or reading the {@link InputStream}
	 */
	private void copyInputStream(InputStream in, File tempFile)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(tempFile));

			while ((len = in.read(buffer)) >= 0) {
				bos.write(buffer, 0, len);
			}
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception e) {
					logger.warn("in.close() exception writing to '{}'",
							tempFile.getAbsolutePath(), e);
				}
			if (bos != null)
				try {
					bos.close();
				} catch (Exception e) {
					logger.warn("out.close() exception writing to '{}'",
							tempFile.getAbsolutePath(), e);
				}
		}
	}

	@Override
	public String getAbsolutePath(String path) {
		// TODO localization!
		File file = fileMap.get(path);
		if (file == null) {
			// TODO improve?
			String location = path.replaceAll("@", "ead/resources/");
			return ClassLoader.getSystemResource(location).getFile();
		} else {
			return file.getAbsolutePath();
		}
	}
}
