package es.eucm.eadventure.common.elementfactories.scenedemos;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdInventoryEffect;
import es.eucm.eadventure.common.model.effects.impl.enums.InventoryEffectAction;
import es.eucm.eadventure.common.model.elements.EAdInventory;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdInventoryImpl;
import es.eucm.eadventure.common.model.guievents.enums.MouseActionType;
import es.eucm.eadventure.common.model.guievents.enums.MouseButton;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

public class InventoryScene extends EmptyScene {
	
	public InventoryScene( ){
		super();
		EAdBasicSceneElement item = new EAdBasicSceneElement( new ImageImpl("@drawable/ng_key.png"));
		
		item.getActions().add(EAdElementsFactory.getInstance().getActionsFactory().getBasicAction());
		
		item.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, new EAdActorActionsEffect( item ));
		item.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, new EAdActorActionsEffect( item ));
//		item.addBehavior(EAdMouseEventImpl.getMouseEvent(MouseActionType.PRESSED, MouseButton.BUTTON_3), new EAdActorActionsEffect( item ));
		
		EAdBasicSceneElement item2 = new EAdBasicSceneElement( new RectangleShape( 10, 10, EAdColor.BLUE ));
		EAdBasicSceneElement item3 = new EAdBasicSceneElement( new RectangleShape( 90, 90, EAdColor.GREEN ));
		EAdInventory inventory = new EAdInventoryImpl();
//		inventory.getInitialInventory().add(item);
		inventory.getInitialInventory().add(item2);
		inventory.getInitialInventory().add(item3);
		EAdElementsFactory.getInstance().setInventory(inventory);
		
		
		EAdBasicSceneElement key = new EAdBasicSceneElement( new ImageImpl("@drawable/ng_key.png") );
		key.setPosition(200, 200);
		EAdInventoryEffect effect = new EAdInventoryEffect( item, InventoryEffectAction.ADD_TO_INVENTORY );
		
		key.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, effect);
		
		getElements().add(key);
	}
	
	@Override
	public String getSceneDescription() {
		return "A scene with inventory.";
	}

	public String getDemoName() {
		return "Inventory Scene";
	}

}
