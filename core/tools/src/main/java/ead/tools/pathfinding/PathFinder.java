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

package ead.tools.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.Polygon;
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

	public List<Integer> getPath(int startX, int startY, int endX, int endY) {
		ArrayList<Integer> list = new ArrayList<Integer>();

		if (!isPossibleStraightLine(startX, startY, endX, endY)) {
			DelaunayTriangle startNode = getTriangleIn(startX, startY);
			endNode = getTriangleIn(endX, endY);
			if (startNode != null && endNode != null) {
				if (startNode != endNode
						&& !isPossibleStraightLine(startX, startY, endX, endY)) {
					List<DelaunayTriangle> path = super.compute(startNode);
					for (DelaunayTriangle t : path) {
						list.add((int) t.centroid().getX());
						list.add((int) t.centroid().getY());
					}
				}
			}
		}
		list.add(endX);
		list.add(endY);
		return list;
	}

	/**
	 * Returns if is possible a straight line from start to end
	 * 
	 * @return
	 */
	private boolean isPossibleStraightLine(int startX, int startY, int endX,
			int endY) {
		for (DelaunayTriangle t : allTriangles) {
			if (!t.isInterior() && t.intersects(startX, startY, endX, endY)) {
				return false;
			}
		}
		return true;
	}

	public DelaunayTriangle getTriangleIn(int x, int y) {
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
