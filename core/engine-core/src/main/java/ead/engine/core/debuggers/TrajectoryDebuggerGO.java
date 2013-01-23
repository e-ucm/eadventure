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
import java.util.Map.Entry;

import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;
import org.poly2tri.triangulation.point.TPoint;

import com.google.inject.Inject;

import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.scenes.GhostElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.trajectories.EAdTrajectory;
import ead.common.model.elements.trajectories.Node;
import ead.common.model.elements.trajectories.NodeTrajectory;
import ead.common.model.elements.trajectories.PolygonTrajectory;
import ead.common.model.elements.trajectories.Side;
import ead.common.model.elements.trajectories.SimpleTrajectory;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.Paint;
import ead.common.params.paint.EAdPaint;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.drawable.basics.EAdShape;
import ead.common.resources.assets.drawable.basics.shapes.BezierShape;
import ead.common.resources.assets.drawable.basics.shapes.CircleShape;
import ead.common.resources.assets.drawable.basics.shapes.LineShape;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.resources.assets.drawable.compounds.ComposedDrawable;
import ead.common.resources.assets.drawable.compounds.EAdComposedDrawable;
import ead.common.util.EAdRectangle;
import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.factories.TrajectoryFactory;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneElementGOImpl;
import ead.engine.core.gameobjects.trajectories.polygon.PolygonTrajectoryGO;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.tools.pathfinding.PathFinder;

/**
 * 
 * A debugger showing representations of the trajectories in the game
 * 
 */
public class TrajectoryDebuggerGO extends SceneElementGOImpl {

	private TrajectoryFactory trajectoryFactory;

	private EAdScene currentScene;

	private EAdTrajectory currentTrajectory;

	private List<EAdShape> barriers;

	@Inject
	public TrajectoryDebuggerGO(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory,
			TrajectoryFactory trajectoryFactory) {
		super(assetHandler, sceneElementFactory, gui, gameState, eventFactory);
		this.trajectoryFactory = trajectoryFactory;
		barriers = new ArrayList<EAdShape>();
	}

	public void update() {
		super.update();
		EAdScene newScene = (EAdScene) gui.getScene().getElement();
		EAdTrajectory newTrajectory = gameState.getValue(currentScene,
				BasicScene.VAR_TRAJECTORY_DEFINITION);
		if (currentScene != newScene || newTrajectory != currentTrajectory) {
			currentTrajectory = newTrajectory;
			currentScene = newScene;
			if (currentTrajectory != null && currentScene != null) {
				createTrajectory();
			}
		}

		if (currentTrajectory instanceof NodeTrajectory) {
			int i = 0;
			for (EAdSceneElement e : ((NodeTrajectory) currentTrajectory)
					.getBarriers()) {
				barriers
						.get(i)
						.setPaint(
								gameState.getValue(e,
										NodeTrajectory.VAR_BARRIER_ON) ? ColorFill.YELLOW
										: ColorFill.TRANSPARENT);
				i++;
			}
		}
	}

	private void createTrajectory() {
		getChildren().clear();
		if (currentTrajectory instanceof SimpleTrajectory) {
			SceneElementGO<?> representation = getSimpleTrajectory((SimpleTrajectory) currentTrajectory);
			addSceneElement(representation);
		} else if (currentTrajectory instanceof NodeTrajectory) {
			createNodes((NodeTrajectory) currentTrajectory);
			addInfluenceAreas(((EAdScene) gui.getScene().getElement())
					.getSceneElements());
		} else if (currentTrajectory instanceof PolygonTrajectory) {
			PolygonTrajectory def = (PolygonTrajectory) currentTrajectory;
			PolygonTrajectoryGO trajectoryGO = (PolygonTrajectoryGO) trajectoryFactory
					.get(def);
			PathFinder pathFinder = trajectoryGO.getPathFinder();
			// Add Polygon
			Polygon p = pathFinder.getPolygon();
			ComposedDrawable polygon = new ComposedDrawable();
			Paint paint = new Paint(new ColorFill(0, 200, 0, 100),
					ColorFill.GREEN);
			for (DelaunayTriangle t : p.getTriangles()) {
				BezierShape triangle = new BezierShape();
				triangle.setPaint(paint);
				triangle.moveTo((int) t.points[0].getX(), (int) t.points[0]
						.getY());
				triangle.lineTo((int) t.points[1].getX(), (int) t.points[1]
						.getY());
				triangle.lineTo((int) t.points[2].getX(), (int) t.points[2]
						.getY());
				polygon.addDrawable(triangle);
			}
			paint = new Paint(new ColorFill(200, 0, 0, 100), ColorFill.RED);
			for (DelaunayTriangle n : pathFinder.getPolygon().getTriangles()) {
				for (DelaunayTriangle t : n.neighbors) {
					if (t != null && !t.isInterior()) {
						BezierShape triangle = new BezierShape();
						triangle.setPaint(paint);
						triangle.moveTo((int) t.points[0].getX(),
								(int) t.points[0].getY());
						triangle.lineTo((int) t.points[1].getX(),
								(int) t.points[1].getY());
						triangle.lineTo((int) t.points[2].getX(),
								(int) t.points[2].getY());
						polygon.addDrawable(triangle);
					}
				}
			}
			SceneElement area = new SceneElement(polygon);
			addSceneElement(sceneElementFactory.get(area));

			ComposedDrawable centers = new ComposedDrawable();
			ComposedDrawable edges = new ComposedDrawable();
			ComposedDrawable graph = new ComposedDrawable();
			graph.addDrawable(edges);
			graph.addDrawable(centers);
			CircleShape center = new CircleShape(4, ColorFill.ORANGE);
			for (Entry<DelaunayTriangle, List<DelaunayTriangle>> entry : pathFinder
					.getSucessorsMap().entrySet()) {
				DelaunayTriangle t = entry.getKey();
				List<DelaunayTriangle> sucessors = entry.getValue();
				TPoint centroid = t.centroid();

				centers.addDrawable(center, (int) (centroid.getX() - 2),
						(int) (centroid.getY() - 2));

				for (DelaunayTriangle s : sucessors) {
					TPoint ncentroid = s.centroid();
					LineShape line = new LineShape((int) centroid.getX(),
							(int) centroid.getY(), (int) ncentroid.getX(),
							(int) ncentroid.getY(), 2);
					line.setPaint(ColorFill.BLACK);
					edges.addDrawable(line);
				}

			}

			SceneElement graphElement = new SceneElement(graph);
			addSceneElement(sceneElementFactory.get(graphElement));

		}
	}

	private SceneElementGO<?> getSimpleTrajectory(SimpleTrajectory def) {

		SceneElementGO<?> root = sceneElementFactory.get(new SceneElement());

		String text = "";

		if (def.isOnlyHorizontal()) {
			text += "/ Only horizontal";
		}

		if (def.isFreeWalk()) {
			text += "/ Free walk ";
		}

		if (!"".equals(text)) {
			SceneElement title = new SceneElement(new Caption(text));
			title.setPosition(10, 10);
			root.addSceneElement(sceneElementFactory.get(title));
		}

		if (def.isOnlyHorizontal() && !def.isFreeWalk()) {
			LineShape line = new LineShape(def.getLeft(), 40, def.getRight(),
					40, 5);
			line.setPaint(new ColorFill(0, 200, 0, 100));
			SceneElement lineElement = new SceneElement(line);
			root.addSceneElement(sceneElementFactory.get(lineElement));

		}

		if (!def.isOnlyHorizontal() && !def.isFreeWalk()) {
			if (def.getRight() > def.getLeft()
					&& def.getTop() < def.getBottom()) {
				RectangleShape area = new RectangleShape(def.getRight()
						- def.getLeft(), def.getBottom() - def.getTop(),
						new ColorFill(0, 200, 0, 100));
				SceneElement areaElement = new SceneElement(area);
				areaElement.setPosition(def.getLeft(), def.getTop());
				root.addSceneElement(sceneElementFactory.get(areaElement));
			}
		}

		return root;
	}

	private void addInfluenceAreas(EAdList<EAdSceneElement> sceneElements) {
		EAdPaint p = new ColorFill(0, 0, 200, 100);
		for (EAdSceneElement sceneElement : sceneElements) {
			EAdRectangle rectangle = gameState.getValue(sceneElement,
					NodeTrajectory.VAR_INFLUENCE_AREA);
			if (rectangle != null) {
				RectangleShape shape = new RectangleShape(rectangle.getWidth(),
						rectangle.getHeight());
				shape.setPaint(p);

				SceneElement area = new SceneElement(shape);
				area.setVarInitialValue(SceneElement.VAR_ENABLE, false);
				area.setPosition(rectangle.getX(), rectangle.getY());
				addSceneElement(sceneElementFactory.get(area));
			}
		}
	}

	private void createNodes(NodeTrajectory trajectory) {
		EAdComposedDrawable map = new ComposedDrawable();
		for (Side s : trajectory.getSides()) {
			int x1 = s.getIdStart().getX();
			int y1 = s.getIdStart().getY();
			int x2 = s.getIdEnd().getX();
			int y2 = s.getIdEnd().getY();

			LineShape line = new LineShape(x1, y1, x2, y2, 4);
			line.setPaint(ColorFill.DARK_BROWN);
			map.addDrawable(line);

		}

		for (Node n : trajectory.getNodes()) {
			CircleShape circle = new CircleShape(20);
			ColorFill color = trajectory.getInitial() == n ? ColorFill.RED
					: ColorFill.BLUE;

			circle.setPaint(new Paint(color, ColorFill.BLACK, 2));
			map.addDrawable(circle, n.getX() - 20, n.getY() - 20);
		}

		SceneElement mapElement = new GhostElement(map, null);
		mapElement.setInitialEnable(false);

		for (EAdSceneElement e : trajectory.getBarriers()) {
			EAdSceneElementDef def = e.getDefinition();
			EAdShape s = (EAdShape) def.getAsset(SceneElementDef.appearance);
			EAdShape barrier = (EAdShape) s.clone();
			barrier.setPaint(ColorFill.YELLOW);
			barriers.add(barrier);
			SceneElementGO<?> go = (SceneElementGO<?>) sceneElementFactory
					.get(e);
			map.addDrawable(barrier, go.getX(), go.getY());
		}

		addSceneElement(sceneElementFactory.get(mapElement));

	}

}
