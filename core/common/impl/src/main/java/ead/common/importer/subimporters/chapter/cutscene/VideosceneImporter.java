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

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import ead.common.importer.interfaces.EAdElementFactory;
import ead.common.importer.interfaces.EffectsImporterFactory;
import ead.common.importer.interfaces.ResourceImporter;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scenes.VideoScene;
import ead.common.resources.assets.multimedia.Video;
import ead.common.util.StringHandler;
import es.eucm.eadventure.common.data.chapter.scenes.Videoscene;

/**
 * Video cutscenes importer
 * 
 */
public class VideosceneImporter extends CutsceneImporter<Videoscene> {

	@Inject
	public VideosceneImporter(StringHandler stringHandler,
			ResourceImporter resourceImporter, EAdElementFactory factory,
			EffectsImporterFactory effectsImporter) {
		super(stringHandler, factory, effectsImporter, resourceImporter);
	}

	@Override
	public VideoScene init(Videoscene oldSlideScene) {
		VideoScene videoScene = new VideoScene();
		videoScene.setId(oldSlideScene.getId());
		return videoScene;
	}

	protected void importResources(Videoscene oldCutscene, EAdScene scene) {
		HashMap<String, String> correspondences = new HashMap<String, String>();
		correspondences.put(Videoscene.RESOURCE_TYPE_VIDEO, VideoScene.video);
		Map<String, Object> resourcesClasses = new HashMap<String, Object>();
		resourcesClasses.put(VideoScene.video, Video.class);

		resourceImporter.importResources(scene.getDefinition(),
				oldCutscene.getResources(), correspondences, resourcesClasses);
	}

	@Override
	protected void importConfiguration(EAdScene scene, EAdEffect endEffect) {
		VideoScene videoScene = (VideoScene) scene;
		videoScene.getFinalEffects().add(endEffect);
	}

}
