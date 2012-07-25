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

package ead.engine.core.gameobjects.huds;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.BasicInventory;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.guievents.enums.MouseGEvButtonType;
import ead.common.model.elements.scenes.ComplexSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.SystemFields;
import ead.common.params.fills.ColorFill;
import ead.common.params.text.EAdString;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.resources.assets.text.BasicFont;
import ead.common.resources.assets.text.EAdFont;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;
import ead.engine.core.game.GameState;
import ead.engine.core.game.ValueMap;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.input.InputAction;
import ead.engine.core.input.InputHandler;
import ead.engine.core.input.actions.MouseInputAction;
import ead.engine.core.inventory.InventoryHandler;
import ead.engine.core.inventory.InventoryItem;
import ead.engine.core.platform.GUI;
import ead.engine.core.util.EAdTransformation;
import ead.tools.StringHandler;

@Singleton
public class InventoryHUDImpl extends AbstractHUD implements InventoryHUD {

	private static final int TIME_TO_SHOW = 500;

	private enum InventoryState {
		HIDDEN, GOING_UP, GOING_DOWN, SHOWN
	};

	private static final int INVENTORY_HEIGHT = 60;

	private ComplexSceneElement inventory;

	private ValueMap valueMap;

	private SceneElementGOFactory sceneElementFactory;

	private InputHandler inputHandler;

	private int guiHeight;

	private EAdField<Float> inventoryDispYField;

	private float inventoryDispY;

	private float disp;

	private InventoryState state = InventoryState.HIDDEN;

	private InventoryHandler inventoryHandler;

	private long currentUpdate = -1;

	private int delay = 0;

	private int mouseY = 0;

	public boolean isShowing;

	private Integer height;

	@Inject
	public InventoryHUDImpl(GUI gui, GameState gameState,
			SceneElementGOFactory factory, InventoryHandler inventoryHandler,
			InputHandler inputHandler) {
		super(gui);
		valueMap = gameState.getValueMap();
		this.sceneElementFactory = factory;
		this.inputHandler = inputHandler;
		this.inventoryHandler = inventoryHandler;
		isShowing = true;
		height = valueMap.getValue(SystemFields.GAME_HEIGHT);
	}

	@Override
	public void doLayout(EAdTransformation t) {
		if (isShowing) {
			super.doLayout(t);
		}
	}

	@Override
	public void update() {
		disp = (float) gui.getSkippedMilliseconds() / (float) TIME_TO_SHOW;
		isShowing = valueMap.getValue(SystemFields.SHOW_INVENTORY);
		if (isShowing) {
			mouseY = valueMap.getValue(SystemFields.MOUSE_Y);
			updateState();
			updateDisp();
			updateItems();
			super.update();
		}
	}

	private void initInventory() {
		int width = valueMap.getValue(SystemFields.GAME_WIDTH);
		guiHeight = valueMap.getValue(SystemFields.GAME_HEIGHT);
		RectangleShape rectangle = new RectangleShape(width, INVENTORY_HEIGHT);
		rectangle.setPaint(new ColorFill(200, 200, 200, 100));

		inventory = new ComplexSceneElement(rectangle);

		inventoryDispY = 0.0f;
		inventoryDispYField = new BasicField<Float>(inventory,
				SceneElement.VAR_DISP_Y);

		inventory.setPosition(new EAdPosition(0, guiHeight, 0.0f,
				inventoryDispY));
		SceneElementGO<?> go = sceneElementFactory.get(inventory);
		addElement(go);
	}

	private void updateState() {
		if (mouseY > guiHeight - INVENTORY_HEIGHT
				&& state == InventoryState.HIDDEN) {
			state = InventoryState.GOING_UP;
		}

		if (!isItemDragged()
				&& mouseY < guiHeight - INVENTORY_HEIGHT * 3
				&& (state == InventoryState.SHOWN || state == InventoryState.GOING_UP)) {
			state = InventoryState.GOING_DOWN;
		}
	}

	private void updateDisp() {
		if (delay <= 0) {
			boolean change = false;
			switch (state) {
			case GOING_UP:
				inventoryDispY = Math.min(inventoryDispY + disp, 1.0f);
				change = true;
				break;
			case GOING_DOWN:
				inventoryDispY = Math.max(inventoryDispY - disp, 0.0f);
				change = true;
				break;
			default:
				break;
			}

			if (change) {
				valueMap.setValue(inventoryDispYField, inventoryDispY);
				if (inventoryDispY >= 1.0f) {
					state = InventoryState.SHOWN;
				} else if (inventoryDispY <= 0.0f) {
					state = InventoryState.HIDDEN;
				}
			}
		} else {
			delay -= gui.getSkippedMilliseconds();
		}
	}

	private void updateItems() {
		if (currentUpdate != inventoryHandler.updateNumber()) {
			inventory.getSceneElements().clear();
			delay = 1000;
			this.inventoryDispY = 1.0f;
			valueMap.setValue(inventoryDispYField, inventoryDispY);
			state = InventoryState.GOING_DOWN;
			int x = INVENTORY_HEIGHT;
			for (InventoryItem i : inventoryHandler.getItems()) {
				SceneElement element = new SceneElement(i.getElement());
				element.setPosition(Corner.CENTER, x, INVENTORY_HEIGHT / 2);

				SceneElementGO<?> go = sceneElementFactory.get(element);
				float width = go.getWidth();
				float height = go.getHeight();
				float size = INVENTORY_HEIGHT * 0.8f;
				float scale = size / width < size / height ? size / width
						: size / height;

				sceneElementFactory.remove(element);

				element.setDragCond(EmptyCond.TRUE_EMPTY_CONDITION);
				element.setVarInitialValue(SceneElement.VAR_SCALE, scale);
				element.setVarInitialValue(BasicInventory.VAR_IN_INVENTORY,
						true);
				element.setVarInitialValue(
						SceneElement.VAR_RETURN_WHEN_DRAGGED, true);

				inventory.getSceneElements().add(element);

				if (i.getCount() > 1) {
					float counterSize = size / 3;
					SceneElement counter = getCounter(i.getCount(), counterSize);
					counter.setPosition(Corner.CENTER, x, INVENTORY_HEIGHT / 2);

					inventory.getSceneElements().add(counter);
				}
				x += INVENTORY_HEIGHT;
				sceneElementFactory.remove(inventory);
			}
			currentUpdate = inventoryHandler.updateNumber();
		}
	}

	private EAdFont counterFont = new BasicFont(10);

	private SceneElement getCounter(int count, float counterSize) {
		Caption number = new Caption(new EAdString(
				StringHandler.TEXTUAL_STRING_PREFIX + count));
		number.setTextPaint(ColorFill.WHITE);
		number.setBubblePaint(new ColorFill(0, 0, 0, 100));
		number.setPadding(3);
		number.setFont(counterFont);
		SceneElement numberElement = new SceneElement(number);
		numberElement.setPosition(Corner.CENTER, 0, 0);
		return numberElement;
	}

	public boolean isItemDragged() {
		SceneElementGO<?> go = inputHandler.getDraggingGameObject();
		if (go != null) {
			return valueMap.getValue(go.getElement(),
					BasicInventory.VAR_IN_INVENTORY);
		}
		return false;
	}

	@Override
	public boolean contains(int x, int y) {
		if (state != InventoryState.HIDDEN) {
			return true;
		}
		return false;
	}

	@Override
	public boolean processAction(InputAction<?> action) {
		if (action instanceof MouseInputAction) {
			MouseInputAction mouseAction = (MouseInputAction) action;

			if (height - INVENTORY_HEIGHT > mouseAction.getVirtualY()
					&& mouseAction.getButton() != MouseGEvButtonType.NO_BUTTON) {
				state = InventoryState.GOING_DOWN;
			}
		}
		return super.processAction(action);
	}

	@Override
	public void init() {
		initInventory();
		updateItems();
	}

}
