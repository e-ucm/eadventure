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

package es.eucm.eadventure.plugin.box2d;

import java.util.ArrayList;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.resources.assets.drawable.impl.RectangleShape;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.AbstractEffectGO;

public class Box2DGO extends AbstractEffectGO<Box2DEffect> {

	@Inject
	private ValueMap valueMap;

	private ArrayList<Body> bodies = new ArrayList<Body>();

	private World world;

	@Override
	public void initilize() {
		super.initilize();
		AABB aabb = new AABB(new Vec2(0, 0), new Vec2(1000, 1000));
		world = new World(aabb, new Vec2(0, 10.0f), true);

		RectangleShape groundRectangle = (RectangleShape) element.ground
				.getAsset(element.ground.getInitialBundle(),
						EAdBasicSceneElement.appearance);

		// Ground
		{
			PolygonDef sd = new PolygonDef();
			sd.setAsBox(groundRectangle.getWidth() / 2.0f, groundRectangle.getHeight() / 2.0f);

			BodyDef bd = new BodyDef();
			bd.position = new Vec2(element.ground.getPosition().getX(),
					element.ground.getPosition().getY());
			world.createBody(bd).createShape(sd);
		}
		// Box

		for (EAdSceneElement e : element.box) {
			RectangleShape boxRectangle = (RectangleShape) e.getAsset(
					e.getInitialBundle(), EAdBasicSceneElement.appearance);

			PolygonDef sd = new PolygonDef();
			sd.setAsBox(boxRectangle.getWidth() /2.0f, boxRectangle.getHeight() / 2.0f);

			BodyDef bd = new BodyDef();

			sd.density = 1.0f;
			sd.friction = 0.9f;
			sd.restitution = 1.0f;

			bd.position = new Vec2(e.getPosition().getX(), e.getPosition()
					.getY());

			Body body = world.createBody(bd);
			body.createShape(sd);
			body.setMassFromShapes();

			bodies.add(body);
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

	@Override
	public void update(GameState gameState) {
		super.update(gameState);

		world.step((float) GameLoop.SKIP_MILLIS_TICK / 200.0f, 6);

		for (int i = 0; i < bodies.size(); i++) {

			Body body = bodies.get(i);
			EAdSceneElement e = element.box.get(i);

			valueMap.setValue(e.positionXVar(), (int) body.getPosition().x);
			valueMap.setValue(e.positionYVar(), (int) body.getPosition().y);
			valueMap.setValue(e.rotationVar(), body.getAngle());
		}
	}

}
