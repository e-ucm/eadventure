package es.eucm.eadventure.engine.core.gameobjects.huds.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexElementImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
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

		inventory = new EAdComplexElementImpl();
		inventory.getResources().addAsset(inventory.getInitialBundle(),
				EAdBasicSceneElement.appearance, rectangle);

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

	public void updateState() {
		if (mouseState.getMouseY() > guiHeight - INVENTORY_HEIGHT
				&& state == InventoryState.HIDDEN) {
			state = InventoryState.GOING_UP;
		}

		if (mouseState.getMouseY() < guiHeight - INVENTORY_HEIGHT * 3
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
			inventory.getElements().clear();
			int x = INVENTORY_HEIGHT;
			for (InventoryItem i : inventoryHandler.getItems()) {
				EAdBasicSceneElement element = new EAdBasicSceneElement(
						i.getElement());
				element.setPosition(Corner.CENTER, x, INVENTORY_HEIGHT / 2);
				x += INVENTORY_HEIGHT;

				SceneElementGO<?> go = sceneElementFactory.get(element);
				float width = go.getWidth();
				float height = go.getHeight();
				float size = INVENTORY_HEIGHT * 0.8f;
				float scale = size / width < size / height ? size / width
						: size / height;
				
//				element.setDraggableCondition(EmptyCondition.TRUE_EMPTY_CONDITION);

				sceneElementFactory.remove(element);

				element.setVarInitialValue(EAdBasicSceneElement.VAR_SCALE,
						scale);

				inventory.getElements().add(element);
			}
			currentUpdate = inventoryHandler.updateNumber();
		}
	}
}
