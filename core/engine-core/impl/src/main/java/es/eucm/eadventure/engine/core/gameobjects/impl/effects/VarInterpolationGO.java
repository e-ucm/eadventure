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

package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.operator.OperatorFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

public class VarInterpolationGO extends
		AbstractEffectGO<EAdVarInterpolationEffect> {

	private static Logger logger = Logger.getLogger("VarInterpolationGO");

	private int currentTime;

	private int delay;

	private float interpolationLength;

	private boolean integer;

	private boolean finished;

	private OperatorFactory operatorFactory;

	private float startValue;

	private float endValue;

	private boolean reverse;

	@Inject
	public VarInterpolationGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration,
			OperatorFactory operatorFactory) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState,
				valueMap, platformConfiguration);
		this.operatorFactory = operatorFactory;
	}

	@Override
	public void initilize() {
		super.initilize();
		currentTime = 0;
		reverse = false;
		integer = element.getField().getVarDefinition().getType()
				.equals(Integer.class);
		startValue = ((Number) operatorFactory.operate(Float.class,
				element.getInitialValue())).floatValue();
		endValue = ((Number) operatorFactory.operate(Float.class,
				element.getEndValue())).floatValue();
		interpolationLength = endValue - startValue;
		finished = false;
		logger.info(element.getField() + " is going to be inerpolated from "
				+ startValue + " to " + endValue);
		delay = element.getDelay();

		operatorFactory.operate(element.getField(), element.getInitialValue());
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
		if (delay <= 0) {
			currentTime += GameLoop.SKIP_MILLIS_TICK;
			if (currentTime > element.getInterpolationTime()) {
				switch (element.getLoopType()) {
				case RESTART:
					while (currentTime > element.getInterpolationTime()) {
						currentTime -= element.getInterpolationTime();
					}
					break;
				case REVERSE:
					while (currentTime > element.getInterpolationTime()) {
						currentTime -= element.getInterpolationTime();
					}
					reverse = !reverse;
					break;
				default:
					currentTime = element.getInterpolationTime();
					finished = true;
					if (integer)
						valueMap.setValue(
								(EAdField<Integer>) element.getField(),
								Math.round(endValue));
					else
						valueMap.setValue((EAdField<Float>) element.getField(),
								(Float) endValue);

					logger.info(element.getField().toString() + " set to "
							+ endValue);
				}
			} else {

				// TODO this should be done "automatically"
				if (integer)
					valueMap.setValue((EAdField<Integer>) element.getField(),
							(Integer) interpolation());
				else
					valueMap.setValue((EAdField<Float>) element.getField(),
							(Float) interpolation());
			}
		} else {
			delay -= GameLoop.SKIP_MILLIS_TICK;
		}
	}

	public Object interpolation() {
		float f = 0;

		switch (element.getInterpolationType()) {
		case BOUNCE_END:
			f = bounceEndInterpolation();
			break;
		default:
			f = linearInterpolation();
		}

		if (integer)
			return new Integer(Math.round(f));
		else
			return new Float(f);

	}

	private float bounceEndInterpolation() {
		float linearLength = interpolationLength * 1.1f;
		float bounceLength = linearLength - interpolationLength;
		float bounceValue = startValue + linearLength;
		float linearTime = element.getInterpolationTime() * 0.98f;
		float bounceTime = element.getInterpolationTime() - linearTime;

		if (currentTime <= linearTime) {
			return startValue + ((float) currentTime / linearTime)
					* linearLength;
		} else {
			float timeToFinish = 1.0f
					- (element.getInterpolationTime() - currentTime)
					/ bounceTime;
			return bounceValue - bounceLength * timeToFinish;
		}

	}

	public float linearInterpolation() {
		float f = (float) currentTime / element.getInterpolationTime()
				* interpolationLength;

		if (reverse) {
			f = endValue - f;
		} else
			f += startValue;

		return f;
	}

}
