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

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.EAdAction;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.guievents.enums.KeyGEvCode;
import ead.common.model.elements.variables.SystemFields;
import ead.common.util.Interpolator;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;
import ead.engine.core.evaluators.EvaluatorFactory;
import ead.engine.core.game.GameLoop;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.GameObjectManager;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.input.InputAction;
import ead.engine.core.input.actions.KeyActionImpl;
import ead.engine.core.input.actions.MouseActionImpl;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.rendering.GenericCanvas;
import ead.engine.core.util.EAdTransformation;
import ead.engine.core.util.EAdTransformationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Default generic implementation of the {@link ActionsHUD}
 * </p>
 *
 */
@Singleton
public class ActionsHUDImpl extends AbstractHUD implements ActionsHUD {

	private static final int ANIMATION_TIME = 500;

	private int currentTime;

	private EAdTransformationImpl transformation = new EAdTransformationImpl();

	private int width;

	private int height;

	/**
	 * The logger
	 */
	private static final Logger logger = LoggerFactory.getLogger("ActionsHUDImpl");

	/**
	 * List of the {@link EAdAction}s
	 */
	protected EAdList<EAdAction> actions;

	/**
	 * The position in the x coordinates
	 */
	private int x;

	/**
	 * The position in the y coordinates
	 */
	private int y;

	/**
	 * The radius of the actions HUD
	 */
	protected int radius;

	protected SceneElementGO<?> sceneElement;

	private List<EAdPosition> positions;

	private GameObjectManager gameObjectManager;

	private List<SceneElementGO<?>> actionsGO;

	private SceneElementGOFactory sceneElementFactory;

	private EvaluatorFactory evaluatorFactory;

	private float alpha;

	@Inject
	public ActionsHUDImpl(GUI gui, GameObjectManager gameObjectManager,
			GameState gameState, SceneElementGOFactory sceneElementFactory,
			EvaluatorFactory evaluatorFactory) {
		super(gui);
		logger.info("New instance");
		this.gameObjectManager = gameObjectManager;
		actionsGO = new ArrayList<SceneElementGO<?>>();
		positions = new ArrayList<EAdPosition>();
		width = gameState.getValueMap().getValue(SystemFields.GAME_WIDTH);
		height = gameState.getValueMap().getValue(SystemFields.GAME_HEIGHT);
		this.sceneElementFactory = sceneElementFactory;
		this.evaluatorFactory = evaluatorFactory;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.GameObject#processAction(es
	 * .eucm.eadventure.engine.core.guiactions.GUIAction)
	 */
	@Override
	public boolean processAction(InputAction<?> action) {
		boolean remove = false;
		if (action instanceof MouseActionImpl) {
			MouseActionImpl temp = (MouseActionImpl) action;

			switch (temp.getType()) {
                case CLICK: remove = true; break;
                default:
                    logger.warn("Non-click MouseActionImpl in HUD - totally unexpected");
			}

		} else if (action instanceof KeyActionImpl) {
			KeyActionImpl keyAction = (KeyActionImpl) action;
			remove = keyAction.getKeyCode() == KeyGEvCode.ESC;
		}

		if (remove) {
			logger.info("Remove actions HUD");
			gameObjectManager.removeHUD(this);
			action.consume();
		}

		action.consume();
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.GameObject#setElement(java
	 * .lang.Object)
	 */

    @Override
	public void setElement(SceneElementGO<?> ref, int x, int y) {
		currentTime = 0;
		sceneElement = ref;
		radius = height / 8;
		int maxRadius = width - x < height - y ? width - x : height - y;
		maxRadius = x < maxRadius ? x : maxRadius;
		maxRadius = y < maxRadius ? y : maxRadius;
		this.x = x;
		this.y = y;
		radius = Math.min(maxRadius, radius);
		actions = ref.getActions();
		logger.info("Set element, actions: {}", actions);
		initActionGOs();
	}

	protected void initActionGOs() {
		actionsGO.clear();
		positions.clear();

		float signum = -1.0f;
		if (x < width - x) {
			signum = 1.0f;
		}

		float accAngle = (float) -Math.PI / 2;
		if (y < height - y) {
			accAngle = -accAngle;
		}

		float angle = (float) (Math.PI / 4.5) * signum;
		for (EAdAction a : actions) {
			boolean add = false;
			int k = 0;
			while ( k < a.getEffects().size() && !add ){
				add = evaluatorFactory.evaluate(a.getEffects().get(k).getCondition());
				k++;
			}

			if (add) {
				ActionSceneElement action = new ActionSceneElement(a);
				positions.add(new EAdPosition(
						(int) (Math.cos(accAngle) * radius), (int) (Math
								.sin(accAngle) * radius)));
				action.setPosition(Corner.CENTER, x, y);
				actionsGO.add(sceneElementFactory.get(action));
				accAngle += angle;
			}
		}
	}

    @Override
	public void doLayout(EAdTransformation t) {
		float interpolation1 = Interpolator.BOUNCE_END.interpolate(
				currentTime, ANIMATION_TIME, 1.0f);
		float interpolation2 = Interpolator.LINEAR.interpolate(currentTime,
				ANIMATION_TIME, 1.0f);
		transformation.setAlpha(alpha);
		int i = 0;
		for (SceneElementGO<?> go : actionsGO) {
			EAdTransformationImpl posT = new EAdTransformationImpl();
			EAdPosition p = this.positions.get(i);
			posT.getMatrix().translate(p.getX() * interpolation1,
					p.getY() * interpolation2, true);
			gui.addElement(
					go,
					gui.addTransformation(
							gui.addTransformation(t, transformation), posT));
			i++;
		}
	}

	@Override
	public EAdList<EAdAction> getActions() {
		return actions;
	}

	public int getRadius() {
		return radius;
	}

	@Override
	public void render(GenericCanvas<?> c) {

	}

    @Override
	public void update() {

		if (currentTime < ANIMATION_TIME) {
			currentTime += GameLoop.SKIP_MILLIS_TICK;
			alpha = Interpolator.LINEAR.interpolate(currentTime,
					ANIMATION_TIME, 1.0f);
		} else {
			alpha = 1.0f;
		}

		currentTime = currentTime > ANIMATION_TIME ? ANIMATION_TIME
				: currentTime;

		alpha = alpha > 1.0f ? 1.0f : alpha;
	}

	@Override
	public boolean contains(int x, int y) {
		return true;
	}

}
