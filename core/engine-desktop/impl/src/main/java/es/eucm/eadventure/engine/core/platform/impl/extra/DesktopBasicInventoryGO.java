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

package es.eucm.eadventure.engine.core.platform.impl.extra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarCondition.Operator;
import es.eucm.eadventure.common.model.conditions.impl.VarValCondition;
import es.eucm.eadventure.common.model.effects.EAdMacro;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeAppearance;import es.eucm.eadventure.common.model.effects.impl.EAdMacroImpl;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;

import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement.MovementSpeed;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.impl.EAdActorReferenceImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexSceneElement;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;
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
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState,
				valueMap, platformConfiguration);

		inventory = new EAdComplexSceneElement("inventory");
		inventory.setDraggabe(EmptyCondition.FALSE_EMPTY_CONDITION);
		inventory.setPosition(new EAdPositionImpl(Corner.BOTTOM_LEFT, 0, 700));
		RectangleShape rect2 = new RectangleShape(800, INVENTORY_HEIGHT,
				EAdBorderedColor.BLACK_ON_WHITE);
		inventory.getResources().addAsset(inventory.getInitialBundle(),
				EAdBasicSceneElement.appearance, rect2);

		inventoryContent = new EAdComplexSceneElement("inventoryContent");
		inventoryContent.setDraggabe(EmptyCondition.FALSE_EMPTY_CONDITION);
		inventoryContent.setPosition(new EAdPositionImpl(Corner.TOP_LEFT,
				INVENTORY_HEIGHT / 2 + 10, 0));
		rect2 = new RectangleShape(800, INVENTORY_HEIGHT,
				EAdBorderedColor.TRANSPARENT);
		inventoryContent.getResources().addAsset(
				inventoryContent.getInitialBundle(),
				EAdBasicSceneElement.appearance, rect2);
		inventory.getComponents().add(inventoryContent);

		createArrow("Right", "+", 800, Corner.TOP_RIGHT);
		createArrow("Left", "-", 0, Corner.TOP_LEFT);

		RectangleShape rect = new RectangleShape(800, SENSE_HEIGHT + 2,
				EAdBorderedColor.TRANSPARENT);

		createCenterPart();

		bottomSensor = createSensorPart(rect, 601, 600, 700);

		topSensor = createSensorPart(rect, -1, 100, 0);

		includedActors = new HashMap<EAdActor, EAdActorReferenceImpl>();
	}

	/**
	 * Create an inventory arrow
	 * 
	 * @param dirname
	 *            the name of the direction (to get the resoucers, i.e. "Left"
	 *            or "Right")
	 * @param sign
	 *            The sign of the increment/decrement
	 * @param pos
	 *            The position of the arrow
	 * @param corner
	 *            The center of the arrow image
	 * @return the arrow
	 */
	private EAdBasicSceneElement createArrow(String dirname, String sign,
			int pos, Corner corner) {
		EAdBasicSceneElement arrow = new EAdBasicSceneElement("arrow" + dirname);
		arrow.setDraggabe(EmptyCondition.FALSE_EMPTY_CONDITION);
		arrow.setPosition(new EAdPositionImpl(corner, pos, 0));
		ImageImpl image = new ImageImpl("@drawable/arrow" + dirname + ".png");
		arrow.getResources().addAsset(arrow.getInitialBundle(),
				EAdBasicSceneElement.appearance, image);

		ImageImpl image2 = new ImageImpl("@drawable/arrowHighlight" + dirname
				+ ".png");
		EAdBundleId highlightBundle = new EAdBundleId("highlight");
		arrow.getResources().addAsset(highlightBundle,
				EAdBasicSceneElement.appearance, image2);

		arrow.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED,
				new EAdChangeAppearance("id", arrow, highlightBundle));
		arrow.addBehavior(EAdMouseEventImpl.MOUSE_EXITED,
				new EAdChangeAppearance("id", arrow, arrow.getInitialBundle()));

		inventory.getComponents().add(arrow);

		EAdFieldImpl<Integer> xField = new EAdFieldImpl<Integer>(
				inventoryContent, EAdBasicSceneElement.VAR_X);
		arrow.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK,
				new EAdVarInterpolationEffect("id", xField,
						new LiteralExpressionOperation("id", "[0]" + sign
								+ INVENTORY_HEIGHT, xField),
						200));
		return arrow;
	}

	/**
	 * Create the center sensor part
	 */
	private void create								+ INVENTORY_HEIGHT, xField), 200));
lement("centerPart");
		centerSensor.setDraggabe(EmptyCondition.FALSE_EMPTY_CONDITION);
		centerSensor.getResources().addAsset(centerSensor.getInitialBundle(),
				EAdBasicSceneElement.appearance,
				new RectangleShape(800, 600, EAdBorderedColor.TRANSPARENT));
		centerSensor.setPosition(new EAdPositionImpl(Corner.TOP_LEFT, 0, 0));

		EAdFieldImpl<Integer> yField = new EAdFieldImpl<Integer>(inventory,
				EAdBasicSceneElement.VAR_Y);

		EAdFieldImpl<Boolean> visibleField = new EAdFieldImpl<Boolean>(
				centerSensor, EAdBasicSceneElement.VAR_VISIBLE);

		EAdEffect e2 = new EAdMoveSceneElement("hideInventoryBottom",
				inventory, 0, 700, MovementSpeed.NORMAL);
		e2.setCondition(		// Hide inventory bottom
		EAdMacro macro = new EAdMacroImpl("hideInventory");

		macro.getEffects().add(
				new EAdMoveSceneElement("hideInventoryBottom", inventory, 0,
						700, MovementSpeed.NORMAL));

		macro.getEffects().add(
				new EAdChangeFieldValueEffect("id", visibleField,
						BooleanOperation.FALSE_OP));

		EAdTriggerMacro triggerMacro = new EAdTriggerMacro(macro);
		triggerMacro.setCondition(new VarValCondition(yField, 350,
				Operator.GREATER));
		centerSensor.addBehavior(EAdMouseEventImpl.MOUSE_MOVED, triggerMacro);

		// Hide inventory top
		macro = new EAdMacroImpl("hideInventory");

		macro.getEffects().add(
				new EAdMoveSceneElement("hideInventoryTop", inventory, 0, 0,
						MovementSpeed.NORMAL));

		macro.getEffects().add(
				new EAdChangeFieldValueEffect("id", visibleField,
						BooleanOperation.FALSE_OP));
ition(yField, 350, Operator.LESS));
		centerSensor.addBehavior(EAdMouseEventImpl.MOUSE_MOVED, e2);
		e2 = new EAdChangeFieldValueEffect("id", visibleField,
				BooleanOperation.FALSE_OP);
		e2.setCondition(new VarValCondition(yField, 350, Operator.LESS));
		centerSensor.addBehavior(EAdMouseEventImpl.MOUSE_MOVED, e2);
	}

	/**
	 * Create sensor part
	 * 
	 * @param rect
	 *           		triggerMacro = new EAdTriggerMacro(macro);
		triggerMacro.setCondition(new VarValCondition(yField, 350,
				Operator.LESS));
		centerSensor.addBehavior(EAdMouseEventImpl.MOUSE_MOVED, triggerMacro);
nventorySensor");
		part.setDraggabe(EmptyCondition.FALSE_EMPTY_CONDITION);
		part.getResources().addAsset(part.getInitialBundle(),
				EAdBasicSceneElement.appearance, rect);
		part.setPosition(new EAdPositionImpl(Corner.BOTTOM_LEFT, 0, sensorPos));

		EAdEffect e = new EAdMoveSceneElement("moveInventory", inventory, 0,
				hidePos, MovementSpeed.INSTANT);
		part.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, e);
		e = new EAdMoveSceneElement("showInventory", inventory, 0,
				inventoryPos, MovementSpeed.FAST);
		p		EAdMacroImpl macro = new EAdMacroImpl("showInventoryMacro");
		macro.getEffects().add(
				new EAdMoveSceneElement("moveInventory", inventory, 0, hidePos,
						MovementSpeed.INSTANT));

		macro.getEffects().add(
				new EAdMoveSceneElement("showInventory", inventory, 0,
						inventoryPos, MovementSpeed.FAST));

		macro.getEffects().add(
				new EAdChangeFieldValueEffect("showCentralSensor",
						new EAdFieldImpl<Boolean>(centerSensor,
								EAdBasicSceneElement.VAR_VISIBLE),
						BooleanOperation.TRUE_OP));

		part.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, new EAdTriggerMacro(
				"showInventoryMacro", macro));
addElement(gameObjectFactory.get(bottomSensor), 0, 0);
//			gui.addElement(gameObjectFactory.get(topSensor), 0, 0);
		}
	}

	@Override
	public void update(GameState gameState) {
		super.update(gameState);
		gameObj			gui.addElement(gameObjectFactory.get(centerSensor), 0, 0);
			gui.addElement(gameObjectFactory.get(inventory), 0, 0);
			gui.addElement(gameObjectFactory.get(bottomSensor), 0, 0);
			gui.addElement(gameObjectFactory.get(topSensor), 0, 0);

		for (EAdActor actor : includedActors.keySet()) {
			if (!gameState.getInventoryActors().contains(actor))
				removedActors.add(actor);
			else {
				EAdActorReferenceImpl ref = includedActors.get(actor);
				valueMap.setValue(ref, EAdBasicSceneElement.VAR_X, cont
						* (10 + INVENTORY_HEIGHT) + INVENTORY_HEIGHT / 2);

				// TODO position actor
				cont++;
			}
		}
		removeOldActors(removedActors);
	}

	/**
	 * Add new actors (recently added ones) to the inventory
	 */
	private void addNewActors() {
		for (EAdActor actor : gameState.getInventoryActors()) {
			if (!includedActors.keySet().contains(actor)) {
				EAdActorReferenceImpl ref = new EAdActorReferenceImpl(actor);
				EAdActorActionsEffect showActions = new EAdActorActionsEffect(
						ref.getId() + "_showActions", ref);
				ref.getBehavior().addBehavior(
						EAdMouseEventImpl.MOUSE_RIGHT_CLICK, showActions);
				ref.setPosition(new EAdPositionImpl(
						EAdPositionImpl.Corner.CENTER,
						INVENTORY_HEIGHT / 2 + 10, INVENTORY_HEIGHT / 2));
				((ActorReferenceGOImpl) gameObjectFactory.get(ref))
						.setInventoryReference(true);
				includedActors.put(actor, ref);
				inventoryContent.getComponents().add(ref);
				SceneElementGO<?> go = (SceneElementGO<?>) gameObjectFactory
						.get(ref);
				int maxSide = Math.max(go.getRenderAsset().getHeight(), go
						.getRenderAsset().getWidth());
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
			// TODO free resources?
		}
	}

}
