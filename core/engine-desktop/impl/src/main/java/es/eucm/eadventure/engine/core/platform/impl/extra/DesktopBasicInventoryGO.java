package es.eucm.eadventure.engine.core.platform.impl.extra;

import es.eucm.eadventure.common.model.conditions.impl.VarCondition.Operator;
import es.eucm.eadventure.common.model.conditions.impl.VarValCondition;
import es.eucm.eadventure.common.model.effects.impl.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.effects.impl.EAdMoveSceneElement.MovementSpeed;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexSceneElement;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.params.EAdPosition.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.impl.RectangleShape;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.impl.inventory.BasicInventoryGO;

public class DesktopBasicInventoryGO extends BasicInventoryGO {

	private EAdBasicSceneElement bottomPart;

	private EAdBasicSceneElement topPart;

	private EAdComplexSceneElement inventory;
	
	public DesktopBasicInventoryGO() {
		bottomPart = new EAdBasicSceneElement("");
		RectangleShape rect = new RectangleShape(800, 40, EAdBorderedColor.TRANSPARENT);
		
		bottomPart.getResources().addAsset(bottomPart.getInitialBundle(), EAdBasicSceneElement.appearance, rect);
		bottomPart.setPosition(new EAdPosition(Corner.BOTTOM_LEFT, 0, 600));
		
		topPart = new EAdBasicSceneElement("");
		topPart.getResources().addAsset(topPart.getInitialBundle(), EAdBasicSceneElement.appearance, rect);
		topPart.setPosition(new EAdPosition(Corner.TOP_LEFT, 0, 0));
		
		inventory = new EAdComplexSceneElement("");
		inventory.setPosition(new EAdPosition(Corner.BOTTOM_LEFT, 0, 700));
		
		RectangleShape rect2 = new RectangleShape(800, 100, EAdBorderedColor.BLACK_ON_WHITE);
		inventory.getResources().addAsset(inventory.getInitialBundle(), EAdBasicSceneElement.appearance, rect2);

		
		EAdMoveSceneElement e = new EAdMoveSceneElement("", inventory, 0, 700, MovementSpeed.INSTANT);
		bottomPart.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, e);
		e = new EAdMoveSceneElement("", inventory, 0, 600, MovementSpeed.FAST);
		bottomPart.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, e);
		
		EAdMoveSceneElement e2 = new EAdMoveSceneElement("", inventory, 0, 700, MovementSpeed.NORMAL);
		e2.setCondition(new VarValCondition(inventory.positionYVar(), 350, Operator.GREATER));
		inventory.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, e2);
		

		e = new EAdMoveSceneElement("", inventory, 0, 0, MovementSpeed.INSTANT);
		topPart.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, e);
		e = new EAdMoveSceneElement("", inventory, 0, 100, MovementSpeed.FAST);
		topPart.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, e);

		
		e2 = new EAdMoveSceneElement("", inventory, 0, 0, MovementSpeed.NORMAL);
		e2.setCondition(new VarValCondition(inventory.positionYVar(), 350, Operator.LESS));
		inventory.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, e2);


	}
	
	@Override
	public void doLayout(int offsetX, int offsetY) {
		gui.addElement(gameObjectFactory.get(bottomPart), 0, 0);
		gui.addElement(gameObjectFactory.get(topPart), 0, 0);
		gui.addElement(gameObjectFactory.get(inventory), 0, 0);
	}
	
	@Override
	public void update(GameState gameState) {
		super.update(gameState);
		gameObjectFactory.get(bottomPart).update(gameState);
		gameObjectFactory.get(topPart).update(gameState);
		gameObjectFactory.get(inventory).update(gameState);
	}
	
}
