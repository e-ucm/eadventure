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

		modelQuerier.setCurrentChapter(chapter, c);

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
