package es.eucm.eadventure.engine.core.gameobjects.impl;

import java.util.List;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public abstract class GameObjectImpl<T extends EAdElement> implements GameObject<T> {
	/**
	 * The game's asset handler
	 */
	protected AssetHandler assetHandler;

	/**
	 * The string handler
	 */
	protected StringHandler stringHandler;

	protected GameObjectFactory gameObjectFactory;

	protected GUI gui;

	protected GameState gameState;

	protected ValueMap valueMap;

	protected PlatformConfiguration platformConfiguration;

	protected T element;
	
	@Inject
	public GameObjectImpl(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		this.assetHandler = assetHandler;
		this.stringHandler = stringHandler;
		this.gameObjectFactory = gameObjectFactory;
		this.gui = gui;
		this.gameState = gameState;
		this.valueMap = valueMap;
		this.platformConfiguration = platformConfiguration;
	}
	
	@Override
	public void setElement(T element) {
		this.element = element;
	}
	
	@Override
	public T getElement() {
		return element;
	}
	
	@Override
	public GameObject<?> getDraggableElement(MouseState mouseState) {
		// Implemented by inherited classes
		return null;
	}

	@Override
	public void doLayout(int offsetX, int offsetY) {
		// Implemented by inherited classes

	}
	
	@Override
	public EAdPosition getPosition() {
		// Implemented by inherited classes
		return null;
	}
	
	@Override
	public void update(GameState state) {
	
	}
	
	@Override
	public boolean processAction(GUIAction action) {
		return false;
	}
	
	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {
		return assetList;
	}
}
