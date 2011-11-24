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
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.variables.EAdOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.MathOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.ValueOperation;

/**
 * 
 * {@link EAdEffect} to move an {@link EAdActorReference} to its current
 * position to another
 * 
 */
@Element(runtime = EAdMoveSceneElement.class, detailed = EAdMoveSceneElement.class)
public class EAdMoveSceneElement extends AbstractSceneElementEffect {

	/**
	 * Target coordinates
	 */
	@Param("xTarget")
	private EAdOperation xTarget;

	@Param("yTarget")
	private EAdOperation yTarget;

	/**
	 * Movement speed
	 */
	@Param("speed")
	private MovementSpeed speed;

	@Param("speedFactor")
	private float speedFactor;
	
	@Param("boolean")
	private boolean useTrajectory;
	
	@Param("elementTarget")
	private EAdSceneElementDef sceneElementDef;

	/**
	 * Constructs an move actor reference effect, with target set to
	 * {@code ( 0, 0 )} and speed set to {@link MovementSpeed#NORMAL}
	 * 
	 */
	public EAdMoveSceneElement() {
		this(null);
	}

	public EAdMoveSceneElement( EAdSceneElementDef element) {
		this(element, new MathOperation("0"), new MathOperation("0"));
	}

	public EAdMoveSceneElement( EAdSceneElementDef element,
			MathOperation xTarget, MathOperation yTarget) {
		this( element, xTarget, yTarget, MovementSpeed.NORMAL);
	}

	public EAdMoveSceneElement( EAdSceneElementDef element, int xTarget,
			int yTarget, MovementSpeed speed) {
		this( element, new MathOperation( "" + xTarget),
				new MathOperation( "" + yTarget), speed);
	}

	public EAdMoveSceneElement(EAdSceneElementDef element,
			MathOperation xTarget, MathOperation yTarget, MovementSpeed speed) {
		super();
		setSceneElement(element);
		setQueueable(true);
		this.xTarget = xTarget;
		this.yTarget = yTarget;
		this.speed = speed;
		this.speedFactor = 1.0f;
		this.useTrajectory = true;
	}

	/**
	 * Sets target coordinates for this effect, as expresion
	 * 
	 * @param x
	 *            the expression to calculate the x value
	 * @param y
	 *            the expression to calculate the y value
	 */
	public void setTargetCoordiantes(EAdOperation x, EAdOperation y) {
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
	public EAdOperation getXTarget() {
		return xTarget;
	}

	/**
	 * 
	 * @return y coordinate target
	 */
	public EAdOperation getYTarget() {
		return yTarget;
	}

	/**
	 * 
	 * @return the movement speed
	 */
	public MovementSpeed getSpeed() {
		return speed;
	}
	
	public void setUseTrajectory( boolean useTrajectory ){
		this.useTrajectory = useTrajectory;
	}

	public void setTargetCoordiantes(int x, int y) {
		setTargetCoordiantes(new ValueOperation(new Integer(x)),
				new ValueOperation(new Integer(y)));
	}

	public float getSpeedFactor() {
		if (speed == MovementSpeed.CUSTOM)
			return speedFactor;
		return speed.getSpeedFactor();
	}

	public void setSpeedFactor(float speedFactor) {
		this.speed = MovementSpeed.CUSTOM;
		this.speedFactor = speedFactor;
	}
	
	public boolean useTrajectory(){
		return useTrajectory;
	}

	public void setTarget( EAdSceneElementDef sceneElementDef ){
		this.sceneElementDef = sceneElementDef;
	}
	
	public EAdSceneElementDef getTarget( ){
		return sceneElementDef;
	}
}
