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

package es.eucm.ead.engine.gameobjects.events;

import com.google.inject.Inject;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.game.GameState.FieldWatcher;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.events.WatchFieldEv;
import es.eucm.ead.model.elements.events.enums.WatchFieldEvType;
import es.eucm.ead.model.elements.operations.ElementField;

public class WatchFieldEvGO extends AbstractEventGO<WatchFieldEv> implements
		FieldWatcher {

	private boolean fieldUpdated;

	@Inject
	public WatchFieldEvGO(Game game) {
		super(game);
	}

	public void setElement(WatchFieldEv ev) {
		super.setElement(ev);
		fieldUpdated = true;
		for (ElementField f : ev.getFields()) {
			game.getGameState().addFieldWatcher(this, f.getElement());
		}
	}

	@Override
	public void act(float delta) {
		if (fieldUpdated) {
			fieldUpdated = false;
			for (Effect e : element.getEffectsForEvent(WatchFieldEvType.WATCH)) {
				game.addEffect(e);
			}
		}
	}

	public void release() {
		game.getGameState().removeFieldWatcher(this);
	}

	@Override
	public <T> boolean setField(String elementId, String varName, T value) {
		for (ElementField f : this.element.getFields()) {
			if (varName.equals(f.getVarDef())) {
				fieldUpdated = true;
				return false;
			}
		}
		return false;
	}
}
