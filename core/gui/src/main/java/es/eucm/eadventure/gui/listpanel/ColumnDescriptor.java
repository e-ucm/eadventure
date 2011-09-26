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

	public String getName() {
		return name;
	}

	public String getHelp() {
		return help;
	}

	public CellRenderEditor getTypeColumn() {
		return typeColumn;
	}

	public boolean isEditable() {
		return typeColumn != null ? typeColumn.isEditable() : false;
	}

}
