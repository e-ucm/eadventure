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

package ead.common.model.elements.trajectories;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.BasicElement;
import java.util.Random;

@Element(detailed = Node.class, runtime = Node.class)
public class Node extends BasicElement {

	@Param("x")
	private int x;

	@Param("y")
	private int y;

	@Param("scale")
	private float scale;

	public Node(){

	}

	public Node(String id, int x, int y, float scale) {

		this.id = id;
		this.x = x;
		this.y = y;
		this.scale = scale;
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}


	public int getX() {

		return x;
	}

	public int getY() {

		return y;
	}

	public float getScale() {

		return scale;
	}

	public void setScale(float scale) {

		this.scale = scale;
	}

	@Override
	public boolean equals(Object o) {

		if (o == null)
			return false;
		if (o instanceof Node) {
			Node node = (Node) o;
			if (node.id.equals(this.id))
				return true;
			if (node.x == this.x && node.y == this.y)
				return true;
		}
		return false;
	}

	public void setValues(int x, int y, float scale) {
		this.x = x;
		this.y = y;
		this.scale = scale;
	}

	public int hashCode( ){
		return id.hashCode();
	}

	public Node copy() {
		Node n = new Node("node" + (new Random()).nextInt(10000), x, y,
				scale);
		return n;
	}

}
