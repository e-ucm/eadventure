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

package ead.engine.core.gameobjects.sceneelements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ead.common.interfaces.features.enums.Orientation;
import ead.common.model.elements.EAdAction;
import ead.common.model.elements.EAdEvent;
import ead.common.model.elements.ResourcedElement;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.variables.EAdVarDef;
import ead.common.params.fills.PaintFill;
import ead.common.resources.EAdBundleId;
import ead.common.resources.assets.AssetDescriptor;
import ead.common.util.EAdPosition;
import ead.common.util.StringHandler;
import ead.engine.core.game.GameState;
import ead.engine.core.game.ValueMap;
import ead.engine.core.gameobjects.DrawableGameObjectImpl;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.EventGO;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.gameobjects.huds.ActionSceneElement;
import ead.engine.core.input.InputAction;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.RuntimeCompoundDrawable;
import ead.engine.core.platform.assets.RuntimeDrawable;
import ead.engine.core.platform.rendering.GenericCanvas;
import ead.engine.core.util.EAdTransformation;

public abstract class SceneElementGOImpl<T extends EAdSceneElement> extends
		DrawableGameObjectImpl<T> implements SceneElementGO<T> {

	private static final Logger logger = LoggerFactory
			.getLogger("SceneElementGOImpl");

	private EventGOFactory eventFactory;

	protected EAdPosition position;

	protected float scale;

	protected float scaleX;

	protected float scaleY;

	protected Orientation orientation;

	protected String state;

	protected float rotation;

	private int width;

	private int height;

	private int timeDisplayed;

	protected float alpha;

	protected boolean visible;

	private ArrayList<EventGO<?>> eventGOList;

	private EAdBundleId bundle;

	private List<String> statesList;

	private RuntimeCompoundDrawable<?> runtimeDrawable;

	private RuntimeDrawable<?, ?> currentDrawable;

	@Inject
	public SceneElementGOImpl(AssetHandler assetHandler,
			StringHandler stringHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState);
		logger.info("New instance");
		eventGOList = new ArrayList<EventGO<?>>();
		this.eventFactory = eventFactory;
		this.statesList = new ArrayList<String>();
	}

	@Override
	public abstract boolean processAction(InputAction<?> action);

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

		// Caution: this should be removed if we want to remember the element
		// state when the scene changes
		gameState.getValueMap().remove(element);

		gameState.getValueMap().setValue(element.getDefinition(),
				SceneElementDef.VAR_SCENE_ELEMENT, element);
		gameState.getValueMap().checkForUpdates(element.getDefinition());

		// Initial vars
		// Bundle
		gameState.getValueMap().setValue(element,
				ResourcedElement.VAR_BUNDLE_ID,
				element.getDefinition().getInitialBundle());

		// Other vars
		for (Entry<EAdVarDef<?>, Object> entry : element.getVars().entrySet()) {
			gameState.getValueMap().setValue(element, entry.getKey(),
					entry.getValue());
		}

		// Scene element events
		if (element.getEvents() != null) {
			for (EAdEvent event : element.getEvents()) {
				EventGO<?> eventGO = eventFactory.get(event);
				eventGO.setParent(element);
				eventGO.initialize();
				eventGOList.add(eventGO);
			}
		}

		// Definition events
		if (element.getEvents() != null) {
			for (EAdEvent event : element.getDefinition().getEvents()) {
				EventGO<?> eventGO = eventFactory.get(event);
				eventGO.setParent(element);
				eventGO.initialize();
				eventGOList.add(eventGO);
			}

		}

		position = new EAdPosition(0, 0);
		updateVars();
		setVars();
		resetTransfromation();
	}

	/**
	 * Read vars values
	 */
	protected void updateVars() {
		ValueMap valueMap = gameState.getValueMap();
		enable = valueMap.getValue(element, SceneElement.VAR_ENABLE);
		visible = valueMap.getValue(element, SceneElement.VAR_VISIBLE);
		rotation = valueMap.getValue(element, SceneElement.VAR_ROTATION);
		scale = valueMap.getValue(element, SceneElement.VAR_SCALE);
		scaleX = valueMap.getValue(element, SceneElement.VAR_SCALE_X);
		scaleY = valueMap.getValue(element, SceneElement.VAR_SCALE_Y);
		alpha = valueMap.getValue(element, SceneElement.VAR_ALPHA);
		orientation = valueMap.getValue(element, SceneElement.VAR_ORIENTATION);
		state = valueMap.getValue(element, SceneElement.VAR_STATE);

		this.statesList.clear();
		statesList.add(state);
		statesList.add(orientation.toString());

		position.setX(valueMap.getValue(element, SceneElement.VAR_X));
		position.setY(valueMap.getValue(element, SceneElement.VAR_Y));
		position.setDispX(valueMap.getValue(element, SceneElement.VAR_DISP_X));
		position.setDispY(valueMap.getValue(element, SceneElement.VAR_DISP_Y));

		// Bundle update
		EAdBundleId newBundle = valueMap.getValue(element,
				ResourcedElement.VAR_BUNDLE_ID);

		if (bundle != newBundle) {
			bundle = newBundle;
			AssetDescriptor a = element.getDefinition().getResources()
					.getAsset(bundle, SceneElementDef.appearance);
			if (a != null) {
				runtimeDrawable = (RuntimeCompoundDrawable<?>) assetHandler
						.getRuntimeAsset(a, true);
				if ( runtimeDrawable != null ){
					currentDrawable = runtimeDrawable.getDrawable(timeDisplayed, statesList, 0);
				}
			}
		}

	}

	/**
	 * Sets some variables
	 */
	protected void setVars() {
		int scaleW = (int) (width * scale * scaleX);
		int scaleH = (int) (height * scale * scaleY);
		int x = position.getJavaX(scaleW);
		int y = position.getJavaY(scaleH);
		gameState.getValueMap().setValue(element, SceneElement.VAR_LEFT, x);
		gameState.getValueMap().setValue(element, SceneElement.VAR_RIGHT,
				x + scaleW);
		gameState.getValueMap().setValue(element, SceneElement.VAR_TOP, y);
		gameState.getValueMap().setValue(element, SceneElement.VAR_BOTTOM,
				y + scaleH);
		gameState.getValueMap().setValue(element, SceneElement.VAR_CENTER_X,
				x + scaleW / 2);
		gameState.getValueMap().setValue(element, SceneElement.VAR_CENTER_Y,
				y + scaleH / 2);

	}

	public void resetTransfromation() {
		transformation.setAlpha(alpha);
		transformation.setVisible(visible);
		transformation.getMatrix().setIdentity();
		int x = position.getJavaX(width);
		int y = position.getJavaY(height);
		transformation.getMatrix().translate(x, y, true);
		int deltaX = position.getX() - x;
		int deltaY = position.getY() - y;

		transformation.getMatrix().translate(deltaX, deltaY, true);
		transformation.getMatrix().rotate(rotation, true);
		transformation.getMatrix().scale(scale * scaleX, scale * scaleY, true);
		transformation.getMatrix().translate(-deltaX, -deltaY, true);

	}

	@Override
	public SceneElementGO<?> getDraggableElement() {
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

		if (eventGOList != null)
			for (EventGO<?> eventGO : eventGOList)
				eventGO.update();

		gameState.getValueMap().setUpdateListEnable(false);
		timeDisplayed += gui.getSkippedMilliseconds();
		gameState.getValueMap().setValue(element,
				SceneElement.VAR_TIME_DISPLAYED, timeDisplayed);
		gameState.getValueMap().setUpdateListEnable(true);

		if (gameState.getValueMap().checkForUpdates(element)) {
			gameState.getValueMap().setUpdateListEnable(false);
			updateVars();
			setVars();
			resetTransfromation();
			gameState.getValueMap().setUpdateListEnable(true);
		}
		if (runtimeDrawable != null) {
			runtimeDrawable.update();
			currentDrawable = runtimeDrawable.getDrawable(timeDisplayed,
					statesList, 0);
			if (currentDrawable != null)
				if (currentDrawable.getWidth() != width)
					setWidth(currentDrawable.getWidth());
			if (currentDrawable.getHeight() != height)
				setHeight(currentDrawable.getHeight());
		}
	}

	@Override
	public T getElement() {
		return super.getElement();
	}

	@Override
	public void setPosition(EAdPosition position) {
		ValueMap valueMap = gameState.getValueMap();
		valueMap.setValue(element, SceneElement.VAR_X, position.getX());
		valueMap.setValue(element, SceneElement.VAR_Y, position.getY());
		valueMap.setValue(element, SceneElement.VAR_DISP_X, position.getDispX());
		valueMap.setValue(element, SceneElement.VAR_DISP_Y, position.getDispY());
	}

	@Override
	public void setOrientation(Orientation orientation) {
		gameState.getValueMap().setValue(element, SceneElement.VAR_ORIENTATION,
				orientation);
	}

	@Override
	public Orientation getOrientation() {
		return orientation;
	}

	@Override
	public RuntimeDrawable<?, ?> getRenderAsset() {
		return currentDrawable;
	}

	@Override
	public List<AssetDescriptor> getAssets(List<AssetDescriptor> assetList,
			boolean allAssets) {
		List<EAdBundleId> bundles = new ArrayList<EAdBundleId>();
		if (allAssets)
			bundles.addAll(getElement().getDefinition().getResources()
					.getBundles());
		else
			bundles.add(gameState.getValueMap().getValue(element,
					ResourcedElement.VAR_BUNDLE_ID));

		for (EAdBundleId bundle : bundles) {
			AssetDescriptor a = getElement().getDefinition().getResources()
					.getAsset(bundle, SceneElementDef.appearance);
			assetList.add(a);
		}

		for (EAdAction a : getActions())
			sceneElementFactory.get(new ActionSceneElement(a)).getAssets(
					assetList, true);

		return assetList;
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
		gameState.getValueMap()
				.setValue(element, SceneElement.VAR_SCALE, scale);
	}

	public void setWidth(int width) {
		this.width = width;
		gameState.getValueMap()
				.setValue(element, SceneElement.VAR_WIDTH, width);
	}

	public void setHeight(int height) {
		this.height = height;
		gameState.getValueMap().setValue(element, SceneElement.VAR_HEIGHT,
				height);
	}

	@Override
	public int getCenterX() {
		float[] f = transformation.getMatrix().multiplyPoint(width / 2,
				height / 2, true);
		return (int) f[0];
	}

	@Override
	public int getCenterY() {
		float[] f = transformation.getMatrix().multiplyPoint(width / 2,
				height / 2, true);
		return (int) f[1];
	}

	public EAdPosition getPosition() {
		return position;
	}

	@Override
	public EAdList<EAdAction> getActions() {
		return element.getDefinition().getActions();
	}

	public float getScale() {
		return scale;
	}

	public boolean isEnable() {
		return enable;
	}

	@Override
	public boolean contains(int x, int y) {
		if (this.currentDrawable != null)
			return this.currentDrawable.contains(x, y);
		return false;
	}

	@Override
	public void render(GenericCanvas c) {
		if (this.currentDrawable != null)
			// FIXME fix me! no suppress warnings
			currentDrawable.render(c);
		else {
			// FIXME Improve, when has no asset
			c.setPaint(PaintFill.BLACK_ON_WHITE);
			c.fillRect(0, 0, width, height);
		}
	}

	@Override
	public void doLayout(EAdTransformation transformation) {

	}

	public void setX(int x) {
		this.position.set(x, position.getY());
		gameState.getValueMap().setValue(element, SceneElement.VAR_X, x);
	}

	public void setY(int y) {
		this.position.set(position.getX(), y);
		gameState.getValueMap().setValue(element, SceneElement.VAR_Y, y);
	}

	public void setAlpha(float alpha) {
		gameState.getValueMap()
				.setValue(element, SceneElement.VAR_ALPHA, alpha);
		this.alpha = alpha;
	}

	public void setEnabled(boolean enable) {
		gameState.getValueMap().setValue(element, SceneElement.VAR_ENABLE,
				enable);
		this.enable = enable;
	}

	@Override
	public void collectSceneElements(List<EAdSceneElement> elements) {
		if (getElement() != null)
			elements.add(getElement());
	}

	public boolean isVisible() {
		return visible;
	}
}
