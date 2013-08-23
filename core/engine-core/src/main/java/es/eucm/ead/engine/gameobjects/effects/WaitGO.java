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

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.google.inject.Inject;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.engine.game.interfaces.Game;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.model.elements.effects.timedevents.WaitEf;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.scenes.GhostElement;

public class WaitGO extends AbstractEffectGO<WaitEf> implements EventListener {

	private GhostElement e;
	private int time;

	private GUI gui;

	private SceneElementGO eGO;

	@Inject
	public WaitGO(Game game) {
		super(game);
		this.gui = game.getGUI();
		e = new GhostElement();
		e.setCatchAll(true);
	}

	@Override
	public void initialize() {
		super.initialize();
		if (effect.isWaitUntilClick() || effect.isBlockInput()) {
			time = effect.isWaitUntilClick() ? 1 : effect.getTime();
			SceneElementGO go = gui.getHUD(GUI.EFFECTS_HUD_ID);
			eGO = go.addSceneElement(e);
			eGO.setInputProcessor(this, false);
		} else {
			time = effect.getTime();
		}
	}

	public void act(float delta) {
		if (!effect.isWaitUntilClick()) {
			time -= game.getGameState().getValue(
					SystemFields.ELAPSED_TIME_PER_UPDATE);
		}
	}

	@Override
	public boolean isFinished() {
		return time <= 0;
	}

	public void finish() {
		if (eGO != null)
			eGO.remove();
		super.finish();
	}

	public boolean isQueueable() {
		return true;
	}

	@Override
	public boolean handle(Event event) {
		if (event instanceof InputEvent) {
			InputEvent i = (InputEvent) event;
			event.cancel();
			if (i.getType() == InputEvent.Type.touchDown
					&& effect.isWaitUntilClick()) {
				time = 0;
				return true;
			}
		}
		return false;
	}

}
