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

package ead.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.model.assets.AbstractAssetDescriptor;
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
		AbstractAssetDescriptor.initLastId();
		resourceConverter.setPath(destinyFolder);

		adventureData = oldReader.loadGame(file);

		// This element will be displayed whenever a set of effects is launched,
		// to avoid user interaction with other elements in the scene
		GhostElement effectsGhost = new GhostElement(true);
		effectsGhost.setId(EFFECTS_GHOST_ID);
		effectsGhost.setInitialVisible(false);

		AddChildEf addEffectsGhost = new AddChildEf(BottomHud.ID, effectsGhost);

		SceneElementEv initEvent = new SceneElementEv();
		initEvent.addEffect(SceneElementEvType.INIT, addEffectsGhost);

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
