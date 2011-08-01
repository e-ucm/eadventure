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

package es.eucm.eadventure.common.model.effects.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;

/**
 * Effect that performs an interpolation between two values in an {@link EAdVar}
 * 
 * 
 */
@Element(detailed = EAdVarInterpolationEffect.class, runtime = EAdVarInterpolationEffect.class)
public class EAdVarInterpolationEffect extends AbstractEAdEffect {

	/**
	 * Loops types
	 * 
	 */
	public enum LoopType {
		/**
		 * No loop
		 */
		NO_LOOP,

		/**
		 * When interpolations ends, goes backwards
		 */
		REVERSE,

		/**
		 * When interpolation ends, it restarts
		 */
		RESTART
	};

	public enum InterpolationType {
		/**
		 * Linear interpolation
		 */
		LINEAR,

		/**
		 * Bounces in the end
		 */
		BOUNCE_END;

	}

	@Param("var")
	private EAdVar<?> var;

	@Param("initialValue")
	private LiteralExpressionOperation initialValue;

	@Param("endValue")
	private LiteralExpressionOperation endValue;

	@Param("time")
	private int interpolationTime;

	@Param("loop")
	private LoopType loopType;

	@Param("interpolation")
	private InterpolationType interpolationType;

	public EAdVarInterpolationEffect(String id, EAdVar<?> var,
			LiteralExpressionOperation endValue, int time) {
		super(id);
		LiteralExpressionOperation startValue = new LiteralExpressionOperation(
				"id", "[0]", var);
		setInterpolation(var, startValue, endValue, time, LoopType.NO_LOOP,
				InterpolationType.LINEAR);
	}

	public EAdVarInterpolationEffect(String id, EAdVar<?> var2, float start,
			float end, int time, LoopType loop,
			InterpolationType interpolationType) {
		super(id);
		LiteralExpressionOperation startValue = new LiteralExpressionOperation(
				"id", "" + start);
		LiteralExpressionOperation endValue = new LiteralExpressionOperation(
				"id", "" + end);
		setInterpolation(var2, startValue, endValue, time, loop,
				interpolationType);
	}

	public EAdVarInterpolationEffect(String id, EAdVar<?> var2, float start,
			float end, int time, LoopType loop) {
		super(id);
		LiteralExpressionOperation startValue = new LiteralExpressionOperation(
				"id", "" + start);
		LiteralExpressionOperation endValue = new LiteralExpressionOperation(
				"id", "" + end);
		setInterpolation(var2, startValue, endValue, time, loop,
				InterpolationType.LINEAR);
	}

	public EAdVarInterpolationEffect(String id, EAdVar<?> var,
			LiteralExpressionOperation startValue,
			LiteralExpressionOperation endValue, int time, LoopType noLoop) {
		super(id);
		setInterpolation(var, startValue, endValue, time, LoopType.NO_LOOP,
				InterpolationType.LINEAR);
	}

	public void setInterpolation(EAdVar<?> var,
			LiteralExpressionOperation initialValue,
			LiteralExpressionOperation endValue, int time, LoopType loop,
			InterpolationType interpolationType) {
		this.var = var;
		this.initialValue = initialValue;
		this.endValue = endValue;
		this.interpolationTime = time;
		this.loopType = loop;
		this.interpolationType = interpolationType;
	}

	public void setInterpolation(EAdVar<?> var, float initialValue,
			float endValue, int time, LoopType loop,
			InterpolationType interpolationType) {
		setInterpolation(var, new LiteralExpressionOperation("id", ""
				+ initialValue), new LiteralExpressionOperation("" + endValue),
				time, loop, interpolationType);
	}

	public EAdVar<?> getVar() {
		return var;
	}

	public LiteralExpressionOperation getInitialValue() {
		return initialValue;
	}

	public LiteralExpressionOperation getEndValue() {
		return endValue;
	}

	public int getInterpolationTime() {
		return interpolationTime;
	}

	public LoopType getLoopType() {
		return loopType;
	}

	public InterpolationType getInterpolationType() {
		return interpolationType;
	}

}
