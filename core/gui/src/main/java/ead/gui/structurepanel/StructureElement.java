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

package ead.gui.structurepanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a element of StructurePanel. It has a name, and optionally an icon.
 * Furthermore can have children represent by sub-elements
 * {@link StructureSubElement}
 */
public class StructureElement {

	/**
	 * Provider for the values of the element
	 */
	private StructureElementProvider provider;

	/**
	 * List of the sub-element children of the structure element
	 */
	private List<StructureSubElement> children;

	private List<StructureElementChangeListener> changeListeners;

	/**
	 * Constructor by default.
	 * 
	 * @param provider
	 * 			The provider for the values of the strucutre element
	 */
	public StructureElement(StructureElementProvider provider) {
		this.provider = provider;
		this.changeListeners = new ArrayList<StructureElementChangeListener>();
		children = new ArrayList<StructureSubElement>();
	}

	/**
	 * Method to update the children on the list according to the model
	 */
	private void updateChildren() {
		//TODO update the children list
	}
	
	/**
	 * Count the children of the element.
	 * 
	 * @return The number of sub-elements
	 */
	public int getChildCount() {
		if (children == null)
			return 0;
		return children.size();
	}

	/**
	 * Return a child, a sub-element
	 * 
	 * @param index
	 *            Number of child to return
	 * @return A StructureSubElement {@link StructureSubElement}
	 */
	public StructureSubElement getChild(int index) {
		if (children == null)
			return null;
		return children.get(index);
	}

	/**
	 * Add a child {@link StructureSubElement}
	 */
	public void addChild() {
		if (provider.canHaveChildren())  {
			for (StructureElementChangeListener cl : changeListeners)
				cl.addChild();
			updateChildren();
		}
	}

	/**
	 * Removes a child {@link StructureSubElement}
	 * 
	 * @param subElement
	 * 		Sub-element to eliminate
	 */
	public void removeChild(StructureSubElement subElement) {
		for (StructureElementChangeListener cl : changeListeners)
			cl.removeChild(children.indexOf(subElement));
		updateChildren();
	}

	public void duplicateChild(StructureSubElement subElement) {
		if (provider.canHaveChildren()) {
			for (StructureElementChangeListener cl : changeListeners)
				cl.duplicateChild(children.indexOf(subElement));
			updateChildren();
		}
	}

	public void selectionChanged() {
		for (StructureElementChangeListener cl : changeListeners)
			cl.selectionChanged();
	}

	public void addChangeListener(
			StructureElementChangeListener structurePanelChangeListener) {
		this.changeListeners.add(structurePanelChangeListener);
	}

	public void changeSelectedChild(StructureSubElement child) {
		for (StructureElementChangeListener cl : changeListeners)
			cl.selectedChildChange(child);
	}

	public StructureElementProvider getProvider() {
		return provider;
	}

}
