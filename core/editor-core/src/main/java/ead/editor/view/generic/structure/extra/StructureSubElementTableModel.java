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

package ead.editor.view.generic.structure.extra;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import ead.editor.view.generic.structure.StructureElement;

/**
 * Table model for the table of sub-elements
 */
public class StructureSubElementTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 8800715132450305116L;

	/**
	 * The structure element
	 */
	private StructureElement element;

	/**
	 * The list of sub-elements
	 */
	private JTable list;

	public StructureSubElementTableModel(StructureElement element, String string) {
		this.element = element;
	}

	public void setList(JTable list) {
		this.list = list;
	}

	public int getColumnCount() {
		return 1;
	}

	public int getRowCount() {
		return element.getChildCount();
	}

	public Object getValueAt(int arg0, int arg1) {
		return element.getChild(arg0);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return list.getSelectedRow() == row;
	}
}
