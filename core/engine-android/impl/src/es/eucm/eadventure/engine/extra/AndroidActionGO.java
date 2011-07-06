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

import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.actions.impl.EAdBasicAction;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.EffectGO;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;

/**
 * 
 * @param <EAdAction>
 *
 */
public class AndroidActionGO implements GameObject<EAdAction>, Positioned, Named {	

	private static final Logger logger = Logger.getLogger("AndroidActionGO");
	
	private int x;
	
	private int y;
	
	private float scale = 0.7f;
	
	private EAdAction action;
	
	private GameState gameState;
	
	private GameObjectFactory goFactory;
	
	private boolean mouseInside = false;
	
	private GUI gui;
	
	private String actionName;
	
	private int width;
	
	private int height;
	
	@Inject
	public AndroidActionGO(GameState gameState,
			GameObjectFactory goFactory,
			GUI gui) {
		this.gameState = gameState;
		this.goFactory = goFactory;
		this.gui = gui;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public float getScale() {
		return scale;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public EAdAsset getAsset(AssetHandler assetHandler) {
		actionName = assetHandler.getString(action.getAsset(EAdBasicAction.name));
		return assetHandler.getResourceAsAsset(action, action.getInitialBundle(), EAdBasicAction.appearance);
	}

	public String getName() {
		return actionName;
	}
	
	public EAdAction getAction() {
		return action;
	}

	@Override
	public boolean processAction(GUIAction action) {
		
		if (action instanceof MouseAction) {
			MouseAction temp = (MouseAction) action;

			switch (temp.getType()) {
			case ENTERED:
				logger.info("entered");
				this.setScale(1.0f);
				action.setConsummed(true);
				this.mouseInside = true;
				return true;
			case EXITED:
				logger.info("exited");
				this.setScale(0.7f);
				action.setConsummed(true);
				this.mouseInside = false;
				return true;
			case LEFT_CLICK:
				logger.info("left-click");
				action.setConsummed(true);
				for (int i = this.action.getEffects().size() - 1; i >= 0; i--) {
					EAdEffect effect = this.action.getEffects().get(i);
					EffectGO<?> effectGO = (EffectGO<?>) goFactory.get(effect);
					gameState.getEffects().add(effectGO);
					gui.removeHUD();
				}
			}
		}

		return false;
	}

	public boolean isMouseInside() {
		return mouseInside;
	}
	
	@Override
	public void setElement(EAdAction element) {
		this.action = element;
	}

	@Override
	public GameObject<?> getDraggableElement(MouseState mouseState) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	
}
