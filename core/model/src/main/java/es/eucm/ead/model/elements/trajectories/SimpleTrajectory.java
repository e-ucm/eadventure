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

package es.eucm.ead.model.elements.trajectories;

import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;

/**
 * A simple trajectory generator. Trace the trajectory to a point with a
 * straight line.
 * 
 */
@Element
public class SimpleTrajectory extends Trajectory {

	@Param
	private boolean onlyHorizontal;

	@Param
	private boolean freeWalk;

	@Param
	private int top;

	@Param
	private int bottom;

	@Param
	private int left;

	@Param
	private int right;

	@Param
	private int distanceToTarget;

	public SimpleTrajectory() {
		this(false);
		this.freeWalk = true;
	}

	/**
	 * Creates an instance
	 * 
	 * @param onlyHorizontal
	 *            if trajectories should be only horizontal
	 */
	public SimpleTrajectory(boolean onlyHorizontal) {
		this.onlyHorizontal = onlyHorizontal;
		bottom = right = Integer.MAX_VALUE / 2;
		top = left = Integer.MIN_VALUE / 2;
		this.distanceToTarget = 300;
	}

	public boolean isOnlyHorizontal() {
		return onlyHorizontal;
	}

	public void setLimits(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public int getTop() {
		return top;
	}

	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}

	public int getBottom() {
		return bottom;
	}

	public void setOnlyHorizontal(boolean onlyHorizontal) {
		this.onlyHorizontal = onlyHorizontal;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public void setRight(int right) {
		this.right = right;
	}

	/**
	 * Returns if this trajectory allows to move anywhere, if ignored top, left,
	 * right and bottom parameters
	 * 
	 * @return
	 */
	public boolean isFreeWalk() {
		return freeWalk;
	}

	public void setFreeWalk(boolean freeWalk) {
		this.freeWalk = freeWalk;
	}

	/**
	 * Returns the maximum distance to reach one target
	 * @return
	 */
	public int getDistanceToTarget() {
		return distanceToTarget;
	}

	/**
	 * Sets the maximum distance to reach one target
	 * @param distanceToTarget
	 */
	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}

}
