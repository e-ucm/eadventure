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

package es.eucm.ead.model.elements.effects.sceneelements;

import es.eucm.ead.model.elements.effects.enums.MovementSpeed;
import es.eucm.ead.model.elements.operations.Operation;
import es.eucm.ead.model.elements.operations.MathOp;
import es.eucm.ead.model.elements.operations.ValueOp;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;

/**
 * 
 * {@link es.eucm.ead.model.elements.Effect} to move an {@link EAdActorReference} to its current
 * position to another
 * 
 */
@Element
public class MoveSceneElementEf extends AbstractSceneElementEffect {

	/**
	 * Target coordinates
	 */
	@Param
	private Operation xtarget;

	@Param
	private Operation ytarget;

	/**
	 * Movement speed
	 */
	@Param
	private es.eucm.ead.model.elements.effects.enums.MovementSpeed speed;

	@Param
	private float speedFactor;

	@Param
	private boolean useTrajectory;

	@Param
	private SceneElementDef targetDefinition;

	@Param
	private SceneElement targetSceneElement;

	/**
	 * Constructs an move actor reference effect, with target set to
	 * {@code ( 0, 0 )} and speed set to {@link es.eucm.ead.model.elements.effects.enums.MovementSpeed#NORMAL}
	 * 
	 */
	public MoveSceneElementEf() {

	}

	public MoveSceneElementEf(SceneElementDef element) {
		this(element, new MathOp("0"), new MathOp("0"));
	}

	public MoveSceneElementEf(SceneElementDef element, Operation xTarget,
			Operation yTarget) {
		this(element, xTarget, yTarget, MovementSpeed.NORMAL);
	}

	public MoveSceneElementEf(SceneElementDef element, float xTarget,
			float yTarget, MovementSpeed speed) {
		this(element, new MathOp("" + xTarget), new MathOp("" + yTarget), speed);
	}

	public MoveSceneElementEf(SceneElementDef element, Operation xTarget,
			Operation yTarget, MovementSpeed speed) {
		super();
		setSceneElement(element);
		this.xtarget = xTarget;
		this.ytarget = yTarget;
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
	public void setTargetCoordiantes(Operation x, Operation y) {
		xtarget = x;
		ytarget = y;
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
	public Operation getXtarget() {
		return xtarget;
	}

	/**
	 * 
	 * @return y coordinate target
	 */
	public Operation getYtarget() {
		return ytarget;
	}

	/**
	 * 
	 * @return the movement speed
	 */
	public MovementSpeed getSpeed() {
		return speed;
	}

	public void setUseTrajectory(boolean useTrajectory) {
		this.useTrajectory = useTrajectory;
	}

	public void setTargetCoordiantes(int x, int y) {
		setTargetCoordiantes(new ValueOp(new Integer(x)), new ValueOp(
				new Integer(y)));
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

	public boolean isUseTrajectory() {
		return useTrajectory;
	}

	public void setTarget(SceneElementDef sceneElementDef) {
		setTargetDefinition(sceneElementDef);
	}

	public void setTarget(SceneElement sceneElement) {
		setTargetSceneElement(sceneElement);
	}

	public SceneElementDef getTarget() {
		return targetDefinition;
	}

	public void setXtarget(Operation xTarget) {
		this.xtarget = xTarget;
	}

	public void setYtarget(Operation yTarget) {
		this.ytarget = yTarget;
	}

	public SceneElementDef getTargetDefinition() {
		return targetDefinition;
	}

	public void setTargetDefinition(SceneElementDef targetDefinition) {
		this.targetDefinition = targetDefinition;
	}

	public SceneElement getTargetSceneElement() {
		return targetSceneElement;
	}

	public void setTargetSceneElement(SceneElement targetSceneElement) {
		this.targetSceneElement = targetSceneElement;
	}

}
