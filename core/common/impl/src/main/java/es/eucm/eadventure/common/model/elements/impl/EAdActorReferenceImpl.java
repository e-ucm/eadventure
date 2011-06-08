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

package es.eucm.eadventure.common.model.elements.impl;

import es.eucm.eadventure.common.Element;
import es.eucm.eadventure.common.Param;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.EAdActorReference;

@Element(runtime = EAdActorReferenceImpl.class, detailed = EAdActorReferenceImpl.class)
public class EAdActorReferenceImpl extends EAdBasicSceneElement implements
		EAdActorReference {

	@Param("referencedActor")
	private EAdActor referencedActor;

	/**
	 * Creates an empty actor reference
	 * 
	 * @param parent
	 *            Element's parent
	 * @param id
	 *            Element's id
	 */
	public EAdActorReferenceImpl(String id) {
		this(id, null);
	}

	/**
	 * Creates an actor reference.
	 * 
	 * @param parent
	 *            Element's parent
	 * @param id
	 *            Element's id
	 * @param actor
	 *            Referenced actor
	 */
	public EAdActorReferenceImpl(String id, EAdActor actor) {
		super(id);
		setReferencedActor(actor);
	}

	/**
	 * Creates an actor reference for the given actor
	 * 
	 * @param actor
	 *            the actor
	 */
	public EAdActorReferenceImpl(EAdActor actor) {
		this(actor.getId() + "_reference", actor);
	}

	/**
	 * Sets the referenced actor for this reference
	 * 
	 * @param referencedActor
	 *            the referenced actor
	 */
	public void setReferencedActor(EAdActor referencedActor) {
		this.referencedActor = referencedActor;
	}

	@Override
	public EAdActor getReferencedActor() {
		return referencedActor;
	}

}
