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

package ead.editor.view.generic.accessors;

import java.util.Map;

/**
 * Reads and writes to a map value. Uses a provided accessor to find
 * the map.
 */
public class MapAccessor<K, V> implements Accessor<V> {

	protected K key;
	private Accessor<Map<K, V>> mapDescriptor;

	/**
	 * @param element The element where the map is stored
	 * @param fieldName The name of the field that contains the map
	 * @param key the key under which it is stored
	 */
	public MapAccessor(Object element, String fieldName, K key) {
		mapDescriptor = new IntrospectingAccessor<Map<K, V>>(element, fieldName);
		this.key = key;
	}

	/**
	 * Writes the field
	 */
	@Override
	public void write(V data) {
		try {
			Map<K, V> map = mapDescriptor.read();
			map.put(key, data);
		} catch (Exception e) {
			throw new RuntimeException("Error writing to key " + key, e);
		}
	}

	/**
	 * Reads the field
	 */
	@Override
	public V read() {
		try {
			Map<K, V> map = mapDescriptor.read();
			return map.get(key);
		} catch (Exception e) {
			throw new RuntimeException("Error reading from key " + key, e);
		}
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + (this.key != null ? this.key.hashCode() : 0);
		hash = 97
				* hash
				+ (this.mapDescriptor != null ? this.mapDescriptor.hashCode()
						: 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("unchecked")
		final MapAccessor<K, V> other = (MapAccessor<K, V>) obj;
		if (this.key != other.key
				&& (this.key == null || !this.key.equals(other.key))) {
			return false;
		}
		if (this.mapDescriptor != other.mapDescriptor
				&& (this.mapDescriptor == null || !this.mapDescriptor
						.equals(other.mapDescriptor))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "MapFD{" + mapDescriptor + " key=" + key + '}';
	}
}
