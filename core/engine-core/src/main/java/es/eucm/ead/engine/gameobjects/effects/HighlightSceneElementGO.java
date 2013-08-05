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

package es.eucm.ead.engine.gameobjects.effects;

import com.google.inject.Inject;

import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.factories.SceneElementGOFactory;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.engine.game.interfaces.GameState;
import es.eucm.ead.model.elements.effects.timedevents.HighlightSceneElementEf;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.engine.factories.EventGOFactory;

public class HighlightSceneElementGO extends
		AbstractEffectGO<HighlightSceneElementEf> {

	private int time;

	private float oldScale;

	private boolean started;

	private Object highLightElement;

	@Inject
	public HighlightSceneElementGO(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(gameState);
	}

	@Override
	public void initialize() {
		super.initialize();
		highLightElement = gameState.maybeDecodeField(effect.getSceneElement());
		oldScale = gameState.getValue(highLightElement, SceneElement.VAR_SCALE);
		time = effect.getTime();
		started = false;
	}

	public void act(float delta) {
		if (time > 0) {
			if (!started) {
				gameState.setValue(highLightElement, SceneElement.VAR_SCALE,
						oldScale * 2);
				started = true;
			}
			time -= gameState.getValue(SystemFields.ELAPSED_TIME_PER_UPDATE);
			if (time <= 0) {
				gameState.setValue(highLightElement, SceneElement.VAR_SCALE,
						oldScale);
			}
		}
	}

	@Override
	public boolean isFinished() {
		return time <= 0;
	}

}
