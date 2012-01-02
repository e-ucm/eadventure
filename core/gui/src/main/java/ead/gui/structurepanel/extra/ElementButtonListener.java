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

package ead.gui.structurepanel.extra;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import ead.gui.structurepanel.StructurePanel;

/**
 * Implements the listeners to a selected element.
 * 
 * ActionListener allows us to update the elements when one is selected and
 * return an action on the method
 * {@link ElementButtonListener#actionPerformed(ActionEvent)} MouseListener and
 * MouseMotionListener implement drag and drop between the elements. It allow us
 * to move the selected to up or down on the list.
 */
public class ElementButtonListener implements ActionListener, MouseListener,
		MouseMotionListener {

	private int index;

	private StructurePanel container;

	private Component panel;

	private int lastY;

	private int newY;

	/**
	 * The constructor have the container panel {@link StructurePanel} and the
	 * current element number
	 * 
	 * @param container
	 *            Container with the StructurePanel
	 * @param index
	 *            Element number
	 */
	public ElementButtonListener(StructurePanel container, Component panel,
			int index) {
		this.container = container;
		this.index = index;
		this.panel = panel;
	}

	/**
	 * Event launched by clicked over the element
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		container.updateSelectElement(index);
	}

	/**
	 * Event that control the movement of the element
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		newY = e.getYOnScreen() - lastY;
		container.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * This event advice us when the mouse is over the element
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		e.getComponent().setCursor(
				Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

	}

	@Override
	public void mouseExited(MouseEvent r) {
	}

	/**
	 * Event push the element
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		lastY = e.getYOnScreen();
	}

	/**
	 * When we release the mouse, this event is launched
	 */
	@Override
	public void mouseReleased(MouseEvent e) {

		int height = panel.getHeight();
		boolean isPanel = false;
		if (panel instanceof SelectedElementPanel) {
			((SelectedElementPanel) panel).getButtonHeight();
			isPanel = true;
		}

		if (newY > height)
			container.moveElement(index, index
					+ ( (newY - (isPanel ? panel.getHeight() : 0)) / height));
		else if ((newY < -height) && (index - 1 > 0))
			container.moveElement(index, index + (newY / height));

		lastY = e.getYOnScreen();
		container.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		e.consume();
	}

}
