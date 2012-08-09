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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ead.common.model.elements.extra.EAdList;

/**
 * Generic implementation of a list of eAdventure elements.
 */
public class EAdListImpl<P> implements EAdList<P> {

	/**
	 * Lists elements.
	 */
	private List<P> elements;

	private Class<?> clazz;

	/**
	 * Constructs a {@link EAdListImpl} with the specified parent element.
	 * 
	 * @param parent
	 *            parent element.
	 */
	public EAdListImpl(Class<?> clazz) {
		this.clazz = clazz;
		elements = new ArrayList<P>();
	}

	public void add(P e) {
		this.elements.add(e);
	}

	public void add(P e, int index) {
		this.elements.add(index, e);
	}

	public boolean remove(P e) {
		return elements.remove(e);
	}

	public P remove(int index) {
		return elements.remove(index);
	}

	public int size() {
		return this.elements.size();
	}

	@Override
	public Iterator<P> iterator() {
		return this.elements.iterator();
	}

	@Override
	public P get(int i) {
		return this.elements.get(i);
	}

	@Override
	public boolean contains(P element) {
		return this.elements.contains(element);
	}

	@Override
	public int indexOf(P element) {
		return this.elements.indexOf(element);
	}

	@Override
	public Class<?> getValueClass() {
		return clazz;
	}

	@Override
	public void clear() {
		elements.clear();
	}

	public String toString() {
		String s = "[";
		boolean empty = true;
		for (P e : this) {
			s += e + ", ";
			empty = false;
		}
		if (!empty)
			s = s.substring(0, s.length() - 2);

		s += "]";
		return s;
	}

	@Override
	public void addAll(EAdList<P> list) {
		for (P e : list) {
			this.add(e);
		}

	}

}
