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

package ead.engine.core.gameobjects.effects;

import java.util.List;

import com.google.inject.Inject;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.resources.StringHandler;
import ead.common.resources.assets.AssetDescriptor;
import ead.common.util.EAdPosition;
import ead.engine.core.game.GameLoop;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.DrawableGameObjectImpl;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.EffectGO;
import ead.engine.core.input.InputAction;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.rendering.GenericCanvas;
import ead.engine.core.util.EAdTransformation;

public abstract class AbstractEffectGO<P extends EAdEffect> extends
		DrawableGameObjectImpl<P> implements EffectGO<P> {

	private boolean stopped = false;

	private boolean initialized = false;
	
	protected int currentTime = 0;

	protected InputAction<?> action;
	
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
	public void initialize() {
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

	public void setGUIAction(InputAction<?> action) {
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
	
	public EAdPosition getPosition(){
		return null;
	}
	
	@Override
	public boolean processAction(InputAction<?> action) {
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
	public List<AssetDescriptor> getAssets(List<AssetDescriptor> assetList,
			boolean allAssets) {
		return assetList;
	}

}
