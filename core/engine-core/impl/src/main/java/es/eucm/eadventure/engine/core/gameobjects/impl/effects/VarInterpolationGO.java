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

	private boolean finished;

	@Inject
	public VarInterpolationGO(ValueMap valueMap) {
		this.valueMap = valueMap;
	}

	@Override
	public void initilize() {
		super.initilize();
		currentTime = 0;
		integer = element.getVar().getType().equals(Integer.class);
		interpolationLength = element.getEndValue() - element.getInitialValue();
		finished = false;

	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	@SuppressWarnings("unchecked")
	public void update(GameState gameSate) {
		currentTime += GameLoop.SKIP_MILLIS_TICK;
		if (currentTime > element.getInterpolationTime()) {
			switch (element.getLoopType()) {
			// TODO Backwards
			case RESTART:
				while (currentTime > element.getInterpolationTime()) {
					currentTime -= element.getInterpolationTime();
				}
				break;
			default:
				currentTime = element.getInterpolationTime();
				finished = true;
				break;
			}
		}

		if (integer)
			valueMap.setValue((EAdVar<Integer>) element.getVar(),
					(Integer) interpolation());
		else
			valueMap.setValue((EAdVar<Float>) element.getVar(),
					(Float) interpolation());
	}

	public Object interpolation() {
		float f = element.getInitialValue() + (float) currentTime / element.getInterpolationTime() * interpolationLength;
		if (integer)
			return new Integer((int) f);
		else
			return new Float(f);

	}

}
