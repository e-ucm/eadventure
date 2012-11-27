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

package ead.editor.view.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * An alternative to a vertical BoxLayout that stacks things vertically
 * (assigning all equal spaces) without stretching them.
 * @author mfreire
 */
public class CheapVerticalLayout implements LayoutManager {

	private Dimension min = new Dimension(10, 10);
	private Dimension pref = new Dimension(10, 10);
	private int maxRowHeight = 0;

	@Override
	public void addLayoutComponent(String name, Component comp) {
	}

	@Override
	public void removeLayoutComponent(Component comp) {
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		int maxX = 0;
		int maxY = 0;
		for (int i = 0; i < parent.getComponentCount(); i++) {
			Component c = parent.getComponent(i);
			Dimension d = c.getPreferredSize();
			maxY = Math.max(d.height, maxY);
			maxX = Math.max(d.width, maxX);

		}
		maxRowHeight = maxY;
		pref.setSize(maxX, maxRowHeight * parent.getComponentCount());
		return pref;
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return min;
	}

	@Override
	public void layoutContainer(Container parent) {
		// recalculates row-height
		preferredLayoutSize(parent);
		int x = 0;
		int y = 0;
		for (int i = 0; i < parent.getComponentCount(); i++) {
			Component c = parent.getComponent(i);
			Dimension d = c.getPreferredSize();
			c.setLocation(x, y + (maxRowHeight - d.height) / 2);
			c.setSize(d.width, d.height);
			y += maxRowHeight;
		}
	}
}
