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

import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.params.guievents.EAdMouseEvent.MouseActionType;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.BasicSceneElementGO;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeCaption;

public class ShowTextEffectGO extends AbstractEffectGO<EAdShowText> {

	private static final Logger logger = Logger
			.getLogger("ShowTextEffectGO");

	private BasicSceneElementGO textGO;

	private RuntimeCaption caption;
	
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
		if (element.getLoops() < 0 || (textGO != null && caption.getTimesRead() < element.getLoops() ))
			gui.addElement(textGO, offsetX, offsetY);
	}

	@Override
	public void initilize() {
		super.initilize();
		textGO = (BasicSceneElementGO) gameObjectFactory.get(element.getText());
		textGO.setElement(element.getText());
		textGO.getAsset().loadAsset();
		if (textGO.getAsset() instanceof RuntimeCaption) {
			caption = (RuntimeCaption) textGO.getAsset();
			caption.reset();
			caption.setLoops(getEffect().getLoops());
		} else {
			logger.log(Level.WARNING, "TextGO " + element.getId() + " has a non caption asset");
		}
	}

	@Override
	public boolean isVisualEffect() {
		return true;
	}

	@Override
	public boolean isFinished() {
		if ( element.getLoops() < 0 ){
			return false;
		}
		if (textGO == null)
			return false;
		return caption.getTimesRead() > 0;
	}
	
	public void update( GameState state ){
		super.update(state);
		textGO.update(state);
	}

}
