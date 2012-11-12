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


package ead.editor;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ead.common.util.EAdURI;
import ead.engine.core.gdx.assets.GdxAssetHandler;
import ead.tools.GenericInjector;

/**
 * An AssetHandler for the editor.
 * @author mfreire
 */

@Singleton
public class GdxEditorAssetHandler extends GdxAssetHandler {
		
	@Inject
	public GdxEditorAssetHandler(GenericInjector injector) {
		super(injector);
	}

	public void setResourcePath(File path) {
		EAdURI u = new EAdURI(path.getAbsolutePath());
		setResourcesLocation(u);
	}
	
	@Override
	public void initialize() {
		logger.info("Editor asset handler initialized");
		super.initialize();
	}

	@Override
	public void terminate() {
		super.terminate();
		logger.info("Editor asset handler terminated");
	}	
	
	private StringBuilder tries;
	
	@Override
	public FileHandle getFileHandle(String path) {
		tries = new StringBuilder();
		FileHandle rc = super.getFileHandle(path);
		logger.info("Loading {}; tried {}",
			new Object[] { path, tries });
		return rc;
	}
	
	@Override
	public FileHandle getProjectFileHandle(String uri) {
		String s = resourcesUri.getPath() + "/" 
				+ uri.replaceFirst("/", "_").replaceFirst("_", "/");
		tries.append("[absolute] " + s + "\n");
		return Gdx.files.absolute(s);
	}
	
	@Override
	public FileHandle getProjectInternal( String uri ){
		String s = PROJECT_INTERNAL_PATH + uri;
		tries.append("[p-internal] " + s + "\n");
		return Gdx.files.internal(s);
	}

	@Override
	public FileHandle getEngineFileHandle(String uri) {
		String s = ENGINE_RESOURCES_PATH + uri;
		tries.append("[e-internal] " + s + "\n");
		return Gdx.files.internal(s);
	}	
}
