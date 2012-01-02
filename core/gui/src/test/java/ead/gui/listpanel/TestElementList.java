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

package ead.gui.listpanel;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.gui.listpanel.ListPanelListener;

/**
 * List of elements for the test. Implement {@link ListPanelListener} to update
 * the elements.
 * 
 * Always is created a element list is need to implement {@link ListPanelListener}
 * 
 */
public class TestElementList implements ListPanelListener {

	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory
			.getLogger(TestElementList.class);

	private List<TestElement> elements;

	public TestElementList() {
		elements = new ArrayList<TestElement>();
	}

	@Override
	public boolean addElement() {
		elements.add(new TestElement("pruebaA + " + elements.size(),
				"pruebaB + " + elements.size()));

		logger.info("Listener: add element");
		return true;
	}

	@Override
	public boolean duplicateElement(int i) {
		elements.add((TestElement) elements.get(i).getClone());
		logger.info("Listener: duplicate element");
		return true;
	}

	@Override
	public boolean deleteElement(int i) {
		elements.remove(i);
		logger.info("Listener: delete element");
		return true;
	}

	@Override
	public void moveUp(int i) {
		if (i > 0) {
			elements.add(i - 1, elements.remove(i));
		}
		logger.info("Listener: move up element");
	}

	@Override
	public void moveDown(int i) {

		if (i < elements.size() - 1) {
			elements.add(i + 1, elements.remove(i));
		}
		logger.info("Listener: move down element");
	}

	@Override
	public void selectionChanged() {

		logger.info("Listener: selection changed");
	}

	@Override
	public int getCount() {

		return elements.size();
	}

	@Override
	public boolean setValue(int rowIndex, int columnIndex, Object value) {
		switch (columnIndex) {
		case 0:
			elements.get(rowIndex).setCadena1((String) value);
			break;
		case 1:
			elements.get(rowIndex).setCadena2((String) value);
			break;
		default:
			logger.warn("Problem to modify the cell");
			return false;
		}
		return true;
	}

	@Override
	public Object getValue(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return elements.get(rowIndex).getCadena1();
		case 1:
			return elements.get(rowIndex).getCadena2();
		default:
			return elements.get(rowIndex);
		}
	}

}
