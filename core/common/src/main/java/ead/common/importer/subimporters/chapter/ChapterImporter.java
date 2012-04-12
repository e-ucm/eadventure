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

package ead.common.importer.subimporters.chapter;

import java.util.List;

import com.google.inject.Inject;

import ead.common.EAdElementImporter;
import ead.common.importer.annotation.ImportAnnotator;
import ead.common.importer.interfaces.EAdElementFactory;
import ead.common.model.elements.EAdChapter;
import ead.common.model.elements.BasicChapter;
import ead.common.model.elements.EAdEvent;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.predef.effects.ChangeCursorEf;
import ead.common.util.StringHandler;
import es.eucm.eadventure.common.data.HasId;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.data.chapter.Timer;
import es.eucm.eadventure.common.data.chapter.effects.Macro;
import es.eucm.eadventure.common.data.chapter.elements.ActiveArea;
import es.eucm.eadventure.common.data.chapter.scenes.Scene;

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

	private EAdElementImporter<Timer, EAdEvent> timerImporter;

	protected ImportAnnotator annotator;

	@Inject
	public ChapterImporter(StringHandler stringHandler,
			EAdElementFactory elementFactory, EAdElementImporter<Timer, EAdEvent> timerImporter,
			ImportAnnotator annotator) {
		this.stringHandler = stringHandler;
		this.elementFactory = elementFactory;
		this.timerImporter = timerImporter;
		this.annotator = annotator;
	}

	@Override
	public EAdChapter init(Chapter oldChapter) {
		BasicChapter newChapter = new BasicChapter();
		newChapter.setId("chapter" + CHAPTER_ID++);
		return newChapter;
	}

	@Override
	public EAdChapter convert(Chapter oldChapter, Object object) {
		BasicChapter newChapter = (BasicChapter) object;

        annotator.annotate(newChapter, ImportAnnotator.Type.Open, null);
        annotator.annotate(newChapter, ImportAnnotator.Type.Entry,
                "title:" + oldChapter.getTitle());
        annotator.annotate(newChapter, ImportAnnotator.Type.Entry,
                "description:" + oldChapter.getDescription());
        elementFactory.setCurrentChapterModel(newChapter, oldChapter);

		stringHandler.setString(newChapter.getTitle(), oldChapter.getTitle());
		stringHandler.setString(newChapter.getDescription(),
				oldChapter.getDescription());

		registerActiveAreas(oldChapter.getScenes());
		registerOldElements(oldChapter.getAtrezzo());
		registerOldElements(oldChapter.getItems());
		registerOldElements(oldChapter.getCharacters());
		registerOldElements(oldChapter.getCutscenes());
		registerOldElements(oldChapter.getScenes());
		registerOldElements(oldChapter.getBooks());
		registerOldElements(oldChapter.getGlobalStates());
		registerOldElementMacros(oldChapter.getMacros());
		registerOldElements(oldChapter.getConversations());
		elementFactory.registerOldElement(oldChapter.getPlayer().getId(),
				oldChapter.getPlayer());

		importActiveAreas( oldChapter.getScenes() );
		importElements(oldChapter.getAtrezzo());
		importElements(oldChapter.getItems());
		importElements(oldChapter.getCharacters());
		importElements(oldChapter.getCutscenes());
		importElements(oldChapter.getScenes());

		// Set cursor (yes, here)
		setAdventureCursor(oldChapter);

		importElements(oldChapter.getBooks());
		importElements(oldChapter.getGlobalStates());
		importElementsMacro(oldChapter.getMacros());

		// Timers
		for (Timer timer : oldChapter.getTimers()) {
			EAdEvent timerEvent = timerImporter.init(timer);
			timerEvent = timerImporter.convert(timer, timerEvent);
			newChapter.getEvents().add(timerEvent);
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

		newChapter.setInitialScene((EAdScene) elementFactory
				.getElementById(oldChapter.getInitialGeneralScene().getId()));

        annotator.annotate(newChapter, ImportAnnotator.Type.Close, null);
		return newChapter;
	}

	private void setAdventureCursor(Chapter oldChapter) {

		ChangeCursorEf changeCursor = new ChangeCursorEf(
				elementFactory.getDefaultCursor(AdventureData.DEFAULT_CURSOR));
		EAdScene scene = (EAdScene) elementFactory.getElementById(oldChapter
				.getInitialGeneralScene().getId());

		SceneElementEv event = new SceneElementEv();
		event.addEffect(SceneElementEvType.FIRST_UPDATE, changeCursor);
		scene.getEvents().add(event);

	}

	private void registerActiveAreas(List<Scene> scenes) {
		for ( Scene s: scenes ){
			for ( ActiveArea a: s.getActiveAreas() ){
				elementFactory.registerOldElement(a.getId(), a);
			}
		}
	}

	private void registerOldElements(List<? extends HasId> list) {
		for (HasId element : list){
			elementFactory.registerOldElement(element.getId(), element);
		}
	}

	private void registerOldElementMacros(List<Macro> list){
		for (Macro element : list){
			elementFactory.registerOldElement(element.getId(), element);
		}
	}

	private void importElements(List<? extends HasId> list) {
		for (HasId element : list){
			elementFactory.getElementById(element.getId());
		}
	}

	private void importElementsMacro(List<Macro> list) {
		for (Macro element : list){
			elementFactory.getElementById(element.getId());
		}
	}

	private void importActiveAreas(List<Scene> scenes) {
		for ( Scene s: scenes ){
			for ( ActiveArea a: s.getActiveAreas() ){
				elementFactory.getElementById(a.getId());
			}
		}
	}

}
