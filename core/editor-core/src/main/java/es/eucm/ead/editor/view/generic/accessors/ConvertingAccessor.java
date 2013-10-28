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

/**
 * An accessor that wraps an inner accessor, converting from outer to inner
 * on-the-fly. This is useful in cases where a one-to-one mapping exist; for 
 * example, from integers to strings or filenames to files.
 * @param <O> outer class (of instances returned by read and write)
 * @param <I> inner class (of whatever is actually being accessed)
 */
public abstract class ConvertingAccessor<O, I> implements Accessor<O> {

	private final Accessor<I> inner;
	private final Class<O> outer;

	/**
	 * Constructor.
	 * @param outer outwardly-visible class of returned instances
	 * @param inner accessor used to retrieve inner object, which is 
	 *  then converted via innerToOuter and outerToInner
	 */
	public ConvertingAccessor(Class<O> outer, Accessor<I> inner) {
		this.inner = inner;
		this.outer = outer;
	}

	public abstract O innerToOuter(I inner);

	public abstract I outerToInner(O outer);

	/**
	 * Writes the field.
	 * @param outer instance to write (after to outerToInner conversion)
	 */
	@Override
	public void write(O outer) {
		inner.write(outerToInner(outer));
	}

	/**
	 * Reads the field
	 */
	@Override
	@SuppressWarnings("unchecked")
	public O read() {
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
		final ConvertingAccessor<O, I> other = (ConvertingAccessor<O, I>) obj;
		return this.inner == other.inner
				|| (this.inner != null && this.inner.equals(other.inner));
	}

	@Override
	public Object getSource() {
		return inner.getSource();
	}
}
