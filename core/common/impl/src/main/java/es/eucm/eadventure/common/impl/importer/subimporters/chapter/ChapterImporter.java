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

package es.eucm.eadventure.common.impl.importer.subimporters.chapter;

import com.google.inject.Inject;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.data.chapter.elements.Atrezzo;
import es.eucm.eadventure.common.data.chapter.elements.Item;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.data.chapter.scenes.Scene;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.EAdChapter;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.impl.EAdChapterImpl;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;

/**
 * Chapter importer
 * 
 */
public class ChapterImporter implements Importer<Chapter, EAdChapter> {

	private static int CHAPTER_ID;

	/**
	 * Scenes importer
	 */
	private Importer<Scene, EAdSceneImpl> sceneImporter;

	/**
	 * Atrezzos importer
	 */
	private Importer<Atrezzo, EAdActor> atrezzoImporter;

	/**
	 * Items importer
	 */
	private Importer<Item, EAdActor> itemImporter;

	/**
	 * Characters importer
	 */
	private Importer<NPC, EAdActor> characterImporter;

	/**
	 * String handler
	 */
	private StringHandler stringHandler;
	
	
	private EAdElementFactory elementFactory;

	@Inject
	public ChapterImporter(Importer<Scene, EAdSceneImpl> sceneImporter,
			Importer<Atrezzo, EAdActor> atrezzoImporter,
			Importer<Item, EAdActor> itemImporter,
			Importer<NPC, EAdActor> characterImporter,
			StringHandler stringHandler,
			EAdElementFactory elementFactory) {
		this.sceneImporter = sceneImporter;
		this.atrezzoImporter = atrezzoImporter;
		this.itemImporter = itemImporter;
		this.characterImporter = characterImporter;
		this.stringHandler = stringHandler;
		this.elementFactory = elementFactory;
	}

	@Override
	public EAdChapter convert(Chapter oldChapter) {
		EAdChapterImpl newChapter = new EAdChapterImpl(
				"chapter" + CHAPTER_ID++);
		elementFactory.setCurrentChapterModel( newChapter, oldChapter );

		newChapter.setTitle(new EAdString(stringHandler.getUniqueId()));
		stringHandler.addString(newChapter.getTitle(), oldChapter.getTitle());

		newChapter.setDescription(new EAdString(stringHandler.getUniqueId()));
		stringHandler.addString(newChapter.getDescription(),
				oldChapter.getDescription());

		// TODO oldChapter.getAdaptationName( );
		// TODO oldChapter.getAdaptationProfiles( )
		// TODO oldChapter.getAdaptationProfilesNames( )
		// TODO oldChapter.getAssessmentName( )
		// TODO oldChapter.getAssessmentProfiles( )

		// The order of import must be as follows to work
		importAtrezzos(oldChapter, newChapter);
		importItems(oldChapter, newChapter);
		importCharacter(oldChapter, newChapter);
		importScenes(oldChapter, newChapter);

		return newChapter;
	}

	/**
	 * Imports the scenes from the old chapter to the new chapter
	 * 
	 * @param oldChapter
	 *            old chapter
	 * @param newChapter
	 *            new chapter
	 */
	private void importScenes(Chapter oldChapter, EAdChapterImpl newChapter) {
		for (Scene s : oldChapter.getScenes()) {
			EAdScene screen = sceneImporter.convert(s);
			if (screen != null) {
				newChapter.getScenes().add(screen);
				// Set initial screen
				if (oldChapter.getTargetId().equals(s.getId())) {
					newChapter.setInitialScreen(screen);
				}
			}
		}
	}

	/**
	 * Imports the atrezzos from the old chapter to the new chapter
	 * 
	 * @param oldChapter
	 *            old chapter
	 * @param newChapter
	 *            new chapter
	 */
	private void importAtrezzos(Chapter oldChapter,
			EAdChapterImpl newChapter) {
		for (Atrezzo a : oldChapter.getAtrezzo()) {
			EAdActor actor = atrezzoImporter.convert(a);
			if (actor != null) {
				newChapter.getActors().add(actor);
			}
		}

	}

	/**
	 * Imports the items from the old chapter to the new chapter
	 * 
	 * @param oldChapter
	 *            old chapter
	 * @param newChapter
	 *            new chapter
	 */
	private void importItems(Chapter oldChapter, EAdChapterImpl newChapter) {
		for (Item i : oldChapter.getItems()) {
			EAdActor actor = itemImporter.convert(i);
			if (actor != null) {
				newChapter.getActors().add(actor);
			}
		}

	}

	/**
	 * Imports the characters from the old chapter to the new chapter
	 * 
	 * @param oldChapter
	 *            old chapter
	 * @param newChapter
	 *            new chapter
	 */
	private void importCharacter(Chapter oldChapter,
			EAdChapterImpl newChapter) {
		for (NPC n : oldChapter.getCharacters()) {
			EAdActor actor = characterImporter.convert(n);
			if (actor != null) {
				newChapter.getActors().add(actor);
			}
		}
		
		// Import player
		EAdActor player = characterImporter.convert(oldChapter.getPlayer());
		if ( player !=  null )
			newChapter.getActors().add(player);

	}

	@Override
	public boolean equals(Chapter oldObject, EAdChapter newObject) {
		// FIXME Implement equals
		return false;
	}

}
