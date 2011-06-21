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

package es.eucm.eadventure.engine.core.gameobjects.impl.interfaces;

import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.interfaces.Interpolation;

/**
 * Linear interpolation between two values
 * 
 */
public class LinearInterpolation<T extends Number> implements Interpolation {

	private EAdVar<T> var;

	/**
	 * Initial value for the field
	 */
	private T initValue;

	/**
	 * Finish value for the field
	 */
	private T finishValue;

	/**
	 * Waiting time before interpolation starts
	 */
	private int timeToStart;

	/**
	 * If interpolation loops
	 */
	private boolean loop;

	/**
	 * If interpolations is running
	 */
	private boolean running;

	/**
	 * If time to start has finished and the field is actually updating
	 */
	private boolean started;

	/**
	 * Current value for the field
	 */
	private double currentValue;

	/**
	 * Values advance for each time unit
	 */
	private double advance;

	/**
	 * Remaining time to reach the finish
	 */
	private long remainingTime;

	/**
	 * Time to accomplish the interpolation (in milliseconds)
	 */
	private int timeToFinish;
	
	private ValueMap valueMap;

	public LinearInterpolation(EAdVar<T> var, T initValue,
			T finishValue, int timeToFinish, int timeToStart, boolean loop,
			ValueMap valueMap) {
		this.var = var;
		this.initValue = initValue;
		this.finishValue = finishValue;
		this.timeToFinish = timeToFinish;
		this.timeToStart = timeToStart;
		this.started = timeToStart == 0;
		remainingTime = started ? timeToFinish : timeToStart;
		this.advance = (double) (finishValue.doubleValue() - initValue.doubleValue()) / timeToFinish;
		this.loop = loop;
		this.valueMap = valueMap;
	}

	@Override
	public void play() {
		running = true;
		currentValue = initValue.doubleValue();
		valueMap.setValue(var, initValue);
		updateField();
	}

	@Override
	public void pause() {
		running = false;

	}

	@Override
	public void stop() {
		running = false;

	}

	@Override
	public void update(long elapsedTime) {
		if (running) {
			remainingTime -= elapsedTime;
			if (started) {
				currentValue += elapsedTime * advance;

				if (remainingTime < 0) {
					if (loop) {
						currentValue = initValue.doubleValue();
						started = timeToStart == 0;
						remainingTime = started ? timeToFinish : timeToStart;
					} else {
						running = false;
						currentValue = finishValue.doubleValue();
					}
				}
				updateField();
			} else {
				if (remainingTime <= 0) {
					started = true;
					remainingTime = timeToFinish;
				}
			}

		}
	}

	@SuppressWarnings("unchecked")
	protected void updateField() {
		//TODO no way to do this without specific casting?
		if (var.getType() == Integer.class)
			valueMap.setValue(var, (T) new Integer((int) currentValue));
		if (var.getType() == Float.class)
			valueMap.setValue(var, (T) new Float(currentValue));
	}

	@Override
	public boolean isRunning() {
		return running;
	}

}
