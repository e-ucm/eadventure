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

package es.eucm.eadventure.engine.core.debuggers.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;
import es.eucm.eadventure.common.model.trajectories.impl.Node;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.model.trajectories.impl.Side;
import es.eucm.eadventure.common.model.trajectories.impl.SimpleTrajectoryDefinition;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdPaintImpl;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.EAdRectangle;
import es.eucm.eadventure.common.params.paint.EAdPaint;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.CircleShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.LineShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.ComposedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.impl.ComposedDrawableImpl;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.debuggers.EAdDebugger;
import es.eucm.eadventure.engine.core.gameobjects.DrawableGO;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;

@Singleton
public class TrajectoryDebugger implements EAdDebugger {

	private GameState gameState;

	private SceneElementGOFactory sceneElementFactory;

	private ValueMap valueMap;

	private EAdScene currentScene;

	private TrajectoryDefinition currentTrajectory;

	private List<DrawableGO<?>> gameObjects;

	private List<BezierShape> barriers;

	@Inject
	public TrajectoryDebugger(GameState gameState,
			SceneElementGOFactory gameObjectFactory, ValueMap valueMap) {
		this.gameState = gameState;
		this.sceneElementFactory = gameObjectFactory;
		this.valueMap = valueMap;
		gameObjects = new ArrayList<DrawableGO<?>>();
		barriers = new ArrayList<BezierShape>();

	}

	@Override
	public List<DrawableGO<?>> getGameObjects() {
		if (currentScene != gameState.getScene().getElement()
				|| valueMap.getValue(currentScene,
						EAdSceneImpl.VAR_TRAJECTORY_DEFINITION) != currentTrajectory) {
			createTrajectory();
		}

		if (currentTrajectory instanceof NodeTrajectoryDefinition) {
			int i = 0;
			for (EAdSceneElement e : ((NodeTrajectoryDefinition) currentTrajectory)
					.getBarriers()) {
				barriers.get(i)
						.setPaint(
								valueMap.getValue(e,
										NodeTrajectoryDefinition.VAR_BARRIER_ON) ? EAdColor.YELLOW
										: EAdColor.TRANSPARENT);
				i++;
			}
		}

		return gameObjects;
	}

	private void createTrajectory() {
		gameObjects.clear();
		currentScene = gameState.getScene().getElement();

		if (currentScene != null) {
			currentTrajectory = valueMap.getValue(currentScene,
					EAdSceneImpl.VAR_TRAJECTORY_DEFINITION);

			if (currentTrajectory instanceof NodeTrajectoryDefinition) {
				createNodes((NodeTrajectoryDefinition) currentTrajectory);
				addInfluenceAreas(gameState.getScene().getElement()
						.getComponents());
			} else if (currentTrajectory instanceof SimpleTrajectoryDefinition) {
				SimpleTrajectoryDefinition def = (SimpleTrajectoryDefinition) currentTrajectory;
				EAdBasicSceneElement area = new EAdBasicSceneElement(
						new RectangleShape(def.getRight() - def.getLeft(), def
								.getBottom() - def.getTop(), new EAdColor(0,
								200, 0, 100)));
				area.setId("walking_area");
				area.setPosition(def.getLeft(), def.getTop());
				gameObjects.add(sceneElementFactory.get(area));
			}
		}

	}

	private void addInfluenceAreas(EAdList<EAdSceneElement> sceneElements) {
		EAdPaint p = new EAdColor(0, 0, 200, 100);
		for (EAdSceneElement sceneElement : sceneElements) {
			EAdRectangle rectangle = gameState.getValueMap().getValue(
					sceneElement, NodeTrajectoryDefinition.VAR_INFLUENCE_AREA);
			if (rectangle != null) {
				RectangleShape shape = new RectangleShape(
						rectangle.getWidth(), rectangle.getHeight());
				shape.setPaint(p);
				
				EAdBasicSceneElement area = new EAdBasicSceneElement(shape);
				area.setVarInitialValue(EAdBasicSceneElement.VAR_ENABLE, false);
				area.setPosition(rectangle.getX(), rectangle.getY());
				gameObjects.add(sceneElementFactory.get(area));
			}
		}
	}

	private void createNodes(NodeTrajectoryDefinition trajectory) {
		ComposedDrawable map = new ComposedDrawableImpl();
		for (Side s : trajectory.getSides()) {
			int x1 = trajectory.getNodeForId(s.getIdStart()).getX();
			int y1 = trajectory.getNodeForId(s.getIdStart()).getY();
			int x2 = trajectory.getNodeForId(s.getIdEnd()).getX();
			int y2 = trajectory.getNodeForId(s.getIdEnd()).getY();

			LineShape line = new LineShape(x1, y1, x2, y2, 4);
			line.setPaint(EAdColor.DARK_BROWN);
			map.addDrawable(line);

		}

		for (Node n : trajectory.getNodes()) {
			CircleShape circle = new CircleShape(n.getX(), n.getY(), 20, 20);
			EAdColor color = trajectory.getInitial() == n ? EAdColor.RED
					: EAdColor.BLUE;

			circle.setPaint(new EAdPaintImpl(color, EAdColor.BLACK, 2));
			map.addDrawable(circle);
		}

		EAdBasicSceneElement mapElement = new EAdBasicSceneElement(map);
		mapElement.setId("trajectoryMap");

		for (EAdSceneElement e : trajectory.getBarriers()) {
			EAdSceneElementDef def = e.getDefinition();
			BezierShape s = (BezierShape) def.getAsset(def.getInitialBundle(),
					EAdSceneElementDefImpl.appearance);
			BezierShape barrier = (BezierShape) s.clone();
			barrier.setPaint(EAdColor.YELLOW);
			barriers.add(barrier);
			EAdPosition p = ((DrawableGO<?>) sceneElementFactory.get(e))
					.getPosition();
			map.addDrawable(barrier, p.getX(), p.getY());
		}

		gameObjects.add(sceneElementFactory.get(mapElement));

	}

}
