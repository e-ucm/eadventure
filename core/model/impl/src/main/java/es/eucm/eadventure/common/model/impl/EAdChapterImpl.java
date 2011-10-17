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

package es.eucm.eadventure.common.model.impl;

import es.eucm.eadventure.common.interfaces.CopyNotSupportedException;
import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.EAdChapter;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdTimer;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.EAdMap;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.extra.impl.EAdMapImpl;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.params.EAdString;

/**
 * Model of the eAdventure chapter.
 */
@Element(runtime = EAdChapterImpl.class, detailed = EAdChapterImpl.class)
public class EAdChapterImpl extends EAdGeneralElementImpl implements EAdChapter {

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
	
	/**
	 * Timers of the game
	 */
	@Param("timers")
	private EAdList<EAdTimer> timers;
	
	@Param("title")
	private final EAdString title;
	
	@Param("description")
	private final EAdString description;
	
	@Param("initialScreen")
	private EAdScene initialScreen;
	
	@Param("loadingScreen")
	private EAdScene loadingScreen;
	
	@Param("vars")
	private EAdMap<EAdVarDef<?>, Object> vars;
	
	/**
	 * Default constructor.
	 * 
	 * @param adventureModel The parent adventure model
	 */
	public EAdChapterImpl(String id) {
		super(id);
		scenes = new EAdListImpl<EAdScene>(EAdScene.class);
		actors = new EAdListImpl<EAdSceneElementDef>(EAdSceneElementDef.class);
		timers = new EAdListImpl<EAdTimer>(EAdTimer.class);
		title = EAdString.newEAdString("chapterTitle");
		description = EAdString.newEAdString("chapterDescription");
		vars = new EAdMapImpl<EAdVarDef<?>, Object>(EAdVarDef.class,
				Object.class);

	}
	
	/* (non-Javadoc)
	 * @see es.eucm.eadventure.common.model.EAdChapterModel#getScenes()
	 */
	@Override
	public EAdList<EAdScene> getScenes() {
		return scenes;
	}
	
	/* (non-Javadoc)
	 * @see es.eucm.eadventure.common.model.EAdChapterModel#getActors()
	 */
	@Override
	public EAdList<EAdSceneElementDef> getActors() {
		return actors;
	}
		
	/* (non-Javadoc)
	 * @see es.eucm.eadventure.common.model.EAdChapterModel#getTimers()
	 */
	@Override
	public EAdList<EAdTimer> getTimers() {
		return timers;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.common.model.impl.AbstractEAdElement#copy()
	 */
	@Override
	public EAdChapterImpl copy(){
		try {
			return (EAdChapterImpl)super.copy();
		} catch (ClassCastException e) {
			throw new CopyNotSupportedException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see es.eucm.eadventure.common.model.impl.AbstractEAdElement#copy(boolean)
	 */
	@Override
	public EAdChapterImpl copy(boolean deepCopy){
		try {
			EAdChapterImpl copy = (EAdChapterImpl) super.copy(deepCopy);
			if(deepCopy){

			}
			return copy;
		} catch (ClassCastException e) {
			throw new CopyNotSupportedException(e);
		}
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.common.model.EAdChapterModel#getTitle()
	 */
	@Override
	public EAdString getTitle() {
		return title;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.common.model.EAdChapterModel#getDescription()
	 */
	@Override
	public EAdString getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.common.model.EAdChapterModel#getInitialScreen()
	 */
	@Override
	public EAdScene getInitialScene() {
		return initialScreen;
	}

	/**
	 * Set the initial screen of the game
	 * 
	 * @param screen
	 */
	public void setInitialScene(EAdScene screen) {
		this.initialScreen = screen;
	}
	
	/* (non-Javadoc)
	 * @see es.eucm.eadventure.common.model.EAdChapterModel#getLoadingScreen()
	 */
	@Override
	public EAdScene getLoadingScreen() {
		return loadingScreen;
	}
	
	/**
	 * Set the loading screen of the game, to be used whenever possible
	 * 
	 * @param screen
	 */
	public void setLoadingScreen(EAdScene screen) {
		this.loadingScreen = screen;
	}

	@Override
	public EAdMap<EAdVarDef<?>, Object> getVars() {
		return vars;
	}

	@Override
	public <T> void setVarInitialValue(EAdVarDef<T> var, T value) {
		vars.put(var, value);
	}

}
