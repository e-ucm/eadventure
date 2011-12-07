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

package es.eucm.eadventure.gui.listpanel;

import es.eucm.eadventure.gui.listpanel.columntypes.CellRenderEditor;

/**
 * This class represents a column in the table. It need the name of the
 * column, and optionally can take a help file and a column type.
 * 
 */
public class ColumnDescriptor {
	
	private String name;

	private String help;
	
	private CellRenderEditor typeColumn;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            Name of the column, will be shown at the header.
	 * @param help
	 *            Help file, can be empty.
	 * @param typeColumn
	 *            Type of the column to create, their are defined in
	 *            gui->listpanel->columntypes
	 */
	public ColumnDescriptor(String name, String help, CellRenderEditor typeColumn) {
		this.name = name;
		this.help = help;
		this.typeColumn = typeColumn;
	}

	/**
	 * Return the name or label of the column
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the help file for the column
	 * 
	 * @return
	 */
	public String getHelp() {
		return help;
	}

	/**
	 * Return the {@link CellRendererEditor} type of the column
	 * 
	 * @return
	 */
	public CellRenderEditor getTypeColumn() {
		return typeColumn;
	}

	/**
	 * True if the column's content is editable
	 * 
	 * @return
	 */
	public boolean isEditable() {
		return typeColumn != null ? typeColumn.isEditable() : false;
	}

}
