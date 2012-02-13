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

package ead.common.importer.subimporters.chapter.cutscene;

import com.google.inject.Inject;

import ead.common.EAdElementImporter;
import ead.common.importer.interfaces.EAdElementFactory;
import ead.common.importer.interfaces.EffectsImporterFactory;
import ead.common.importer.interfaces.ResourceImporter;
import ead.common.model.elements.EAdChapter;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.EffectsMacro;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scenes.VideoScene;
import ead.common.resources.assets.multimedia.EAdVideo;
import ead.common.resources.assets.multimedia.Video;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.data.chapter.scenes.Slidescene;
import es.eucm.eadventure.common.data.chapter.scenes.Videoscene;

/**
 * Scenes importer
 * 
 */
public class VideosceneImporter implements
		EAdElementImporter<Videoscene, VideoScene> {

	/**
	 * Resources importer
	 */
	private ResourceImporter resourceImporter;

	private EAdElementFactory factory;

	private EffectsImporterFactory effectsImporter;

	@Inject
	public VideosceneImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			ResourceImporter resourceImporter, EAdElementFactory factory,
			EffectsImporterFactory effectsImporter) {
		this.resourceImporter = resourceImporter;
		this.factory = factory;
		this.effectsImporter = effectsImporter;
	}

	@Override
	public VideoScene init(Videoscene oldSlideScene) {
		VideoScene videoScene = new VideoScene();
		videoScene.setId(oldSlideScene.getId());
		return videoScene;
	}

	@Override
	public VideoScene convert(Videoscene oldSlideScene, Object object) {
		EAdChapter chapter = factory.getCurrentChapterModel();
		VideoScene cutscene = (VideoScene) object;
		EffectsMacro macro = effectsImporter.getMacroEffects(oldSlideScene
				.getEffects());
		if (macro != null)
			for (EAdEffect e : macro.getEffects())
				cutscene.getFinalEffects().add(e);

		// TODO should import conditioned resources
		importResources(cutscene, oldSlideScene, chapter);

		if (oldSlideScene.getNext() == Slidescene.NEWSCENE) {
			EAdScene scene = (EAdScene) factory.getElementById(oldSlideScene
					.getTargetId());
			cutscene.setNextScene(scene);
		}
		// TODO convert the end-game next scene value

		return cutscene;
	}

	private void importResources(VideoScene cutscene,
			Videoscene oldSlidesceneScene, EAdChapter chapter) {
		Resources res = oldSlidesceneScene.getResources().get(0);
		String assetPath = res.getAssetPath(Videoscene.RESOURCE_TYPE_VIDEO);
		String[] temp = assetPath.split("/");
		String name = temp[temp.length - 1];
		resourceImporter.copyFile(assetPath, "binary/" + name);

		EAdVideo video = new Video("@binary/" + name);
		cutscene.getDefinition().getResources()
				.addAsset(VideoScene.video, video);
	}

}
