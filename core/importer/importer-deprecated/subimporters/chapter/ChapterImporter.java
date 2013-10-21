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

package ead.importer.subimporters.chapter;

import com.google.inject.Inject;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EAdElementFactory;
import es.eucm.ead.model.elements.Chapter;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.huds.MouseHud;
import es.eucm.ead.model.elements.predef.effects.ChangeCursorEf;
import es.eucm.ead.tools.StringHandler;
import es.eucm.eadventure.common.data.HasId;
import es.eucm.eadventure.common.data.chapter.Timer;
import es.eucm.eadventure.common.data.chapter.effects.Macro;
import es.eucm.eadventure.common.data.chapter.elements.ActiveArea;
import es.eucm.eadventure.common.data.chapter.scenes.Scene;

import java.util.List;

/**
 * Chapter importer
 *
 */
public class ChapterImporter
		implements
		EAdElementImporter<es.eucm.eadventure.common.data.chapter.Chapter, Chapter> {

	/**
	 * String handler
	 */
	private StringHandler stringHandler;

	private EAdElementFactory elementFactory;

	private EAdElementImporter<Timer, Event> timerImporter;

	protected ImportAnnotator annotator;

	@Inject
	public ChapterImporter(StringHandler stringHandler,
			EAdElementFactory elementFactory,
			EAdElementImporter<Timer, Event> timerImporter,
			ImportAnnotator annotator) {
		this.stringHandler = stringHandler;
		this.elementFactory = elementFactory;
		this.timerImporter = timerImporter;
		this.annotator = annotator;
	}

	@Override
	public Chapter init(
			es.eucm.eadventure.common.data.chapter.Chapter oldChapter) {
		Chapter newChapter = new Chapter();
		return newChapter;
	}

	@Override
	public Chapter convert(
			es.eucm.eadventure.common.data.chapter.Chapter oldChapter,
			Object object) {
		Chapter newChapter = (Chapter) object;

		annotator.annotate(newChapter, ImportAnnotator.Type.Open);
		annotator.annotate(newChapter, ImportAnnotator.Type.Entry,
				ImportAnnotator.Key.Role, oldChapter.getTitle());
		annotator.annotate(newChapter, ImportAnnotator.Type.Entry,
				"description", oldChapter.getDescription());
		//elementFactory.setCurrentChapterModel(newChapter, oldChapter);

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

		importActiveAreas(oldChapter.getScenes());
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
			Event timerEvent = timerImporter.init(timer);
			timerEvent = timerImporter.convert(timer, timerEvent);
			newChapter.addEvent(timerEvent);
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

		//newChapter.setInitialScene((Scene) elementFactory
		//		.getElementById(oldChapter.getInitialGeneralScene().getId()));

		annotator.annotate(newChapter, ImportAnnotator.Type.Close);
		return newChapter;
	}

	private void setAdventureCursor(
			es.eucm.eadventure.common.data.chapter.Chapter oldChapter) {

		ChangeCursorEf changeCursor = new ChangeCursorEf(MouseHud.CURSOR_ID);
		Scene scene = (Scene) elementFactory.getElementById(oldChapter
				.getInitialGeneralScene().getId());

		SceneElementEv event = new SceneElementEv();
		event.addEffect(SceneElementEvType.INIT, changeCursor);
		//scene.addEvent(event);

	}

	private void registerActiveAreas(List<Scene> scenes) {
		for (Scene s : scenes) {
			for (ActiveArea a : s.getActiveAreas()) {
				elementFactory.registerOldElement(a.getId(), a);
			}
		}
	}

	private void registerOldElements(List<? extends HasId> list) {
		for (HasId element : list) {
			elementFactory.registerOldElement(element.getId(), element);
		}
	}

	private void registerOldElementMacros(List<Macro> list) {
		for (Macro element : list) {
			elementFactory.registerOldElement(element.getId(), element);
		}
	}

	private void importElements(List<? extends HasId> list) {
		for (HasId element : list) {
			elementFactory.getElementById(element.getId());
		}
	}

	private void importElementsMacro(List<Macro> list) {
		for (Macro element : list) {
			elementFactory.getElementById(element.getId());
		}
	}

	private void importActiveAreas(List<Scene> scenes) {
		for (Scene s : scenes) {
			for (ActiveArea a : s.getActiveAreas()) {
				elementFactory.getElementById(a.getId());
			}
		}
	}

}
