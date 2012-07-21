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

package ead.writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ead.common.model.EAdElement;
import ead.common.model.elements.extra.EAdList;

public class DepthManager {

	private List<EAdList<Object>> lists;
	
	private List<EAdElement> stored;
	
	private Map<String, String> classAliases;

	private Map<String, String> aliasMap;
	
	private static final boolean USE_DEPTH_CONTROL = true;
	
	private static final int MAX_LEVEL = 4;
	
	EAdList<EAdElement> depthControlList;
	
	private int level;
	
	public DepthManager(EAdList<EAdElement> eAdList) {
		lists = new ArrayList<EAdList<Object>>();
		stored = new ArrayList<EAdElement>();
		classAliases = new HashMap<String, String>();
		aliasMap = new HashMap<String, String>();
		depthControlList = eAdList;
		depthControlList.clear();
	}
	
	public void addList(EAdList<Object> list) {
		lists.add(list);
	}
	
	public void removeList(EAdList<Object> list) {
		lists.remove(list);
	}

	public Map<String, String> getClassAliases() {
		return classAliases;
	}
	
	public boolean inPreviousList(EAdElement o) {
		if (USE_DEPTH_CONTROL) {
			for (int i = 0; i < lists.size() - 1; i++)
				if (lists.get(i).contains(o))
					return true;
			
			if (level > MAX_LEVEL) {
				depthControlList.add(o);
				return true;
			}
		}
		return false;
	}
	
	public boolean isStored(EAdElement o) {
		return stored.contains(o);
	}
	
	public void setStored(EAdElement o) {
		stored.add(o);
	}

	public Map<String, String> getAliasMap() {
		return aliasMap;
	}

	public EAdElement getInstanceOfElement(EAdElement element) {
		return stored.get(stored.indexOf(element));
	}
	
	public void levelUp() {
		level++;
	}
	
	public void levelDown() {
		level--;
	}

	public void clear() {
		lists.clear();
		stored.clear();
		classAliases.clear();
		aliasMap.clear();
		depthControlList.clear();
	}
}
