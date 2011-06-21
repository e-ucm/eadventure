package es.eucm.eadventure.engine.core.platform.impl.extra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.eucm.eadventure.common.model.conditions.impl.VarCondition.Operator;
import es.eucm.eadventure.common.model.conditions.impl.VarValCondition;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.effects.impl.EAdMoveSceneElement.MovementSpeed;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.impl.EAdActorReferenceImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexSceneElement;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.params.EAdPosition.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.impl.RectangleShape;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.impl.ActorReferenceGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.inventory.BasicInventoryGO;

public class DesktopBasicInventoryGO extends BasicInventoryGO {

	private EAdBasicSceneElement bottomPart;

	private EAdBasicSceneElement topPart;

	private EAdComplexSceneElement inventory;
	
	private Map<EAdActor, EAdActorReferenceImpl> includedActors;
	
	private static int SENSE_HEIGHT = 40;
	
	public DesktopBasicInventoryGO() {
		bottomPart = new EAdBasicSceneElement("bottomInventory");
		RectangleShape rect = new RectangleShape(800, SENSE_HEIGHT + 2, EAdBorderedColor.TRANSPARENT);
		
		bottomPart.getResources().addAsset(bottomPart.getInitialBundle(), EAdBasicSceneElement.appearance, rect);
		bottomPart.setPosition(new EAdPosition(Corner.BOTTOM_LEFT, 0, 601));
		
		topPart = new EAdBasicSceneElement("topInventory");
		topPart.getResources().addAsset(topPart.getInitialBundle(), EAdBasicSceneElement.appearance, rect);
		topPart.setPosition(new EAdPosition(Corner.TOP_LEFT, 0, -1));
		
		inventory = new EAdComplexSceneElement("inventory");
		inventory.setPosition(new EAdPosition(Corner.BOTTOM_LEFT, 0, 700));
		
		RectangleShape rect2 = new RectangleShape(800, 100, EAdBorderedColor.BLACK_ON_WHITE);
		inventory.getResources().addAsset(inventory.getInitialBundle(), EAdBasicSceneElement.appearance, rect2);

		
		EAdMoveSceneElement e = new EAdMoveSceneElement("moveInventoryToBottom", inventory, 0, 700, MovementSpeed.INSTANT);
		bottomPart.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, e);
		e = new EAdMoveSceneElement("showInventoryBottom", inventory, 0, 600, MovementSpeed.FAST);
		bottomPart.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, e);
		
		EAdMoveSceneElement e2 = new EAdMoveSceneElement("hideInventoryBottom", inventory, 0, 700, MovementSpeed.NORMAL);
		e2.setCondition(new VarValCondition(inventory.positionYVar(), 350, Operator.GREATER));
		bottomPart.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, e2);
		

		e = new EAdMoveSceneElement("moveInventoryToTop", inventory, 0, 0, MovementSpeed.INSTANT);
		topPart.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, e);
		e = new EAdMoveSceneElement("showInventoryTop", inventory, 0, 100, MovementSpeed.FAST);
		topPart.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, e);

		
		e2 = new EAdMoveSceneElement("hideInventoryTop", inventory, 0, 0, MovementSpeed.NORMAL);
		e2.setCondition(new VarValCondition(inventory.positionYVar(), 350, Operator.LESS));
		topPart.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, e2);
		
		includedActors = new HashMap<EAdActor, EAdActorReferenceImpl>();
		
	}
	
	@Override
	public void doLayout(int offsetX, int offsetY) {
		gui.addElement(gameObjectFactory.get(inventory), 0, 0);
		gui.addElement(gameObjectFactory.get(bottomPart), 0, 0);
		gui.addElement(gameObjectFactory.get(topPart), 0, 0);
	}
	
	@Override
	public void update(GameState gameState) {
		super.update(gameState);
		gameObjectFactory.get(bottomPart).update(gameState);
		gameObjectFactory.get(topPart).update(gameState);
		gameObjectFactory.get(inventory).update(gameState);
		
		List<EAdActor> removedActors = new ArrayList<EAdActor>();
		addNewActors();
		for (EAdActor actor : includedActors.keySet()) {
			if (!gameState.getInventoryActors().contains(actor))
				removedActors.add(actor);
			else {
				EAdActorReferenceImpl ref = includedActors.get(actor);
				ref.getPosition().setX(20);
				ref.getPosition().setY(60);
				//TODO position actor
			}
		}
		removeOldActors(removedActors);
	}
	
	/**
	 * Add new actors (recently added ones) to the inventory
	 */
	private void addNewActors() {
		for (EAdActor actor : gameState.getInventoryActors())  {
			if (!includedActors.keySet().contains(actor)) {
				EAdActorReferenceImpl ref = new EAdActorReferenceImpl(actor);
				EAdActorActionsEffect showActions = new EAdActorActionsEffect( ref.getId()+ "_showActions", ref);
				ref.getBehavior().addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, showActions);
				((ActorReferenceGOImpl) gameObjectFactory.get(ref)).setInventoryReference(true);
				includedActors.put(actor, ref);
				inventory.getComponents().add(ref);
			}
		}
	}
	
	/**
	 * Remove now unused actors from the inventory
	 */
	private void removeOldActors(List<EAdActor> removedActors) {
		for (EAdActor actor : removedActors) {
			inventory.getComponents().remove(includedActors.get(actor));
			includedActors.remove(actor);
			//TODO free resources?
		}
	}
	
}
