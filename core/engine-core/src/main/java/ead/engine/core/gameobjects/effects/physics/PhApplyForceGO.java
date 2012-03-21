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

package ead.engine.core.gameobjects.effects.physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import com.google.inject.Inject;

import ead.common.model.elements.effects.enums.PhShape;
import ead.common.model.elements.effects.physics.PhApplyImpluseEf;
import ead.common.model.elements.effects.physics.PhysicsEffect;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.util.StringHandler;
import ead.engine.core.game.GameState;
import ead.engine.core.game.ValueMap;
import ead.engine.core.gameobjects.effects.AbstractEffectGO;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.operators.OperatorFactory;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

public class PhApplyForceGO extends AbstractEffectGO<PhApplyImpluseEf> {

	private OperatorFactory operatorFactory;

	@Inject
	public PhApplyForceGO(AssetHandler assetHandler,
			StringHandler stringsReader, SceneElementGOFactory gameObjectFactory,
			GUI gui, GameState gameState, OperatorFactory operatorFactory) {
		super(assetHandler, stringsReader, gameObjectFactory, gui, gameState);
		this.operatorFactory = operatorFactory;
	}

	@Override
	public void initialize() {
		super.initialize();
		float x = operatorFactory.operate(Float.class, element.getxForce());
		float y = operatorFactory.operate(Float.class, element.getyForce());
		ValueMap valueMap = gameState.getValueMap();
		Body b = valueMap.getValue(element.getSceneElement(),
				PhysicsEffectGO.VAR_PH_BODY);
		if (b != null) {
			b.applyForce(new Vec2(x, y), b.getWorldCenter());
		} else {
			World w = valueMap.getValue(null, PhysicsEffectGO.VAR_PH_WORLD);
			if (w != null) {
				valueMap.setValue(element.getSceneElement(),
						PhysicsEffect.VAR_PH_SHAPE, PhShape.CIRCULAR);
				valueMap.setValue(element.getSceneElement(),
						PhysicsEffect.VAR_PH_RESTITUTION, 0.3f);
				valueMap.setValue(element.getSceneElement(),
						PhysicsEffect.VAR_PH_FRICTION, 0.1f);
				valueMap.setValue(element.getSceneElement(),
						PhysicsEffect.VAR_PH_DENSITY, 1.0f);
				PhysicsEffectGO.createBody(w, (EAdSceneElement) element.getSceneElement(),
						valueMap);
				b = valueMap.getValue(element.getSceneElement(),
						PhysicsEffectGO.VAR_PH_BODY);
				if (b != null) {
					b.setType(BodyType.DYNAMIC);
					b.applyForce(new Vec2(x, y), b.getWorldCenter());
				}
			}
		}
	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return true;
	}

}
