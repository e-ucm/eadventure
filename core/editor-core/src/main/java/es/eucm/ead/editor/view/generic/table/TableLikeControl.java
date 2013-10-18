/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eucm.ead.editor.view.generic.table;

/**
 * Common interface for table-like controls.
 * 
 * @author mfreire
 */
public interface TableLikeControl<T, K> {

	/**
	 * Removes an object from the list. Triggered either externally or via
	 * button-click.
	 *
	 * @param index
	 */
	void remove(K index);

	/**
	 * Launches UI prompt to add an element to the list
	 *
	 * @return
	 */
	T chooseElementToAdd();
	
	/**
	 * Launches UI prompt to add a key to a list element
	 */
	K chooseKeyToAdd();

	/**
	 * Adds an object to the list. Triggered either externally or via
	 * button-click.
	 * 
	 * @param added element 
	 * @param index 
	 */
	public void add(T added, K index);

	/**
	 * Moves an object one position up. Triggered either externally or via
	 * button-click. Optional operation.
	 *
	 * @param index
	 */
	void moveUp(K index);

	/**
	 * Removes an object from the list. Triggered either externally or via
	 * button-click. Optional operation.
	 *
	 * @param index
	 */
	void moveDown(K index);
	
	/**
	 * Returns the key for a given row
	 * @param row
	 * @return corresponding key (probably the row itself...)
	 */
	K keyForRow(int row);
}
