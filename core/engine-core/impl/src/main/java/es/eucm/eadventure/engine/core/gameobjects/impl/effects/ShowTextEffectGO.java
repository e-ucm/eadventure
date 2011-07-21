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

package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.LoopType;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementTimedEvent.SceneElementTimedEventType;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementTimedEventImpl;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.params.EAdPosition.Corner;
import es.eucm.eadventure.common.model.params.guievents.EAdMouseEvent.MouseActionType;
import es.eucm.eadventure.common.model.variables.impl.extra.EAdSceneElementVars;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.impl.BezierShape;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.BasicSceneElementGO;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeCaption;

public class ShowTextEffectGO extends AbstractEffectGO<EAdShowText> {

	private static final Logger logger = Logger.getLogger("ShowTextEffectGO");

	private BasicSceneElementGO textGO;

	private BasicSceneElementGO indicatorGO;

	private RuntimeCaption caption;

	@Inject
	public ShowTextEffectGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);
	}

	@Override
	public boolean processAction(GUIAction action) {
		if (action instanceof MouseAction) {
			MouseAction mAction = (MouseAction) action;
			if (mAction.getType() == MouseActionType.RIGHT_CLICK
					|| mAction.getType() == MouseActionType.LEFT_CLICK) {
				logger.info("Click - forward");
				caption.goForward(1);
				mAction.consume();
			}
		}
		return false;
	}

	@Override
	public void doLayout(int offsetX, int offsetY) {
		if (element.getLoops() < 0
				|| (textGO != null && caption.getTimesRead() < element
						.getLoops())) {
			gui.addElement(textGO, offsetX, offsetY);
			if (caption.getCurrentPart() != caption.getTotalParts() - 1)
				gui.addElement(indicatorGO, offsetX, offsetY);
		}
	}

	@Override
	public void initilize() {
		super.initilize();
		textGO = (BasicSceneElementGO) gameObjectFactory.get(element.getText());
		textGO.setElement(element.getText());
		textGO.getRenderAsset().loadAsset();
		if (textGO.getRenderAsset() instanceof RuntimeCaption) {
			caption = (RuntimeCaption) textGO.getRenderAsset();
			caption.reset();
			caption.setLoops(element.getLoops());
			int textRight = textGO.getPosition().getJavaX(caption.getWidth())
					+ caption.getWidth();
			if (textRight > gui.getWidth()) {
				int newX = textGO.getPosition().getJavaX(caption.getWidth())
						- (textRight - (int) (platformConfiguration.getWidth() / platformConfiguration
								.getScale()));
				this.valueMap
						.setValue(textGO.getElement().getVars().getVar(EAdSceneElementVars.VAR_X), newX);
				textGO.getPosition().setX(newX);

			}
			initIndicator(caption, textGO.getPosition());
		} else {
			logger.log(Level.WARNING, "TextGO " + element.getId()
					+ " has a non caption asset");
		}
	}

	private void initIndicator(RuntimeCaption caption, EAdPosition p) {
		EAdBasicSceneElement indicator = new EAdBasicSceneElement("text indicator");
		
		EAdSceneElementTimedEventImpl event = new EAdSceneElementTimedEventImpl( "triangle_blink" );
		event.addEffect(SceneElementTimedEventType.START_TIME, new EAdVarInterpolationEffect("blink_interpolation", indicator.getVars().getVar(EAdSceneElementVars.VAR_ALPHA), 1.0f, 0.0f, 1000, LoopType.NO_LOOP ));
		event.setTime(1100);
		indicator.getEvents().add(event);
		
		int size = 15;

		BezierShape triangle = new BezierShape(0, 0);
		triangle.lineTo(size, 0);
		triangle.lineTo(size / 2, size );
		triangle.close();
		triangle.setColor(caption.getCaption().getTextColor());
		indicator.getResources().addAsset(indicator.getInitialBundle(),
				EAdBasicSceneElement.appearance, triangle);

		EAdPosition position = new EAdPosition(Corner.TOP_LEFT,
				p.getJavaX(caption.getWidth()) + caption.getWidth() - size * 2,
				p.getJavaY(caption.getHeight()) + caption.getHeight() - size
						* 2);
		indicator.setPosition(position);
		
		indicatorGO = (BasicSceneElementGO) gameObjectFactory.get(indicator);
	}

	@Override
	public boolean isVisualEffect() {
		return true;
	}

	@Override
	public boolean isFinished() {
		if (element.getLoops() < 0) {
			return false;
		}
		if (textGO == null)
			return false;
		return caption.getTimesRead() > 0;
	}

	public void update(GameState state) {
		super.update(state);
		textGO.update(state);
		if ( indicatorGO != null )
			indicatorGO.update(state);

	}

}
