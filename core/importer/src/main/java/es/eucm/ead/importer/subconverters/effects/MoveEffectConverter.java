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

package es.eucm.ead.importer.subconverters.effects;

import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.model.elements.effects.enums.MovementSpeed;
import es.eucm.ead.model.elements.effects.sceneelements.MoveSceneElementEf;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.importer.EAdElementsCache;
import es.eucm.ead.importer.subconverters.effects.EffectsConverter.EffectConverter;
import es.eucm.eadventure.common.data.chapter.effects.*;
import es.eucm.eadventure.common.data.chapter.elements.Player;

import java.util.ArrayList;
import java.util.List;

public class MoveEffectConverter implements EffectConverter<AbstractEffect> {

	private EAdElementsCache elementsCache;

	public MoveEffectConverter(EAdElementsCache elementsCache) {
		this.elementsCache = elementsCache;
	}

	@Override
	public List<EAdEffect> convert(AbstractEffect e) {
		// XXX It doesn't work if there's more than one element with the definition
		ArrayList<EAdEffect> list = new ArrayList<EAdEffect>();
		SceneElementDef sceneElementDef = null;
		boolean useTrajectory = false;
		float x = 0;
		float y = 0;
		switch (e.getType()) {
		case Effect.MOVE_NPC:
			MoveNPCEffect npcEf = (MoveNPCEffect) e;
			x = npcEf.getX();
			y = npcEf.getY();
			sceneElementDef = (SceneElementDef) elementsCache.get(npcEf
					.getTargetId());
			break;
		case Effect.MOVE_PLAYER:
			MovePlayerEffect ef = (MovePlayerEffect) e;
			x = ef.getX();
			y = ef.getY();
			useTrajectory = true;
			sceneElementDef = (SceneElementDef) elementsCache
					.get(Player.IDENTIFIER);
			break;
		case Effect.MOVE_OBJECT:
			MoveObjectEffect obEf = (MoveObjectEffect) e;
			x = obEf.getX();
			y = obEf.getY();
			// XXX isAnimated, scale...
			sceneElementDef = (SceneElementDef) elementsCache.get(obEf
					.getTargetId());
		}
		MoveSceneElementEf moveSceneElement = new MoveSceneElementEf(
				sceneElementDef, x, y, MovementSpeed.NORMAL);
		moveSceneElement.setUseTrajectory(useTrajectory);
		list.add(moveSceneElement);
		return list;
	}
}
