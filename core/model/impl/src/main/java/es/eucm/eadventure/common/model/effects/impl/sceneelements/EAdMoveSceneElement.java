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

package es.eucm.eadventure.common.model.effects.impl.sceneelements;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.EAdSceneElementEffect;
import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;
import es.eucm.eadventure.common.model.elements.EAdActorReference;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;

/**
 * 
 * {@link EAdEffect} to move an {@link EAdActorReference} to its current
 * position to another
 * 
 */
@Element(runtime = EAdMoveSceneElement.class, detailed = EAdMoveSceneElement.class)
public class EAdMoveSceneElement extends AbstractEAdEffect implements
		EAdSceneElementEffect {

	public static final EAdVarDef<Boolean> VAR_ANIMATION_ENDED = new EAdVarDefImpl<Boolean>(
			"animation_ended", Boolean.class, Boolean.FALSE);

	/**
	 * 
	 * Enum with all possibles speeds for the movement effect
	 * 
	 */
	public enum MovementSpeed {
		SLOW, NORMAL, FAST, INSTANT;

		public float getSpeedFactor() {
			switch (this) {
			case FAST:
				return 0.5f;
			case SLOW:
				return 2.0f;
			case INSTANT:
				return 0.0f;
			default:
				return 1.0f;
			}
		}
	};

	/**
	 * Actor reference
	 */
	@Param("element")
	private EAdSceneElement element;

	/**
	 * Target coordinates
	 */
	@Param("xTarget")
	private LiteralExpressionOperation xTarget;

	@Param("yTarget")
	private LiteralExpressionOperation yTarget;

	/**
	 * Movement speed
	 */
	@Param("speed")
	private MovementSpeed speed;

	/**
	 * Constructs an move actor reference effect, with target set to
	 * {@code ( 0, 0 )} and speed set to {@link MovementSpeed#NORMAL}
	 * 
	 * @param id
	 *            Element's id
	 */
	public EAdMoveSceneElement(String id) {
		this(id, null);
	}

	public EAdMoveSceneElement(String id, EAdSceneElement element) {
		this(id, element, new LiteralExpressionOperation("id", "0"),
				new LiteralExpressionOperation("id", "0"));
	}

	public EAdMoveSceneElement(String id, EAdSceneElement element,
			LiteralExpressionOperation xTarget,
			LiteralExpressionOperation yTarget) {
		this(id, element, xTarget, yTarget, MovementSpeed.NORMAL);
	}

	public EAdMoveSceneElement(String id, EAdSceneElement element, int xTarget,
			int yTarget, MovementSpeed speed) {
		this(id, element, new LiteralExpressionOperation("id", "" + xTarget),
				new LiteralExpressionOperation("id", "" + yTarget), speed);
	}

	public EAdMoveSceneElement(String id, EAdSceneElement element,
			LiteralExpressionOperation xTarget,
			LiteralExpressionOperation yTarget, MovementSpeed speed) {
		super(id);
		this.element = element;
		this.xTarget = xTarget;
		this.yTarget = yTarget;
		this.speed = speed;
	}

	/**
	 * Sets scene element for this effect
	 * 
	 * @param sceneElement
	 *            scene element reference to be moved
	 */
	public void setSceneElement(EAdSceneElement sceneElement) {
		this.element = sceneElement;
	}

	/**
	 * Sets target coordinates for this effect, as expresion
	 * 
	 * @param x
	 *            the expression to calculate the x value
	 * @param y
	 *            the expression to calculate the y value
	 */
	public void setTargetCoordiantes(LiteralExpressionOperation x,
			LiteralExpressionOperation y) {
		xTarget = x;
		yTarget = y;
	}

	/**
	 * Sets the movement speed
	 * 
	 * @param speed
	 *            the movement speed
	 */
	public void setSpeed(MovementSpeed speed) {
		this.speed = speed;
	}

	/**
	 * 
	 * @return x coordinate target
	 */
	public LiteralExpressionOperation getXTarget() {
		return xTarget;
	}

	/**
	 * 
	 * @return y coordinate target
	 */
	public LiteralExpressionOperation getYTarget() {
		return yTarget;
	}

	/**
	 * 
	 * @return Actor reference associated to this effect
	 */
	public EAdSceneElement getSceneElement() {
		return element;
	}

	/**
	 * 
	 * @return the movement speed
	 */
	public MovementSpeed getSpeed() {
		return speed;
	}

	public void setTargetCoordiantes(int i, int j) {
		setTargetCoordiantes(new LiteralExpressionOperation("id", "" + i),
				new LiteralExpressionOperation("id", "" + j));
	}

}
