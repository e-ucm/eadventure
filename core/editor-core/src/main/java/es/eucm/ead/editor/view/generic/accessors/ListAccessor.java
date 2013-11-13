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

package es.eucm.ead.editor.view.generic.accessors;

import java.util.List;

/**
 * Reads and writes to a list value.
 * @param <V> value-type
 */
public class ListAccessor<V> implements Accessor<V> {

	private final Accessor<List<V>> listDescriptor;
	protected final int index;
	private final List<V> list;

	/**
	 * @param element The element where the list is stored
	 * @param fieldName The name of the field that contains the list
	 * @param index the index under which the interesting element is stored
	 */
	public ListAccessor(Object element, String fieldName, int index) {
		this.listDescriptor = new IntrospectingAccessor<List<V>>(element,
				fieldName);
		this.index = index;
		this.list = null;
	}

	/**
	 * @param list The list 
	 * @param index the index under which the interesting element is stored
	 */
	public ListAccessor(List<V> list, int index) {
		this.listDescriptor = null;
		this.index = index;
		this.list = list;
	}

	/**
	 * Writes the field
	 */
	@Override
	public void write(V data) {
		try {
			List<V> l = (list == null ? listDescriptor.read() : list);
			l.set(index, data);
		} catch (Exception e) {
			throw new RuntimeException("Error writing to index " + index, e);
		}
	}

	/**
	 * Reads the field
	 */
	@Override
	public V read() {
		try {
			List<V> l = (list == null ? listDescriptor.read() : list);
			return l.get(index);
		} catch (Exception e) {
			throw new RuntimeException("Error reading from index " + index, e);
		}
	}

	@Override
	public String toString() {
		return "ListA{" + listDescriptor + " list=" + list + " idx=" + index
				+ '}';
	}

	@Override
	public Object getSource() {
		return list == null ? listDescriptor : list;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 73
				* hash
				+ (this.listDescriptor != null ? this.listDescriptor.hashCode()
						: 0);
		hash = 73 * hash + this.index;
		hash = 73 * hash + (this.list != null ? this.list.hashCode() : 0);
		return hash;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ListAccessor<V> other = (ListAccessor<V>) obj;
		if (this.listDescriptor != other.listDescriptor
				&& (this.listDescriptor == null || !this.listDescriptor
						.equals(other.listDescriptor))) {
			return false;
		}
		if (this.index != other.index) {
			return false;
		}
		return this.list == other.list
				|| (this.list != null && this.list.equals(other.list));
	}
}
