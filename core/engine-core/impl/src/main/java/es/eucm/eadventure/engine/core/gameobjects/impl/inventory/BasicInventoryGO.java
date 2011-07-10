package es.eucm.eadventure.engine.core.gameobjects.impl.inventory;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.EAdInventory;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.impl.AbstractGameObject;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

/**
 * <p>
 * Basic inventory game object
 * </p>
 * <p>
 * The basic inventory represents the inventory as in eAdventure 1.0
 * </p>
 */
public abstract class BasicInventoryGO extends AbstractGameObject<EAdInventory> {

	@Inject
	public BasicInventoryGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);
	}

}
