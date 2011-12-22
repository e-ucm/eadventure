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

package es.eucm.eadventure.common.elementfactories.demos.scenes;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.elements.VideoScene;
import es.eucm.eadventure.common.model.elements.effects.ChangeSceneEf;
import es.eucm.eadventure.common.model.elements.guievents.KeyEventImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.ImageImpl;
import es.eucm.eadventure.common.resources.assets.multimedia.Video;
import es.eucm.eadventure.common.resources.assets.multimedia.VideoImpl;

public class WebMVideoScene extends EmptyScene {

	public WebMVideoScene() {
		VideoScene videoScene = new VideoScene();
		videoScene.setId("videoScene");
		Video video = new VideoImpl("@binary/bbb_trailer_400p.ogv");
		videoScene.getDefinition().getResources().addAsset(VideoScene.video, video);
		videoScene.setUpForEngine();
		
		ChangeSceneEf changeScene = new ChangeSceneEf();
		changeScene.setId("changeScene");
		changeScene.setNextScene(videoScene);

		SceneElementImpl goRightArrow = EAdElementsFactory
				.getInstance()
				.getSceneElementFactory()
				.createSceneElement(new ImageImpl("@drawable/arrow_right.png"),
						200, 60, changeScene);
		this.getComponents().add(goRightArrow);
		
		goRightArrow.addBehavior(KeyEventImpl.KEY_ARROW_RIGHT, changeScene);

	}
	
	@Override
	public String getSceneDescription() {
		return "A scene with a button to launch a video";
	}
	
	public String getDemoName(){
		return "WebM video Scene";
	}

}
