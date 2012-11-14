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
import ead.common.model.elements.extra.EAdListImpl;
import ead.common.model.elements.extra.EAdMap;
import ead.common.model.elements.extra.EAdMapImpl;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.variables.EAdVarDef;
import ead.common.params.text.EAdString;

/**
 * Model of the eAdventure chapter.
 */
@Element
public class BasicChapter extends ResourcedElement implements EAdChapter,
		Evented {

	/**
	 * Scenes of the game
	 */
	@Param("scenes")
	private EAdList<EAdScene> scenes;

	/**
	 * Actors of the game
	 */
	@Param("actors")
	private EAdList<EAdSceneElementDef> actors;

	@Param("title")
	private EAdString title;

	@Param("description")
	private EAdString description;

	@Param("initialScene")
	private EAdScene initialScene;

	@Param("vars")
	private EAdMap<EAdVarDef<?>, Object> vars;

	/**
	 * Default constructor.
	 * 
	 * @param adventureModel
	 *            The parent adventure model
	 */
	public BasicChapter() {
		super();
		scenes = new EAdListImpl<EAdScene>(EAdScene.class);
		actors = new EAdListImpl<EAdSceneElementDef>(EAdSceneElementDef.class);
		events = new EAdListImpl<EAdEvent>(EAdEvent.class);
		title = EAdString.newRandomEAdString("title");
		description = EAdString.newRandomEAdString("desc");
		vars = new EAdMapImpl<EAdVarDef<?>, Object>(EAdVarDef.class,
				Object.class);

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

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 89 * hash + (this.scenes != null ? this.scenes.hashCode() : 0);
		hash = 89 * hash + (this.actors != null ? this.actors.hashCode() : 0);
		hash = 89 * hash + (this.title != null ? this.title.hashCode() : 0);
		hash = 89 * hash + (this.description != null ? this.description.hashCode() : 0);
		hash = 89 * hash + (this.initialScene != null ? this.initialScene.hashCode() : 0);
		hash = 89 * hash + (this.vars != null ? this.vars.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BasicChapter other = (BasicChapter) obj;
		if (this.scenes != other.scenes && (this.scenes == null || !this.scenes.equals(other.scenes))) {
			return false;
		}
		if (this.actors != other.actors && (this.actors == null || !this.actors.equals(other.actors))) {
			return false;
		}
		if (this.title != other.title && (this.title == null || !this.title.equals(other.title))) {
			return false;
		}
		if (this.description != other.description && (this.description == null || !this.description.equals(other.description))) {
			return false;
		}
		if (this.initialScene != other.initialScene && (this.initialScene == null || !this.initialScene.equals(other.initialScene))) {
			return false;
		}
		if (this.vars != other.vars && (this.vars == null || !this.vars.equals(other.vars))) {
			return false;
		}
		return true;
	}
}
