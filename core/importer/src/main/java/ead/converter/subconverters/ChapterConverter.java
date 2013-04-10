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

package ead.converter.subconverters;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.BasicChapter;
import ead.common.model.elements.EAdChapter;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.converter.EAdElementsCache;
import ead.converter.ModelQuerier;
import ead.converter.subconverters.actors.AtrezzoConverter;
import ead.converter.subconverters.actors.ItemConverter;
import ead.converter.subconverters.actors.NPCConverter;
import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.data.chapter.elements.Atrezzo;
import es.eucm.eadventure.common.data.chapter.elements.Item;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.data.chapter.scenes.Cutscene;
import es.eucm.eadventure.common.data.chapter.scenes.Scene;

@Singleton
public class ChapterConverter {

	private SceneConverter sceneConverter;

	private CutsceneConverter cutsceneConverter;

	private EAdElementsCache elementsCache;

	private AtrezzoConverter atrezzoConverter;

	private ItemConverter itemConverter;

	private NPCConverter npcConverter;

	private ModelQuerier modelQuerier;

	@Inject
	public ChapterConverter(SceneConverter sceneConverter,
			CutsceneConverter cutsceneConverter,
			EAdElementsCache elementsCache, AtrezzoConverter atrezzoConverter,
			ModelQuerier modelQuerier, ItemConverter itemConverter,
			NPCConverter npcConverter) {
		this.sceneConverter = sceneConverter;
		this.cutsceneConverter = cutsceneConverter;
		this.elementsCache = elementsCache;
		this.atrezzoConverter = atrezzoConverter;
		this.modelQuerier = modelQuerier;
		this.itemConverter = itemConverter;
		this.npcConverter = npcConverter;
	}

	public EAdChapter convert(Chapter c) {
		BasicChapter chapter = new BasicChapter();
		modelQuerier.setCurrentChapter(chapter, c);
		modelQuerier.loadGlobalStates();

		// Import atrezzos
		for (Atrezzo a : c.getAtrezzo()) {
			EAdSceneElementDef def = atrezzoConverter.convert(a);
			elementsCache.put(def);
		}

		// Import items
		for (Item a : c.getItems()) {
			EAdSceneElementDef def = itemConverter.convert(a);
			elementsCache.put(def);
		}

		// Import NPCs
		for (NPC a : c.getCharacters()) {
			EAdSceneElementDef def = npcConverter.convert(a);
			elementsCache.put(def);
		}

		// Loads macros and global states. It
		// must be here, after all the actors has been loaded
		modelQuerier.loadMacros();

		// Import scenes
		for (Scene s : c.getScenes()) {
			EAdScene scene = sceneConverter.convert(s);
			chapter.addScene(scene);
			if (c.getInitialGeneralScene() == s) {
				chapter.setInitialScene(scene);
			}
		}

		// Import cutscenes
		for (Cutscene cs : c.getCutscenes()) {
			List<EAdScene> cutscene = cutsceneConverter.convert(cs);
			for (EAdScene s : cutscene) {
				chapter.addScene(s);
			}
			if (c.getInitialGeneralScene() == cs) {
				chapter.setInitialScene(cutscene.get(0));
			}
		}

		return chapter;
	}

}
