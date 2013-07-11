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

package ead.common.model.elements.weev.story.element.impl.effects;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.weev.story.element.impl.AbstractEffect;
import ead.common.model.weev.Actor;

/**
 * Effect to change the {@link Actor} visibility (i.e. hide or show)
 */
@Element
public class ChangeActorVisibilityEffect extends AbstractEffect {

	/**
	 * <p>
	 * The change to the visibility:
	 * <li>SHOW: display the actor in the spaces where it is placed</li>
	 * <li>HIDE: hide the actor, even in the spaces where it is placed</li>
	 * </p>
	 * 
	 */
	public static enum Change {
		SHOW, HIDE
	}

	/**
	 * The {@link Change} to the {@link Actor}'s visibility
	 */
	@Param
	private Change change;

	/**
	 * The {@link Actor} to which to apply the change
	 */
	@Param
	private Actor actor;

	/**
	 * @param actor
	 *            The {@link Actor} to which to apply the change
	 * @param change
	 *            The {@link Change} to the {@link Actor}'s visibility
	 */
	public ChangeActorVisibilityEffect(Actor actor, Change change) {
		this.actor = actor;
		this.change = change;
	}

	public Change getChange() {
		return change;
	}

	public void setChange(Change change) {
		this.change = change;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

}
