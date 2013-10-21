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

package es.eucm.ead.model.elements;

import es.eucm.ead.model.elements.events.Event;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.interfaces.features.Evented;
import es.eucm.ead.model.interfaces.features.Resourced;
import es.eucm.ead.model.interfaces.features.Variabled;
import es.eucm.ead.model.params.variables.EAdVarDef;

/**
 * Model of the eAdventure chapter.
 */
@Element
public class Chapter extends ResourcedElement implements Resourced, Variabled,
		Evented {

	/**
	 * Scenes of the game
	 */
	private EAdList<Scene> scenes;

	private Scene initialScene;

	@Param
	private EAdMap<EAdVarDef<?>, Object> vars;

	/**
	 * Default constructor.
	 *
	 */
	public Chapter() {
		super();
		scenes = new EAdList<Scene>();
		events = new EAdList<Event>();
		vars = new EAdMap<EAdVarDef<?>, Object>();
	}

	/**
	 * Creates a chapter with the initial scene
	 *
	 * @param initScene
	 */
	public Chapter(Scene initScene) {
		this();
		setInitialScene(initScene);
	}

	public EAdList<Scene> getScenes() {
		return scenes;
	}

	public Scene getInitialScene() {
		return initialScene;
	}

	/**
	 * Set the initial screen of the game
	 *
	 * @param scene
	 */
	public void setInitialScene(Scene scene) {
		this.initialScene = scene;
		if (!getScenes().contains(scene)) {
			getScenes().add(scene);
		}

	}

	@Override
	public EAdMap<EAdVarDef<?>, Object> getVars() {
		return vars;
	}

	@Override
	public <T> void setVarInitialValue(EAdVarDef<T> var, T value) {
		vars.put(var, value);
	}

	public Scene getSceneById(String nextSceneId) {
		if (nextSceneId == null) {
			return null;
		}
		for (Scene s : scenes) {
			if (s.getId().equals(nextSceneId)) {
				return s;
			}
		}
		return null;
	}

	public void setScenes(EAdList<Scene> scenes) {
		this.scenes = scenes;
	}

	public void setVars(EAdMap<EAdVarDef<?>, Object> vars) {
		this.vars = vars;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getVarInitialValue(EAdVarDef<T> var) {
		if (vars.containsKey(var)) {
			return (T) vars.get(var);
		}
		return var.getInitialValue();
	}

	public void addScene(Scene scene) {
		if (this.initialScene == null) {
			this.initialScene = scene;
		}
		scenes.add(scene);
	}

}
