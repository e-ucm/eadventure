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
