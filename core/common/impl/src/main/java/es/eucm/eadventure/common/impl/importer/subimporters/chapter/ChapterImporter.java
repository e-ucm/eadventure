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

import java.util.List;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.HasId;
import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.data.chapter.Timer;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.elements.EAdChapter;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdTimer;
import es.eucm.eadventure.common.model.impl.EAdChapterImpl;
import es.eucm.eadventure.common.resources.StringHandler;

/**
 * Chapter importer
 * 
 */
public class ChapterImporter implements EAdElementImporter<Chapter, EAdChapter> {

	private static int CHAPTER_ID;

	/**
	 * String handler
	 */
	private StringHandler stringHandler;

	private EAdElementFactory elementFactory;

	@Inject
	public ChapterImporter(StringHandler stringHandler,
			EAdElementFactory elementFactory) {
		this.stringHandler = stringHandler;
		this.elementFactory = elementFactory;
	}

	@Override
	public EAdChapter init(Chapter oldChapter) {
		EAdChapterImpl newChapter = new EAdChapterImpl("chapter" + CHAPTER_ID++);
		return newChapter;
	}

	@Override
	public EAdChapter convert(Chapter oldChapter, Object object) {
		EAdChapterImpl newChapter = (EAdChapterImpl) object;
		elementFactory.setCurrentChapterModel(newChapter, oldChapter);

		newChapter.setTitle(stringHandler.addString(oldChapter.getTitle()));
		newChapter.setDescription(stringHandler.addString(oldChapter
				.getDescription()));

		registerOldElements(oldChapter.getAtrezzo());
		registerOldElements(oldChapter.getItems());
		registerOldElements(oldChapter.getCharacters());
		registerOldElements(oldChapter.getCutscenes());
		registerOldElements(oldChapter.getScenes());
		registerOldElements(oldChapter.getBooks());
		registerOldElements(oldChapter.getGlobalStates());
		registerOldElements(oldChapter.getMacros());
		registerOldElements(oldChapter.getConversations());
		elementFactory.registerOldElement(oldChapter.getPlayer().getId(),
				oldChapter.getPlayer());

		importElements(oldChapter.getAtrezzo());
		importElements(oldChapter.getItems());
		importElements(oldChapter.getCharacters());
		importElements(oldChapter.getCutscenes());
		importElements(oldChapter.getScenes());
		importElements(oldChapter.getBooks());
		importElements(oldChapter.getGlobalStates());
		importElements(oldChapter.getMacros());

		for (Timer timer : oldChapter.getTimers()) {
			newChapter.getTimers().add(
					(EAdTimer) elementFactory.getElement("timer", timer));
		}

		// Import player
		/*
		 * EAdActor player = characterImporter.convert(oldChapter.getPlayer());
		 * if ( player != null ) newChapter.getActors().add(player);
		 */

		// TODO oldChapter.getAdaptationName( );
		// oldChapter.getAdaptationProfiles( )
		// oldChapter.getAdaptationProfilesNames( )
		// oldChapter.getAssessmentName( )
		// oldChapter.getAssessmentProfiles( )

		newChapter.setInitialScreen((EAdScene) elementFactory
				.getElementById(oldChapter.getInitialGeneralScene().getId()));

		return newChapter;
	}

	private void registerOldElements(List<? extends HasId> list) {
		for (HasId element : list)
			elementFactory.registerOldElement(element.getId(), element);
	}

	private void importElements(List<? extends HasId> list) {
		for (HasId element : list)
			elementFactory.getElementById(element.getId());
	}

}
