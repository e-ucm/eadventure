package es.eucm.eadventure.engine.core.gameobjects.impl.effects.physics;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect;
import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect.PhShape;
import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect.PhType;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.AbstractEffectGO;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

public class PhysicsEffectGO extends AbstractEffectGO<EAdPhysicsEffect> {

	private World world;

	private ArrayList<Body> bodies = new ArrayList<Body>();

	private float timeStep;

	public static final EAdVarDef<Body> VAR_PH_BODY = new EAdVarDefImpl<Body>(
			"ph_body", Body.class, null);

	@Inject
	public PhysicsEffectGO(AssetHandler assetHandler,
			StringHandler stringsReader, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringsReader, gameObjectFactory, gui, gameState,
				valueMap, platformConfiguration);
	}

	@Override
	public void initilize() {
		super.initilize();

		world = new World(new Vec2(0.0f, 10.0f), false);
		world.setContinuousPhysics(true);
		world.setWarmStarting(true);
		timeStep = 1.0f / (float) GameLoop.SKIP_MILLIS_TICK;

		for (EAdSceneElement e : element.getElements()) {
			int x = valueMap.getValue(e, EAdBasicSceneElement.VAR_X);
			int y = valueMap.getValue(e, EAdBasicSceneElement.VAR_Y);
			int width = valueMap.getValue(e, EAdBasicSceneElement.VAR_WIDTH);
			int height = valueMap.getValue(e, EAdBasicSceneElement.VAR_HEIGHT);

			// TODO what if corner is not center?
			PhType phType = valueMap.getValue(e, EAdPhysicsEffect.VAR_PH_TYPE);
			PhShape phShape = valueMap.getValue(e,
					EAdPhysicsEffect.VAR_PH_SHAPE);

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
			fixture.density = valueMap.getValue(e,
					EAdPhysicsEffect.VAR_PH_DENSITY);
			fixture.friction = valueMap.getValue(e,
					EAdPhysicsEffect.VAR_PH_FRICTION);
			fixture.restitution = valueMap.getValue(e,
					EAdPhysicsEffect.VAR_PH_RESTITUTION);

			Body body = world.createBody(bd);
			body.createFixture(fixture);

			body.resetMassData();

			valueMap.setValue(e, VAR_PH_BODY, body);
			bodies.add(body);
		}

	}

	@Override
	public void update(GameState gameState) {
		super.update(gameState);
		for (int i = 0; i < GameLoop.SKIP_MILLIS_TICK / 2; i++)
			world.step(timeStep, 8, 3);

		for (int i = 0; i < bodies.size(); i++) {

			Body b = bodies.get(i);
			EAdSceneElement e = element.getElements().get(i);

			valueMap.setValue(e, EAdBasicSceneElement.VAR_X,
					(int) b.getPosition().x);
			valueMap.setValue(e, EAdBasicSceneElement.VAR_Y,
					(int) b.getPosition().y);
			valueMap.setValue(e, EAdBasicSceneElement.VAR_ROTATION,
					b.getAngle());
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

}
