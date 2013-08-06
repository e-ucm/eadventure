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

package es.eucm.ead.engine.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import es.eucm.ead.model.interfaces.features.Variabled;
import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.model.elements.scenes.EAdScene;
import es.eucm.ead.model.params.variables.EAdVarDef;

public class GameStateData {

	private EAdScene currentScene;

	private List<EAdEffect> currentEffects;

	private Stack<EAdScene> previousSceneStack;

	private Map<EAdVarDef<?>, Object> systemVars;

	private Map<Variabled, Map<EAdVarDef<?>, Object>> map;

	private ArrayList<Variabled> updateList;

	public GameStateData(EAdScene currentScene, List<EAdEffect> currentEffects,
			Stack<EAdScene> previousSceneStack,
			Map<EAdVarDef<?>, Object> systemVars,
			Map<Variabled, Map<EAdVarDef<?>, Object>> map,
			ArrayList<Variabled> updateList) {
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

	public Map<Variabled, Map<EAdVarDef<?>, Object>> getElementVars() {
		return map;
	}

	public ArrayList<Variabled> getUpdateList() {
		return updateList;
	}

}
