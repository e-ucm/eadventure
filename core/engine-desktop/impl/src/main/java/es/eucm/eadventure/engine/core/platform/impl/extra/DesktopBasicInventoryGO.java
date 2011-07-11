package es.eucm.eadventure.engine.core.platform.impl.extra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarCondition.Operator;
import es.eucm.eadventure.common.model.conditions.impl.VarValCondition;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeAppearance;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement.MovementSpeed;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.impl.EAdActorReferenceImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexSceneElement;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.params.EAdPosition.Corner;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.impl.RectangleShape;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.inventory.BasicInventoryGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.ActorReferenceGOImpl;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

/**
 * Desktop implementation of the {@link BasicInventoryGO}
 */
public class DesktopBasicInventoryGO extends BasicInventoryGO {
	
	/**
	 * The height of the top and bottom sensors
	 */
	private static int SENSE_HEIGHT = 40;
	
	/**
	 * The inventory height
	 */
	private static int INVENTORY_HEIGHT = 100;

	/**
	 * bottom sensor which activates the inventory
	 */
	private EAdBasicSceneElement bottomSensor;

	/**
	 * top sensor which activates the inventory
	 */
	private EAdBasicSceneElement topSensor;
	
	/**
	 * center sensor, which hides the inventory
	 */
	private EAdBasicSceneElement centerSensor;
	
	/**
	 * the actual inventory
	 */
	private EAdComplexSceneElement inventory;
	
	/**
	 * the object that contains the elements in the inventory
	 */
	private EAdComplexSceneElement inventoryContent;
	
	/**
	 * the map of actors and actor references in the inventory
	 */
	private Map<EAdActor, EAdActorReferenceImpl> includedActors;
	
	@Inject
	public DesktopBasicInventoryGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);

		inventory = new EAdComplexSceneElement("inventory");
		inventory.setDraggabe(EmptyCondition.FALSE_EMPTY_CONDITION);
		inventory.setPosition(new EAdPosition(Corner.BOTTOM_LEFT, 0, 700));
		RectangleShape rect2 = new RectangleShape(800, INVENTORY_HEIGHT, EAdBorderedColor.BLACK_ON_WHITE);
		inventory.getResources().addAsset(inventory.getInitialBundle(), EAdBasicSceneElement.appearance, rect2);
		
		inventoryContent = new EAdComplexSceneElement("inventoryContent");
		inventoryContent.setDraggabe(EmptyCondition.FALSE_EMPTY_CONDITION);
		inventoryContent.setPosition(new EAdPosition(Corner.TOP_LEFT, INVENTORY_HEIGHT / 2 + 10, 0));
		rect2 = new RectangleShape(800, INVENTORY_HEIGHT, EAdBorderedColor.TRANSPARENT);
		inventoryContent.getResources().addAsset(inventoryContent.getInitialBundle(), EAdBasicSceneElement.appearance, rect2);
		inventory.getComponents().add(inventoryContent);

		createArrow("Right", "+", 800, Corner.TOP_RIGHT);
		createArrow("Left", "-", 0, Corner.TOP_LEFT);
		
		RectangleShape rect = new RectangleShape(800, SENSE_HEIGHT + 2, EAdBorderedColor.TRANSPARENT);

		createCenterPart();

		bottomSensor = createSensorPart(rect, 601, 600, 700);
		
		topSensor = createSensorPart(rect, -1, 100, 0);		
		
		includedActors = new HashMap<EAdActor, EAdActorReferenceImpl>();
	}
	
	/**
	 * Create an inventory arrow
	 * 
	 * @param dirname the name of the direction (to get the resoucers, i.e. "Left" or "Right")
	 * @param sign The sign of the increment/decrement
	 * @param pos The position of the arrow
	 * @param corner The center of the arrow image
	 * @return the arrow
	 */
	private EAdBasicSceneElement createArrow(String dirname, String sign, int pos, Corner corner) {
		EAdBasicSceneElement arrow = new EAdBasicSceneElement("arrow" + dirname);
		arrow.setDraggabe(EmptyCondition.FALSE_EMPTY_CONDITION);
		arrow.setPosition(new EAdPosition(corner, pos, 0));
		ImageImpl image = new ImageImpl("@drawable/arrow" + dirname + ".png");
		arrow.getResources().addAsset(arrow.getInitialBundle(), EAdBasicSceneElement.appearance, image);
		
		ImageImpl image2 = new ImageImpl("@drawable/arrowHighlight" + dirname + ".png");
		EAdBundleId highlightBundle = new EAdBundleId("highlight");
		arrow.getResources().addAsset(highlightBundle, EAdBasicSceneElement.appearance, image2);

		arrow.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, new EAdChangeAppearance("id", arrow, highlightBundle));
		arrow.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, new EAdChangeAppearance("id", arrow, arrow.getInitialBundle()));

		inventory.getComponents().add(arrow);
		
		arrow.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, new EAdVarInterpolationEffect("id", inventoryContent.positionXVar(), new LiteralExpressionOperation("id", "[0]" + sign + INVENTORY_HEIGHT, inventoryContent.positionXVar()), 200));
		return arrow;
	}

	/**
	 * Create the center sensor part
	 */
	private void createCenterPart() {
		centerSensor = new EAdBasicSceneElement("centerPart");
		centerSensor.setDraggabe(EmptyCondition.FALSE_EMPTY_CONDITION);
		centerSensor.getResources().addAsset(centerSensor.getInitialBundle(), EAdBasicSceneElement.appearance,  new RectangleShape(800, 600, EAdBorderedColor.TRANSPARENT));
		centerSensor.setPosition(new EAdPosition(Corner.TOP_LEFT, 0, 0));
		
		EAdEffect e2 = new EAdMoveSceneElement("hideInventoryBottom", inventory, 0, 700, MovementSpeed.NORMAL);
		e2.setCondition(new VarValCondition(inventory.positionYVar(), 350, Operator.GREATER));
		centerSensor.addBehavior(EAdMouseEventImpl.MOUSE_MOVED, e2);
		e2 = new EAdChangeVarValueEffect("id", centerSensor.visibleVar(), BooleanOperation.FALSE_OP);
		e2.setCondition(new VarValCondition(inventory.positionYVar(), 350, Operator.GREATER));
		centerSensor.addBehavior(EAdMouseEventImpl.MOUSE_MOVED, e2);

		e2 = new EAdMoveSceneElement("hideInventoryTop", inventory, 0, 0, MovementSpeed.NORMAL);
		e2.setCondition(new VarValCondition(inventory.positionYVar(), 350, Operator.LESS));
		centerSensor.addBehavior(EAdMouseEventImpl.MOUSE_MOVED, e2);
		e2 = new EAdChangeVarValueEffect("id", centerSensor.visibleVar(), BooleanOperation.FALSE_OP);
		e2.setCondition(new VarValCondition(inventory.positionYVar(), 350, Operator.LESS));
		centerSensor.addBehavior(EAdMouseEventImpl.MOUSE_MOVED, e2);
	}
	
	/**
	 * Create sensor part
	 * 
	 * @param rect The rectangle resource
	 * @param sensorPos The position of the sensor part
	 * @param inventoryPos The position of the inventory
	 * @param hidePos The position of the inventory when hidden
	 * @return The sensor part
	 */
	private EAdBasicSceneElement createSensorPart(RectangleShape rect, int sensorPos, int inventoryPos, int hidePos) {
		EAdBasicSceneElement part = new EAdBasicSceneElement("inventorySensor");
		part.setDraggabe(EmptyCondition.FALSE_EMPTY_CONDITION);
		part.getResources().addAsset(part.getInitialBundle(), EAdBasicSceneElement.appearance, rect);
		part.setPosition(new EAdPosition(Corner.BOTTOM_LEFT, 0, sensorPos));
		
		EAdEffect e = new EAdMoveSceneElement("moveInventory", inventory, 0, hidePos, MovementSpeed.INSTANT);
		part.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, e);
		e = new EAdMoveSceneElement("showInventory", inventory, 0, inventoryPos, MovementSpeed.FAST);
		part.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, e);
		e = new EAdChangeVarValueEffect("showCentralSensor", centerSensor.visibleVar(), BooleanOperation.TRUE_OP);
		part.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, e);
		return part;
	}
	
	@Override
	public void doLayout(int offsetX, int offsetY) {
		gui.addElement(gameObjectFactory.get(centerSensor), 0, 0);
		gui.addElement(gameObjectFactory.get(inventory), 0, 0);
		gui.addElement(gameObjectFactory.get(bottomSensor), 0, 0);
		gui.addElement(gameObjectFactory.get(topSensor), 0, 0);
	}
	
	@Override
	public void update(GameState gameState) {
		super.update(gameState);
		gameObjectFactory.get(bottomSensor).update(gameState);
		gameObjectFactory.get(topSensor).update(gameState);
		gameObjectFactory.get(inventory).update(gameState);
		
		List<EAdActor> removedActors = new ArrayList<EAdActor>();
		addNewActors();
		int cont = 0;
		for (EAdActor actor : includedActors.keySet()) {
			if (!gameState.getInventoryActors().contains(actor))
				removedActors.add(actor);
			else {
				EAdActorReferenceImpl ref = includedActors.get(actor);
				valueMap.setValue(ref.positionXVar(), cont * (10 + INVENTORY_HEIGHT) + INVENTORY_HEIGHT / 2);
				
				//TODO position actor
				cont++;
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
				ref.setPosition(new EAdPosition(EAdPosition.Corner.CENTER, INVENTORY_HEIGHT / 2 + 10, INVENTORY_HEIGHT / 2));
				((ActorReferenceGOImpl) gameObjectFactory.get(ref)).setInventoryReference(true);
				includedActors.put(actor, ref);
				inventoryContent.getComponents().add(ref);
				SceneElementGO<?> go = (SceneElementGO<?>) gameObjectFactory.get(ref);
				int maxSide = Math.max(go.getAsset().getHeight(), go.getAsset().getWidth());
				float scale = (float) INVENTORY_HEIGHT / maxSide;
				go.setScale(scale);
			}
		}
	}
	
	/**
	 * Remove now unused actors from the inventory
	 */
	private void removeOldActors(List<EAdActor> removedActors) {
		for (EAdActor actor : removedActors) {
			inventoryContent.getComponents().remove(includedActors.get(actor));
			includedActors.remove(actor);
			//TODO free resources?
		}
	}
	
}
