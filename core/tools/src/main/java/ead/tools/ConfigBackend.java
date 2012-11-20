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

package ead.tools;

/**
 * Allows loading, saving and querying of simple key-value stores. Singleton.
 */
public abstract class ConfigBackend {

	/**
	 * Loads from a source URL
	 * @param sourceURL
	 * @return true if loaded correctly
	 */
	public abstract boolean load(String sourceURL);

	/**
	 * Saves to the same URL it was loaded from
	 * @param targetURL; if null, previously-loaded sourceURL is used
	 * @return true if loaded correctly
	 */
	public abstract boolean save(String targetURL);

	/**
	 * Retrieves a value from a toString()ed key; returns "null" if unspecified
	 */
	public abstract String getValue(Object key);

	/**
	 * Sets a value. Use 'null' to unset.
	 * @param key - will be toStringed before comparison
	 * @param value
	 */
	public abstract void put(Object key, String value);

	/**
	 * Returns true if key's toString() is contained in store
	 */
	public boolean containsKey(Object key) {
		return getValue("" + key) != null;
	}

	/**
	 * Retrieves an integer
	 */
	public int getInt(Object key) {
		String v = getValue("" + key);
		return (Integer.valueOf(v));
	}

	/**
	 * Retrieves a double
	 */
	public double getDouble(Object key) {
		String v = getValue("" + key);
		return (Double.valueOf(v));
	}

	/**
	 * Retrieves a boolean
	 */
	public boolean getBoolean(Object key) {
		String v = getValue("" + key);
		return (Boolean.parseBoolean(v));
	}

	/**
	 * Retrieves a string array
	 */
	public String[] getArray(Object key, String separator) {
		String v = getValue("" + key);
		return v.split(separator);
	}
}
