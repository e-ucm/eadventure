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

import java.util.ArrayList;
import java.util.List;

import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.transitions.EAdTransition;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.SystemFields;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.params.fills.PaintFill;
import ead.common.resources.assets.drawable.basics.shapes.CircleShape;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.util.EAdPosition.Corner;
import ead.common.util.StringHandler;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.SceneGOImpl;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.SceneGO;
import ead.engine.core.gameobjects.go.transitions.SceneLoader;
import ead.engine.core.gameobjects.go.transitions.TransitionGO;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

public abstract class AbstractTransitionGO<T extends EAdTransition> extends
		SceneGOImpl implements TransitionGO<T> {

	protected T transition;

	protected SceneGO<?> previousScene;

	protected EAdScene nextScene;

	protected SceneGO<?> nextSceneGO;

	protected SceneLoader sceneLoader;

	private boolean loading;

	private boolean loaded;

	private float rotation = 0.0f;

	private EAdField<Float> rotationField;

	private EAdField<Boolean> visibleField;

	private ArrayList<TransitionListener> listeners;

	private int timeLoading;
	
	private boolean firstUpdate;

	public AbstractTransitionGO(AssetHandler assetHandler,
			StringHandler stringHandler,
			SceneElementGOFactory sceneElementFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory,
			SceneLoader sceneLoader) {
		super(assetHandler, stringHandler, sceneElementFactory, gui, gameState,
				eventFactory);
		this.sceneLoader = sceneLoader;
		EAdScene scene = this.createLoadingScene();
		scene.setReturnable(false);
		this.setElement(scene);
		listeners = new ArrayList<TransitionListener>();
		firstUpdate = true;
	}

	public void setTransition(T transition) {
		this.transition = transition;
	}

	public void setPrevious(SceneGO<?> scene) {
		this.previousScene = scene;
		gameState.getValueMap().clearUpdateList();
		gameState.getValueMap().setValue(SystemFields.PROCESS_INPUT, false);
		for (TransitionListener l : this.getTransitionListeners()) {
			l.transitionBegins();
		}
		timeLoading = 0;
	}

	@Override
	public void sceneLoaded(SceneGO<?> sceneGO) {
		nextSceneGO = sceneGO;
		loaded = true;
		loading = false;
	}

	@Override
	public boolean isLoadedNextScene() {
		return loaded;
	}

	@Override
	public boolean isFinished() {
		return loaded;
	}

	protected EAdScene createLoadingScene() {
		BasicScene scene = new BasicScene();

		RectangleShape rs = new RectangleShape(gameState.getValueMap()
				.getValue(SystemFields.GAME_WIDTH), gameState.getValueMap()
				.getValue(SystemFields.GAME_HEIGHT));
		rs.setPaint(new PaintFill(new ColorFill(100, 100, 100, 0),
				ColorFill.BLACK));

		int circleRadius = 15;
		CircleShape circle = new CircleShape(circleRadius, circleRadius,
				circleRadius, 40, new LinearGradientFill(ColorFill.ORANGE,
						ColorFill.YELLOW, circleRadius * 2, circleRadius * 2));
		SceneElement loadingLogo = new SceneElement(circle);
		loadingLogo.setInitialAlpha(0.8f);
		loadingLogo.setId("loadingText");
		loadingLogo.setPosition(Corner.CENTER, circleRadius * 2,
				circleRadius * 2);
		rotationField = new BasicField<Float>(loadingLogo,
				SceneElement.VAR_ROTATION);
		visibleField = new BasicField<Boolean>(loadingLogo,
				SceneElement.VAR_VISIBLE);

		scene.setBackground(new SceneElement(rs));
		scene.getSceneElements().add(loadingLogo);

		loadingLogo.setInitialAlpha(0.7f);
		return scene;
	}

	@Override
	public void setNext(EAdScene scene) {
		nextScene = scene;
		loading = false;
	}

	public void update() {
		super.update();
		if (!loaded && !loading) {
			loading = true;
			sceneLoader.loadScene(nextScene, this);
		}

		if (loaded && firstUpdate) {
			gameState.getEffects().clear();
			firstUpdate = false;
			nextSceneGO.update();
		}

		// If time loading is bigger than 1 second, we show the loading symbol
		if (timeLoading > 1000) {
			rotation += 0.5f;
			if (rotation > 2 * Math.PI) {
				rotation -= 2 * Math.PI;
			}
			gameState.getValueMap().setValue(rotationField, rotation);
			gameState.getValueMap().setValue(visibleField, true);
		} else {
			timeLoading += gui.getSkippedMilliseconds();
			gameState.getValueMap().setValue(visibleField, false);
		}

		if (isFinished()) {
			sceneLoader.freeUnusedAssets(nextSceneGO, previousScene);

			gameState.setScene(nextSceneGO);
			gameState.getValueMap().setValue(SystemFields.PROCESS_INPUT, true);
			for (TransitionListener l : this.getTransitionListeners()) {
				l.transitionEnds();
			}
		}
	}

	public List<TransitionListener> getTransitionListeners() {
		return listeners;
	}

	public SceneGO<?> getNextSceneGO() {
		return nextSceneGO;
	}
}
