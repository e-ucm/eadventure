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

package ead.engine.desktop.utils.assetviewer;

import java.awt.Canvas;
import java.util.List;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.resources.assets.drawable.EAdDrawable;
import ead.engine.core.platform.FontHandler;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.RuntimeCompoundDrawable;
import ead.engine.core.utils.assetviewer.AssetApplicationListener;

/**
 * Contains a canvas in which assets can be represented as they will be
 * represented in the game engine
 * 
 */
public class AssetViewer {

	/**
	 * Injector is shared by all the instances
	 */
	private static Injector injector;

	private LwjglAWTCanvas lwjglCanvas;

	private AssetApplicationListener app;

	private AssetHandler assetHandler;

	public AssetViewer() {
		this(null);
	}

	public AssetViewer(LwjglAWTCanvas canvas) {
		if (injector == null) {
			injector = Guice.createInjector(new AssetViewerModule());
			injector.getInstance(AssetHandler.class).setCacheEnabled(false);
		}

		assetHandler = injector.getInstance(AssetHandler.class);

		app = new AssetApplicationListener(
				injector.getInstance(FontHandler.class));

		if (canvas == null) {
			lwjglCanvas = new LwjglAWTCanvas(app, true);
		} else {
			lwjglCanvas = new LwjglAWTCanvas(app, true, canvas);
		}
		app.setGraphics(lwjglCanvas.getGraphics());
	}

	public void setDrawable(final EAdDrawable drawable) {
		lwjglCanvas.postRunnable(new Runnable() {

			@SuppressWarnings("rawtypes")
			@Override
			public void run() {
				app.setDrawable((RuntimeCompoundDrawable) assetHandler
						.getRuntimeAsset(drawable));
			}

		});

	}

	public void setList(final List<String> states) {
		lwjglCanvas.postRunnable(new Runnable() {

			@Override
			public void run() {
				app.setStates(states);
			}

		});
	}

	/**
	 * Returns a canvas displaying assets
	 * 
	 * @return
	 */
	public Canvas getCanvas() {
		return lwjglCanvas.getCanvas();

	}

	public LwjglAWTCanvas getLwjglAWTCanvas() {
		return lwjglCanvas;
	}

	/**
	 * This method must be called when the asset viewer disappears or is
	 * destroyed
	 */
	public void stop() {
		lwjglCanvas.stop();
	}

}
