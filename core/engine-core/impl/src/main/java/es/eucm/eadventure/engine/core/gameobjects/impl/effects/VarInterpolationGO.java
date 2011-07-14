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

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.model.variables.impl.vars.IntegerVar;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.OperatorFactory;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

public class VarInterpolationGO extends
		AbstractEffectGO<EAdVarInterpolationEffect> {

	private int currentTime;

	private float interpolationLength;

	private boolean integer;

	private boolean finished;
	
	private OperatorFactory operatorFactory;

	private float startValue;

	@Inject
	public VarInterpolationGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration, OperatorFactory operatorFactory) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);
		this.operatorFactory = operatorFactory;
	}

	@Override
	public void initilize() {
		super.initilize();
		currentTime = 0;
		integer = element.getVar().getType().equals(Integer.class);
		startValue = operatorFactory.operate(new IntegerVar(" "), element.getInitialValue());
		float endValue = operatorFactory.operate(new IntegerVar(" "), element.getEndValue());
		interpolationLength = endValue - startValue;
		finished = false;

		operatorFactory.operate(element.getVar(), element.getInitialValue());
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

		//TODO this should be done "automatically"
		if (integer)
			valueMap.setValue((EAdVar<Integer>) element.getVar(),
					(Integer) interpolation());
		else
			valueMap.setValue((EAdVar<Float>) element.getVar(),
					(Float) interpolation());
	}

	public Object interpolation() {
		float f = startValue + (float) currentTime / element.getInterpolationTime() * interpolationLength;
		if (integer)
			return new Integer((int) f);
		else
			return new Float(f);

	}

}
