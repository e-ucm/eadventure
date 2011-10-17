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
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.behavior.EAdBehavior;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.events.EAdEvent;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.resources.annotation.Asset;
import es.eucm.eadventure.common.resources.annotation.Bundled;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;

@Element(runtime = EAdSceneElementDefImpl.class, detailed = EAdSceneElementDefImpl.class)
public class EAdSceneElementDefImpl extends AbstractEAdElementWithBehavior
		implements EAdSceneElementDef {

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

	@Param("draggable")
	private EAdCondition draggableCondition;

	public EAdSceneElementDefImpl(String id) {
		super(id);
		this.actions = new EAdListImpl<EAdAction>(EAdAction.class);
		this.events = new EAdListImpl<EAdEvent>(EAdEvent.class);
		this.draggableCondition = EmptyCondition.FALSE_EMPTY_CONDITION;

		this.name = EAdString.newEAdString("actorName");
		this.description = EAdString.newEAdString("actorDescription");
		this.detailedDescription = EAdString
				.newEAdString("actorDetailedDescription");
		this.documentation = EAdString.newEAdString("actorDocumentation");

	}

/**
	 * Sets the condition that makes the element holding this definition draggable. The default value is {@link EmptyCondition#FALSE_EMPTY_CONDITION)
	 * @param condition the condition
	 */
	public void setDraggable(EAdCondition condition) {
		this.draggableCondition = condition;
	}

	@Override
	public EAdCondition getDraggabe() {
		return draggableCondition;
	}

	@Override
	public EAdList<EAdAction> getValidActions() {
		return actions;
	}
	
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

	@Override
	public EAdElement copy() {
		return copy(false);
	}

	@Override
	public EAdElement copy(boolean deepCopy) {
		EAdSceneElementDefImpl def = new EAdSceneElementDefImpl(id);
		def.events = events;
		def.actions = actions;
		if (deepCopy) {
			def.behavior = (EAdBehavior) behavior.copy(deepCopy);
			def.draggableCondition = (EAdCondition) draggableCondition
					.copy(deepCopy);
		} else {
			def.behavior = behavior;
			def.draggableCondition = draggableCondition;
		}
		return def;
	}

}
