package ead.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicElement;
import ead.common.model.elements.EAdChapter;
import ead.common.model.elements.effects.AddChildEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.huds.BottomHud;
import ead.common.model.elements.scenes.GhostElement;
import ead.converter.resources.ResourceConverter;
import ead.converter.subconverters.ChapterConverter;
import ead.tools.java.reflection.JavaReflectionProvider;
import ead.tools.java.xml.JavaXMLParser;
import ead.writer.AdventureWriter;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.chapter.Chapter;

public class AdventureConverter {

	public static final String EFFECTS_GHOST_ID = "#engine.import.effects_ghost";

	private static final Logger logger = LoggerFactory.getLogger("Converter");

	private OldReader oldReader;

	private ChapterConverter chapterConverter;

	private AdventureData adventureData;

	private ResourceConverter resourceConverter;

	public AdventureConverter() {
		Injector i = Guice.createInjector();
		oldReader = i.getInstance(OldReader.class);
		chapterConverter = i.getInstance(ChapterConverter.class);
		resourceConverter = i.getInstance(ResourceConverter.class);
	}

	public void convert(String file, String destinyFolder) {
		BasicElement.initLastId();
		resourceConverter.setPath(destinyFolder);

		adventureData = oldReader.loadGame(file);

		// This element will be displayed whenever a set of effects is launched,
		// to avoid user interaction with other elements in the scene
		GhostElement effectsGhost = new GhostElement(true);
		effectsGhost.setId(EFFECTS_GHOST_ID);
		effectsGhost.setInitialVisible(false);

		AddChildEf addEffectsGhost = new AddChildEf(BottomHud.ID, effectsGhost);

		SceneElementEv initEvent = new SceneElementEv();
		initEvent.addEffect(SceneElementEvType.FIRST_UPDATE, addEffectsGhost);

		BasicAdventureModel model = new BasicAdventureModel();
		model.getEvents().add(initEvent);

		for (Chapter c : adventureData.getChapters()) {
			EAdChapter chapter = chapterConverter.convert(c);
			model.addChapter(chapter);
		}

		// Create data.xml
		AdventureWriter writer = new AdventureWriter(
				new JavaReflectionProvider(), new JavaXMLParser());

		writer.write(model, destinyFolder
				+ (destinyFolder.charAt(destinyFolder.length() - 1) == '/' ? ""
						: "/") + "data.xml");

	}
}
