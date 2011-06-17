package es.eucm.eadventure.common.model.effects.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.model.variables.impl.vars.NumberVar;

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
	private NumberVar<?> var;

	@Param("initialValue")
	private float initialValue;

	@Param("endValue")
	private float endValue;

	@Param("time")
	private int interpolationTime;
	
	@Param("loop")
	private LoopType loopType;

	public EAdVarInterpolationEffect(String id) {
		super(id);
	}

	public void setInterpolation(NumberVar<?> var, float initialValue,
			float endValue, int time, LoopType loop) {
		this.var = var;
		this.initialValue = initialValue;
		this.endValue = endValue;
		this.interpolationTime = time;
		this.loopType = loop;
	}

	public NumberVar<?> getVar() {
		return var;
	}

	public float getInitialValue() {
		return initialValue;
	}

	public float getEndValue() {
		return endValue;
	}

	public int getInterpolationTime() {
		return interpolationTime;
	}

	public LoopType getLoopType() {
		return loopType;
	}

}
