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

package ead.engine.gl.utils;

import java.util.ArrayList;
import java.util.List;

import ead.common.resources.assets.drawable.EAdDrawable;

public class BitmapNode {
	
	public BitmapNode leftNode;
	public BitmapNode rightNode;
	public int left, top, right, bottom;
	public EAdDrawable drawable = null;
	
	public BitmapNode( ){
		this( 1, 1 );
	}
	
	public BitmapNode( int width, int height ){
		left = 0;
		top = 0;
		right = width - 1;
		bottom = height - 1;
	}

	public BitmapNode insert(EAdDrawable drawable, int width, int height) {
		BitmapNode result = null;
		if (!isLeaf()) {
			if (leftNode != null) {
				result = leftNode.insert(drawable, width, height);
			}

			if (result == null && rightNode != null) {
				result = rightNode.insert(drawable, width, height);
			}
		} else {
			if (this.drawable != null) {
				return null;
			}

			if (left + width - 1 > right || top + height - 1 > bottom) {
				return null;
			}

			if (left + width - 1 == right && top + height - 1 == bottom) {
				this.drawable = drawable;
				return this;
			}

			leftNode = new BitmapNode();
			rightNode = new BitmapNode();

			int dw = (right - left) - width;
			int dh = (bottom - top) - height;

			if (dw > dh) {
				leftNode.setBounds(left, top, left + width - 1, bottom);
				rightNode.setBounds(left + width, top, right, bottom);
			} else {
				leftNode.setBounds(left, top, right, top + height - 1);
				rightNode.setBounds(left, top + height, right, bottom);
			}

			return leftNode.insert(drawable, width, height);

		}
		return result;
	}

	public void setBounds(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public boolean isLeaf() {
		return leftNode == null && rightNode == null;
	}
	
	public ArrayList<BitmapNode> getNodes() {
		ArrayList<BitmapNode> nodes = new ArrayList<BitmapNode>();
		getIds(this, nodes);
		return nodes;
	}

	private void getIds(BitmapNode node, List<BitmapNode> ids) {
		if (node.isLeaf() && node.drawable != null) {
			ids.add(node);
		} else {
			if (node.leftNode != null) {
				getIds(node.leftNode, ids);
			}

			if (node.rightNode != null) {
				getIds(node.rightNode, ids);
			}
		}
	}
}
