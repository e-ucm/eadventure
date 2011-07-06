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

package es.eucm.eadventure.engine.gameobjectrenderers;

import android.graphics.Canvas;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;
import es.eucm.eadventure.engine.core.platform.GraphicRendererFactory;

@Singleton
public class AndroidActionRenderer implements GameObjectRenderer<Canvas, AndroidActionGO> {

	private static final Logger logger = Logger.getLogger("AndroidActionRenderer");

	private GraphicRendererFactory<Canvas> factory;
	
	private AssetHandler assetHandler;
	
	@SuppressWarnings({ "unchecked" })
	@Inject
	public AndroidActionRenderer(GraphicRendererFactory<?> factory,
			AssetHandler assetHandler) {
		this.factory = (GraphicRendererFactory<Canvas>) factory;
		this.assetHandler = assetHandler;
		logger.info("New instance");
	}
	
	@Override
	public void render(Canvas g, AndroidActionGO action, float interpolation) {
		EAdAsset asset =  action.getAsset(assetHandler);
		factory.render(g, action.getAsset(assetHandler), action.getX(), action.getY(), action.getScale());
		if (asset instanceof EAdDrawable) {
			action.setWidth(((EAdDrawable) asset).getWidth());
			action.setHeight(((EAdDrawable) asset).getHeight());
		}
		if (action.isMouseInside()) {
			String actionName = assetHandler.getString(action.getName());
			int y = action.getY() + action.getHeight() + 30;
			int x = action.getX() + action.getWidth() / 2;
			factory.render(g, new TextGOImpl(actionName, x, y, null, null), interpolation);
		}
	}

	@Override
	public void render(Canvas g, AndroidActionGO action, int x, int y, float scale) {
		factory.render(g, action.getAsset(assetHandler), x, y, scale);
	}

	@Override
	public boolean contains(AndroidActionGO action, int virutalX, int virtualY) {
		int x = virutalX - action.getX();
		int y = virtualY - action.getY();
		return x > 0 && y > 0 && factory.contains(x, y, action.getAsset(assetHandler));
	}

}
