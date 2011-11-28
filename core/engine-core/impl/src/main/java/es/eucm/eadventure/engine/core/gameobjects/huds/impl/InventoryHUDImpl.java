package es.eucm.eadventure.engine.core.gameobjects.huds.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexElementImpl;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.huds.InventoryHUD;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.GUI;

@Singleton
public class InventoryHUDImpl extends AbstractHUD implements InventoryHUD {

	private static final int INVENTORY_HEIGHT = 30;

	private EAdComplexElementImpl inventory;

	private ValueMap valueMap;

	@Inject
	public InventoryHUDImpl(GUI gui, GameState gameState) {
		super(gui);
		valueMap = gameState.getValueMap();
		initInventory();
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public boolean processAction(GUIAction action) {
		// TODO Auto-generated method stub
		return super.processAction(action);
	}

	private void initInventory() {
		int width = valueMap.getValue(SystemFields.GUI_WIDTH);
		int height = valueMap.getValue(SystemFields.GUI_HEIGHT);
		RectangleShape rectangle = new RectangleShape(width, INVENTORY_HEIGHT);
		rectangle.setPaint(new EAdColor(200, 200, 200, 100));

		inventory = new EAdComplexElementImpl();
		inventory.getResources().addAsset(inventory.getInitialBundle(),
				EAdBasicSceneElement.appearance, rectangle);
		
//		inventory.setPosition(new EAdPositionImpl( ));

	}

}
