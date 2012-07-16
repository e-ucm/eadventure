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

package ead.engine.core.gameobjects.effects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ead.common.model.EAdElement;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.util.Interpolator;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.operators.OperatorFactory;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

public class InterpolationGO extends AbstractEffectGO<InterpolationEf> {

	private static final Logger logger = LoggerFactory
			.getLogger("VarInterpolationGO");

	private int currentTime;

	private int delay;

	private float interpolationLength;

	private boolean integer;

	private boolean finished;

	private OperatorFactory operatorFactory;

	private float startValue;

	private float endValue;

	private boolean reverse;

	private int loops;

	private EAdElement owner;

	private Interpolator interpolator;

	@Inject
	public InterpolationGO(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, OperatorFactory operatorFactory) {
		super(assetHandler, gameObjectFactory, gui, gameState);
		this.operatorFactory = operatorFactory;
	}

	@Override
	public void initialize() {
		super.initialize();
		currentTime = 0;
		loops = 0;
		reverse = false;
		integer = element.getVarDef().getType().equals(Integer.class);
		startValue = ((Number) operatorFactory.operate(Float.class,
				element.getInitialValue())).floatValue();
		endValue = ((Number) operatorFactory.operate(Float.class,
				element.getEndValue())).floatValue();

		float offset = ((Number) gameState.getValueMap().getValue(
				element.getElement(), element.getVarDef())).floatValue();
		startValue += offset;
		endValue += offset;
		interpolationLength = endValue - startValue;
		finished = false;
		logger.info("{}.{} is going to be interpolated from {} to {}",
				new Object[] { element.getElement(), element.getVarDef(),
						startValue, endValue });
		delay = element.getDelay();

		owner = element.getElement() == null ? parent : gameState.getValueMap()
				.getFinalElement(element.getElement());

		switch (element.getInterpolationType()) {
		case BOUNCE_END:
			interpolator = Interpolator.BOUNCE_END;
			break;
		case DESACCELERATE:
			interpolator = Interpolator.DESACCELERATE;
			break;
		default:
			interpolator = Interpolator.LINEAR;
			break;

		}
	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	public void update() {
		if (delay <= 0) {
			currentTime += gui.getSkippedMilliseconds();
			if (currentTime > element.getInterpolationTime()) {
				loops++;
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
						gameState.getValueMap().setValue(element.getElement(),
								element.getVarDef(), Math.round(endValue));
					else
						gameState.getValueMap().setValue(element.getElement(),
								element.getVarDef(), (Float) endValue);
				}
				finished |= (element.getLoops() > 0 && loops >= element
						.getLoops());
			} else {
				if (integer)
					gameState.getValueMap().setValue(owner,
							element.getVarDef(), interpolation().intValue());
				else
					gameState.getValueMap().setValue(owner,
							element.getVarDef(), interpolation().floatValue());
			}
		} else {
			delay -= gui.getSkippedMilliseconds();
			if (delay < 0) {
				currentTime -= delay;
			}
		}
	}

	public Number interpolation() {
		int time = reverse ? element.getInterpolationTime() - currentTime
				: currentTime;
		float f = interpolator.interpolate(time,
				element.getInterpolationTime(), interpolationLength);
		f += startValue;
		return (integer) ? new Integer(Math.round(f)) : new Float(f);
	}
}
