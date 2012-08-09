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
import ead.common.model.elements.BasicInventory;
import ead.common.model.elements.effects.ActorActionsEf;
import ead.common.model.elements.effects.ModifyInventoryEf;
import ead.common.model.elements.effects.enums.InventoryEffectAction;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.guievents.enums.MouseGEvButtonType;
import ead.common.model.elements.guievents.enums.MouseGEvType;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.params.fills.ColorFill;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.elementfactories.EAdElementsFactory;

public class InventoryScene extends EmptyScene {
	
	public InventoryScene( ){
		super();
		setId("InventoryScene");
		SceneElementDef item = new SceneElementDef( new Image("@drawable/ng_key.png"));
		
		item.getActions().add(EAdElementsFactory.getInstance().getActionsFactory().getBasicAction());
		
		item.addBehavior(MouseGEv.MOUSE_LEFT_CLICK, new ActorActionsEf( item ));
		item.addBehavior(MouseGEv.MOUSE_RIGHT_CLICK, new ActorActionsEf( item ));
		item.addBehavior(MouseGEv.getMouseEvent(MouseGEvType.PRESSED, MouseGEvButtonType.BUTTON_3), new ActorActionsEf( item ));
		
		EAdSceneElementDef item2 = new SceneElementDef( new RectangleShape( 10, 10, ColorFill.BLUE ));
		EAdSceneElementDef item3 = new SceneElementDef( new RectangleShape( 90, 90, ColorFill.GREEN ));
		EAdInventory inventory = new BasicInventory();
//		inventory.getInitialItems().add(item);
		inventory.getInitialItems().add(item2);
		inventory.getInitialItems().add(item3);
		EAdElementsFactory.getInstance().setInventory(inventory);
		
		
		SceneElement key = new SceneElement( new Image("@drawable/ng_key.png") );
		key.setPosition(200, 200);
		ModifyInventoryEf effect = new ModifyInventoryEf( item, InventoryEffectAction.ADD_TO_INVENTORY );
		
		key.addBehavior(MouseGEv.MOUSE_LEFT_CLICK, effect);
		
		getSceneElements().add(key);
	}
	
	@Override
	public String getSceneDescription() {
		return "A scene with inventory.";
	}

	public String getDemoName() {
		return "Inventory Scene";
	}

}
