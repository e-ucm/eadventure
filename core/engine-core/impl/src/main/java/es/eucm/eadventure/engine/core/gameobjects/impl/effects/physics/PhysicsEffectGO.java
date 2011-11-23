package es.eucm.eadventure.engine.core.gameobjects.impl.effects.physics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect;
import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect.PhShape;
import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect.PhType;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.AbstractEffectGO;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;

public class PhysicsEffectGO extends AbstractEffectGO<EAdPhysicsEffect> {

	public static float WORLD_SCALE = 15.0f;
	
	private World world;

	private float timeStep;

	public static final EAdVarDef<Body> VAR_PH_BODY = new EAdVarDefImpl<Body>(
			"ph_body", Body.class, null);

	public static final EAdVarDef<World> VAR_PH_WORLD = new EAdVarDefImpl<World>(
			"ph_world", World.class, null);

	@Inject
	public PhysicsEffectGO(AssetHandler assetHandler,
			StringHandler stringsReader,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState) {
		super(assetHandler, stringsReader, gameObjectFactory, gui, gameState);
	}

	@Override
	public void initilize() {
		super.initilize();

		//doStep true = not simulate inactive bodies
		world = new World(new Vec2(0.0f, 10.0f), true);
		world.setContinuousPhysics(true);
		world.setWarmStarting(true);
		world.setAutoClearForces(true);
		ValueMap valueMap = gameState.getValueMap();
		valueMap.setValue(null, VAR_PH_WORLD, world);

		timeStep = 1.0f / (float) GameLoop.TICKS_PER_SECOND;

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
			jd.initialize(b2, b1, new Vec2(b1.getPosition().x,
					b1.getPosition().y));
			world.createJoint(jd);
		}

	}

	@Override
	public void update() {
		super.update();
		// FIXME this must depend on FPS
		//for (int i = 0; i < 3; i++)
			world.step(timeStep, 24, 8);

		EAdScene scene = gameState.getScene().getElement();

		if (scene != null) {
			for (EAdSceneElement e : scene.getElements()) {
				ValueMap valueMap = gameState.getValueMap();
				Body b = valueMap.getValue(e, VAR_PH_BODY);
				if (b != null) {

					valueMap.setValue(e, EAdBasicSceneElement.VAR_X,
							(int) (b.getWorldCenter().x * WORLD_SCALE));
					valueMap.setValue(e, EAdBasicSceneElement.VAR_Y,
							(int) (b.getWorldCenter().y * WORLD_SCALE));
					valueMap.setValue(e, EAdBasicSceneElement.VAR_ROTATION,
							b.getAngle());
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

	public static void createBody(World world, EAdSceneElementDef e,
			ValueMap valueMap) {
		float x = valueMap.getValue(e, EAdBasicSceneElement.VAR_X) / WORLD_SCALE;
		float y = valueMap.getValue(e, EAdBasicSceneElement.VAR_Y) / WORLD_SCALE;
		float width = valueMap.getValue(e, EAdBasicSceneElement.VAR_WIDTH) / WORLD_SCALE;
		float height = valueMap.getValue(e, EAdBasicSceneElement.VAR_HEIGHT) / WORLD_SCALE;

		// TODO what if corner is not center?
		PhType phType = valueMap.getValue(e, EAdPhysicsEffect.VAR_PH_TYPE);
		PhShape phShape = valueMap.getValue(e, EAdPhysicsEffect.VAR_PH_SHAPE);

		Shape s = null;
		switch (phShape) {
		case CIRCULAR:
			s = new CircleShape();
			s.m_radius = width / 2;
			break;
		default:
			s = new PolygonShape();
			((PolygonShape) s).setAsBox(width / 2, height / 2);

		}

		BodyDef bd = new BodyDef();

		switch (phType) {
		case STATIC:
			bd.type = BodyType.STATIC;
			break;
		case DYNAMIC:
			bd.type = BodyType.DYNAMIC;
			break;
		}

		bd.position.set(x, y);
		bd.angle = valueMap.getValue(e, EAdBasicSceneElement.VAR_ROTATION);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = s;
		fixture.density = valueMap.getValue(e, EAdPhysicsEffect.VAR_PH_DENSITY);
		fixture.friction = valueMap.getValue(e,
				EAdPhysicsEffect.VAR_PH_FRICTION);
		fixture.restitution = valueMap.getValue(e,
				EAdPhysicsEffect.VAR_PH_RESTITUTION);

		Body body = world.createBody(bd);
		body.createFixture(fixture);

		body.resetMassData();

		valueMap.setValue(e, VAR_PH_BODY, body);
	}

}
