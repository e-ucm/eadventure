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

package ead.engine.core.gameobjects.transitions;

import com.google.inject.Inject;

import ead.common.model.elements.transitions.FadeInTransition;
import ead.common.util.Interpolator;
import ead.common.util.StringHandler;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.transitions.SceneLoader;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.GUI;
import ead.engine.core.util.EAdTransformation;
import ead.engine.core.util.EAdTransformationImpl;

public class FadeInTransitionGO extends AbstractTransitionGO<FadeInTransition>{
	
	private boolean finished;
	
	private int startTime = -1;

	private float sceneAlpha;
	
	private EAdTransformation transformation;
	
	private int currentTime;

	@Inject
	public FadeInTransitionGO(AssetHandler assetHandler,
			StringHandler stringHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory,
			SceneLoader sceneLoader) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState,
				eventFactory, sceneLoader);
		finished = false;
		transformation = new EAdTransformationImpl();
		currentTime = 0;
	}
	
	public void update() {
		super.update();
		if (isLoadedNextScene()) {

			currentTime += gui.getSkippedMilliseconds();
			if (startTime == -1) {
				startTime = currentTime;
				sceneAlpha = 0.0f;
			}
			
			if (currentTime - startTime >= transition.getTime()) {
				finished = true;
			}
			else {
				sceneAlpha = (Interpolator.LINEAR.interpolate(currentTime - startTime, transition.getTime(), 1.0f));
			}
		}
	}
	
	public void doLayout(EAdTransformation t) {
		if (this.isLoadedNextScene()) {
			gui.addElement(previousScene, t);
			transformation.setAlpha(sceneAlpha);
			gui.addElement(nextSceneGO, gui.addTransformation(t, transformation));
		} else {
			super.doLayout(t);
		}
	}
	
	public boolean isFinished( ){
		return finished;
	}

}
