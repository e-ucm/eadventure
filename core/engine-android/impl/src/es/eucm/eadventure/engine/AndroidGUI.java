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

package es.eucm.eadventure.engine;

import android.graphics.Canvas;
import android.graphics.Matrix;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.KeyboardState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.platform.impl.AbstractGUI;
import es.eucm.eadventure.engine.extra.BitmapCanvas;
import es.eucm.eadventure.engine.extra.EAdventureRenderingThread;

@Singleton
public class AndroidGUI extends AbstractGUI<Canvas> {

	@Inject
	public AndroidGUI(PlatformConfiguration platformConfiguration,
			GameObjectManager gameObjectManager, MouseState mouseState,
			KeyboardState keyboardState,
			ValueMap valueMap, GameState gameState,
			SceneElementGOFactory gameObjectFactory,
			AndroidCanvas canvas) {
		super(platformConfiguration, gameObjectManager, mouseState,
				keyboardState, gameState, gameObjectFactory, canvas);
	}

	@Override
	public RuntimeAsset<Image> commitToImage() {
		// FIXME does not commit to image
		// canvas.getBitmap();?
		return null;
	}

	@Override
	public void initilize() {
		// Nothing to initialize
	}

	@Override
	public void commit(float interpolation) {

		processInput();

		synchronized (EAdventureRenderingThread.mSurfaceHolder) {
			// canvas.drawColor(Color.WHITE);
			render(interpolation);
		}
	}

	@Override
	public void showSpecialResource(Object object, int x, int y,
			boolean fullscreen) {
		// TODO Process request, possible object could be an intent?
	}

	public void setCanvas(BitmapCanvas aCanvas) {

		Matrix matrix = aCanvas.getMatrix();

		if (platformConfiguration.isFullscreen())
			matrix.preScale(
					(float) ((AndroidPlatformConfiguration) platformConfiguration)
							.getScaleW(),
					(float) ((AndroidPlatformConfiguration) platformConfiguration)
							.getScaleH());
		else
			matrix.preScale(
					(float) ((AndroidPlatformConfiguration) platformConfiguration)
							.getScaleH(),
					(float) ((AndroidPlatformConfiguration) platformConfiguration)
							.getScaleH());

		setWidth( platformConfiguration.getWidth());
		setHeight( platformConfiguration.getHeight());
		aCanvas.setMatrix(matrix);
		aCanvas.save();

		eAdCanvas.setGraphicContext(aCanvas);

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
