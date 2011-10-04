package es.eucm.eadventure.common.model.trajectories.impl;

import java.util.Random;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElement;

@Element(detailed = Node.class, runtime = Node.class)
public class Node implements EAdElement {

	@Param("id")
	private String id;

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

	public String getId() {

		return id;
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

	public Node copy() {
		Node n = new Node("node" + (new Random()).nextInt(10000), x, y,
				scale);
		return n;
	}

	@Override
	public EAdElement copy(boolean deepCopy) {
		return copy();
	}

}
