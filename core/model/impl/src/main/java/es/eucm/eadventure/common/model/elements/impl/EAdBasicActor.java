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

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.resources.annotation.Asset;
import es.eucm.eadventure.common.resources.annotation.Bundled;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;

@Element(runtime = EAdBasicActor.class, detailed = EAdBasicActor.class)
public class EAdBasicActor extends EAdBasicSceneElement implements EAdActor {

	@Param("actions")
	private EAdList<EAdAction> actions;

	@Bundled
	@Asset({ Drawable.class })
	public static final String appearance = "appearance";

	@Param("name")
	private final EAdString name;

	@Param("description")
	private final EAdString description;

	@Param("detailedDescription")
	private final EAdString detailedDescription;

	@Param("documentation")
	private final EAdString documentation;
	
	public EAdBasicActor(String id) {
		super(id);
		this.actions = new EAdListImpl<EAdAction>(EAdAction.class);
		this.name = EAdString.newEAdString("actorName");
		this.description = EAdString.newEAdString("actorDescription");
		this.detailedDescription = EAdString.newEAdString("actorDetailedDescription");
		this.documentation = EAdString.newEAdString("actorDocumentation");
	}

	public EAdList<EAdAction> getActions() {
		return actions;
	}

	/**
	 * @return the description
	 */
	public EAdString getDescription() {
		return description;
	}

	/**
	 * @return the detailedDescription
	 */
	public EAdString getDetailedDescription() {
		return detailedDescription;
	}

	/**
	 * @return the documentation
	 */
	public EAdString getDocumentation() {
		return documentation;
	}
	/**
	 * @return the name
	 */
	public EAdString getName() {
		return name;
	}


}
