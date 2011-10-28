package es.eucm.eadventure.engine.core.gameobjects.impl;

import java.util.List;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.util.EAdTransformation;
import es.eucm.eadventure.engine.core.util.impl.EAdTransformationImpl;

public abstract class GameObjectImpl<T extends EAdElement> implements
		GameObject<T> {
	/**
	 * The game's asset handler
	 */
	protected AssetHandler assetHandler;

	/**
	 * The string handler
	 */
	protected StringHandler stringsReader;

	protected GameObjectFactory gameObjectFactory;

	protected GUI gui;

	protected GameState gameState;

	protected T element;

	protected EAdTransformationImpl transformation;
	
	protected boolean enable;

	@Inject
	public GameObjectImpl(AssetHandler assetHandler,
			StringHandler stringReader, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState) {
		this.assetHandler = assetHandler;
		this.stringsReader = stringReader;
		this.gameObjectFactory = gameObjectFactory;
		this.gui = gui;
		this.gameState = gameState;
		this.enable = false;
	}

	@Override
	public void setElement(T element) {
		this.element = element;
		this.transformation = new EAdTransformationImpl();
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
	public void doLayout(EAdTransformation transformation) {
		// Implemented by inherited classes

	}

	@Override
	public EAdPosition getPosition() {
		// Implemented by inherited classes
		return null;
	}

	@Override
	public void setPosition(EAdPosition p) {
		// Implemented by inherited classes
	}

	@Override
	public void update() {

	}

	public EAdTransformation getTransformation() {
		return transformation;
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
	
	public boolean isEnable(){
		return enable;
	}
}
