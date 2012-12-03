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

package ead.engine.core.gameobjects.huds;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scenes.ComplexSceneElement;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.SystemFields;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;
import ead.common.util.Interpolator;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.input.InputAction;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.util.EAdTransformation;
import ead.engine.core.util.EAdTransformationImpl;

/**
 * <p>
 * Default generic implementation of the {@link ActionsHUD}
 * </p>
 * 
 */
@Singleton
public class ActionsHUD extends AbstractHUD {

	private static final int ANIMATION_TIME = 500;

	private int currentTime;

	/**
	 * The logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger("ActionsHUDImpl");

	/**
	 * The radius of the actions HUD
	 */
	protected int radius;

	private EAdList<EAdSceneElementDef> actions;

	private List<EAdPosition> positions;

	private float alpha;

	private int gameHeight;

	private int gameWidth;

	private int actionsX;

	private int actionsY;

	@Inject
	public ActionsHUD(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(assetHandler, gameObjectFactory, gui, gameState, eventFactory, 5);
		logger.info("New instance");
		positions = new ArrayList<EAdPosition>();
	}

	@Override
	public void init() {
		ComplexSceneElement e = new ComplexSceneElement();
		e.setVarInitialValue(SceneElement.VAR_VISIBLE, false);
		e.setId("ActionsHUD");
		setElement(e);

		gameWidth = gameState.getValueMap().getValue(SystemFields.GAME_WIDTH);
		gameHeight = gameState.getValueMap().getValue(SystemFields.GAME_HEIGHT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.GameObject#processAction(es
	 * .eucm.eadventure.engine.core.guiactions.GUIAction)
	 */
	@Override
	public DrawableGO<?> processAction(InputAction<?> action) {
		if (MouseGEv.MOUSE_LEFT_PRESSED.equals(action.getGUIEvent())
				|| MouseGEv.MOUSE_RIGHT_PRESSED.equals(action.getGUIEvent())) {
			setVisible(false);
		}
		return super.processAction(action);
	}

	public void showActions(EAdList<EAdSceneElementDef> list, int x, int y) {
		setVisible(true);
		currentTime = 0;
		radius = gameHeight / 8;
		int maxRadius = gameWidth - x < gameHeight - y ? gameWidth - x
				: gameHeight - y;
		maxRadius = x < maxRadius ? x : maxRadius;
		maxRadius = y < maxRadius ? y : maxRadius;
		this.actionsX = x;
		this.actionsY = y;
		radius = Math.min(maxRadius, radius);
		actions = list;
		logger.info("Set element, actions: {}", actions);
		initActionGOs();
	}

	protected void initActionGOs() {
		sceneElements.clear();
		positions.clear();

		float signum = -1.0f;
		if (actionsX < gameWidth - actionsX) {
			signum = 1.0f;
		}

		float accAngle = (float) -Math.PI / 2;
		if (actionsY < gameHeight - actionsY) {
			accAngle = -accAngle;
		}

		float angle = (float) (Math.PI / 4.5) * signum;
		for (EAdSceneElementDef a : actions) {
			boolean add = false;
			int k = 0;

			while (k < a.getBehavior().getAllEffects().size() && !add) {
				add = gameState.evaluate(a.getBehavior().getAllEffects().get(k)
						.getCondition());
				k++;
			}

			if (add) {
				SceneElement action = new SceneElement(a);
				positions.add(new EAdPosition(
						(int) (Math.cos(accAngle) * radius), (int) (Math
								.sin(accAngle) * radius)));
				action.setPosition(Corner.CENTER, actionsX, actionsY);
				sceneElements.add(sceneElementFactory.get(action));
				accAngle += angle;
			}
		}
	}

	@Override
	public void doLayout(EAdTransformation t) {
		float interpolation1 = Interpolator.BOUNCE_END.interpolate(currentTime,
				ANIMATION_TIME, 1.0f);
		float interpolation2 = Interpolator.LINEAR.interpolate(currentTime,
				ANIMATION_TIME, 1.0f);
		transformation.setAlpha(alpha);
		int i = 0;
		for (DrawableGO<?> go : sceneElements) {
			EAdTransformationImpl posT = new EAdTransformationImpl();
			EAdPosition p = this.positions.get(i);
			posT.getMatrix().translate(p.getX() * interpolation1,
					p.getY() * interpolation2, true);

			gui.addElement(go, gui.addTransformation(posT, t));
			i++;
		}
	}

	public int getRadius() {
		return radius;
	}

	@Override
	public void update() {
		float alpha = 1.0f;

		if (currentTime < ANIMATION_TIME) {
			currentTime += gui.getSkippedMilliseconds();
			alpha = Interpolator.LINEAR.interpolate(currentTime,
					ANIMATION_TIME, 1.0f);
		} else {
			alpha = 1.0f;
		}

		currentTime = currentTime > ANIMATION_TIME ? ANIMATION_TIME
				: currentTime;

		alpha = alpha > 1.0f ? 1.0f : alpha;
		setAlpha(alpha);

		for (DrawableGO<?> go : sceneElements) {
			go.update();
		}
	}

	public boolean contains(int x, int y) {
		return isVisible();
	}

}
