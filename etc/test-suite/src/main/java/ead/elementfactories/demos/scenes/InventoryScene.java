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

package ead.elementfactories.demos.scenes;

import ead.common.model.elements.EAdInventory;
import ead.common.model.elements.InventoryImpl;
import ead.common.model.elements.effects.ActorActionsEf;
import ead.common.model.elements.effects.ModifyInventoryEf;
import ead.common.model.elements.effects.enums.InventoryEffectAction;
import ead.common.model.elements.guievents.EAdMouseEvent;
import ead.common.model.elements.guievents.enums.MouseButtonType;
import ead.common.model.elements.guievents.enums.MouseEventType;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElementDefImpl;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.params.fills.EAdColor;
import ead.common.resources.assets.drawable.basics.ImageImpl;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.elementfactories.EAdElementsFactory;

public class InventoryScene extends EmptyScene {
	
	public InventoryScene( ){
		super();
		SceneElementDefImpl item = new SceneElementDefImpl( new ImageImpl("@drawable/ng_key.png"));
		
		item.getActions().add(EAdElementsFactory.getInstance().getActionsFactory().getBasicAction());
		
		item.addBehavior(EAdMouseEvent.MOUSE_LEFT_CLICK, new ActorActionsEf( item ));
		item.addBehavior(EAdMouseEvent.MOUSE_RIGHT_CLICK, new ActorActionsEf( item ));
		item.addBehavior(EAdMouseEvent.getMouseEvent(MouseEventType.PRESSED, MouseButtonType.BUTTON_3), new ActorActionsEf( item ));
		
		EAdSceneElementDef item2 = new SceneElementDefImpl( new RectangleShape( 10, 10, EAdColor.BLUE ));
		EAdSceneElementDef item3 = new SceneElementDefImpl( new RectangleShape( 90, 90, EAdColor.GREEN ));
		EAdInventory inventory = new InventoryImpl();
//		inventory.getInitialItems().add(item);
		inventory.getInitialItems().add(item2);
		inventory.getInitialItems().add(item3);
		EAdElementsFactory.getInstance().setInventory(inventory);
		
		
		SceneElementImpl key = new SceneElementImpl( new ImageImpl("@drawable/ng_key.png") );
		key.setPosition(200, 200);
		ModifyInventoryEf effect = new ModifyInventoryEf( item, InventoryEffectAction.ADD_TO_INVENTORY );
		
		key.addBehavior(EAdMouseEvent.MOUSE_LEFT_CLICK, effect);
		
		getComponents().add(key);
	}
	
	@Override
	public String getSceneDescription() {
		return "A scene with inventory.";
	}

	public String getDemoName() {
		return "Inventory Scene";
	}

}
