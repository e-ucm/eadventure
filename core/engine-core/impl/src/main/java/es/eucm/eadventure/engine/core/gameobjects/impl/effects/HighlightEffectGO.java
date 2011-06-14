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

import es.eucm.eadventure.common.model.effects.impl.actorreference.EAdHighlightActorReference;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.ActorReferenceGO;
import es.eucm.eadventure.engine.core.GameLoop;

public class HighlightEffectGO extends AbstractEffectGO<EAdHighlightActorReference> {

	private int time;

	private ActorReferenceGO actorGO;

	private float oldScale;

	private boolean started;

	@Override
	public void initilize() {
		super.initilize();
		actorGO = (ActorReferenceGO) gameObjectFactory.get(element
				.getActorReference());
		oldScale = actorGO.getScale();
		time = element.getTime();
		started = false;
	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	public void update(GameState gameSate) {
		//FIXME ¿Y si queremos que el highlight se represente de otra manera?
		if (time > 0) {
			if (!started) {
				actorGO.setScale(oldScale * 2);
				started = true;
			}
			time -= GameLoop.SKIP_MILLIS_TICK;
			if (time <= 0) {
				actorGO.setScale(oldScale);
			}
		}
	}

	@Override
	public boolean isFinished() {
		return time <= 0;
	}

}
