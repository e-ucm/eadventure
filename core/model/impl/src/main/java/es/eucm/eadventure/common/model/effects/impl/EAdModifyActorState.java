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

package es.eucm.eadventure.common.model.effects.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;

/**
 * <p>This effects modifies the state of an {@link EAdSceneElementDef}</p>
 * <p>The state can change to the values specified by the 
 * modification value:
 * {@link Modification}
 *</p>
 */
@Element(detailed = EAdModifyActorState.class, runtime = EAdModifyActorState.class)
public class EAdModifyActorState extends AbstractEAdEffect {

	/**
	 * <p>The modification to the state:
	 * <li>PLACE_IN_INVENTORY: move the {@link EAdSceneElementDef} to the inventory (remove from scene)</li>
	 * <li>PLACE_IN_SCENE; move the {@link EAdSceneElementDef} to the scene (remove from inventory)</li>
	 * <li>REMOVE_SCENE_AND_INVENTORY: remove the {@link EAdSceneElementDef} from scene and inventory</li>
	 * </p>
	 *
	 */
	public static enum Modification {
		PLACE_IN_INVENTORY, PLACE_IN_SCENE, REMOVE_SCENE_AND_INVENTORY
	}
	
	@Param("modification")
	private Modification modification;
	
	@Param("actor")
	private EAdSceneElementDef actor;
	
	/**
	 * Constructor. Actor will be null, default modification will be {@link Modification.PLACE_IN_INVENTORY}
	 * 
	 */
	public EAdModifyActorState() {
		this(null, Modification.PLACE_IN_INVENTORY);
	}
	
	/**
	 * Constructor
	 * 
	 * @param actor The {@link EAdSceneElementDef} to be modified
	 * @param modification The {@link Modification} to the actor state
	 */
	public EAdModifyActorState( EAdSceneElementDef actor, Modification modification) {
		super();
		this.modification = modification;
		this.actor = actor;
	}
	
	/**
	 * @param modification The {@link Modification} to be performed to the actor
	 */
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	
	/**
	 * @return The {@link Modification} to be performed to the actor
	 */
	public Modification getModification() {
		return modification;
	}
	
	/**
	 * @param actor Set the {@link EAdSceneElementDef} to be modified
	 */
	public void setActor(EAdSceneElementDef actor) {
		this.actor = actor;
	}
	
	/**
	 * @return Get the {@link EAdSceneElementDef} to be modified
	 */
	public EAdSceneElementDef getActor() {
		return actor;
	}
	
}
