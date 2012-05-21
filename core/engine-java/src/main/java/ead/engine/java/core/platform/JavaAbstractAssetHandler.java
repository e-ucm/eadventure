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

package ead.engine.java.core.platform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.inject.Injector;

import ead.common.resources.assets.AssetDescriptor;
import ead.engine.core.platform.AbstractAssetHandler;
import ead.engine.core.platform.FontHandler;
import ead.engine.core.platform.assets.RuntimeAsset;

public abstract class JavaAbstractAssetHandler extends AbstractAssetHandler {

	/**
	 * An instance of the guice injector, used to load the necessary runtime
	 * assets
	 */
	private Injector injector;

	/**
	 * Map of original file names to file references in the temporary directory
	 */
	protected Map<String, File> fileMap;

	public JavaAbstractAssetHandler(
			Injector injector,
			Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>> classMap,
			FontHandler fontHandler) {
		super(classMap, fontHandler);
		this.injector = injector;
	}

	@Override
	public RuntimeAsset<?> getInstance(Class<? extends RuntimeAsset<?>> clazz) {
		return injector.getInstance(clazz);
	}

	/**
	 * Helper method to create a directory within the system temporary directory
	 * 
	 * @param name
	 *            The name of the directory
	 * @return The reference to the directory
	 * @throws IOException
	 *             A exception if the directory couldn't be created
	 */
	protected File createTempDirectory(String name) throws IOException {
		final File temp;

		temp = File.createTempFile(name, null);

		if (!(temp.delete())) {
			throw new IOException("Could not delete temp file: "
					+ temp.getAbsolutePath());
		}

		if (!(temp.mkdir())) {
			throw new IOException("Could not create temp directory: "
					+ temp.getAbsolutePath());
		}

		return (temp);
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
			String location = path.replaceAll("@", "ead/engine/resources/");
			is = ClassLoader.getSystemResourceAsStream(location);
			if (is == null) {
				logger.error(
						"resource not found {} (from classpath-location '{}')",
						new Object[] { path, location });
			}
		}

		return is;
	}

	@Override
	public List<String> getTextFile(String path) {
		ArrayList<String> lines = new ArrayList<String>();
		String NL = System.getProperty("line.separator");
		Scanner scanner = null;
		try {
			scanner = new Scanner(getResourceAsStream(path));

			while (scanner.hasNextLine()) {
				lines.add(scanner.nextLine() + NL);
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		return lines;
	}
}
