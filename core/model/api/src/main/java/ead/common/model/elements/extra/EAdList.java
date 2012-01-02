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

package ead.common.model.elements.extra;

/**
 * Interface for all the list elements in the eAdventure game model
 */
public interface EAdList<P> extends Iterable<P> {

	/**
	 * <p>
	 * Appends the specified element at the end of this list.
	 * </p>
	 * <p>
	 * The invocation of this method is equivalent to:
	 * </p>
	 * <p>
	 * {@code add(e, size())}
	 * </p>
	 * 
	 * @param e
	 *            element to be appended.
	 * 
	 * @throws NullPointerException
	 *             if {@code e} is {@code null}.
	 */
	public void add(P e);

	/**
	 * Inserts the specified element at the specified position. Shifts the
	 * element currently at that position (if any) and any subsequent elements
	 * to the right (adds one to their indices).
	 * 
	 * @param e
	 *            element to be inserted.
	 * 
	 * @param index
	 *            position at which the specified element is to be inserted.
	 * 
	 * @throws NullPointerException
	 *             if {@code e} is {@code null}.
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range
	 *             {@code index < 0 || index > size())}.
	 * 
	 */
	public void add(P e, int index);

	/**
	 * Removes the specified element (if exists) from this list.
	 * 
	 * @param e
	 *            element to remove.
	 * @return
	 * 
	 * @throws NullPointerException
	 *             if {@code e} is {@code null}.
	 */
	public boolean remove(P e);

	/**
	 * Remove the element at the specified position. Shifts the element
	 * currently at that position (if any) and any subsequent elements to the
	 * left (subtracts one to their indices).
	 * 
	 * @param index
	 *            position at which the specified element is to be removed.
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range
	 *             {@code index < 0 || index >= size()}.
	 */
	public P remove(int index);

	/**
	 * Returns the number of elements in this list.
	 * 
	 * @return the number of elements in this list {@code size() >= 0}
	 */
	public int size();

	/**
	 * Returns the element at position i in this list.
	 * 
	 * @param i
	 *            the position or index of the element.
	 * @return the element in the position.
	 */
	public P get(int i);

	/**
	 * Returns true if this list contains the specified element. More formally,
	 * returns true if and only if this list contains at least one element e
	 * such that (o==null ? e==null : o.equals(e)).
	 * 
	 * @param o
	 *            the element
	 * @return true if this list contains the specified element
	 */
	public boolean contains(P element);

	/**
	 * Returns the index of the first occurrence of the specified element in
	 * this list, or -1 if this list does not contain the element. More
	 * formally, returns the lowest index i such that (o==null ? get(i)==null :
	 * o.equals(get(i))), or -1 if there is no such index
	 * 
	 * @param o
	 *            - element to search for
	 * @return the index of the first occurrence of the specified element in
	 *         this list, or -1 if this list does not contain the element
	 */
	public int indexOf(P o);

	/**
	 * Returns the class object of the elements hold by this list
	 * 
	 * @return the list class
	 */
	public Class<?> getValueClass();

	/**
	 * Clear the elements of the list
	 */
	public void clear();

}
