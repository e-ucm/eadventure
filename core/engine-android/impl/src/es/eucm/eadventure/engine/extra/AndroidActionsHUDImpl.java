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

package es.eucm.eadventure.engine.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.huds.ActionsHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.AbstractHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.ActionSceneElement;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.platform.EAdCanvas;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.util.EAdTransformation;
import es.eucm.eadventure.engine.core.util.impl.EAdInterpolator;
import es.eucm.eadventure.engine.core.util.impl.EAdTransformationImpl;


@Singleton
public class AndroidActionsHUDImpl extends AbstractHUD implements ActionsHUD {

	private enum ActionsState {
		MOVING_RIGHT, MOVING_LEFT, STOPPED
	};

	private ActionsState state = ActionsState.STOPPED;

	private static int width = 160;

	private static int height = 96;

	private List<EAdPosition> positions;

	private List<SceneElementGO<?>> actionsGO;

	private SceneElementGOFactory sceneElementFactory;

	private GradientDrawable rightGrad, leftGrad;

	private int currentTime, swapTime;

	private float alpha;

	private static final int ANIMATION_TIME = 1000;
	
	private int SWAP_TIME;

	private EAdTransformationImpl transformation = new EAdTransformationImpl();

	private Rect actionBounds = new Rect(100,100,GUI.VIRTUAL_WIDTH-100,GUI.VIRTUAL_HEIGHT-100);

	private GameObjectManager gameObjectManager;

	protected SceneElementGO<?> sceneElement;

	private GameState gameState;
	
	private EAdPosition posInicial, posFinal;

	/**
	 * The logger
	 */
	private static final Logger logger = Logger.getLogger("ActionsHUDImpl");

	/**
	 * List of the {@link EAdAction}s
	 */
	protected EAdList<EAdAction> actions;

	private int x;

	private int y;


	@Inject
	public AndroidActionsHUDImpl(GUI gui, GameObjectManager gameObjectManager,
			GameState gameState, SceneElementGOFactory factory ) {
		super(gui);
		this.gameState = gameState;
		actionsGO = new ArrayList<SceneElementGO<?>>();
		positions = new ArrayList<EAdPosition>();
		this.sceneElementFactory = factory;
		this.gameObjectManager = gameObjectManager;

		rightGrad = new GradientDrawable(
				GradientDrawable.Orientation.LEFT_RIGHT, new int[] {
						Color.argb(0, 0, 0, 0), Color.argb(255, 0, 0, 0) });
		rightGrad.setShape(GradientDrawable.RECTANGLE);
		rightGrad.setBounds(0, 0, 15, height);

		leftGrad = new GradientDrawable(
				GradientDrawable.Orientation.RIGHT_LEFT, new int[] {
						Color.argb(0, 0, 0, 0), Color.argb(255, 0, 0, 0) });
		leftGrad.setShape(GradientDrawable.RECTANGLE);
		leftGrad.setBounds(0, 0, 15, height);
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
				break;
			case SWIPE_RIGHT: 
				moveHUDright();
				break;				
			case SWIPE_LEFT:
				moveHUDleft();
				break;
			default:
			}
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
		this.x = x;
		this.y = y;
		actions = ref.getValidActions();	
		SWAP_TIME = 50 * actions.size();
		initActionGOs();


	}

	protected void initActionGOs() {

		actionsGO.clear();
		positions.clear();
		int place = actionBounds.left;
		for (EAdAction a : actions) {
			ActionSceneElement action = new ActionSceneElement(a);
			action.setPosition(Corner.TOP_LEFT, x, y); 
			positions.add(new EAdPositionImpl(place - x, GUI.VIRTUAL_HEIGHT/2 - height/2 - y));
			actionsGO.add(sceneElementFactory.get(action));
			place += width;
		}
		
	}

	public void doLayout(EAdTransformation t) {
		
		float interpolation1 = EAdInterpolator.BOUNCE_END.interpolate(currentTime, ANIMATION_TIME, 1.0f);
		float interpolation2 = EAdInterpolator.LINEAR.interpolate(currentTime, ANIMATION_TIME, 1.0f);
		int i = 0;
		transformation.setAlpha(alpha);
		for (SceneElementGO<?> go : actionsGO) {
			EAdTransformationImpl posT = new EAdTransformationImpl();
			EAdPosition p = this.positions.get(i);
			if (state == ActionsState.STOPPED) {
				posT.getMatrix().translate(p.getX() * interpolation1, p.getY() * interpolation2, true);
				gui.addElement(go, gui.addTransformation(gui.addTransformation(t, transformation), posT));
			}
			else {
				if (state == ActionsState.MOVING_RIGHT) p.setX(p.getX() + swapTime);
				else p.setX(p.getX() - swapTime);
				
				posT.getMatrix().translate(p.getX(), p.getY(), true);
				gui.addElement(go, gui.addTransformation(t, posT));
			}				
			i++;
		}
		
		if (swapTime == SWAP_TIME) state = ActionsState.STOPPED;
		
	}

	@Override
	public EAdList<EAdAction> getActions() {
		return actions;
	}

	@Override
	public void render(EAdCanvas<?> c) {

		Canvas canvas = (Canvas) c.getNativeGraphicContext();
		canvas.drawARGB(150, 100, 100, 100);		
	}

	public void update() {

		if (currentTime < ANIMATION_TIME) {
			currentTime += GameLoop.SKIP_MILLIS_TICK;
			alpha = EAdInterpolator.LINEAR.interpolate(currentTime,
					ANIMATION_TIME, 1.0f);
		} else {
			alpha = 1.0f;			
		}
		
		if (swapTime < SWAP_TIME) swapTime += GameLoop.SKIP_MILLIS_TICK;

		currentTime = currentTime > ANIMATION_TIME ? ANIMATION_TIME : currentTime;
		swapTime = swapTime > SWAP_TIME ? SWAP_TIME : swapTime;
		alpha = alpha > 1.0f ? 1.0f : alpha;

	}

	private void moveHUDleft() {
		posFinal = positions.get(positions.size()-1);
		if (posFinal.getX() + width + this.x > actionBounds.right){
			swapTime = 0;
			state = ActionsState.MOVING_LEFT;
		}
	}

	private void moveHUDright() {
		posInicial = positions.get(0);
		if (posInicial.getX() + this.x < actionBounds.left){
			swapTime = 0;
			state = ActionsState.MOVING_RIGHT;
		}
	}


	@Override
	public boolean contains(int x, int y) {
		return true;
	}

}
