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

package es.eucm.eadventure.common.util.impl;

import es.eucm.eadventure.common.util.impl.EAdInterpolator;

public class EAdInterpolator {

	/**
	 * Interpolator f(x) = x
	 */
	public static final EAdInterpolator LINEAR = new EAdInterpolator(
			new float[] { 0.0f, 1.0f });
	
	public static final EAdInterpolator BOUNCE_END = new EAdInterpolator(
			new float[] { 0.0f, 3.5f, -2.5f });
	
	public static final EAdInterpolator ACCELERATE = new EAdInterpolator(
			new float[] { 0.0f, 0.0f, 1.0f });
	
	public static final EAdInterpolator DESACCELERATE = new EAdInterpolator(
			new float[] { 0.0f, 0.0f, 1.0f });
	
	public static final EAdInterpolator BOUNCE_START_END = new EAdInterpolator(
			new float[] { 0.0f, -3.1667f, 12.5f, -8.3333f });
	
	public static final EAdInterpolator DASH = new EAdInterpolator(
			new float[] { 0.0f, -3.0f, 4.0f });
	
	private float[] polynomial;

	public EAdInterpolator(float[] polynomial) {
		this.polynomial = polynomial;
	}

	/**
	 * 
	 * @return
	 */
	public float interpolate(float currentTime, float totalTime, float totalLength ) {
		float v = currentTime / totalTime;
		float r = 0;
		for (int i = 0; i < polynomial.length; i++) {
			r += Math.pow(v, i) * polynomial[i];
		}
		return r * totalLength;
	}

}
