package es.eucm.eadventure.engine.core.gameobjects.huds.impl;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.DrawableGO;
import es.eucm.eadventure.engine.core.gameobjects.huds.HudGO;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.EAdCanvas;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.util.EAdTransformation;
import es.eucm.eadventure.engine.core.util.impl.EAdTransformationImpl;

public abstract class AbstractHUD implements HudGO {

	protected GUI gui;

	private List<DrawableGO<?>> gameObjects;

	public AbstractHUD(GUI gui) {
		this.gui = gui;
		gameObjects = new ArrayList<DrawableGO<?>>();
	}

	public void addElement(DrawableGO<?> drawable) {
		gameObjects.add(drawable);
	}

	public void render(EAdCanvas<?> c) {

	}

	public void doLayout(EAdTransformation t) {
		for (DrawableGO<?> go : gameObjects) {
			gui.addElement(go, t);
		}
	}

	public void update() {
		for (DrawableGO<?> go : gameObjects) {
			go.update();
		}
	}

	public boolean contains(int x, int y) {
		return false;
	}
	
	public boolean processAction(GUIAction action){
		return false;
	}
	
	@Override
	public void setElement(Void element) {
		// Do nothing
	}

	@Override
	public Void getElement() {
		// Return nothing
		return null;
	}
	
	@Override
	public EAdTransformation getTransformation() {
		return EAdTransformationImpl.INITIAL_TRANSFORMATION;
	}
	
	@Override
	public DrawableGO<?> getDraggableElement(MouseState mouseState) {
		return null;
	}

	@Override
	public boolean isEnable() {
		return true;
	}

	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {
		return assetList;
	}

	@Override
	public EAdPosition getPosition() {
		return null;
	}

	@Override
	public void setPosition(EAdPosition p) {
		
	}
	

}
