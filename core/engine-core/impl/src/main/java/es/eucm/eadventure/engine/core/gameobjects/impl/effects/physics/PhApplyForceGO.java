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

package es.eucm.eadventure.engine.core.gameobjects.impl.effects.physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect;
import es.eucm.eadventure.common.model.effects.impl.physics.PhApplyImpluse;
import es.eucm.eadventure.common.model.effects.impl.physics.PhShape;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.AbstractEffectGO;
import es.eucm.eadventure.engine.core.operator.OperatorFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;

public class PhApplyForceGO extends AbstractEffectGO<PhApplyImpluse> {

	private OperatorFactory operatorFactory;

	@Inject
	public PhApplyForceGO(AssetHandler assetHandler,
			StringHandler stringsReader, SceneElementGOFactory gameObjectFactory,
			GUI gui, GameState gameState, OperatorFactory operatorFactory) {
		super(assetHandler, stringsReader, gameObjectFactory, gui, gameState);
		this.operatorFactory = operatorFactory;
	}

	@Override
	public void initilize() {
		super.initilize();
		float x = operatorFactory.operate(Float.class, element.getxForce());
		float y = operatorFactory.operate(Float.class, element.getyForce());
		ValueMap valueMap = gameState.getValueMap();
		Body b = valueMap.getValue(element.getSceneElement(),
				PhysicsEffectGO.VAR_PH_BODY);
		if (b != null) {
			b.applyLinearImpulse(new Vec2(x, y), b.getWorldCenter());
		} else {
			World w = valueMap.getValue(null, PhysicsEffectGO.VAR_PH_WORLD);
			if (w != null) {
				valueMap.setValue(element.getSceneElement(),
						EAdPhysicsEffect.VAR_PH_SHAPE, PhShape.CIRCULAR);
				valueMap.setValue(element.getSceneElement(),
						EAdPhysicsEffect.VAR_PH_RESTITUTION, 0.3f);
				valueMap.setValue(element.getSceneElement(),
						EAdPhysicsEffect.VAR_PH_FRICTION, 0.1f);
				valueMap.setValue(element.getSceneElement(),
						EAdPhysicsEffect.VAR_PH_DENSITY, 1.0f);
				PhysicsEffectGO.createBody(w, (EAdSceneElement) element.getSceneElement(),
						valueMap);
				b = valueMap.getValue(element.getSceneElement(),
						PhysicsEffectGO.VAR_PH_BODY);
				if (b != null) {
					b.setType(BodyType.DYNAMIC);
//					b.setLinearVelocity();
					b.applyForce(new Vec2(600, 100), b.getWorldCenter());
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
