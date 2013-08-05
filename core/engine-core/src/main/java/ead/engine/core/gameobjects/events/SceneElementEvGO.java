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

package ead.engine.core.gameobjects.events;

import com.google.inject.Inject;

import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.params.variables.VarDef;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.gameobjects.effects.ChangeSceneGO;

public class SceneElementEvGO extends AbstractEventGO<SceneElementEv> {

	private static final VarDef<Boolean> INIT = new VarDef<Boolean>("init",
			Boolean.class, false);

	private int checks;

	private boolean hasAlways;

	private boolean inTransition;

	@Inject
	public SceneElementEvGO(GameState gameState) {
		super(gameState);
	}

	public void setElement(SceneElementEv ev) {
		super.setElement(ev);
		checks = 2;
		hasAlways = element.getEffectsForEvent(SceneElementEvType.ALWAYS) != null;
		inTransition = true;
	}

	@Override
	public void act(float delta) {

		if (inTransition) {
			inTransition = gameState.getValue(ChangeSceneGO.IN_TRANSITION);
			if (inTransition) {
				return;
			}
		}

		if (checks >= 0) {
			checks--;
		}

		if (checks == 0) {
			boolean init = gameState.getValue(element, INIT);
			if (!init) {
				runEffects(element.getEffectsForEvent(SceneElementEvType.INIT));
				gameState.setValue(element, INIT, true);
			}
			runEffects(element.getEffectsForEvent(SceneElementEvType.ADDED));
		}

		if (hasAlways)
			runEffects(element.getEffectsForEvent(SceneElementEvType.ALWAYS));
	}

	public void release() {
		runEffects(element.getEffectsForEvent(SceneElementEvType.REMOVED));
	}

}
