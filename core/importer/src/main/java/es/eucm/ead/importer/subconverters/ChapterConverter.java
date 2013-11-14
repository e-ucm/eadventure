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

package es.eucm.ead.importer.subconverters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.importer.testers.ConverterTester;
import es.eucm.ead.importer.EAdElementsCache;
import es.eucm.ead.importer.ModelQuerier;
import es.eucm.ead.importer.subconverters.actors.AtrezzoConverter;
import es.eucm.ead.importer.subconverters.actors.ElementConverter.DropEvent;
import es.eucm.ead.importer.subconverters.actors.ItemConverter;
import es.eucm.ead.importer.subconverters.actors.NPCConverter;
import es.eucm.ead.model.Commands;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.Chapter;
import es.eucm.ead.model.elements.events.Event;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.model.interfaces.features.WithBehavior;
import es.eucm.ead.model.params.guievents.DragGEv;
import es.eucm.ead.model.params.guievents.enums.DragGEvType;
import es.eucm.eadventure.common.data.chapter.Timer;
import es.eucm.eadventure.common.data.chapter.elements.Atrezzo;
import es.eucm.eadventure.common.data.chapter.elements.Item;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.data.chapter.scenes.Cutscene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class ChapterConverter {

	private static Logger logger = LoggerFactory
			.getLogger(ChapterConverter.class);

	private SceneConverter sceneConverter;

	private CutsceneConverter cutsceneConverter;

	private EAdElementsCache elementsCache;

	private AtrezzoConverter atrezzoConverter;

	private ItemConverter itemConverter;

	private NPCConverter npcConverter;

	private ModelQuerier modelQuerier;

	private TimerConverter timerConverter;

	private ConverterTester converterTest;

	@Inject
	public ChapterConverter(SceneConverter sceneConverter,
			CutsceneConverter cutsceneConverter,
			EAdElementsCache elementsCache, AtrezzoConverter atrezzoConverter,
			ModelQuerier modelQuerier, ItemConverter itemConverter,
			NPCConverter npcConverter,
			ConversationsConverter conversationsConverter,
			TimerConverter timerConverter, ConverterTester converterTest) {
		this.sceneConverter = sceneConverter;
		this.cutsceneConverter = cutsceneConverter;
		this.elementsCache = elementsCache;
		this.atrezzoConverter = atrezzoConverter;
		this.modelQuerier = modelQuerier;
		this.itemConverter = itemConverter;
		this.npcConverter = npcConverter;
		this.timerConverter = timerConverter;
		this.converterTest = converterTest;
		modelQuerier.setConversationsConverter(conversationsConverter);
	}

	public Chapter convert(es.eucm.eadventure.common.data.chapter.Chapter c) {
		modelQuerier.clear();
		Chapter chapter = new Chapter();
		modelQuerier.setCurrentChapter(chapter, c);
		modelQuerier.loadGlobalStates();

		// Import Player
		logger.debug("Importing player");
		SceneElementDef player = npcConverter.convert(c.getPlayer());
		elementsCache.put(player);

		// Import atrezzos
		logger.debug("Importing {} attrezzos", c.getAtrezzo().size());
		for (Atrezzo a : c.getAtrezzo()) {
			SceneElementDef def = atrezzoConverter.convert(a);
			elementsCache.put(def);
		}

		logger.debug("Importing {} items", c.getItems().size());
		// Import items
		for (Item a : c.getItems()) {
			SceneElementDef def = itemConverter.convert(a);
			elementsCache.put(def);
		}

		logger.debug("Importing {} NPCs", c.getCharacters().size());
		// Import NPCs
		for (NPC a : c.getCharacters()) {
			SceneElementDef def = npcConverter.convert(a);
			elementsCache.put(def);
		}

		logger.debug("Adding actions to items");
		// Add actions after the cache contains all the actors
		// Items actions
		for (Item a : c.getItems()) {
			SceneElementDef def = (SceneElementDef) elementsCache
					.get(a.getId());
			itemConverter.addActions(a, def);

		}
		logger.debug("Adding actions to NPCs");
		// NPCs actions
		for (NPC a : c.getCharacters()) {
			SceneElementDef def = (SceneElementDef) elementsCache
					.get(a.getId());
			npcConverter.addActions(a, def);
		}

		// Loads macros and conversations. It
		// must be here, after all the actors has been loaded
		modelQuerier.loadMacros();
		modelQuerier.loadConversations();

		// Import scenes
		logger.debug("Importing {} scenes", c.getScenes().size());
		for (es.eucm.eadventure.common.data.chapter.scenes.Scene s : c
				.getScenes()) {
			String sceneId = s.getId();
			logger.debug("Importing {} scene", sceneId);
			converterTest.command(Commands.GO_SCENE + " " + sceneId);
			converterTest.check(Commands.SCENE, sceneId);
			Scene scene = sceneConverter.convert(s);

			chapter.addScene(scene);
			if (c.getInitialGeneralScene() == s) {
				chapter.setInitialScene(scene);
			}
		}

		logger.debug("Importing {} cutscenes", c.getScenes().size());
		// Import cutscenes
		for (Cutscene cs : c.getCutscenes()) {
			logger.debug("Importing {} cutscene", cs.getId());
			List<Scene> cutscene = cutsceneConverter.convert(cs);
			for (Scene s : cutscene) {
				chapter.addScene(s);
			}
			if (c.getInitialGeneralScene() == cs) {
				if (cutscene.isEmpty()) {
					logger
							.error("Cannot import chapter: empty cutscene as first entry");
				}
				chapter.setInitialScene(cutscene.get(0));
			}
		}

		for (DropEvent e : npcConverter.getDropEvents()) {
			WithBehavior w = (WithBehavior) elementsCache.get(e.target);
			BasicElement v = (BasicElement) w;
			v.putProperty(SceneElement.VAR_ENABLE, false);
			w.addBehavior(new DragGEv(e.owner, DragGEvType.DROP), e.effects);
		}

		for (DropEvent e : itemConverter.getDropEvents()) {
			WithBehavior w = (WithBehavior) elementsCache.get(e.target);
			BasicElement v = (BasicElement) w;
			v.putProperty(SceneElement.VAR_ENABLE, false);
			w.addBehavior(new DragGEv(e.owner, DragGEvType.DROP), e.effects);
		}

		npcConverter.getDropEvents().clear();
		itemConverter.getDropEvents().clear();

		// Import timers
		// [TI - Chapter]
		for (Timer t : c.getTimers()) {
			Event timer = timerConverter.convert(t);
			chapter.addEvent(timer);
		}

		return chapter;
	}

}
