package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.EAdPhysicsEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdPhysicsEffect.PhShape;
import es.eucm.eadventure.common.model.effects.impl.EAdPhysicsEffect.PhType;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

public class PhysicsEffectGO extends AbstractEffectGO<EAdPhysicsEffect> {

	private World world;

	private ArrayList<Body> bodies = new ArrayList<Body>();

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
		world = new World(new Vec2(0, 10.0f), true);

		for (EAdSceneElement e : element.getElements()) {
			int x = valueMap.getValue(e, EAdBasicSceneElement.VAR_X);
			int y = valueMap.getValue(e, EAdBasicSceneElement.VAR_Y);
			int width = valueMap.getValue(e, EAdBasicSceneElement.VAR_WIDTH);
			int height = valueMap.getValue(e, EAdBasicSceneElement.VAR_HEIGHT);
			
			
			
			// TODO what if corner is not center?
			PhType phType = valueMap.getValue(e, EAdPhysicsEffect.VAR_PH_TYPE);
			PhShape phShape = valueMap.getValue(e, EAdPhysicsEffect.VAR_PH_SHAPE);
			
			Shape s = null;
			switch( phShape ){
			case CIRCULAR:
				// TODO
				break;
			default:
				s = new PolygonShape();
				((PolygonShape) s).setAsBox(width / 2, height / 2 );
				
			}
			
			BodyDef bd = new BodyDef();
			
			
			switch( phType ){
			case STATIC:
				bd.type = BodyType.STATIC;
				break;
			case DYNAMIC:
				bd.type = BodyType.DYNAMIC;
				break;
			}
			
			bd.position = new Vec2(x, y);

			FixtureDef fixture = new FixtureDef();
			fixture.shape = s;
			fixture.density = valueMap.getValue(e, EAdPhysicsEffect.VAR_PH_DENSITY);
			fixture.friction = valueMap.getValue(e, EAdPhysicsEffect.VAR_PH_FRICTION);
			fixture.restitution = valueMap.getValue(e, EAdPhysicsEffect.VAR_PH_RESTITUTION);

			Body body = world.createBody(bd);
			body.createFixture(fixture);

			MassData data = new MassData();
			data.mass = 1000.0f;

			body.setMassData(data);

			bodies.add(body);
		}

	}
	
	@Override
	public void update(GameState gameState) {
		super.update(gameState);

		world.step((float) GameLoop.SKIP_MILLIS_TICK / 200.0f, 6, 2);

		for (int i = 0; i < bodies.size(); i++) {

			Body body = bodies.get(i);
			EAdSceneElement e = element.getElements().get(i);

			valueMap.setValue(e, EAdBasicSceneElement.VAR_X, (int) body.getPosition().x);
			valueMap.setValue(e, EAdBasicSceneElement.VAR_Y, (int) body.getPosition().y);
			valueMap.setValue(e, EAdBasicSceneElement.VAR_ROTATION, body.getAngle());
		}
	}

	@Override
	public boolean isVisualEffect() {
		return true;
	}

	@Override
	public boolean isFinished() {
		return false;
	}

}
