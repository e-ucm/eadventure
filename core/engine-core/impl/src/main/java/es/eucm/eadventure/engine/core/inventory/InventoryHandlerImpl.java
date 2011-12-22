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

package es.eucm.eadventure.engine.core.inventory;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.elements.scene.EAdSceneElementDef;

@Singleton
public class InventoryHandlerImpl implements InventoryHandler {
	
	private long update = 0;
	
	private List<InventoryItem> inventory;
	
	@Inject
	public InventoryHandlerImpl( ){
		inventory = new ArrayList<InventoryItem>();
	}

	@Override
	public List<InventoryItem> getItems() {
		return inventory;
	}

	@Override
	public void add(EAdSceneElementDef item) {
		boolean add = true;
		for ( InventoryItem i: inventory ){
			if ( i.getElement() == item ){
				add = false;
				i.add(1);
				break;
			}
		}
		
		if ( add ){
			inventory.add(new InventoryItemImpl( item, 1 ));
		}
		update++;
	}

	@Override
	public void remove(EAdSceneElementDef item) {
		InventoryItem itemToRemove = null;
		for ( InventoryItem i: inventory ){
			if ( i.getElement() == item ){
				if ( i.getCount() == 1 ){
					itemToRemove = i;
					break;
				}
				else {
					i.add(-1);
				}
				break;
			}
		}
		
		if ( itemToRemove != null ){
			inventory.remove(itemToRemove);
		}
		update++;
	}

	@Override
	public long updateNumber() {
		return update;
	}

}
