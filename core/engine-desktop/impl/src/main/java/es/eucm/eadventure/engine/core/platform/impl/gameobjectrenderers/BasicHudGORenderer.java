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

import java.awt.Graphics2D;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.ActorReferenceGO;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.BasicHUDImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.SceneElementGOImpl;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;
import es.eucm.eadventure.engine.core.platform.GraphicRendererFactory;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

@Singleton
public class BasicHudGORenderer implements
		GameObjectRenderer<Graphics2D, BasicHUDImpl> {

	private MouseState mouseState;

	private GraphicRendererFactory<Graphics2D> graphicRendererFactory;

	private static final Logger logger = Logger.getLogger("BasicHudGORenderer");

	private CaptionImpl caption;

	private ValueMap valueMap;

	private EAdBasicSceneElement textElement;

	private GameObjectFactory gameObjectFactory;

	private GUI gui;
	
	private StringHandler stringHandler;

	@SuppressWarnings({ "unchecked" })
	@Inject
	public BasicHudGORenderer(GraphicRendererFactory<?> graphicRendererFactory,
			MouseState mouseState, GameObjectFactory gameObjectFactory,
			ValueMap valueMap, GUI gui, StringHandler stringHandler) {
		this.mouseState = mouseState;
		this.graphicRendererFactory = (GraphicRendererFactory<Graphics2D>) graphicRendererFactory;
		this.valueMap = valueMap;
		this.gameObjectFactory = gameObjectFactory;
		this.gui = gui;
		logger.info("New intance");
		caption = new CaptionImpl();
		caption.setTextColor(EAdBorderedColor.BLACK_ON_WHITE);
		caption.setFont(EAdFontImpl.REGULAR);

		this.stringHandler = stringHandler;
		textElement = new EAdBasicSceneElement("text", caption);
	}

	@Override
	public void render(Graphics2D g, BasicHUDImpl object, EAdTransformation t) {

		// TODO probably should check if it wants its name to be painted
		GameObject<?> underMouse = mouseState.getGameObjectUnderMouse();
		EAdString name = null;
		if (underMouse != null) {
			if (underMouse instanceof ActorReferenceGO
					&& ((ActorReferenceGO) underMouse).getName() != null) {
				name = ((ActorReferenceGO) underMouse).getName();
			} else if (underMouse.getElement() instanceof EAdElement) {
				name = valueMap.getValue((EAdElement) underMouse.getElement(),
						EAdBasicSceneElement.VAR_NAME);
			}
		}

		if (name != null && !caption.getText().equals(name))
			renewCaption(name);

		if (name != null) {
			EAdPosition p = EAdPositionImpl.volatileEAdPosition(
					mouseState.getMouseX(),
					mouseState.getMouseY());
			textElement.setPosition(p);
			SceneElementGOImpl<?> go = (SceneElementGOImpl<?>) gameObjectFactory
					.get(textElement);
			go.update();
			graphicRendererFactory.render(g, go,
					gui.addTransformation(t, go.getTransformation()));
		}
	}

	private void renewCaption(EAdString text) {
		stringHandler.setString(caption.getText(), stringHandler.getString(text));
	}

	@Override
	public boolean contains(BasicHUDImpl object, int virutalX, int virtualY) {
		return false;
	}

}
