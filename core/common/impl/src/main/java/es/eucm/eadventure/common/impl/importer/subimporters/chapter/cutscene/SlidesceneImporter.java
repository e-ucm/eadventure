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

package es.eucm.eadventure.common.impl.importer.subimporters.chapter.cutscene;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.data.chapter.scenes.Slidescene;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.model.elements.EAdChapter;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.extra.EAdCutscene;
import es.eucm.eadventure.common.model.elements.extra.EAdSlide;
import es.eucm.eadventure.common.model.elements.scene.EAdScene;
import es.eucm.eadventure.common.resources.assets.drawable.basics.animation.Frame;
import es.eucm.eadventure.common.resources.assets.drawable.basics.animation.FramesAnimation;
import es.eucm.eadventure.common.resources.assets.multimedia.Sound;
import es.eucm.eadventure.common.resources.assets.multimedia.SoundImpl;

/**
 * Scenes importer
 * 
 */
public class SlidesceneImporter implements
		EAdElementImporter<Slidescene, EAdCutscene> {

	private EAdElementFactory factory;

	private ResourceImporter resourceImporter;

	@Inject
	public SlidesceneImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EAdElementFactory factory, ResourceImporter resourceImporter) {
		this.factory = factory;
		this.resourceImporter = resourceImporter;
	}

	@Override
	public EAdCutscene init(Slidescene oldSlideScene) {
		EAdCutscene cutscene = new EAdCutscene();
		cutscene.setId(oldSlideScene.getId());
		return cutscene;
	}

	@Override
	public EAdCutscene convert(Slidescene oldSlideScene, Object object) {
		EAdChapter chapter = factory.getCurrentChapterModel();
		EAdCutscene cutscene = (EAdCutscene) object;

		importDocumentation(cutscene, oldSlideScene);
		importResources(cutscene, oldSlideScene, chapter);

		if (oldSlideScene.getNext() == Slidescene.NEWSCENE) {
			EAdScene scene = (EAdScene) factory.getElementById(oldSlideScene
					.getTargetId());
			cutscene.setNextScene(scene);
		}
		// TODO convert the end-game next scene value

		cutscene.setUpForEngine(chapter);

		return cutscene;
	}

	private void importResources(EAdCutscene cutscene,
			Slidescene oldSlidesceneScene, EAdChapter chapter) {
		Resources res = oldSlidesceneScene.getResources().get(0);
		String assetPath = res.getAssetPath(Slidescene.RESOURCE_TYPE_SLIDES);

		FramesAnimation asset = (FramesAnimation) resourceImporter
				.getAssetDescritptor(assetPath, FramesAnimation.class);
		for (int i = 0; i < asset.getFrameCount(); i++) {
			Frame f = asset.getFrame(i);
			EAdSlide slide = new EAdSlide(f.getDrawable());
			slide.setId("slide_" + i);
			slide.setTime(f.getTime());
			cutscene.addSlide(slide);
		}

		for (Resources r : oldSlidesceneScene.getResources()) {
			// Music is imported to chapter level. So, the chapter will
			// remain with the last sound track appeared in the scenes
			String musicPath = r.getAssetPath(Slidescene.RESOURCE_TYPE_MUSIC);

			if (musicPath != null) {
				Sound sound = (Sound) resourceImporter.getAssetDescritptor(
						musicPath, SoundImpl.class);
				chapter.getResources().addAsset(chapter.getInitialBundle(),
						EAdChapter.music, sound);
			}
		}

	}

	private void importDocumentation(EAdCutscene space, Slidescene oldScene) {
		/*
		 * FIXME space.setName(new EAdString(stringHandler.getUniqueId()));
		 * stringHandler.addString(space.getName(), oldScene.getName());
		 * 
		 * space.setDocumentation(new EAdString(stringHandler.getUniqueId()));
		 * stringHandler.addString(space.getDocumentation(),
		 * oldScene.getDocumentation());
		 */
	}

}
