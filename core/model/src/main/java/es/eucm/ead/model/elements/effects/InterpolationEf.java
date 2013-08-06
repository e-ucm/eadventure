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

package es.eucm.ead.model.elements.effects;

import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.elements.EAdElement;
import es.eucm.ead.model.elements.effects.enums.InterpolationLoopType;
import es.eucm.ead.model.elements.effects.enums.InterpolationType;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.operations.BasicField;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.model.elements.operations.EAdOperation;
import es.eucm.ead.model.elements.operations.MathOp;
import es.eucm.ead.model.elements.operations.ValueOp;
import es.eucm.ead.model.params.variables.EAdVarDef;

/**
 * Effect that performs an interpolation between two values in an {@link EAdVar}
 * 
 * 
 */
@SuppressWarnings( { "rawtypes", "unchecked" })
@Element
public class InterpolationEf extends AbstractEffect {

	@Param
	private EAdList<EAdField<?>> fields;

	@Param
	private EAdList<EAdOperation> initialValues;

	@Param
	private EAdList<EAdOperation> endValues;

	@Param
	private int interpolationTime;

	@Param
	private int delay;

	@Param
	private InterpolationLoopType loopType;

	@Param
	private int loops;

	@Param
	private InterpolationType interpolationType;

	@Param
	private boolean relative;

	/**
	 * @param elementField
	 *            field containing the element holding the variable
	 * @param element
	 *            the element holding the variable
	 * @param varDef
	 *            the variable definition
	 * @param initialValue
	 *            the initial value for the interpolation
	 * @param endValue
	 *            the end value for the interpolation
	 * @param interpolationTime
	 *            the time for the interpolation
	 * @param delay
	 *            the delay until the interpolation begins
	 * @param loopType
	 *            the loop type
	 * @param loops
	 *            the number of loops. If loops < 0, is considered to be
	 *            infinite
	 * @param interpolationType
	 *            the interpolation type
	 */
	public InterpolationEf(EAdElement element, EAdVarDef<?> varDef,
			EAdOperation initialValue, EAdOperation endValue,
			int interpolationTime, int delay, InterpolationLoopType loopType,
			int loops, InterpolationType interpolationType) {
		this();
		this.initialValues.add(initialValue);
		this.endValues.add(endValue);
		this.fields.add(new BasicField(element, varDef));
		this.interpolationTime = interpolationTime;
		this.delay = delay;
		this.loopType = loopType;
		this.loops = loopType == InterpolationLoopType.NO_LOOP ? 1 : loops;
		this.interpolationType = interpolationType;
	}

	public InterpolationEf() {
		super();
		fields = new EAdList<EAdField<?>>();
		initialValues = new EAdList<EAdOperation>();
		endValues = new EAdList<EAdOperation>();
		delay = 0;
		relative = true;
		loops = 0;
		loopType = InterpolationLoopType.NO_LOOP;
		interpolationType = InterpolationType.LINEAR;
	}

	public InterpolationEf(EAdElement element, EAdVarDef<?> varDef,
			Number initialValue, Number endValue, int interpolationTime,
			int delay, InterpolationLoopType loopType, int loops,
			InterpolationType interpolationType) {
		this(element, varDef, new MathOp(initialValue + ""), new MathOp(
				endValue + ""), interpolationTime, delay, loopType, loops,
				interpolationType);
	}

	public InterpolationEf(EAdElement element, EAdVarDef<?> varDef,
			Number initialValue, Number endValue, int interpolationTime) {
		this(element, varDef, new MathOp(initialValue + ""), new MathOp(
				endValue + ""), interpolationTime, 0,
				InterpolationLoopType.NO_LOOP, 1, InterpolationType.LINEAR);
	}

	public InterpolationEf(EAdField<?> field, EAdOperation target,
			int interpolationTime) {
		this(field.getElement(), field.getVarDef(), new ValueOp(0), target,
				interpolationTime, 0, InterpolationLoopType.NO_LOOP, 0,
				InterpolationType.LINEAR);
	}

	public InterpolationEf(EAdField<?> field, float startValue, float endValue,
			int time) {
		this(field, startValue, endValue, time, InterpolationLoopType.NO_LOOP,
				InterpolationType.LINEAR);
	}

	public InterpolationEf(EAdField<?> field, float startValue, float endValue,
			int time, InterpolationLoopType loopType,
			InterpolationType interpolationType) {
		this(field.getElement(), field.getVarDef(), startValue, endValue, time,
				0, loopType, -1, interpolationType);
	}

	public InterpolationEf(BasicField<?> field, float start, float endValue,
			int time, InterpolationLoopType loopType) {
		this(field, start, endValue, time, loopType, InterpolationType.LINEAR);
	}

	public InterpolationEf(BasicField<?> field, int start, int end,
			int timeToFinish, InterpolationLoopType loopType) {
		this(field, (float) start, (float) end, timeToFinish, loopType);
	}

	public InterpolationEf(EAdField<Integer> field, MathOp start, MathOp end,
			int time, InterpolationLoopType loopType,
			InterpolationType interpolation) {
		this(field.getElement(), field.getVarDef(), start, end, time, 0,
				loopType, -1, interpolation);
	}

	public void setFields(EAdList<EAdField<?>> fields) {
		this.fields = fields;
	}

	public EAdList<EAdField<?>> getFields() {
		return fields;
	}

	public int getInterpolationTime() {
		return interpolationTime;
	}

	public void setInterpolationTime(int interpolationTime) {
		this.interpolationTime = interpolationTime;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public InterpolationLoopType getLoopType() {
		return loopType;
	}

	public void setLoopType(InterpolationLoopType loopType) {
		this.loopType = loopType;
	}

	public int getLoops() {
		return loops;
	}

	public void setLoops(int loops) {
		this.loops = loops;
	}

	public InterpolationType getInterpolationType() {
		return interpolationType;
	}

	public void setInterpolationType(InterpolationType interpolationType) {
		this.interpolationType = interpolationType;
	}

	public boolean isRelative() {
		return relative;
	}

	public void setRelative(boolean relative) {
		this.relative = relative;
	}

	public void addField(EAdField<?> field, EAdOperation target) {
		addField(field, new ValueOp(0), target);
	}

	public void addField(EAdField<?> field, EAdOperation initialValue,
			EAdOperation target) {
		fields.add(field);
		initialValues.add(initialValue);
		endValues.add(target);
	}

	public EAdList<EAdOperation> getInitialValues() {
		return initialValues;
	}

	public void setInitialValues(EAdList<EAdOperation> initialValues) {
		this.initialValues = initialValues;
	}

	public EAdList<EAdOperation> getEndValues() {
		return endValues;
	}

	public void setEndValues(EAdList<EAdOperation> endValues) {
		this.endValues = endValues;
	}
}
