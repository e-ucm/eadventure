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

package ead.importer.interfaces;

import java.util.Map;

import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.chapter.Chapter;

public interface EAdElementFactory {

	/**
	 * Returns the element with the given id from the current chapter
	 * 
	 * @param id
	 *            the id
	 * @return the element with the given id from the current chapter
	 */
	BasicElement getElementById(String id);

	/**
	 * Returns the currently chapter being imported
	 * 
	 * @return currently chapter being imported
	 */
	Chapter getCurrentChapterModel();

	/**
	 * Returns the old model from the chapter being imported
	 * 
	 * @return the old model from the chapter being imported
	 */
	Chapter getCurrentOldChapterModel();

	/**
	 * Sets the current chapter model imported
	 * 
	 * @param chapter
	 *            the new model for the chapter
	 * @param oldChapter
	 *            the old data from the chapter
	 */
	void setCurrentChapterModel(Chapter chapter, Chapter oldChapter);

	/**
	 * Returns whether the game imported is first person
	 * 
	 * @return whether the game imported is first person
	 */
	boolean isFirstPerson();

	/**
	 * Sets the old data model
	 * 
	 * @param data
	 *            the old data model
	 */
	void setOldDataModel(AdventureData data);

	/**
	 * Returns the variable's reference linked to the given id
	 * 
	 * @param id
	 *            variables's id
	 * @param varibles
	 *            type. Can be
	 *            {@link es.eucm.eadventure.common.data.chapter.conditions.Condition#FLAG_CONDITION}
	 *            or
	 *            {@link es.eucm.eadventure.common.data.chapter.conditions.Condition#VAR_CONDITION}
	 * @return the variable's reference
	 */
	EAdField<?> getVarByOldId(String id, int type);

	/**
	 * Returns the conditions associated with the global state represented by
	 * the given id
	 * 
	 * @param globalStateId
	 *            the global state's id
	 * @return the conditions representing the global state
	 */
	Condition getGlobalStateCondition(String globalStateId);

	void registerOldElement(String id, Object oldElement);

	<S> BasicElement getElement(String id, S oldElement);

	AdventureData getOldDataModel();

	Map<String, EAdElement> getChapterElements();

	void addDraggableActor(SceneElementDef actor);

	boolean isDraggableActor(SceneElementDef actor);

	void init();

	String createCursor(Image assetDescritptor);

	String getDefaultCursor(String cursor);

}
