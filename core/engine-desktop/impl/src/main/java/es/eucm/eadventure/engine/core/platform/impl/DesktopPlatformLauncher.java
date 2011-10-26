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

import java.io.File;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.params.EAdURI;
import es.eucm.eadventure.common.params.EAdURIImpl;
import es.eucm.eadventure.engine.core.GameController;
import es.eucm.eadventure.engine.core.impl.modules.BasicGameModule;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.PlatformLauncher;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopAssetHandlerModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopModule;

/**
 * <p>
 * Engine launcher for the desktop platform
 * </p>
 */
@Singleton
public class DesktopPlatformLauncher implements PlatformLauncher {

	private GameController gameController;
	
	/**
	 * Engine asset handler {@link AssetHandler}
	 */
	private AssetHandler assetHandler;
	
	@Inject
	public DesktopPlatformLauncher(GameController gameController,
			AssetHandler assetHandler) {
		this.gameController = gameController;
		this.assetHandler = assetHandler;
	}

	@Override
	public void launch(EAdURI uri) {
		if ( uri != null ){
			File resourceFile = new File( uri.getPath() );
			((DesktopAssetHandler) assetHandler).setResourceLocation(resourceFile);
		}
		gameController.start();
	}
	
	public static void main(String[] args) {
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "eAdventure");
		
		Injector injector = Guice.createInjector(new DesktopAssetHandlerModule(), new DesktopModule(), new BasicGameModule());

		PlatformLauncher launcher = injector.getInstance(PlatformLauncher.class);
		
		EAdURI file = null;

		if (args.length == 0) {
			//TODO use default file
			//File file = new File("/ProyectoJuegoFINAL.ead");
		} else {
			//TODO extract file from args
			file = new EAdURIImpl( args[0] );
		}
		
		launcher.launch( file );
	}

}
