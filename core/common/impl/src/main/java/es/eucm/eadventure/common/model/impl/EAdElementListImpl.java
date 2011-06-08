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

package es.eucm.eadventure.common.model.impl;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.eucm.eadventure.common.CopyNotSupportedException;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.resources.EAdResources;

/**
 * Generic implementation of a list of eAdventure elements.
 */
public class EAdElementListImpl<P> extends AbstractEAdElement implements EAdElementList<P> {

	/**
	 * Lists elements.
	 */
	private List<P> elements;
	
	/**
	 * Constructs a {@link EAdElementListImpl} with the specified parent element.
	 * 
	 * @param parent parent element.
	 */
	public EAdElementListImpl(){
		super("list");
		elements = new ArrayList<P>();
	}
	
	@SuppressWarnings("unchecked")
	public EAdElementListImpl<P> copy() {
		try {
			return (EAdElementListImpl<P>) super.copy();
		} catch (ClassCastException e) {
			throw new CopyNotSupportedException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public EAdElementListImpl<P> copy(boolean deepCopy){
		try {
			EAdElementListImpl<P> copy = (EAdElementListImpl<P>) super.copy(deepCopy);
			if(deepCopy){
				copy.elements = new ArrayList<P>();
				for(P e: this.elements){
					if (e instanceof EAdElement)
						copy.elements.add((P) ((EAdElement) e).copy(true));
					//TODO possibly need a fix
					//else
					//	 copy.elements.add((P) e.clone());
				}
			}
			return copy;
		} catch (ClassCastException e) {
			throw new CopyNotSupportedException(e);
		}
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

	public void remove(int index) {
		this.elements.remove(index);
	}

	public int size() {
		return this.elements.size();
	}

	@Override
	public EAdResources getResources() {
		return null;
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
	
	public boolean equals( Object o ){
		if ( o instanceof EAdElementList ){
			EAdElementList<?> list = ( EAdElementList<?> ) o;
			if ( list.size() == this.size() ){
				for ( int i = 0; i < this.size(); i++ ){
					if ( !list.get(i).equals(this.get(i)) )
						return false;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public Class<?> getValueClass() {
		TypeVariable<?>[] list = this.getClass().getTypeParameters();
		if (list.length < 1)
			return null;
		Class<?> temp = (Class<?>) list[0].getGenericDeclaration();
		return temp;
	}

	@Override
	public void clear() {
		elements.clear();
	}

}
