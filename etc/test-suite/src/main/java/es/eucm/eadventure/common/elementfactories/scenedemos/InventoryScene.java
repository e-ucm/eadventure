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

package es.eucm.eadventure.common.elementfactories.scenedemos;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdInventoryEffect;
import es.eucm.eadventure.common.model.effects.impl.enums.InventoryEffectAction;
import es.eucm.eadventure.common.model.elements.EAdInventory;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdInventoryImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.guievents.enums.MouseActionType;
import es.eucm.eadventure.common.model.guievents.enums.MouseButton;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

public class InventoryScene extends EmptyScene {
	
	public InventoryScene( ){
		super();
		EAdSceneElementDefImpl item = new EAdSceneElementDefImpl( new ImageImpl("@drawable/ng_key.png"));
		
		item.getActions().add(EAdElementsFactory.getInstance().getActionsFactory().getBasicAction());
		
		item.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, new EAdActorActionsEffect( item ));
		item.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, new EAdActorActionsEffect( item ));
		item.addBehavior(EAdMouseEventImpl.getMouseEvent(MouseActionType.PRESSED, MouseButton.BUTTON_3), new EAdActorActionsEffect( item ));
		
		EAdSceneElementDef item2 = new EAdSceneElementDefImpl( new RectangleShape( 10, 10, EAdColor.BLUE ));
		EAdSceneElementDef item3 = new EAdSceneElementDefImpl( new RectangleShape( 90, 90, EAdColor.GREEN ));
		EAdInventory inventory = new EAdInventoryImpl();
//		inventory.getInitialItems().add(item);
		inventory.getInitialItems().add(item2);
		inventory.getInitialItems().add(item3);
		EAdElementsFactory.getInstance().setInventory(inventory);
		
		
		EAdBasicSceneElement key = new EAdBasicSceneElement( new ImageImpl("@drawable/ng_key.png") );
		key.setPosition(200, 200);
		EAdInventoryEffect effect = new EAdInventoryEffect( item, InventoryEffectAction.ADD_TO_INVENTORY );
		
		key.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, effect);
		
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
