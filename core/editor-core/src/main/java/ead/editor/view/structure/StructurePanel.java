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

package ead.editor.view.structure;

import ead.editor.control.Controller;
import ead.editor.control.change.ChangeListener;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class of the StructurePanel. A vertically-stacked panel of
 * buttons providing shortcuts to main views.
 */
public class StructurePanel extends JPanel implements ChangeListener {

	private static Logger logger = LoggerFactory
			.getLogger(StructurePanel.class);

	private static final long serialVersionUID = -1768584184321389780L;

	private ArrayList<StructureElement> elements = new ArrayList<StructureElement>();

	private JPanel spacerPanel = new JPanel();

	private Controller controller;

	public StructurePanel() {
		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	/**
	 * Prepare the panel for drawing. Makes changes visible.
	 */
	public void commit() {
		removeAll();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 0;
		for (StructureElement e : elements) {
			gbc.gridy++;
			add(e, gbc);
		}
		gbc.gridy++;
		gbc.weighty = 1.0;
		add(spacerPanel, gbc);
		revalidate();
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
	 * order represented by index.
	 * @param element
	 *            Element to add
	 * @param index
	 *            The order of the element
	 */
	public void addElement(StructureElement element, int index) {
		try {
			elements.add(index, element);
		} catch (IndexOutOfBoundsException e) {
			logger.error("Wrong position. Inserted at the end of the list", e);
			elements.add(element);
		}
	}

	/**
	 * Add an element at the end of the list
	 *
	 * @param element
	 *            Element to add.
	 */
	public void addElement(StructureElement element) {
		addElement(element, elements.size());
	}

	@Override
	public void processChange(Object event) {
		boolean somethingLoaded = controller.getModel().getEngineModel() != null;
		for (StructureElement e : elements) {
			e.setEnabled(somethingLoaded);
		}
	}
}
