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

package es.eucm.ead.legacyplugins.engine;

import com.google.inject.Inject;
import com.gwtent.reflection.client.Reflectable;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.assets.drawables.RuntimeCaption;
import es.eucm.ead.engine.assets.drawables.RuntimeNinePatchImage;
import es.eucm.ead.engine.factories.SceneElementFactory;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.engine.gameobjects.events.AbstractEventGO;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.legacyplugins.model.BubbleNameEv;
import es.eucm.ead.legacyplugins.model.LegacyVars;
import es.eucm.ead.model.assets.drawable.basics.Caption;
import es.eucm.ead.model.assets.drawable.basics.EAdCaption;
import es.eucm.ead.model.assets.drawable.compounds.ComposedDrawable;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.model.params.util.Position.Corner;

@Reflectable
public class BubbleNameGO extends AbstractEventGO<BubbleNameEv> {

	private String NAME = "name";

	private final float TIME = 100.0f;

	private float scale = 0.0f;

	private boolean growing;

	private GUI gui;

	private SceneElementGO current;

	private SceneElementFactory sceneElementFactory;

	private SceneElementGO bubble;

	private ElementField currentDescription;

	private AssetHandler assetHandler;

	private RuntimeCaption caption;

	private RuntimeNinePatchImage ninePatch;

	@Inject
	public BubbleNameGO(Game game, SceneElementFactory sceneElementFactory,
			AssetHandler assetHandler) {
		super(game);
		this.gui = game.getGUI();
		this.sceneElementFactory = sceneElementFactory;
		this.assetHandler = assetHandler;
	}

	@Override
	public void setElement(BubbleNameEv element) {
		super.setElement(element);
		currentDescription = new ElementField(element, NAME);
		SceneElement e = new SceneElement();
		e.setId("#engine.huds.namesbubble");
		e.setZ(500);
		SceneElementGO hud = sceneElementFactory.get(e);
		gui.addHud(hud);
		// Bubble creation
		ComposedDrawable drawable = new ComposedDrawable();
		drawable.addDrawable(element.getBubble());
		Caption c = new Caption("[0]");
		c.setPadding(element.getBubble().getLeft());
		c.getOperations().add(currentDescription);
		c.setFont(element.getFont());
		c.setTextPaint(element.getTextPaint());
		drawable.addDrawable(c);
		SceneElement b = new SceneElement(drawable);
		b.setPosition(Corner.BOTTOM_CENTER, 0, 0);
		b.setEnable(false);
		b.setVisible(false);
		b.setScale(0.0f);
		caption = (RuntimeCaption) assetHandler.getRuntimeAsset((EAdCaption) c);
		ninePatch = (RuntimeNinePatchImage) assetHandler
				.getRuntimeAsset(element.getBubble());
		bubble = sceneElementFactory.get(b);
		hud.addSceneElement(bubble);
	}

	@Override
	public void act(float delta) {
		SceneElementGO s = gui.getGameObjectUnderPointer();
		if (s != current) {
			current = s;
			if (current != null) {
				EAdString name = current.getElement().getProperty(
						LegacyVars.BUBBLE_NAME, null);
				EAdList operations = current.getElement().getProperty(
						LegacyVars.BUBBLE_OPERATIONS, null);
				if (operations != null && operations.size() > 0) {
					caption.getCaption().getOperations().clear();
					caption.getCaption().getOperations().addAll(operations);
				}
				game.getGameState().setValue(currentDescription, name);
				if (name != null) {
					bubble.act(delta);
					bubble.setVisible(true);
					float centerX = current.getCenterX();
					float top = current.getTop();
					if (centerX - bubble.getWidth() / 2 < 0) {
						centerX += bubble.getWidth() / 2 - centerX;
					}

					if (top < bubble.getHeight()) {
						top = bubble.getHeight();
					}
					bubble.setX(centerX);
					bubble.setY(top);
					scale = 0.0f;
					growing = true;
				} else {
					bubble.setVisible(false);
					scale = 0.0f;
				}
			} else {
				bubble.setVisible(false);
				scale = 0.0f;
			}
		}

		if (growing) {
			scale += 1.0f * (delta / TIME);
			if (scale >= 1.0f) {
				scale = 1.0f;
				growing = false;
			}
		}

		bubble.setScale(scale);
		ninePatch.setWidth(caption.getWidth());
		ninePatch.setHieght(caption.getHeight());
	}

}
