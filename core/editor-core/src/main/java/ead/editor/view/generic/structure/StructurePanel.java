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

package ead.editor.view.generic.structure;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.jdesktop.swingx.search.Searchable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.editor.view.generic.structure.extra.ElementButtonListener;
import ead.editor.view.generic.structure.extra.SelectedElementPanel;
import ead.editor.view.generic.structure.extra.StructureElementButton;
import ead.editor.view.generic.structure.extra.StructurePanelLayout;
import ead.utils.swing.SwingUtilities;

/**
 * Main class of the StructurePanel
 *
 * To Add elements call to {@link StructurePanel#addElement(StructureElement)},
 * it's would be added on top. With
 * {@link StructurePanel#addElement(StructureElement, int)} is possible give a
 * position through 'int'
 *
 * By the method {@link StructurePanel#createElements()} the panel is created
 * and drawn.
 */
public class StructurePanel extends JPanel {

	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory
			.getLogger(StructurePanel.class);

	private static final long serialVersionUID = -1768584184321389780L;
	//
	private static final int DEFAULT_WIDTH = 320;
	//
	private static final int DEFAULT_SELECT_ELEMENT = 0;

	public static final int NORMAL_ROW_SIZE = 80;

	public static final int SELECTED_ROW_SIZE = 70;

	private static final int INCREMENT = 5;

	private static final int UNSELECTED_BUTTON_HEIGHT = 35;
	// element
	private int selectedElement;

	private List<StructureElement> structureElements;

	// subelement, until not used (need to load a game)
	protected int selectedListItem = -1;

	// for rendering
	private JTable list;

	private Image image;

	private Image basicImage;

	private Image tempImage;

	private int increment;

	private int top;

	private int bottom;

	private int cont;

	/***
	 * Constructor, it call to {@link StructurePanel#StructurePanel(int, int)}
	 * with a 'width' and 'selected element' by default. For more details see
	 * {@link StructurePanel#StructurePanel(int, int)}
	 */
	public StructurePanel() {
		this(DEFAULT_WIDTH, DEFAULT_SELECT_ELEMENT);
	}

	/**
	 * Constructor with a width and selectedElement predefined.
	 *
	 * @param width
	 *            It represent the width of the panel
	 * @param selectedElement
	 *            To have a element selected at first
	 */
	public StructurePanel(int width, int selectedElement) {
		this.selectedElement = selectedElement;

		this.setLayout(new StructurePanelLayout());

		structureElements = new ArrayList<StructureElement>();

		// to adjust the width automatically
		this.addComponentListener(new ComponentListener() {

			public void componentHidden(ComponentEvent arg0) {
			}

			public void componentMoved(ComponentEvent arg0) {
			}

			/**
			 * creates the image to show the elements, required to manager the
			 * images
			 */
			public void componentResized(ComponentEvent arg0) {
				if (getWidth() > 0 && getHeight() > 0)
					basicImage = new BufferedImage(getWidth(), getHeight(),
							BufferedImage.TYPE_4BYTE_ABGR);
			}

			public void componentShown(ComponentEvent arg0) {
			}

		});

		setWidth(width);
	}

	/**
	 * To change the width of the panel
	 *
	 * @param width
	 */
	public void setWidth(int width) {
		this.setMinimumSize(new Dimension(width, 0));
		this.setMaximumSize(new Dimension(width, Integer.MAX_VALUE));
		this.setPreferredSize(new Dimension(width, 0));
	}

	/**
	 * Add an element to the list of elements StructureListElement with the
	 * order represented by index, If the index already exists, it would be
	 * created on top of their order. Also if index is older than size of
	 * StructureListElement, the element is added at the end.
	 *
	 * @param element
	 *            Element to add
	 * @param index
	 *            The order of the element
	 */
	public void addElement(StructureElement element, int index) {
		try {
			structureElements.add(index, element);
		} catch (IndexOutOfBoundsException e) {
			logger.error("Wrong position. Inserted at the end of the list");
			structureElements.add(element);
		}
	}

	/**
	 * Add an element at the end of the list
	 *
	 * @param element
	 *            Element to add.
	 */
	public void addElement(StructureElement element) {

		structureElements.add(element);
	}

	/**
	 * When you add the elements, you need call to this method for show the
	 * StructurePanel
	 */
	public void createElements() {
		Dimension verticalSize = new Dimension(0, structureElements.size()
				* UNSELECTED_BUTTON_HEIGHT + 2 * UNSELECTED_BUTTON_HEIGHT
				+ SELECTED_ROW_SIZE + NORMAL_ROW_SIZE);
		setMinimumSize(verticalSize);
		update();
	}

	/**
	 * Updates the GUI
	 *
	 * @param oldIndex
	 *            Last element selected
	 * @param newIndex
	 *            Element selected now
	 */
	private void update(int oldIndex, final int newIndex) {
		// check if the selected item is not the current element
		if (oldIndex != newIndex && oldIndex != -1) {
			this.setEnabled(false);
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			increment = 10;
			top = 0;
			bottom = 0;
			cont = 0;

			calculateTranslateConstants(newIndex, oldIndex);

			if (this.getHeight() > 0 && this.getWidth() > 0 && increment != 0
					&& cont != 0) {
				image = basicImage;
				this.paint(image.getGraphics());
				if (bottom - top > 0) {
					tempImage = new BufferedImage(this.getWidth(),
							bottom - top, BufferedImage.TYPE_4BYTE_ABGR);
					tempImage.getGraphics().drawImage(image, 0, 0,
							this.getWidth(), bottom - top, 0, top,
							this.getWidth(), bottom, null);
				} else {
					image = null;
					cont = 0;
				}
			}
			selectedElement = newIndex;
			new Thread(new Runnable() {
				public void run() {
					removeAll();
					setIgnoreRepaint(true);
					for (int i = 0; i < cont; i++) {
						Graphics2D g2 = (Graphics2D) getGraphics();
						Graphics g = image.getGraphics();
						g.setColor(StructurePanel.this.getBackground());
						if (i > 0)
							g.fillRect(0, top + increment * (i - 1),
									getWidth(), bottom - top);
						g.drawImage(tempImage, 0, top + increment * i, null);
						g2.drawImage(image, 0, 0, null);
						g2.finalize();
						try {
							Thread.sleep(400 / cont);
						} catch (InterruptedException e) {
						}
					}
					image = null;
					update();
					list.requestFocusInWindow();
					setEnabled(true);
					setIgnoreRepaint(false);
					SwingUtilities.doInEDT(new Runnable() {
						public void run() {
							setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						}
					});
				}
			}).start();
			SwingUtilities.doInEDT(new Runnable() {
				public void run() {
					repaint();
				}
			});

		}

	}

	/**
	 * This method calculates variables "increment, bottom, top, and cont " to
	 * draw the elements as an image, is called in
	 * {@link StructurePanel#update(int, int)}
	 *
	 * @param newIndex
	 *            The element selected now
	 * @param oldIndex
	 *            The last element selected
	 */
	private void calculateTranslateConstants(int newIndex, int oldIndex) {
		boolean canHaveChildrenOld = structureElements.get(oldIndex)
				.getProvider().canHaveChildren();
		boolean canHaveChildrenNew = structureElements.get(newIndex)
				.getProvider().canHaveChildren();

		if (newIndex > oldIndex) {
			if (canHaveChildrenOld && canHaveChildrenNew) {
				increment = -INCREMENT;
				bottom = this.getHeight() - UNSELECTED_BUTTON_HEIGHT
						* (structureElements.size() - newIndex - 1);
				top = bottom - UNSELECTED_BUTTON_HEIGHT * (newIndex - oldIndex);
				int reach = UNSELECTED_BUTTON_HEIGHT * (oldIndex + 1);
				cont = -(top - reach) / increment;

			} else if (canHaveChildrenOld && !canHaveChildrenNew) {
				increment = -INCREMENT;
				bottom = this.getHeight();
				top = bottom - UNSELECTED_BUTTON_HEIGHT
						* (this.structureElements.size() - oldIndex - 1);
				int reach = UNSELECTED_BUTTON_HEIGHT * (oldIndex + 1);
				cont = -(top - reach) / increment;
			} else if (!canHaveChildrenOld && canHaveChildrenNew) {
				increment = INCREMENT;
				top = UNSELECTED_BUTTON_HEIGHT * newIndex + 40;
				bottom = top + UNSELECTED_BUTTON_HEIGHT
						* (this.structureElements.size() - newIndex - 1);
				int reach = this.getHeight() - UNSELECTED_BUTTON_HEIGHT
						* (this.structureElements.size() - newIndex + 1);
				cont = -(top - reach) / increment;
			} else if (!canHaveChildrenOld && !canHaveChildrenNew) {
				cont = 0;
			}
		} else {
			if (canHaveChildrenOld && canHaveChildrenNew) {
				increment = INCREMENT;
				top = UNSELECTED_BUTTON_HEIGHT * newIndex
						+ UNSELECTED_BUTTON_HEIGHT;
				bottom = top + UNSELECTED_BUTTON_HEIGHT
						* (oldIndex - newIndex - 1) + 40;
				int reach = this.getHeight() - UNSELECTED_BUTTON_HEIGHT
						* (this.structureElements.size() - newIndex);
				cont = -(top - reach) / increment;

			} else if (canHaveChildrenOld && !canHaveChildrenNew) {
				increment = -INCREMENT;
				top = this.getHeight() - UNSELECTED_BUTTON_HEIGHT
						* (structureElements.size() - oldIndex - 1);
				bottom = this.getHeight();
				int reach = UNSELECTED_BUTTON_HEIGHT * (oldIndex + 1);
				cont = -(top - reach) / increment;
			} else if (!canHaveChildrenOld && canHaveChildrenNew) {
				increment = INCREMENT;
				top = UNSELECTED_BUTTON_HEIGHT * newIndex
						+ UNSELECTED_BUTTON_HEIGHT;
				bottom = UNSELECTED_BUTTON_HEIGHT
						* this.structureElements.size()
						- UNSELECTED_BUTTON_HEIGHT + 40;
				int reach = this.getHeight() - UNSELECTED_BUTTON_HEIGHT
						* (this.structureElements.size() - newIndex);
				cont = -(top - reach) / increment;
			} else if (!canHaveChildrenOld && !canHaveChildrenNew) {
				cont = 0;
			}
		}
	}

	/**
	 * Update all GUI
	 */
	public void update() {

		int i = 0;
		removeAll();

		for (StructureElement element : structureElements) {
			if (i == selectedElement)
				add(new SelectedElementPanel(element, i, this), new Integer(
						element.getProvider().canHaveChildren() ? -1 : 40));
			else {
				JButton button = new StructureElementButton(element.getProvider().getLabel(),
						element.getProvider().getIcon());

				ElementButtonListener elementListener = new ElementButtonListener(
						this, button, i);
				button.addActionListener(elementListener);

				add(button, new Integer(UNSELECTED_BUTTON_HEIGHT));
			}
			i++;
		}
		this.updateUI();
	}

	/**
	 * The event actionPerformed in {@link ElementButtonListener} calls to here
	 * for update the GUI and notify the action to the element.
	 *
	 * @param index
	 *            Index of the element selected
	 */
	public void updateSelectElement(int index) {
		update(selectedElement, index);
		structureElements.get(index).selectionChanged();
	}

	/**
	 * Moves the elements over the list {@link StructurePanel#structureElements}
	 *
	 * @param index
	 *            The index of one element to be swapped
	 * @param i
	 *            The index of the other element to be swapped
	 */
	public void moveElement(int index, int i) {
		if (i < structureElements.size()) {
			int j;
			if (i > index) {
				for (j = 0; j < i - index; j++) {
					Collections.swap(structureElements, index + j, index + j
							+ 1);
				}
				selectedElement = index + j;
			} else {

				for (j = 0; j < index - i; j++) {
					Collections.swap(structureElements, index - j, index - j
							- 1);
				}
				selectedElement = index - j;
			}
			update();
		}
	}

	// ----------------UNIMPLEMENTED---------------------
	// TODO it's need to load a list previous saved.
	public void setSelectedItem(List<Searchable> path) {

		boolean element = true;
		while (path.size() > 0 && element) {
			element = false;
			for (int i = 0; i < structureElements.size() && !element; i++) {

				selectedElement = i;
				selectedListItem = -1;
				element = true;
			}
			// }
			if (element)
				path.remove(path.size() - 1);
		}

		update();
		if (structureElements.get(selectedElement).getChildCount() > 0
				&& path.size() > 0) {
			element = false;
			for (int i = 0; i < structureElements.get(selectedElement)
					.getChildCount() && !element; i++) {
				selectedListItem = i;
				path.remove(path.size() - 1);
				element = true;
			}
		}

	}

	public JTable getList() {
		return list;
	}

	public void setList(JTable list) {
		this.list = list;
	}

}
