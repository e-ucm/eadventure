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

package es.eucm.eadventure.engine.core.platform.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

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
 */
@Singleton
public class DesktopAssetHandler extends AbstractAssetHandler {

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
	private static final Logger logger = Logger
			.getLogger("DesktopAssetHandler");

	@Inject
	public DesktopAssetHandler(
			Injector injector,
			Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> classMap) {
		super(injector, classMap);
		fileMap = new HashMap<String, File>();
		logger.info("New instance");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.AssetHandler#initilize()
	 */
	@Override
	public void initilize() {
		logger.info("Initilizing asset handler");
		if (resourceLocation == null) {
			logger.log(Level.SEVERE, "No game location");
			setLoaded(true);
		}
		else if (resourceLocation.isFile())
			extractZipFile();
		else if (resourceLocation.isDirectory())
			mapDirectory();
	}

	private void mapDirectory() {
		Runnable runnable = new Runnable() {
			public void run() {
				addFilesInDir("@", resourceLocation);
				
				File directory = resourceLocation;
				directory.listFiles();
				
				setLoaded(true);
			}
			
			private void addFilesInDir(String current, File directory) {
				for (File f : directory.listFiles()) {
					if (f.isFile())
						fileMap.put(current + f.getName(), f);
					else if (f.isDirectory())
						addFilesInDir(current + f.getName() + "/", f);
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
							logger.info("Temp file: @" + entry.getName() + " "
									+ tempFile.getAbsolutePath());
						}
					}

					setLoaded(true);

				} catch (ZipException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
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
	 *            Path of the file
	 * @return The file as an input stream
	 */
	public InputStream getResourceAsStream(String path) {
		// TODO localization!
		File file = fileMap.get(path);
		if (file == null) {
			// TODO improve?
			String location = path.replaceAll("@", "");
			return ClassLoader.getSystemResourceAsStream(location);
		} else {
			try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				return null;
			}
		}
	}

	/**
	 * Set the location of resources in the system
	 * 
	 * @param file
	 *            The file pointing to the resources
	 */
	public void setResourceLocation(File file) {
		this.resourceLocation = file;
	}

	/**
	 * Copy the contents of an {@link InputStream} into a temporary {@link File}
	 * 
	 * @param in
	 *            The {@link InputStream} with the data
	 * @param tempFile
	 *            The {@link File} where to dump the data
	 * @throws IOException
	 *             Exception while writing to a file or reading the
	 *             {@link InputStream}
	 */
	private final void copyInputStream(InputStream in, File tempFile)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		FileOutputStream fos = new FileOutputStream(tempFile);
		OutputStream bos = new BufferedOutputStream(fos);

		while ((len = in.read(buffer)) >= 0)
			bos.write(buffer, 0, len);

		in.close();
		bos.close();
		fos.close();
	}

}
