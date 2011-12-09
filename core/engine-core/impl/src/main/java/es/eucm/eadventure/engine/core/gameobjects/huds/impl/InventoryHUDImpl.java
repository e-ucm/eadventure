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

package es.eucm.eadventure.engine.core.gameobjects.huds.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexElementImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdInventoryImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.params.EAdFont;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.huds.InventoryHUD;
import es.eucm.eadventure.engine.core.inventory.InventoryHandler;
import es.eucm.eadventure.engine.core.inventory.InventoryItem;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

@Singleton
public class InventoryHUDImpl extends AbstractHUD implements InventoryHUD {

	private static final int TIME_TO_SHOW = 300;

	private enum InventoryState {
		HIDDEN, GOING_UP, GOING_DOWN, SHOWN
	};

	private static final int INVENTORY_HEIGHT = 60;

	private EAdComplexElementImpl inventory;

	private ValueMap valueMap;

	private SceneElementGOFactory sceneElementFactory;

	private MouseState mouseState;

	private int guiHeight;

	private EAdField<Float> inventoryDispYField;

	private float inventoryDispY;

	private float disp;

	private InventoryState state = InventoryState.HIDDEN;

	private InventoryHandler inventoryHandler;

	private long currentUpdate = -1;

	@Inject
	public InventoryHUDImpl(GUI gui, GameState gameState,
			SceneElementGOFactory factory, MouseState mouseState,
			InventoryHandler inventoryHandler) {
		super(gui);
		valueMap = gameState.getValueMap();
		this.sceneElementFactory = factory;
		this.mouseState = mouseState;
		disp = (float) GameLoop.SKIP_MILLIS_TICK / (float) TIME_TO_SHOW;
		this.inventoryHandler = inventoryHandler;
		initInventory();
		updateItems();
	}

	@Override
	public void update() {
		updateState();
		updateDisp();
		updateItems();
		super.update();
	}

	private void initInventory() {
		int width = valueMap.getValue(SystemFields.GUI_WIDTH);
		guiHeight = valueMap.getValue(SystemFields.GUI_HEIGHT);
		RectangleShape rectangle = new RectangleShape(width, INVENTORY_HEIGHT);
		rectangle.setPaint(new EAdColor(200, 200, 200, 100));

		inventory = new EAdComplexElementImpl(rectangle);

		inventoryDispY = 0.0f;
		inventoryDispYField = new EAdFieldImpl<Float>(inventory,
				EAdBasicSceneElement.VAR_DISP_Y);

		inventory.setPosition(new EAdPositionImpl(0, guiHeight, 0.0f,
				inventoryDispY));
		addElement(sceneElementFactory.get(inventory));
	}

	@Override
	public void doLayout(EAdTransformation t) {
		super.doLayout(t);
	}

	private void updateState() {
		if (mouseState.getMouseScaledY() > guiHeight - INVENTORY_HEIGHT
				&& state == InventoryState.HIDDEN) {
			state = InventoryState.GOING_UP;
		}

		if (mouseState.getMouseScaledY() < guiHeight - INVENTORY_HEIGHT * 3
				&& (state == InventoryState.SHOWN || state == InventoryState.GOING_UP)) {
			state = InventoryState.GOING_DOWN;
		}
	}

	private void updateDisp() {
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
		}

		if (change) {
			valueMap.setValue(inventoryDispYField, inventoryDispY);
			if (inventoryDispY >= 1.0f) {
				state = InventoryState.SHOWN;
			} else if (inventoryDispY <= 0.0f) {
				state = InventoryState.HIDDEN;
			}
		}
	}

	private void updateItems() {
		if (currentUpdate != inventoryHandler.updateNumber()) {
			inventory.getComponents().clear();
			int x = INVENTORY_HEIGHT;
			for (InventoryItem i : inventoryHandler.getItems()) {
				EAdBasicSceneElement element = new EAdBasicSceneElement(
						i.getElement());
				element.setPosition(Corner.CENTER, x, INVENTORY_HEIGHT / 2);

				SceneElementGO<?> go = sceneElementFactory.get(element);
				float width = go.getWidth();
				float height = go.getHeight();
				float size = INVENTORY_HEIGHT * 0.8f;
				float scale = size / width < size / height ? size / width
						: size / height;

				sceneElementFactory.remove(element);

				element.setDragCond(EmptyCondition.TRUE_EMPTY_CONDITION);
				element.setVarInitialValue(EAdBasicSceneElement.VAR_SCALE,
						scale);
				element.setVarInitialValue(EAdInventoryImpl.VAR_IN_INVENTORY,
						true);

				inventory.getComponents().add(element);

				if (i.getCount() > 1) {
					float counterSize = size / 3;
					EAdBasicSceneElement counter = getCounter(i.getCount(),
							counterSize);
					counter.setPosition(Corner.CENTER, x, INVENTORY_HEIGHT / 2);

					inventory.getComponents().add(counter);
				}
				x += INVENTORY_HEIGHT;
			}
			currentUpdate = inventoryHandler.updateNumber();
		}
	}

	private EAdFont counterFont = new EAdFontImpl(10);

	private EAdBasicSceneElement getCounter(int count, float counterSize) {
		CaptionImpl number = new CaptionImpl(new EAdString(
				StringHandler.TEXTUAL_STRING_PREFIX + count));
		number.setTextPaint(EAdColor.WHITE);
		number.setBubblePaint(new EAdColor(0, 0, 0, 100));
		number.setPadding(3);
		number.setFont(counterFont);
		EAdBasicSceneElement numberElement = new EAdBasicSceneElement(number);
		numberElement.setPosition(Corner.CENTER, 0, 0);
		return numberElement;
	}
}
