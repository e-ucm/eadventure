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

import java.util.List;

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

	private static final String ENGINE_RESOURCES_PATH = "ead/engine/resources/";

	private GenericInjector injector;

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
	public List<String> getTextFile(String path) {
		return null;
	}

	@Override
	public RuntimeAsset<?> getInstance(Class<? extends RuntimeAsset<?>> clazz) {
		return injector.getInstance(clazz);
	}

	@Override
	public String getAbsolutePath(String uri) {
		return this.resourcesUri.getPath() + "/" + uri.substring(1);
	}

	public FileHandle getFileHandle(String path) {
		String uri = path.substring(1);
		if (resourcesUri != null) {
			FileHandle absolute = getProjectFileHandle(uri);
			if (absolute.exists()) {
				return absolute;
			}
		}

		return getEngineFileHandle(uri);
	}

	public FileHandle getProjectFileHandle(String uri) {
		return Gdx.files.absolute(this.resourcesUri.getPath() + "/" + uri);
	}

	public FileHandle getEngineFileHandle(String uri) {
		return Gdx.files.internal(ENGINE_RESOURCES_PATH + uri);
	}

}
