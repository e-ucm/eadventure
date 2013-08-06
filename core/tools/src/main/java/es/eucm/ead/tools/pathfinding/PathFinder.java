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

package es.eucm.ead.tools.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.triangulation.TriangulationPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;
import org.poly2tri.triangulation.point.TPoint;

public class PathFinder extends AStar<DelaunayTriangle> {

	private Map<DelaunayTriangle, List<DelaunayTriangle>> sucessorsMap;

	private Polygon polygon;

	private List<DelaunayTriangle> allTriangles;

	private DelaunayTriangle endNode;

	public PathFinder() {
		allTriangles = new ArrayList<DelaunayTriangle>();
	}

	public void setPolygon(Polygon p) {
		this.polygon = p;
		Poly2Tri.triangulate(p);
		allTriangles.clear();
		// Fetch all triangles (including not interiors)
		for (DelaunayTriangle t : polygon.getTriangles()) {
			if (!allTriangles.contains(t)) {
				allTriangles.add(t);
			}
			for (DelaunayTriangle n : t.neighbors) {
				if (n != null && !allTriangles.contains(n)) {
					allTriangles.add(n);
				}
			}
		}

		sucessorsMap = new HashMap<DelaunayTriangle, List<DelaunayTriangle>>();
		for (DelaunayTriangle t : polygon.getTriangles()) {
			ArrayList<DelaunayTriangle> sucessors = new ArrayList<DelaunayTriangle>();
			for (DelaunayTriangle n : t.neighbors) {
				if (n != null && n.isInterior())
					sucessors.add(n);
			}
			sucessorsMap.put(t, sucessors);
		}
	}

	public List<Float> getPath(float startX, float startY, float endX,
			float endY) {
		ArrayList<Float> list = new ArrayList<Float>();
		list.add(startX);
		list.add(startY);

		if (!isPossibleStraightLine(startX, startY, endX, endY)) {
			DelaunayTriangle startNode = getTriangleIn(startX, startY);
			endNode = getTriangleIn(endX, endY);
			// Point out of trajectory
			if (endNode == null) {
				TriangulationPoint p = getNearestPoint(endX, endY);
				endNode = getTriangleIn(p.getXf(), p.getYf());
				endX = (int) p.getXf();
				endY = (int) p.getYf();
			}
			if (startNode != null && endNode != null) {
				if (startNode != endNode
						&& !isPossibleStraightLine(startX, startY, endX, endY)) {
					List<DelaunayTriangle> path = super.compute(startNode);
					path.remove(0);
					path.remove(0);
					float lastX = startX;
					float lastY = startY;
					for (DelaunayTriangle t : path) {
						TriangulationPoint p = t.nearest(lastX, lastY);
						list.add((float) p.getX());
						list.add((float) p.getY());
						lastX = p.getXf();
						lastY = p.getYf();
					}
				}
			}
		}
		list.add(endX);
		list.add(endY);
		return list;
	}

	private TriangulationPoint getNearestPoint(float endX, float endY) {
		TriangulationPoint p = null;
		float dist = 0;
		for (DelaunayTriangle t : polygon.getTriangles()) {
			TriangulationPoint p1 = t.nearest(endX, endY);
			float dist1 = (endX - p1.getXf()) * (endX - p1.getXf())
					+ (endY - p1.getYf()) * (endY - p1.getYf());
			if (p == null || dist1 < dist) {
				p = p1;
				dist = dist1;
			}
		}
		return p;
	}

	/**
	 * Returns if is possible a straight line from start to end
	 * 
	 * @return
	 */
	private boolean isPossibleStraightLine(float startX, float startY,
			float endX, float endY) {
		for (DelaunayTriangle t : allTriangles) {
			if (!t.isInterior() && t.intersects(startX, startY, endX, endY)) {
				return false;
			}
		}
		return true;
	}

	public DelaunayTriangle getTriangleIn(float x, float y) {
		TPoint p = new TPoint(x, y);
		for (DelaunayTriangle t : polygon.getTriangles()) {
			if (t.inside(p)) {
				return t;
			}
		}
		return null;
	}

	@Override
	protected boolean isGoal(DelaunayTriangle node) {
		return node == endNode;
	}

	@Override
	protected Double g(DelaunayTriangle from, DelaunayTriangle to) {
		double diffX = from.centroid().getX() - to.centroid().getX();
		double diffY = from.centroid().getY() - to.centroid().getY();
		return diffX * diffX + diffY * diffY;
	}

	@Override
	protected Double h(DelaunayTriangle from, DelaunayTriangle to) {
		return g(from, to);
	}

	@Override
	protected List<DelaunayTriangle> generateSuccessors(DelaunayTriangle node) {
		return sucessorsMap.get(node);
	}

	public Map<DelaunayTriangle, List<DelaunayTriangle>> getSucessorsMap() {
		return sucessorsMap;
	}

	public Polygon getPolygon() {
		return polygon;
	}

}
