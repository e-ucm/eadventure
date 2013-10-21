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

package es.eucm.ead.engine.gameobjects.debuggers;

import com.google.inject.Inject;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.factories.EventFactory;
import es.eucm.ead.engine.factories.SceneElementFactory;
import es.eucm.ead.engine.factories.TrajectoryFactory;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.engine.gameobjects.trajectories.TrajectoryGO;
import es.eucm.ead.engine.gameobjects.trajectories.polygon.PolygonTrajectoryGO;
import es.eucm.ead.model.assets.drawable.basics.Caption;
import es.eucm.ead.model.assets.drawable.basics.EAdShape;
import es.eucm.ead.model.assets.drawable.basics.shapes.BezierShape;
import es.eucm.ead.model.assets.drawable.basics.shapes.CircleShape;
import es.eucm.ead.model.assets.drawable.basics.shapes.LineShape;
import es.eucm.ead.model.assets.drawable.basics.shapes.RectangleShape;
import es.eucm.ead.model.assets.drawable.compounds.ComposedDrawable;
import es.eucm.ead.model.assets.drawable.compounds.EAdComposedDrawable;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.scenes.*;
import es.eucm.ead.model.elements.trajectories.*;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.fills.Paint;
import es.eucm.ead.model.params.paint.EAdPaint;
import es.eucm.ead.model.params.util.Rectangle;
import es.eucm.ead.tools.pathfinding.PathFinder;
import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;
import org.poly2tri.triangulation.point.TPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * 
 * A debugger showing representations of the trajectories in the game
 * 
 */
public class TrajectoryDebuggerGO extends SceneElementGO {

	private TrajectoryFactory trajectoryFactory;

	private Scene currentScene;

	private Trajectory currentTrajectory;

	private List<EAdShape> barriers;

	private List<Float> currentPath;

	private String currentPathString;

	private TrajectoryGO<? extends Trajectory> trajectoryGO;

	private SceneElementGO currentPathGO;

	@Inject
	public TrajectoryDebuggerGO(AssetHandler assetHandler,
			SceneElementFactory sceneElementFactory, Game game,
			EventFactory eventFactory, TrajectoryFactory trajectoryFactory) {
		super(assetHandler, sceneElementFactory, game, eventFactory);
		this.trajectoryFactory = trajectoryFactory;
		barriers = new ArrayList<EAdShape>();
	}

	public void setElement(SceneElement element) {
		super.setElement(element);
		SceneElement e = new SceneElement();
		currentPathGO = sceneElementFactory.get(e);
	}

	public void act(float delta) {
		super.act(delta);
		Scene newScene = (Scene) gui.getScene().getElement();
		Trajectory newTrajectory = gameState.getValue(currentScene,
				Scene.VAR_TRAJECTORY_DEFINITION, null);
		if (currentScene != newScene || newTrajectory != currentTrajectory) {
			currentTrajectory = newTrajectory;
			currentScene = newScene;
			getChildren().clear();
			trajectoryGO = null;
			if (currentTrajectory != null && currentScene != null) {
				createTrajectory();
				addSceneElement(currentPathGO);
				trajectoryGO = trajectoryFactory.get(currentTrajectory);
			}
		}

		if (currentTrajectory instanceof NodeTrajectory) {
			int i = 0;
			for (SceneElement e : ((NodeTrajectory) currentTrajectory)
					.getBarriers()) {
				barriers.get(i).setPaint(
						gameState.getValue(e, NodeTrajectory.VAR_BARRIER_ON,
								false) ? ColorFill.YELLOW
								: ColorFill.TRANSPARENT);
				i++;
			}
		}

		// Update current path
		if (trajectoryGO != null) {
			currentPath = trajectoryGO.getCurrentPath();
			if (currentPathString == null
					|| (!currentPath.toString().equals(currentPathString))) {
				currentPathString = currentPath.toString();
				currentPathGO.getChildren().clear();
				if (currentPath.size() > 0) {
					float lastX = currentPath.get(0);
					float lastY = currentPath.get(1);
					ComposedDrawable pathDrawable = new ComposedDrawable();
					for (int i = 2; i < currentPath.size(); i += 2) {
						float x = currentPath.get(i);
						float y = currentPath.get(i + 1);
						LineShape l = new LineShape((int) lastX, (int) lastY,
								(int) x, (int) y, 3);
						l.setPaint(Paint.BLACK_ON_WHITE);
						pathDrawable.addDrawable(l);
						lastX = x;
						lastY = y;
					}
					currentPathGO
							.addSceneElement(new SceneElement(pathDrawable));
				}
			}
		}
	}

	private void createTrajectory() {
		if (currentTrajectory instanceof SimpleTrajectory) {
			SceneElementGO representation = getSimpleTrajectory((SimpleTrajectory) currentTrajectory);
			addSceneElement(representation);
		} else if (currentTrajectory instanceof NodeTrajectory) {
			createNodes((NodeTrajectory) currentTrajectory);
			addInfluenceAreas(((Scene) gui.getScene().getElement())
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

	private SceneElementGO getSimpleTrajectory(SimpleTrajectory def) {

		SceneElementGO root = sceneElementFactory.get(new SceneElement());

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

	private void addInfluenceAreas(EAdList<SceneElement> sceneElements) {
		EAdPaint p = new ColorFill(0, 0, 200, 100);
		for (SceneElement sceneElement : sceneElements) {
			Rectangle rectangle = gameState.getValue(sceneElement,
					NodeTrajectory.VAR_INFLUENCE_AREA, null);
			if (rectangle != null) {
				RectangleShape shape = new RectangleShape(rectangle.getWidth(),
						rectangle.getHeight());
				shape.setPaint(p);

				SceneElement area = new SceneElement(shape);
				area.setVar(SceneElement.VAR_ENABLE, false);
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

		SceneElement mapElement = new SceneElement(map);
		mapElement.setInitialEnable(false);

		for (SceneElement e : trajectory.getBarriers()) {
			SceneElementDef def = e.getDefinition();
			EAdShape s = (EAdShape) def.getAsset(SceneElementDef.appearance);
			EAdShape barrier = (EAdShape) s.clone();
			barrier.setPaint(ColorFill.YELLOW);
			barriers.add(barrier);
			SceneElementGO go = (SceneElementGO) sceneElementFactory.get(e);
			map.addDrawable(barrier, (int) go.getX(), (int) go.getY());
		}

		for (SceneElement e : ((Scene) gui.getScene().getElement())
				.getSceneElements()) {
			Rectangle r = (Rectangle) e.getVars().get(
					NodeTrajectory.VAR_INFLUENCE_AREA);
			if (r != null) {
				RectangleShape influenceArea = new RectangleShape(r.width,
						r.height, new ColorFill(0, 0, 200, 122));
				map.addDrawable(influenceArea, r.x, r.y);
			}
		}

		addSceneElement(sceneElementFactory.get(mapElement));

	}

}
