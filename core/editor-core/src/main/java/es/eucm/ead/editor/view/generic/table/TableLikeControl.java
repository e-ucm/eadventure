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
public interface TableLikeControl<T> {

	/**
	 * Removes an object from the list. Triggered either externally or via
	 * button-click.
	 *
	 * @param index
	 */
	void remove(int index);

	/**
	 * Allows user to choose from available elements to add one to the list
	 *
	 * @return
	 */
	T chooseElementToAdd();

	/**
	 * Adds an object to the list. Triggered either externally or via
	 * button-click.
	 * 
	 * @param added element 
	 */
	public void add(T added);

	/**
	 * Moves an object one position up. Triggered either externally or via
	 * button-click.
	 *
	 * @param index
	 */
	void moveUp(int index);

	/**
	 * Removes an object from the list. Triggered either externally or via
	 * button-click.
	 *
	 * @param index
	 */
	void moveDown(int index);
}
