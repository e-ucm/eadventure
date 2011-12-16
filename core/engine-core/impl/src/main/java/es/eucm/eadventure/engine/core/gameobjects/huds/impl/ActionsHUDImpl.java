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

package es.eucm.eadventure.engine.core.gameobjects.huds.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.guievents.enums.KeyCode;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.util.EAdTransformation;
import es.eucm.eadventure.common.util.impl.EAdInterpolator;
import es.eucm.eadventure.common.util.impl.EAdTransformationImpl;
import es.eucm.eadventure.engine.core.game.GameLoop;
import es.eucm.eadventure.engine.core.game.GameState;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.go.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.huds.ActionsHUD;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.guiactions.KeyAction;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.rendering.EAdCanvas;

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
	private static final Logger logger = Logger.getLogger("ActionsHUDImpl");

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

	private float alpha;

	@Inject
	public ActionsHUDImpl(GUI gui, GameObjectManager gameObjectManager,
			GameState gameState, SceneElementGOFactory sceneElementFactory) {
		super(gui);
		logger.info("New instance");
		this.gameObjectManager = gameObjectManager;
		actionsGO = new ArrayList<SceneElementGO<?>>();
		positions = new ArrayList<EAdPosition>();
		width = gameState.getValueMap().getValue(SystemFields.GAME_WIDTH);
		height = gameState.getValueMap().getValue(SystemFields.GAME_HEIGHT);
		this.sceneElementFactory = sceneElementFactory;
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.GameObject#processAction(es
	 * .eucm.eadventure.engine.core.guiactions.GUIAction)
	 */
	@Override
	public boolean processAction(GUIAction action) {
		boolean remove = false;
		if (action instanceof MouseAction) {
			MouseAction temp = (MouseAction) action;

			switch (temp.getType()) {
			case CLICK:
				remove = true;
			default:
			}

		} else if (action instanceof KeyAction) {
			KeyAction keyAction = (KeyAction) action;
			remove = keyAction.getKeyCode() == KeyCode.ESC;
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
		logger.info("Set element, actions: " + actions );
		initActionGOs();
	}

	protected void initActionGOs() {
		actionsGO.clear();
		positions.clear(); 
		
		float signum = -1.0f;
		if ( x < width - x ){
			signum = 1.0f;
		}
		
		float accAngle = (float) - Math.PI / 2;
		if ( y < height - y ){
			accAngle = -accAngle;
		}
				
		float angle = (float) (Math.PI / 4.5) * signum;
		for (EAdAction a : actions) {
			ActionSceneElement action = new ActionSceneElement(a);
			positions.add(new EAdPositionImpl((int) (Math.cos(accAngle) * radius), (int) (Math.sin(accAngle) * radius )));
			action.setPosition(Corner.CENTER, x, y); 
			actionsGO.add(sceneElementFactory.get(action));
			accAngle += angle;
		}
	}

	public void doLayout(EAdTransformation t) {
		float interpolation1 = EAdInterpolator.BOUNCE_END.interpolate(currentTime, ANIMATION_TIME, 1.0f);
		float interpolation2 = EAdInterpolator.LINEAR.interpolate(currentTime, ANIMATION_TIME, 1.0f);
		transformation.setAlpha(alpha);
		int i = 0;
		for (SceneElementGO<?> go : actionsGO) {
			EAdTransformationImpl posT = new EAdTransformationImpl();
			EAdPosition p = this.positions.get(i);
			posT.getMatrix().translate(p.getX() * interpolation1, p.getY() * interpolation2, true);
			gui.addElement(go, gui.addTransformation(gui.addTransformation(t, transformation), posT));
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
	public void render(EAdCanvas<?> c) {

	}

	public void update() {

		if (currentTime < ANIMATION_TIME) {
			currentTime += GameLoop.SKIP_MILLIS_TICK;
			alpha = EAdInterpolator.LINEAR.interpolate(currentTime,
					ANIMATION_TIME, 1.0f);
		} else {
			alpha = 1.0f;
			
		}
		
		currentTime = currentTime > ANIMATION_TIME ? ANIMATION_TIME : currentTime;

		alpha = alpha > 1.0f ? 1.0f : alpha;
	}

	@Override
	public boolean contains(int x, int y) {
		return true;
	}

}
