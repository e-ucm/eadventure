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

import java.util.Map;

/**
 * Reads and writes to a map value.
 * @param <K> key-type
 * @param <V> value-type
 */
public class MapAccessor<K, V> implements Accessor<V> {

	protected K key;
	private final Accessor<Map<K, V>> mapDescriptor;
	private final Map<K, V> map;

	/**
	 * @param element The element where the map is stored
	 * @param fieldName The name of the field that contains the map
	 * @param key the key under which it is stored
	 */
	public MapAccessor(Object element, String fieldName, K key) {
		this.mapDescriptor = new IntrospectingAccessor<Map<K, V>>(element,
				fieldName);
		this.map = null;
		this.key = key;
	}

	public MapAccessor(Map<K, V> map, K key) {
		this.mapDescriptor = null;
		this.map = map;
		this.key = key;
	}

	/**
	 * Writes the field
	 */
	@Override
	public void write(V data) {
		try {
			Map<K, V> m = (map == null ? mapDescriptor.read() : map);
			m.put(key, data);
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
			Map<K, V> m = (map == null ? mapDescriptor.read() : map);
			return m.get(key);
		} catch (Exception e) {
			throw new RuntimeException("Error reading from key " + key, e);
		}
	}

	@Override
	public String toString() {
		return "MapA{" + mapDescriptor + " map=" + map + " key=" + key + '}';
	}

	@Override
	public Object getSource() {
		return map == null ? mapDescriptor : map;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 31 * hash + (this.key != null ? this.key.hashCode() : 0);
		hash = 31
				* hash
				+ (this.mapDescriptor != null ? this.mapDescriptor.hashCode()
						: 0);
		hash = 31 * hash + (this.map != null ? this.map.hashCode() : 0);
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
		final MapAccessor other = (MapAccessor) obj;
		if (this.key != other.key
				&& (this.key == null || !this.key.equals(other.key))) {
			return false;
		}
		@SuppressWarnings("unchecked")
		final Accessor<Map<K, V>> md = (Accessor<Map<K, V>>) other.mapDescriptor;
		if (mapDescriptor != md
				&& (mapDescriptor == null || !mapDescriptor.equals(md))) {
			return false;
		}
		return this.map == other.map
				|| (this.map != null && this.map.equals(other.map));
	}
}
