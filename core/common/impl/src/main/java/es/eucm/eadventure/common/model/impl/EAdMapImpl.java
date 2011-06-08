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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import es.eucm.eadventure.common.model.EAdMap;

public class EAdMapImpl<S, T> extends AbstractEAdElement implements EAdMap<S, T> {
	
	private Map<S, T> map;

	private Class<?> keyClass;
	
	private Class<?> valueClass;
	
	/**
	 * Creates an empty map
	 * 
	 * @param parent
	 *            Element's parent
	 * @param id
	 *            Element's id
	 */
	public EAdMapImpl(String id, Class<?> keyClass, Class<?> valueClass) {
		super(id);
		map = new HashMap<S, T>( );
    	this.keyClass = keyClass;
    	this.valueClass = valueClass;
	}
	
	@Override
	public void clear() {
		map.clear();
		
	}

	@Override
	public boolean containsKey(Object arg0) {
		return map.containsKey(arg0);
	}

	@Override
	public boolean containsValue(Object arg0) {
		return map.containsValue(arg0);
	}

	@Override
	public Set<java.util.Map.Entry<S, T>> entrySet() {
		return map.entrySet();
	}

	@Override
	public T get(Object arg0) {
		return map.get(arg0);
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Set<S> keySet() {
		return map.keySet();
	}

	@Override
	public T put(S arg0, T arg1) {
		return map.put(arg0, arg1);
	}

	@Override
	public void putAll(Map<? extends S, ? extends T> arg0) {
		map.putAll(arg0);
	}

	@Override
	public T remove(Object arg0) {
		return map.remove(arg0);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Collection<T> values() {
		return map.values();
	}

	@Override
	public Class<?> getKeyClass() {
		return this.keyClass;
	}

	@Override
	public Class<?> getValueClass() {
		return this.valueClass;
	}
	
}
