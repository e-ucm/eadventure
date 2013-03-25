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

package ead.common.model.elements;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.interfaces.features.Evented;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdMap;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.params.text.EAdString;
import ead.common.model.params.variables.EAdVarDef;

/**
 * Model of the eAdventure chapter.
 */
@Element
public class BasicChapter extends ResourcedElement implements EAdChapter,
		Evented {

	/**
	 * Scenes of the game
	 */
	@Param
	private EAdList<EAdScene> scenes;

	/**
	 * Actors of the game
	 */
	@Param
	private EAdList<EAdSceneElementDef> actors;

	@Param
	private EAdString title;

	@Param
	private EAdString description;

	@Param
	private EAdScene initialScene;

	@Param
	private EAdMap<EAdVarDef<?>, Object> vars;

	/**
	 * Default constructor.
	 * 
	 * @param adventureModel
	 *            The parent adventure model
	 */
	public BasicChapter() {
		super();
		scenes = new EAdList<EAdScene>();
		actors = new EAdList<EAdSceneElementDef>();
		events = new EAdList<EAdEvent>();
		title = new EAdString("title");
		description = new EAdString("desc");
		vars = new EAdMap<EAdVarDef<?>, Object>();

	}

	/**
	 * Creates a chapter with the initial scene
	 * 
	 * @param initScene
	 */
	public BasicChapter(EAdScene initScene) {
		this();
		setInitialScene(initScene);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.common.model.EAdChapterModel#getScenes()
	 */
	@Override
	public EAdList<EAdScene> getScenes() {
		return scenes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.common.model.EAdChapterModel#getActors()
	 */
	@Override
	public EAdList<EAdSceneElementDef> getActors() {
		return actors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.common.model.EAdChapterModel#getTitle()
	 */
	@Override
	public EAdString getTitle() {
		return title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.common.model.EAdChapterModel#getDescription()
	 */
	@Override
	public EAdString getDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.common.model.EAdChapterModel#getInitialScreen()
	 */
	@Override
	public EAdScene getInitialScene() {
		return initialScene;
	}

	/**
	 * Set the initial screen of the game
	 * 
	 * @param scene
	 */
	public void setInitialScene(EAdScene scene) {
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

	public void setTitle(EAdString title) {
		this.title = title;
	}

	public void setDescription(EAdString description) {
		this.description = description;
	}

	public EAdScene getSceneById(String nextSceneId) {
		if (nextSceneId == null) {
			return null;
		}
		for (EAdScene s : scenes) {
			if (s.getId().equals(nextSceneId)) {
				return s;
			}
		}
		return null;
	}

	public void setScenes(EAdList<EAdScene> scenes) {
		this.scenes = scenes;
	}

	public void setActors(EAdList<EAdSceneElementDef> actors) {
		this.actors = actors;
	}

	public void setVars(EAdMap<EAdVarDef<?>, Object> vars) {
		this.vars = vars;
	}

	public void addScene(EAdScene scene) {
		if (this.initialScene == null) {
			this.initialScene = scene;
		}
		scenes.add(scene);
	}

}
