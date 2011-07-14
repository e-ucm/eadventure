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

package es.eucm.eadventure.common.model.elements.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.EAdTransition;
import es.eucm.eadventure.common.model.impl.EAdListImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.model.variables.impl.vars.BooleanVar;
import es.eucm.eadventure.common.resources.annotation.Asset;
import es.eucm.eadventure.common.resources.assets.multimedia.Video;

@Element(detailed = EAdVideoScene.class, runtime = EAdVideoScene.class)
public class EAdVideoScene extends EAdSceneImpl implements EAdScene {

	@Asset({ Video.class })
	public static final String video = "video";
	
	@Param("videoFinishedVar")
	private BooleanVar videoFinishedVar;
	
	@Param("nextScene")
	EAdScene nextScene;
	
	private EAdList<EAdEffect> finalEffects;

	public EAdVideoScene(String id) {
		super(id);
		videoFinishedVar = new BooleanVar(id + "_videoFinishedVar");
		finalEffects = new EAdListImpl<EAdEffect>(EAdEffect.class);
	}
	
	public BooleanVar getVideoFinishedVar() {
		return videoFinishedVar;
	}
	
	@Override
	public EAdList<EAdSceneElement> getSceneElements() {
		return null;
	}

	public EAdList<EAdEffect> getFinalEffects() {
		return finalEffects;
	}

	@Override
	public boolean isReturnable() {
		return false;
	}	
	
	public void setUpForEngine() {
		finalEffects.add(new EAdChangeVarValueEffect("finishVide", videoFinishedVar, new BooleanOperation("id", EmptyCondition.TRUE_EMPTY_CONDITION)));
		EAdChangeScene e3 = new EAdChangeScene("changeScene", nextScene, EAdTransition.DISPLACE);
		finalEffects.add(e3);
	}

	public void setNextScene(EAdScene nextScene) {
		this.nextScene = nextScene;
	}
	
	public EAdScene getNextScene() {
		return nextScene;
	}

}
