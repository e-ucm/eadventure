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

	public EAdVarInterpolationEffect(String id, EAdVar<?> var, 
			LiteralExpressionOperation endValue, int time) {
		super(id);
		LiteralExpressionOperation startValue = new LiteralExpressionOperation("id", "[0]", var);
		setInterpolation(var, startValue, endValue, time, LoopType.NO_LOOP);
	}
	
	public EAdVarInterpolationEffect(String id, EAdVar<?> var2, float start, float end, int time, LoopType loop) {
		super(id);
		LiteralExpressionOperation startValue = new LiteralExpressionOperation("id", "" + start);
		LiteralExpressionOperation endValue = new LiteralExpressionOperation("id", "" + end);
		setInterpolation(var2, startValue, endValue, time, loop);
	}


	public EAdVarInterpolationEffect(String id,
			EAdVar<?> positionXVar,
			LiteralExpressionOperation literalExpressionOperation,
			LiteralExpressionOperation literalExpressionOperation2, int time,
			LoopType noLoop) {
		super(id);
		LiteralExpressionOperation startValue = new LiteralExpressionOperation("id", "[0]", var);
		setInterpolation(var, startValue, endValue, time, LoopType.NO_LOOP);
	}

	public void setInterpolation(EAdVar<?> var, LiteralExpressionOperation initialValue,
			LiteralExpressionOperation endValue, int time, LoopType loop) {
		this.var = var;
		this.initialValue = initialValue;
		this.endValue = endValue;
		this.interpolationTime = time;
		this.loopType = loop;
	}

	public void setInterpolation(EAdVar<?> var, float initialValue,
			float endValue, int time, LoopType loop) {
		setInterpolation(var, new LiteralExpressionOperation("id", "" + initialValue), new LiteralExpressionOperation("" + endValue), time, loop);
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

}
