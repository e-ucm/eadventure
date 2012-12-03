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

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Cubic;
import aurelienribon.tweenengine.equations.Linear;

import com.google.inject.Inject;

import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.EAdOperation;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.TweenController;

public class InterpolationGO extends
		AbstractEffectGO<InterpolationEf> implements TweenCallback {

	private int finished;

	private TweenController tweenController;

	@Inject
	public InterpolationGO(SceneElementGOFactory gameObjectFactory,
			GUI gui, GameState gameState,
			TweenController tweenController) {
		super(gameObjectFactory, gui, gameState);
		this.tweenController = tweenController;
	}

	@Override
	public void initialize() {
		super.initialize();
		finished = 0;
		TweenEquation eq = Linear.INOUT;
		switch (element.getInterpolationType()) {
		case BOUNCE_END:
			eq = Bounce.OUT;
			break;
		case DESACCELERATE:
			eq = Cubic.OUT;
			break;
		default:
			eq = Linear.INOUT;
			break;

		}

		int i = 0;
		for (EAdField<?> f : element.getFields()) {
			EAdOperation op = element.getInitialValues().get(i);
			Number n1 = gameState.operate(Number.class, op);
			EAdOperation opR = element.getEndValues().get(i);
			Number n2 = gameState.operate(Number.class, opR);
			if (n1 != null && n2 != null) {
				float startValue = n1.floatValue();
				float endValue = n2.floatValue();
				Tween t = Tween
						.to(f, 0, element.getInterpolationTime())
						.ease(eq).delay(element.getDelay());

				switch (element.getLoopType()) {
				case RESTART:
					t.repeat(element.getLoops(), element.getDelay());
					break;
				case REVERSE:
					t.repeatYoyo(element.getLoops(),
							element.getDelay());
					break;
				default:

				}

				if (element.isRelative()) {
					t.targetRelative(endValue - startValue);
				} else {
					Tween.set(f, 0).target(startValue);
					t.target(endValue);
				}

				t.setCallback(this);
				tweenController.add(t);
				finished++;
			}
			i++;
		}

	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return finished == 0;
	}

	@Override
	public void onEvent(int arg0, BaseTween<?> arg1) {
		if (arg0 == END) {
			this.stop();
		} else if (arg0 == COMPLETE) {
			this.finished--;
		}
	}

}
