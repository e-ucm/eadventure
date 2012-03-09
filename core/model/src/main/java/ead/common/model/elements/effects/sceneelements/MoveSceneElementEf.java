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

package ead.common.model.elements.effects.sceneelements;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.enums.MovementSpeed;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.variables.EAdOperation;
import ead.common.model.elements.variables.operations.MathOp;
import ead.common.model.elements.variables.operations.ValueOp;

/**
 * 
 * {@link EAdEffect} to move an {@link EAdActorReference} to its current
 * position to another
 * 
 */
@Element(runtime = MoveSceneElementEf.class, detailed = MoveSceneElementEf.class)
public class MoveSceneElementEf extends AbstractSceneElementEffect {

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
	
	@Param("target")
	private EAdSceneElementDef target;

	/**
	 * Constructs an move actor reference effect, with target set to
	 * {@code ( 0, 0 )} and speed set to {@link MovementSpeed#NORMAL}
	 * 
	 */
	public MoveSceneElementEf() {
		this(null);
	}

	public MoveSceneElementEf( EAdSceneElementDef element) {
		this(element, new MathOp("0"), new MathOp("0"));
	}

	public MoveSceneElementEf( EAdSceneElementDef element,
			MathOp xTarget, MathOp yTarget) {
		this( element, xTarget, yTarget, MovementSpeed.NORMAL);
	}

	public MoveSceneElementEf( EAdSceneElementDef element, int xTarget,
			int yTarget, MovementSpeed speed) {
		this( element, new MathOp( "" + xTarget),
				new MathOp( "" + yTarget), speed);
	}

	public MoveSceneElementEf(EAdSceneElementDef element,
			MathOp xTarget, MathOp yTarget, MovementSpeed speed) {
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
	public EAdOperation getxTarget() {
		return xTarget;
	}

	/**
	 * 
	 * @return y coordinate target
	 */
	public EAdOperation getyTarget() {
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
		setTargetCoordiantes(new ValueOp(new Integer(x)),
				new ValueOp(new Integer(y)));
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
	
	public boolean isUseTrajectory(){
		return useTrajectory;
	}

	public void setTarget( EAdSceneElementDef sceneElementDef ){
		this.target = sceneElementDef;
	}
	
	public EAdSceneElementDef getTarget( ){
		return target;
	}

	public void setxTarget(EAdOperation xTarget) {
		this.xTarget = xTarget;
	}

	public void setyTarget(EAdOperation yTarget) {
		this.yTarget = yTarget;
	}
	
	
	
}
