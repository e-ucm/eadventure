package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;

public class VarInterpolationGO extends
		AbstractEffectGO<EAdVarInterpolationEffect> {

	private int currentTime;
	
	private float interpolationLength;

	private boolean integer;

	private ValueMap valueMap;

	@Inject
	public VarInterpolationGO(ValueMap valueMap) {
		this.valueMap = valueMap;
	}

	@Override
	public void initilize() {
		currentTime = 0;
		integer = element.getVar().getClass().equals(Integer.class);
		interpolationLength = element.getEndValue() - element.getInitialValue();

	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@SuppressWarnings("unchecked")
	public void update(GameState gameSate) {
		currentTime += GameLoop.SKIP_MILLIS_TICK;
		while (currentTime > element.getInterpolationTime()) {
			currentTime -= element.getInterpolationTime();
		}

		if (integer)
			valueMap.setValue((EAdVar<Integer>) element.getVar(),
					(Integer) interpolation());
		else
			valueMap.setValue((EAdVar<Float>) element.getVar(),
					(Float) interpolation());
	}

	public Object interpolation() {
		float f = element.getInitialValue() + currentTime * interpolationLength / element.getInterpolationTime();
		if ( integer )
			return new Integer( (int) f );
		else
			return new Float( f );

	}

}
