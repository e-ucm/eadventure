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

package ead.common.model.elements.effects;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.effects.sceneelements.AbstractSceneElementEffect;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.util.EAdPosition;

/**
 * Effect that adds an actor reference to the current scene
 * 
 * 
 */
@Element
public class AddActorReferenceEf extends AbstractEffect {

	@Param("actor")
	private EAdSceneElementDef actor;

	@Param("position")
	private EAdPosition position;

	@Param("initialEffect")
	private AbstractSceneElementEffect effect;

	public AddActorReferenceEf() {

	}

	public AddActorReferenceEf(EAdSceneElementDef actor, EAdPosition p,
			AbstractSceneElementEffect effect) {
		this.actor = actor;
		this.position = p;
		this.effect = effect;
	}

	public void setEffect(AbstractSceneElementEffect effect) {
		this.effect = effect;
	}

	public void setActor(EAdSceneElementDef actor) {
		this.actor = actor;
	}

	public void setPosition(EAdPosition position) {
		this.position = position;
	}

	public EAdSceneElementDef getActor() {
		return actor;
	}

	public EAdPosition getPosition() {
		return position;
	}

	public AbstractSceneElementEffect getEffect() {
		return effect;
	}

}
