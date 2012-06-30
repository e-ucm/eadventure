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

package ead.engine.core.debuggers;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.trajectories.EAdTrajectoryDefinition;
import ead.common.model.elements.trajectories.Node;
import ead.common.model.elements.trajectories.NodeTrajectoryDefinition;
import ead.common.model.elements.trajectories.Side;
import ead.common.model.elements.trajectories.SimpleTrajectoryDefinition;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.Paint;
import ead.common.params.paint.EAdPaint;
import ead.common.resources.assets.drawable.basics.shapes.BezierShape;
import ead.common.resources.assets.drawable.basics.shapes.CircleShape;
import ead.common.resources.assets.drawable.basics.shapes.LineShape;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.resources.assets.drawable.compounds.ComposedDrawable;
import ead.common.resources.assets.drawable.compounds.EAdComposedDrawable;
import ead.common.util.EAdPosition;
import ead.common.util.EAdRectangle;
import ead.engine.core.game.GameState;
import ead.engine.core.game.ValueMap;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.DrawableGO;

/**
 * 
 * A debugger showing representations of the trajectories in the game
 * 
 */
public class TrajectoryDebugger implements Debugger {

	private GameState gameState;

	private SceneElementGOFactory sceneElementFactory;

	private ValueMap valueMap;

	private EAdScene currentScene;

	private EAdTrajectoryDefinition currentTrajectory;

	private List<DrawableGO<?>> gameObjects;

	private List<BezierShape> barriers;

	@Inject
	public TrajectoryDebugger(GameState gameState, SceneElementGOFactory gameObjectFactory,
			ValueMap valueMap) {
		this.gameState = gameState;
		this.sceneElementFactory = gameObjectFactory;
		this.valueMap = valueMap;
		gameObjects = new ArrayList<DrawableGO<?>>();
		barriers = new ArrayList<BezierShape>();

	}

	@Override
	public List<DrawableGO<?>> getGameObjects() {
		if (currentScene != gameState.getScene().getElement()
				|| valueMap.getValue(currentScene, BasicScene.VAR_TRAJECTORY_DEFINITION) != currentTrajectory) {
			createTrajectory();
		}

		if (currentTrajectory instanceof NodeTrajectoryDefinition) {
			int i = 0;
			for (EAdSceneElement e : ((NodeTrajectoryDefinition) currentTrajectory).getBarriers()) {
				barriers.get(i)
						.setPaint(
								valueMap.getValue(e, NodeTrajectoryDefinition.VAR_BARRIER_ON) ? ColorFill.YELLOW
										: ColorFill.TRANSPARENT);
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
					BasicScene.VAR_TRAJECTORY_DEFINITION);

			if (currentTrajectory instanceof NodeTrajectoryDefinition) {
				createNodes((NodeTrajectoryDefinition) currentTrajectory);
				addInfluenceAreas(gameState.getScene().getElement().getSceneElements());
			} else if (currentTrajectory instanceof SimpleTrajectoryDefinition) {
				SimpleTrajectoryDefinition def = (SimpleTrajectoryDefinition) currentTrajectory;
				SceneElement area = new SceneElement(new RectangleShape(def.getRight()
						- def.getLeft(), def.getBottom() - def.getTop(), new ColorFill(0, 200, 0,
						100)));
				area.setId("walking_area");
				area.setPosition(def.getLeft(), def.getTop());
				gameObjects.add(sceneElementFactory.get(area));
			}
		}

	}

	private void addInfluenceAreas(EAdList<EAdSceneElement> sceneElements) {
		EAdPaint p = new ColorFill(0, 0, 200, 100);
		for (EAdSceneElement sceneElement : sceneElements) {
			EAdRectangle rectangle = gameState.getValueMap().getValue(sceneElement,
					NodeTrajectoryDefinition.VAR_INFLUENCE_AREA);
			if (rectangle != null) {
				RectangleShape shape = new RectangleShape(rectangle.getWidth(),
						rectangle.getHeight());
				shape.setPaint(p);

				SceneElement area = new SceneElement(shape);
				area.setVarInitialValue(SceneElement.VAR_ENABLE, false);
				area.setPosition(rectangle.getX(), rectangle.getY());
				gameObjects.add(sceneElementFactory.get(area));
			}
		}
	}

	private void createNodes(NodeTrajectoryDefinition trajectory) {
		EAdComposedDrawable map = new ComposedDrawable();
		for (Side s : trajectory.getSides()) {
			int x1 = trajectory.getNodeForId(s.getIdStart()).getX();
			int y1 = trajectory.getNodeForId(s.getIdStart()).getY();
			int x2 = trajectory.getNodeForId(s.getIdEnd()).getX();
			int y2 = trajectory.getNodeForId(s.getIdEnd()).getY();

			LineShape line = new LineShape(x1, y1, x2, y2, 4);
			line.setPaint(ColorFill.DARK_BROWN);
			map.addDrawable(line);

		}

		for (Node n : trajectory.getNodes()) {
			CircleShape circle = new CircleShape(n.getX(), n.getY(), 20, 20);
			ColorFill color = trajectory.getInitial() == n ? ColorFill.RED : ColorFill.BLUE;

			circle.setPaint(new Paint(color, ColorFill.BLACK, 2));
			map.addDrawable(circle);
		}

		SceneElement mapElement = new SceneElement(map);
		mapElement.setId("trajectoryMap");
		mapElement.setPropagateGUIEvents(true);

		for (EAdSceneElement e : trajectory.getBarriers()) {
			EAdSceneElementDef def = e.getDefinition();
			BezierShape s = (BezierShape) def.getAsset(def.getInitialBundle(),
					SceneElementDef.appearance);
			BezierShape barrier = (BezierShape) s.clone();
			barrier.setPaint(ColorFill.YELLOW);
			barriers.add(barrier);
			EAdPosition p = ((DrawableGO<?>) sceneElementFactory.get(e)).getPosition();
			map.addDrawable(barrier, p.getX(), p.getY());
		}

		gameObjects.add(sceneElementFactory.get(mapElement));

	}

	@Override
	public void setUp(EAdAdventureModel model) {

	}

}
