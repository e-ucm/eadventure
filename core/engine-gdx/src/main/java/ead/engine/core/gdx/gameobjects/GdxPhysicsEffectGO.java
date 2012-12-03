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

package ead.engine.core.gdx.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.google.inject.Inject;

import ead.common.model.elements.effects.enums.PhShape;
import ead.common.model.elements.effects.enums.PhType;
import ead.common.model.elements.effects.physics.PhysicsEffect;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.EAdVarDef;
import ead.common.model.elements.variables.VarDef;
import ead.engine.core.game.GameState;
import ead.engine.core.game.ValueMap;
import ead.engine.core.gameobjects.effects.AbstractEffectGO;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

public class GdxPhysicsEffectGO extends AbstractEffectGO<PhysicsEffect> {

	public static float WORLD_SCALE = 15.0f;

	private World world;

	private float timeStep;

	private int velocityIterations;

	private int positionIterations;

	public static final EAdVarDef<Body> VAR_PH_BODY = new VarDef<Body>(
			"ph_body", Body.class, null);

	public static final EAdVarDef<World> VAR_PH_WORLD = new VarDef<World>(
			"ph_world", World.class, null);

	@Inject
	public GdxPhysicsEffectGO(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState) {
		super(gameObjectFactory, gui, gameState);
	}

	@Override
	public void initialize() {
		super.initialize();

		// doStep true = not simulate inactive bodies
		world = new World(new Vector2(0.0f, 10.0f), true);
		world.setContinuousPhysics(true);
		world.setWarmStarting(true);
		world.setAutoClearForces(true);
		ValueMap valueMap = gameState;
		valueMap.setValue(null, VAR_PH_WORLD, world);

		velocityIterations = 24;
		positionIterations = 8;

		for (EAdSceneElement e : element.getElements()) {
			createBody(world, e, valueMap);
		}

		for (EAdSceneElement e : element.getJoints()) {
			createBody(world, e, valueMap);
		}

		RevoluteJointDef jd = new RevoluteJointDef();
		jd.collideConnected = false;

		for (int i = 0; i < element.getJoints().size() - 1; i += 2) {
			EAdSceneElement e1 = element.getJoints().get(i);
			EAdSceneElement e2 = element.getJoints().get(i + 1);
			Body b1 = valueMap.getValue(e1, VAR_PH_BODY);
			Body b2 = valueMap.getValue(e2, VAR_PH_BODY);
			jd.initialize(b2, b1, new Vector2(b1.getPosition().x, b1
					.getPosition().y));
			world.createJoint(jd);
		}

	}

	@Override
	public void update() {
		super.update();
		timeStep = 1.0f / gui.getTicksPerSecond();
		world.step(timeStep, velocityIterations, positionIterations);

		EAdScene scene = gameState.getScene().getElement();

		if (scene != null) {
			for (EAdSceneElement e : scene.getSceneElements()) {
				ValueMap valueMap = gameState;
				Body b = valueMap.getValue(e, VAR_PH_BODY);
				if (b != null) {

					valueMap.setValue(e, SceneElement.VAR_X, (int) (b
							.getWorldCenter().x * WORLD_SCALE));
					valueMap.setValue(e, SceneElement.VAR_Y, (int) (b
							.getWorldCenter().y * WORLD_SCALE));
					valueMap.setValue(e, SceneElement.VAR_ROTATION, b
							.getAngle());
				}
			}
		} else {
			stop();
		}
	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	public static void createBody(World world, EAdSceneElement e,
			ValueMap valueMap) {
		float x = valueMap.getValue(e, SceneElement.VAR_X) / WORLD_SCALE;
		float y = valueMap.getValue(e, SceneElement.VAR_Y) / WORLD_SCALE;
		float width = valueMap.getValue(e, SceneElement.VAR_WIDTH)
				/ WORLD_SCALE;
		float height = valueMap.getValue(e, SceneElement.VAR_HEIGHT)
				/ WORLD_SCALE;

		// TODO what if corner is not center?
		PhType phType = valueMap.getValue(e, PhysicsEffect.VAR_PH_TYPE);
		PhShape phShape = valueMap.getValue(e, PhysicsEffect.VAR_PH_SHAPE);

		Shape s = null;
		switch (phShape) {
		case CIRCULAR:
			s = new CircleShape();
			s.setRadius(width / 2);
			break;
		default:
			s = new PolygonShape();
			((PolygonShape) s).setAsBox(width / 2, height / 2);

		}

		BodyDef bd = new BodyDef();

		switch (phType) {
		case STATIC:
			bd.type = BodyType.StaticBody;
			break;
		case DYNAMIC:
			bd.type = BodyType.DynamicBody;
			break;
		}

		bd.position.set(x, y);
		bd.angle = valueMap.getValue(e, SceneElement.VAR_ROTATION);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = s;
		fixture.density = valueMap.getValue(e, PhysicsEffect.VAR_PH_DENSITY);
		fixture.friction = valueMap.getValue(e, PhysicsEffect.VAR_PH_FRICTION);
		fixture.restitution = valueMap.getValue(e,
				PhysicsEffect.VAR_PH_RESTITUTION);

		Body body = world.createBody(bd);
		body.createFixture(fixture);

		body.resetMassData();

		valueMap.setValue(e, VAR_PH_BODY, body);
	}

}
