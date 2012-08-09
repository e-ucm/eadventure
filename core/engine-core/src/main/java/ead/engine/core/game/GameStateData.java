package ead.engine.core.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import ead.common.model.EAdElement;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.variables.EAdVarDef;

public class GameStateData {
	
	private EAdScene currentScene;
	
	private List<EAdEffect> currentEffects;
	
	private Stack<EAdScene> previousSceneStack;
	
	private Map<EAdVarDef<?>, Object> systemVars;

	private Map<EAdElement, Map<EAdVarDef<?>, Object>> map;
	
	private ArrayList<EAdElement> updateList;

	public GameStateData(EAdScene currentScene, List<EAdEffect> currentEffects,
			Stack<EAdScene> previousSceneStack,
			Map<EAdVarDef<?>, Object> systemVars,
			Map<EAdElement, Map<EAdVarDef<?>, Object>> map,
			ArrayList<EAdElement> updateList) {
		super();
		this.currentScene = currentScene;
		this.currentEffects = currentEffects;
		this.previousSceneStack = previousSceneStack;
		this.systemVars = systemVars;
		this.map = map;
		this.updateList = updateList;
	}

	public EAdScene getScene() {
		return currentScene;
	}

	public List<EAdEffect> getEffects() {
		return currentEffects;
	}

	public Stack<EAdScene> getPreviousSceneStack() {
		return previousSceneStack;
	}

	public Map<EAdVarDef<?>, Object> getSystemVars() {
		return systemVars;
	}

	public Map<EAdElement, Map<EAdVarDef<?>, Object>> getElementVars() {
		return map;
	}

	public ArrayList<EAdElement> getUpdateList() {
		return updateList;
	}


	

}
