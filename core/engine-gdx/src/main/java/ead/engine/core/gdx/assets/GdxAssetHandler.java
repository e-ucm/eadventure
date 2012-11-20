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

package ead.engine.core.gdx.assets;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.engine.core.platform.AbstractAssetHandler;
import ead.engine.core.platform.FontHandler;
import ead.engine.core.platform.assets.RuntimeAsset;
import ead.tools.GenericInjector;
import ead.tools.SceneGraph;

@Singleton
public class GdxAssetHandler extends AbstractAssetHandler {

	public static final String ENGINE_RESOURCES_PATH = "ead/engine/resources/";

	public static final String PROJECT_INTERNAL_PATH = "";

	protected GenericInjector injector;

	@Inject
	public GdxAssetHandler(GenericInjector injector) {
		super(new GdxAssetHandlerMap(),
				injector.getInstance(FontHandler.class), injector
						.getInstance(SceneGraph.class));
		this.injector = injector;
	}

	@Override
	public void initialize() {

	}

	@Override
	public void terminate() {

	}

	@Override
	public void getTextFile(String path, LoadTextFileListener callback) {
		FileHandle fh = getFileHandle(path);

		if (fh != null) {
			StringBuilder text = new StringBuilder();
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(fh.reader());
				String line = null;
				while ((line = reader.readLine()) != null) {
					text.append(line + "\n");
				}
			} catch (FileNotFoundException e) {

			} catch (IOException e) {

			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {

				}
			}
			callback.read(text.toString());
		}
	}

	@Override
	public RuntimeAsset<?> getInstance(Class<? extends RuntimeAsset<?>> clazz) {
		return injector.getInstance(clazz);
	}

	/**
	 * retrieves a file handle for the path
	 * 
	 * @param path
	 * @return 
	 */
	public FileHandle getFileHandle(String path) {
		String uri = path.substring(1);
		if (resourcesUri != null) {
			FileHandle absolute = getProjectFileHandle(uri);
			if (absolute.exists()) {
				return absolute;
			}
		} else {
			FileHandle internal = getProjectInternal(uri);
			if (internal.exists()) {
				return internal;
			}
		}

		return getEngineFileHandle(uri);
	}

	public FileHandle getProjectFileHandle(String uri) {
		return Gdx.files.absolute(this.resourcesUri.getPath() + "/" + uri);
	}

	public FileHandle getProjectInternal(String uri) {
		return Gdx.files.internal(PROJECT_INTERNAL_PATH + uri);
	}

	public FileHandle getEngineFileHandle(String uri) {
		return Gdx.files.internal(ENGINE_RESOURCES_PATH + uri);
	}

}
