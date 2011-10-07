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

package es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.FramesAnimation;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.OrientedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.StateDrawable;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.AbstractGameObject;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public abstract class SceneElementGOImpl<T extends EAdSceneElement> extends
		AbstractGameObject<T> implements SceneElementGO<T> {

	private static final Logger logger = Logger.getLogger("SceneElementGOImpl");

	protected EAdPositionImpl position;

	protected float scale;

	protected Orientation orientation;

	protected String state;

	protected float rotation;

	private int width;

	private int height;

	private int timeDisplayed;

	protected float alpha;

	protected boolean visible;

	@Inject
	public SceneElementGOImpl(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState,
				valueMap, platformConfiguration);
		logger.info("New instance");
	}

	@Override
	public abstract boolean processAction(GUIAction action);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.impl.AbstractGameObject#setElement
	 * (es.eucm.eadventure.common.model.EAdElement)
	 * 
	 * Should be implemented to get position, scale, orientation and other
	 * values
	 */
	@Override
	public void setElement(T element) {
		super.setElement(element);
		// If element is a clone, all fields associated to it in the value map
		// must be removed
		if (element.isClone()) {
			valueMap.remove(element);
		}

		for (Entry<EAdVarDef<?>, Object> entry : element.getVars().entrySet()) {
			// FIXME this has to change, to disappear
			valueMap.setValue(entry.getKey(), entry.getValue(), element);
		}

		position = new EAdPositionImpl(0, 0);
		updateVars();
		setVars();
		// To load dimensions
		getRenderAsset();
	}

	/**
	 * Read vars values
	 */
	protected void updateVars() {
		visible = valueMap.getValue(element, EAdBasicSceneElement.VAR_VISIBLE);
		rotation = valueMap
				.getValue(element, EAdBasicSceneElement.VAR_ROTATION);
		scale = valueMap.getValue(element, EAdBasicSceneElement.VAR_SCALE);
		alpha = valueMap.getValue(element, EAdBasicSceneElement.VAR_ALPHA);
		orientation = valueMap.getValue(element,
				EAdBasicSceneElement.VAR_ORIENTATION);
		state = valueMap.getValue(element, EAdBasicSceneElement.VAR_STATE);
		timeDisplayed = valueMap.getValue(element,
				EAdBasicSceneElement.VAR_TIME_DISPLAYED);
		position.setX(valueMap.getValue(element, EAdBasicSceneElement.VAR_X));
		position.setY(valueMap.getValue(element, EAdBasicSceneElement.VAR_Y));
		position.setDispX(valueMap.getValue(element,
				EAdBasicSceneElement.VAR_DISP_X));
		position.setDispY(valueMap.getValue(element,
				EAdBasicSceneElement.VAR_DISP_Y));

		updateTransformation();
	}

	/**
	 * Sets some variables
	 */
	protected void setVars() {
		int scaleW = (int) (width * scale);
		int scaleH = (int) (height * scale);
		int x = position.getJavaX(scaleW);
		int y = position.getJavaY(scaleH);
		valueMap.setValue(element, EAdBasicSceneElement.VAR_LEFT, x);
		valueMap.setValue(element, EAdBasicSceneElement.VAR_RIGHT, x + scaleW);
		valueMap.setValue(element, EAdBasicSceneElement.VAR_TOP, y);
		valueMap.setValue(element, EAdBasicSceneElement.VAR_BOTTOM, y + scaleH);

	}

	protected void updateTransformation() {
		transformation.setAlpha(alpha);
		transformation.setVisible(visible);
		transformation.getMatrix().setIdentity();
		int x = position.getJavaX(width);
		int y = position.getJavaY(height);
		transformation.getMatrix().postTranslate(x, y);
		int deltaX = position.getX() - x;
		int deltaY = position.getY() - y;

		transformation.getMatrix().postTranslate(deltaX, deltaY);
		transformation.getMatrix().postRotate(rotation);
		transformation.getMatrix().postScale(scale, scale);
		transformation.getMatrix().postTranslate(-deltaX, -deltaY);

	}

	@Override
	public GameObject<?> getDraggableElement(MouseState mouseState) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.impl.AbstractGameObject#update
	 * (es.eucm.eadventure.engine.core.GameState)
	 * 
	 * Should update the state of all sub-elements and resources
	 */
	@Override
	public void update() {
		super.update();

		valueMap.setValue(element, EAdBasicSceneElement.VAR_TIME_DISPLAYED,
				timeDisplayed + GameLoop.SKIP_MILLIS_TICK);
		if (getAsset() != null)
			getAsset().update();
		updateVars();
		setVars();
	}

	@Override
	public T getElement() {
		return super.getElement();
	}

	@Override
	public void setPosition(EAdPosition position) {
		valueMap.setValue(element, EAdBasicSceneElement.VAR_X, position.getX());
		valueMap.setValue(element, EAdBasicSceneElement.VAR_Y, position.getY());
		valueMap.setValue(element, EAdBasicSceneElement.VAR_DISP_X,
				position.getDispX());
		valueMap.setValue(element, EAdBasicSceneElement.VAR_DISP_Y,
				position.getDispY());
	}

	@Override
	public void setOrientation(Orientation orientation) {
		valueMap.setValue(element, EAdBasicSceneElement.VAR_ORIENTATION,
				orientation);
	}

	@Override
	public Orientation getOrientation() {
		return orientation;
	}

	@Override
	public AssetDescriptor getCurrentAssetDescriptor() {
		AssetDescriptor a = element.getResources().getAsset(getCurrentBundle(),
				EAdBasicSceneElement.appearance);

		return getCurrentAssetDescriptor(a);
	}

	protected AssetDescriptor getCurrentAssetDescriptor(AssetDescriptor a) {
		if (a == null)
			return null;

		// Check state
		if (a instanceof StateDrawable) {
			StateDrawable stateDrawable = (StateDrawable) a;
			return getCurrentAssetDescriptor(stateDrawable.getDrawable(state));
		}
		// Check orientation
		else if (a instanceof OrientedDrawable) {
			return getCurrentAssetDescriptor(((OrientedDrawable) a)
					.getDrawable(orientation));
		}
		// Check frame animation
		else if (a instanceof FramesAnimation) {
			return ((FramesAnimation) a).getFrameFromTime(timeDisplayed);
		} else {
			return a;
		}
	}

	@Override
	public DrawableAsset<?> getAsset() {
		DrawableAsset<?> r = (DrawableAsset<?>) assetHandler
				.getRuntimeAsset(getCurrentAssetDescriptor());
		if (r != null && !r.isLoaded())
			r.loadAsset();
		return r;
	}

	@Override
	public DrawableAsset<?> getRenderAsset() {
		DrawableAsset<?> r = getAsset();
		if (r instanceof DrawableAsset && r.isLoaded()) {
			setWidth(r.getWidth());
			setHeight(r.getHeight());
			return r.getDrawable();
		}
		return r;
	}

	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {

		if (visible && !allAssets) {
			return assetList;
		}

		List<EAdBundleId> bundles = new ArrayList<EAdBundleId>();
		if (allAssets)
			bundles.addAll(getElement().getResources().getBundles());
		else
			bundles.add(getCurrentBundle());

		for (EAdBundleId bundle : bundles) {
			AssetDescriptor a = getElement().getResources().getAsset(bundle,
					EAdBasicSceneElement.appearance);
			getAssetsRecursively(a, assetList, allAssets);
		}
		return assetList;
	}

	protected void getAssetsRecursively(AssetDescriptor a,
			List<RuntimeAsset<?>> assetList, boolean allAssets) {
		if (a == null)
			return;

		if (a instanceof StateDrawable) {
			if (!allAssets)
				getAssetsRecursively(((StateDrawable) a).getDrawable(state),
						assetList, allAssets);
			else {
				for (String s : ((StateDrawable) a).getStates()) {
					getAssetsRecursively(((StateDrawable) a).getDrawable(s),
							assetList, allAssets);
				}

			}

		} else if (a instanceof OrientedDrawable) {
			if (!allAssets)
				getAssetsRecursively(
						((OrientedDrawable) a).getDrawable(orientation),
						assetList, allAssets);
			else {

				for (Orientation o : Orientation.values()) {
					getAssetsRecursively(((OrientedDrawable) a).getDrawable(o),
							assetList, allAssets);
				}
			}
		} else if (a instanceof FramesAnimation) {
			if (!allAssets)
				getAssetsRecursively(
						((FramesAnimation) a).getFrameFromTime(timeDisplayed),
						assetList, allAssets);
			else {
				for (int i = 0; i < ((FramesAnimation) a).getFrameCount(); i++) {
					getAssetsRecursively(
							((FramesAnimation) a).getFrameFromTime(i),
							assetList, allAssets);
				}
			}
		} else
			assetList.add(assetHandler.getRuntimeAsset(a));
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setScale(float scale) {
		valueMap.setValue(element, EAdBasicSceneElement.VAR_SCALE, scale);
	}

	public void setWidth(int width) {
		this.width = width;
		valueMap.setValue(element, EAdBasicSceneElement.VAR_WIDTH, width);
	}

	public void setHeight(int height) {
		this.height = height;
		valueMap.setValue(element, EAdBasicSceneElement.VAR_HEIGHT, height);
	}

	@Override
	public int getCenterX() {
		float[] f = transformation.getMatrix().postMultiplyPoint(width / 2,
				height / 2);
		return (int) f[0];
	}

	@Override
	public int getCenterY() {
		float[] f = transformation.getMatrix().postMultiplyPoint(width / 2,
				height / 2);
		return (int) f[1];
	}

	public EAdPosition getPosition() {
		return position;
	}

	@Override
	public List<EAdAction> getValidActions() {
		return null;
	}

	public float getScale() {
		return scale;
	}

}
