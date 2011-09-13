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

package es.eucm.eadventure.engine.core.platform.impl.gameobjectrenderers;

import java.util.logging.Logger;

import playn.core.Canvas;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.ActorReferenceGO;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.BasicHUDImpl;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;
import es.eucm.eadventure.engine.core.platform.GraphicRendererFactory;

@Singleton
public class BasicHudGORenderer implements
		GameObjectRenderer<Canvas, BasicHUDImpl> {

	private MouseState mouseState;

	private GraphicRendererFactory<Canvas> graphicRendererFactory;

	private AssetHandler assetHandler;
	
	private static final Logger logger = Logger
			.getLogger("BasicHudGORenderer");

	private CaptionImpl caption;

	@SuppressWarnings({ "unchecked" })
	@Inject
	public BasicHudGORenderer(GraphicRendererFactory<?> graphicRendererFactory,
			MouseState mouseState, GameObjectFactory gameObjectFactory,
			AssetHandler assetHandler) {
		this.mouseState = mouseState;
		this.assetHandler = assetHandler;
		this.graphicRendererFactory = (GraphicRendererFactory<Canvas>) graphicRendererFactory;
		logger.info("New intance");
	}

	@Override
	public void render(Canvas g, BasicHUDImpl object, float interpolation, int offsetX, int offsetY) {

		if (mouseState.getDraggingGameObject() != null && mouseState.isInside()) {
			GameObject<?> actor = mouseState.getDraggingGameObject();
			graphicRendererFactory.render(g, actor, EAdPositionImpl
					.volatileEAdPosition(
							mouseState.getVirtualMouseX() - mouseState.getMouseDifX(),
							mouseState.getVirtualMouseY() - mouseState.getMouseDifY(),
							actor.getPosition().getDispX(), actor.getPosition().getY()), 1.0f, 0, 0);
		}

		//TODO probably should check if it wants its name to be painted
		GameObject<?> underMouse = mouseState.getGameObjectUnderMouse();
		if (underMouse != null
				&& underMouse instanceof ActorReferenceGO
				&& ((ActorReferenceGO) underMouse).getName() != null) {
			EAdString name = ((ActorReferenceGO) underMouse).getName();
			if (caption == null || !caption.getText().equals(name))
				renewCaption(name);
			graphicRendererFactory.render(g, assetHandler.getRuntimeAsset(caption), EAdPositionImpl.volatileEAdPosition(
					mouseState.getVirtualMouseX(),
					mouseState.getVirtualMouseY()), 1.0f, 0, 0);
		}
	}
	
	private void renewCaption(EAdString text) {
		caption = new CaptionImpl();
		((CaptionImpl) caption).setTextColor(EAdBorderedColor.BLACK_ON_WHITE);
		((CaptionImpl) caption).setFont(EAdFontImpl.REGULAR);
		caption.setText(text);
	}

	@Override
	public void render(Canvas graphicContext, BasicHUDImpl object,
			EAdPosition position, float scale, int offsetX, int offsetY) {
	}

	@Override
	public boolean contains(BasicHUDImpl object, int virutalX, int virtualY) {
		return false;
	}

}
