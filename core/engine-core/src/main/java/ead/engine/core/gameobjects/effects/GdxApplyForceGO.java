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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.google.inject.Inject;

import ead.common.model.elements.effects.enums.PhShape;
import ead.common.model.elements.effects.physics.PhApplyImpulseEf;
import ead.common.model.elements.effects.physics.PhysicsEffect;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.game.interfaces.ValueMap;

public class GdxApplyForceGO extends AbstractEffectGO<PhApplyImpulseEf> {

	@Inject
	public GdxApplyForceGO(GameState gameState) {
		super(gameState);
	}

	@Override
	public void initialize() {
		super.initialize();
		float x = gameState.operate(Float.class, effect.getxForce());
		float y = gameState.operate(Float.class, effect.getyForce());
		ValueMap valueMap = gameState;
		Object finalElement = valueMap.maybeDecodeField(effect
				.getSceneElement());
		Body b = valueMap
				.getValue(finalElement, GdxPhysicsEffectGO.VAR_PH_BODY);
		if (b != null) {
			b.applyForce(new Vector2(x, y), b.getWorldCenter());
		} else {
			World w = valueMap.getValue(null, GdxPhysicsEffectGO.VAR_PH_WORLD);
			if (w != null) {
				valueMap.setValue(finalElement, PhysicsEffect.VAR_PH_SHAPE,
						PhShape.CIRCULAR);
				valueMap.setValue(finalElement,
						PhysicsEffect.VAR_PH_RESTITUTION, 0.3f);
				valueMap.setValue(finalElement, PhysicsEffect.VAR_PH_FRICTION,
						0.1f);
				valueMap.setValue(finalElement, PhysicsEffect.VAR_PH_DENSITY,
						1.0f);
				GdxPhysicsEffectGO.createBody(w, (EAdSceneElement) effect
						.getSceneElement(), valueMap);
				b = valueMap.getValue(finalElement,
						GdxPhysicsEffectGO.VAR_PH_BODY);
				if (b != null) {
					b.setType(BodyType.DynamicBody);
					b.applyForce(new Vector2(x, y), b.getWorldCenter());
				}
			}
		}
	}

}
