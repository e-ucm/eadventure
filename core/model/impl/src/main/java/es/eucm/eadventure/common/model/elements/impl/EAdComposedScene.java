package es.eucm.eadventure.common.model.elements.impl;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.impl.AbstractEAdElement;
import es.eucm.eadventure.common.model.impl.EAdListImpl;
import es.eucm.eadventure.common.model.variables.impl.vars.BooleanVar;
import es.eucm.eadventure.common.model.variables.impl.vars.IntegerVar;

public class EAdComposedScene extends AbstractEAdElement implements EAdScene {

	protected EAdList<EAdScene> scenes;

	/**
	 * Pointer to the variable that manages the current scene
	 */
	@Param("currentScene")
	protected IntegerVar currentScene;
	
	private BooleanVar sceneLoaded;

	public EAdComposedScene(String id) {
		super(id);
		scenes = new EAdListImpl<EAdScene>(EAdScene.class);
		currentScene = new IntegerVar("currentScene");
		sceneLoaded = new BooleanVar("sceneLoaded");
	}

	@Override
	public EAdList<EAdSceneElement> getSceneElements() {
		return null;
	}

	@Override
	public EAdSceneElement getBackground() {
		return null;
	}

	@Override
	public boolean isReturnable() {
		return false;
	}
	
	public IntegerVar currentSceneVar() {
		return currentScene;
	}

	public EAdList<EAdScene> getScenes() {
		return scenes;
	}

	@Override
	public BooleanVar sceneLoaded() {
		return sceneLoaded;
	}

}
