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
import es.eucm.eadventure.common.data.chapter.scenes.Videoscene;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.model.elements.EAdChapter;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.impl.EAdVideoScene;
import es.eucm.eadventure.common.resources.assets.multimedia.Video;
import es.eucm.eadventure.common.resources.assets.multimedia.impl.VideoImpl;

/**
 * Scenes importer
 * 
 */
public class VideosceneImporter implements EAdElementImporter<Videoscene, EAdVideoScene> {

	/**
	 * Resources importer
	 */
	private ResourceImporter resourceImporter;

	private EAdElementFactory factory;

	@Inject
	public VideosceneImporter(EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			ResourceImporter resourceImporter,
			EAdElementFactory factory) {
		this.resourceImporter = resourceImporter;
		this.factory = factory;
	}

	@Override
	public EAdVideoScene init(Videoscene oldSlideScene) {
		EAdVideoScene videoScene = new EAdVideoScene();
		videoScene.setId(oldSlideScene.getId());
		return videoScene;
	}

	@Override
	public EAdVideoScene convert(Videoscene oldSlideScene, Object object) {
		EAdChapter chapter = factory.getCurrentChapterModel();
		EAdVideoScene cutscene = (EAdVideoScene) object;

		//TODO should import resources
		importResources(cutscene, oldSlideScene, chapter);

		if (oldSlideScene.getNext() == Slidescene.NEWSCENE) {
			EAdScene scene = (EAdScene) factory.getElementById(oldSlideScene.getTargetId());
			cutscene.setNextScene(scene);
		}
		//TODO convert the end-game next scene value

		cutscene.setUpForEngine();

		return cutscene;
	}

	private void importResources(EAdVideoScene cutscene, Videoscene oldSlidesceneScene,
			EAdChapter chapter) {
		Resources res = oldSlidesceneScene.getResources().get(0);
		String assetPath = res.getAssetPath(Videoscene.RESOURCE_TYPE_VIDEO);
		String[] temp = assetPath.split("/");
		String name = temp[temp.length - 1];
		resourceImporter.copyFile(assetPath, "binary/" + name);
		
		Video video = new VideoImpl("@binary/" + name);
		cutscene.getDefinition().getResources().addAsset(EAdVideoScene.video, video);
	}

}
