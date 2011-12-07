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

package es.eucm.eadventure.common.model.weev.world.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.weev.impl.AbstractWEEVElement;
import es.eucm.eadventure.common.model.weev.world.ActorPlacement;
import es.eucm.eadventure.common.model.weev.world.Space;
import es.eucm.eadventure.common.model.weev.world.SpaceLink;
import es.eucm.eadventure.common.model.weev.world.World;

/**
 * Default implementation of the @{link World} interface
 */
@Element(detailed = WorldImpl.class, runtime = WorldImpl.class)
public class WorldImpl extends AbstractWEEVElement implements World {

	private EAdList<Space> spaces;
	
	private EAdList<SpaceLink> spaceLinks;
	
	private EAdList<ActorPlacement> actorPlacements;
	
	@Param(value = "initialSpace")
	private Space initialSpace;
	
	public WorldImpl() {
		spaces = new EAdListImpl<Space>(Space.class);
		spaceLinks = new EAdListImpl<SpaceLink>(SpaceLink.class);
		actorPlacements = new EAdListImpl<ActorPlacement>(ActorPlacement.class);
	}
	
	@Override
	public EAdList<Space> getSpaces() {
		return spaces;
	}

	@Override
	public EAdList<SpaceLink> getSpaceLinks() {
		return spaceLinks;
	}

	@Override
	public EAdList<ActorPlacement> getActorPlacement() {
		return actorPlacements;
	}

	@Override
	public Space getInitialSpace() {
		return initialSpace;
	}

	public void setInitialSpace(Space initialSpace) {
		this.initialSpace = initialSpace;
	}
}
