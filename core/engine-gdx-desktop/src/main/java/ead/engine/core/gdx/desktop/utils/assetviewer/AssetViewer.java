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

package ead.engine.core.gdx.desktop.utils.assetviewer;

import java.awt.Canvas;
import java.util.List;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.google.inject.Inject;

import ead.common.resources.assets.drawable.EAdDrawable;
import ead.common.util.EAdURI;
import ead.engine.core.gdx.utils.assetviewer.AssetApplicationListener;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.RuntimeCompoundDrawable;

/**
 * Contains a canvas in which assets can be represented as they will be
 * represented in the game engine
 *
 */
public class AssetViewer {

	private static LwjglAWTCanvas sharedContext;

	private LwjglAWTCanvas canvas;

	private AssetApplicationListener app;

	private AssetHandler assetHandler;

	@Inject
	public AssetViewer(AssetHandler assetHandler, AssetApplicationListener app) {
		this.assetHandler = assetHandler;
		this.app = app;
		if (sharedContext == null) {
			this.canvas = new LwjglAWTCanvas(app, true);
			sharedContext = canvas;
		} else {
			this.canvas = new LwjglAWTCanvas(app, true, sharedContext);
		}
	}

	public void setDrawable(final EAdDrawable drawable) {
		app.setGraphics(canvas.getGraphics());
		canvas.postRunnable(new Runnable() {
			@SuppressWarnings("rawtypes")
			@Override
			public void run() {
				app.setDrawable((RuntimeCompoundDrawable) assetHandler
						.getRuntimeAsset(drawable));
			}
		});
	}

	public void setList(final List<String> states) {
		canvas.postRunnable(new Runnable() {
			@Override
			public void run() {
				app.setStates(states);
			}
		});
	}

	/**
	 * Replaces the current canvas with a copy of an existing one.
	 *
	 * @param canvas
	 */
	public void setCanvas(LwjglAWTCanvas canvas) {
		this.canvas.stop();
		this.canvas = new LwjglAWTCanvas(app, true, canvas);
	}

	/**
	 * Returns a canvas displaying assets
	 *
	 * @return
	 */
	public Canvas getCanvas() {
		return canvas.getCanvas();

	}

	public LwjglAWTCanvas getLwjglAWTCanvas() {
		return canvas;
	}

	/**
	 * This method must be called when the asset viewer disappears or is
	 * destroyed
	 */
	public void stop() {
		canvas.stop();
	}
}
