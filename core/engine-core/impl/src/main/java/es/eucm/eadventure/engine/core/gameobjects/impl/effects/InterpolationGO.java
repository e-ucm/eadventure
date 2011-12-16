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

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.impl.EAdInterpolationEffect;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.util.impl.EAdInterpolator;
import es.eucm.eadventure.engine.core.game.GameLoop;
import es.eucm.eadventure.engine.core.game.GameState;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.operator.OperatorFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;

public class InterpolationGO extends AbstractEffectGO<EAdInterpolationEffect> {

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

	private int loops;

	private EAdElement owner;

	private EAdInterpolator interpolator;

	@Inject
	public InterpolationGO(AssetHandler assetHandler,
			StringHandler stringHandler, SceneElementGOFactory gameObjectFactory,
			GUI gui, GameState gameState, OperatorFactory operatorFactory) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState);
		this.operatorFactory = operatorFactory;
	}

	@Override
	public void initilize() {
		super.initilize();
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
		logger.info(element.getElement() + "." + element.getVarDef()
				+ " is going to be inerpolated from " + startValue + " to "
				+ endValue);
		delay = element.getDelay();

		owner = element.getElement() == null ? parent : gameState.getValueMap()
				.getFinalElement(element.getElement());

		switch (element.getInterpolationType()) {
		case BOUNCE_END:
			interpolator = EAdInterpolator.BOUNCE_END;
			break;
		case ACCELERATE:
			interpolator = EAdInterpolator.ACCELERATE;
			break;
		case DESACCELERATE:
			interpolator = EAdInterpolator.DESACCELERATE;
		default:
			interpolator = EAdInterpolator.LINEAR;
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

	public void update() {
		if (delay <= 0) {
			currentTime += GameLoop.SKIP_MILLIS_TICK;
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

				// TODO this should be done "automatically"
				if (integer)
					gameState.getValueMap().setValue(owner,
							element.getVarDef(), (Integer) interpolation());
				else
					gameState.getValueMap().setValue(owner,
							element.getVarDef(), (Float) interpolation());
			}
		} else {
			delay -= GameLoop.SKIP_MILLIS_TICK;
			if ( delay < 0 ){
				currentTime -= delay;
			}
		}
	}

	public Object interpolation() {
		int time = reverse ? element.getInterpolationTime() - currentTime : currentTime;
		float f = interpolator.interpolate(time,
				element.getInterpolationTime(), interpolationLength);
		
		f += startValue;

		if (integer)
			return new Integer(Math.round(f));
		else
			return new Float(f);

	}

}
