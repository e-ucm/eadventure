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
import ead.common.model.params.text.EAdString;

/**
 * Assessment of player effect.
 */
@Element
public class AssessmentEffect extends AbstractEffect {

	/**
	 * The positive or negative type of the assessment
	 */
	public static enum Type {
		POSITIVE, NEGATIVE
	}

	/**
	 * The value (i.e. increment or decrement of points to the player) of the
	 * effect
	 */
	@Param
	private Integer value;

	/**
	 * The message in the log for the effect, the justification for the
	 * assessment
	 */
	@Param
	private EAdString message;

	/**
	 * The {@link Type} of this effect
	 */
	@Param
	private Type type;

	/**
	 * @param type
	 *            The {@link Type} of this effect
	 * @param value
	 *            The value (i.e. benefit or cost) of this assessment
	 */
	public AssessmentEffect(Type type, Integer value) {
		this.type = type;
		this.value = value;
		this.message = new EAdString("message");
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public EAdString getMessage() {
		return message;
	}

}
