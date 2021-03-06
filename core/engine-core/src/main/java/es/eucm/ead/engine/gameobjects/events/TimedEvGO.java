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
import es.eucm.ead.model.elements.events.TimedEv;
import es.eucm.ead.model.elements.events.enums.TimedEvType;

public class TimedEvGO extends AbstractEventGO<TimedEv> {

	private int elapsedTime;

	private int repeats;

	@Inject
	public TimedEvGO(Game game) {
		super(game);
	}

	@Override
	public void initialize() {
		super.initialize();
		elapsedTime = 0;
		repeats = element.getRepeats();
	}

	@Override
	public void act(float delta) {
		if (repeats != 0) {
			if (elapsedTime == 0) {
				this.runEffects(element
						.getEffectsForEvent(TimedEvType.START_TIME));
			}
			elapsedTime += game.getSkippedMilliseconds();
			if (elapsedTime > element.getTime()) {
				this.runEffects(element
						.getEffectsForEvent(TimedEvType.END_TIME));
				elapsedTime = 0;
				repeats--;
			}
		}
	}
}
