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

/**
 * An accessor that wraps an inner accessor, converting from outer to inner
 * on-the-fly. This is useful in cases where a one-to-one mapping exist; for 
 * example, from integers to strings or filenames to files.
 */
public abstract class ConvertingAccessor<A, B> implements Accessor<A> {

	private Accessor<B> inner;
	private Class<A> outer;

	/**
	 * @param element
	 *            The element where the value is stored
	 * @param fieldName
	 *            The name of the field
	 */
	public ConvertingAccessor(Class<A> outer, Accessor<B> inner) {
		this.inner = inner;
		this.outer = outer;
	}

	public abstract A innerToOuter(B b);

	public abstract B outerToInner(A a);

	/**
	 * Writes the field
	 */
	@Override
	public void write(A data) {
		inner.write(outerToInner(data));
	}

	/**
	 * Reads the field
	 */
	@Override
	@SuppressWarnings("unchecked")
	public A read() {
		return innerToOuter(inner.read());
	}

	@Override
	public String toString() {
		return "ConvFD [" + inner.toString() + "=>" + outer.getSimpleName();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 71 * hash + (this.inner != null ? this.inner.hashCode() : 0);
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
		final ConvertingAccessor<A, B> other = (ConvertingAccessor<A, B>) obj;
		if (this.inner != other.inner
				&& (this.inner == null || !this.inner.equals(other.inner))) {
			return false;
		}
		return true;
	}
}
