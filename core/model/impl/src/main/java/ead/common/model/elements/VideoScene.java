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

package ead.common.model.elements;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdListImpl;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scenes.SceneImpl;
import ead.common.model.elements.variables.EAdVarDef;
import ead.common.model.elements.variables.VarDefImpl;
import ead.common.resources.annotation.Asset;
import ead.common.resources.assets.multimedia.Video;

@Element(detailed = VideoScene.class, runtime = VideoScene.class)
public class VideoScene extends SceneImpl implements EAdScene {

	/**
	 * Variable to defining if the video is finished playing
	 */
	public static final EAdVarDef<Boolean> VAR_FINISHED = new VarDefImpl<Boolean>(
			"finished", Boolean.class, Boolean.FALSE);

	@Asset({ Video.class })
	public static final String video = "video";

	@Param("nextScene")
	private EAdScene nextScene;

	@Param("finalEffects")
	private EAdList<EAdEffect> finalEffects;

	public VideoScene() {
		super();
		finalEffects = new EAdListImpl<EAdEffect>(EAdEffect.class);
	}

	public EAdList<EAdEffect> getFinalEffects() {
		return finalEffects;
	}

	@Override
	public Boolean getReturnable() {
		return false;
	}

	public void setNextScene(EAdScene nextScene) {
		this.nextScene = nextScene;
	}

	public EAdScene getNextScene() {
		return nextScene;
	}

}
