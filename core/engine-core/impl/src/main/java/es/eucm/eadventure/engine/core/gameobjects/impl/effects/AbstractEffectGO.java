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

package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import java.util.List;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.elements.EAdEffect;
import es.eucm.eadventure.common.model.elements.scene.EAdSceneElement;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.util.EAdPositionImpl;
import es.eucm.eadventure.engine.core.game.GameLoop;
import es.eucm.eadventure.engine.core.game.GameState;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.go.EffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.DrawableGameObjectImpl;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.platform.rendering.GenericCanvas;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

public abstract class AbstractEffectGO<P extends EAdEffect> extends
		DrawableGameObjectImpl<P> implements EffectGO<P> {

	private boolean stopped = false;

	private boolean initialized = false;
	
	protected int currentTime = 0;

	protected GUIAction action;
	
	/**
	 * Element that launched the effect
	 */
	protected EAdSceneElement parent;
	
	@Inject
	public AbstractEffectGO(AssetHandler assetHandler,
			StringHandler stringsReader, SceneElementGOFactory gameObjectFactory,
			GUI gui, GameState gameState) {
		super(assetHandler, stringsReader, gameObjectFactory, gui, gameState);
	}

	@Override
	public void initilize() {
		initialized = true;
		stopped = false;
	}
	
	public void update(){
		currentTime += GameLoop.SKIP_MILLIS_TICK;
	}

	public P getEffect() {
		return element;
	}

	@Override
	public boolean isBlocking() {
		return element.isBlocking();
	}

	@Override
	public boolean isOpaque() {
		return element.isOpaque();
	}
	
	@Override
	public boolean isQueueable() {
		return element.isQueueable();
	}

	@Override
	public boolean isStopped() {
		return stopped;
	}

	public void stop() {
		stopped = true;
	}

	public void run() {
		stopped = false;
	}

	public boolean isInitilized() {
		return initialized;
	}

	public void finish() {
		initialized = false;
		stopped = true;
		for ( EAdEffect e: element.getNextEffects() ){
			gameState.addEffect(e, action, parent);
		}
	}

	public void setGUIAction(GUIAction action) {
		this.action = action;
	}
	
	public void setParent( EAdSceneElement parent ){
		this.parent = parent;
	}
	
	@Override
	public void render(GenericCanvas<?> c) {

	}

	@Override
	public boolean contains(int x, int y) {
		return element.isOpaque();
	}
	
	public EAdPositionImpl getPosition(){
		return null;
	}
	
	@Override
	public boolean processAction(GUIAction action) {
		return false;
	}

	@Override
	public void doLayout(EAdTransformation transformation) {
		
	}

	@Override
	public boolean isEnable() {
		return element.isOpaque();
	}

	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {
		return assetList;
	}

	@Override
	public void setPosition(EAdPositionImpl p) {
		
	}

}
