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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ead.common.interfaces.features.enums.Orientation;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.EAdEvent;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.resources.assets.AssetDescriptor;
import ead.common.util.EAdPosition;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.InputActionProcessor;
import ead.engine.core.gameobjects.events.EventGO;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.input.InputAction;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.RuntimeCompoundDrawable;
import ead.engine.core.platform.assets.RuntimeDrawable;
import ead.engine.core.util.EAdTransformation;
import ead.engine.core.util.EAdTransformationImpl;

public class SceneElementGOImpl implements SceneElementGO<EAdSceneElement> {

	protected static final Logger logger = LoggerFactory
			.getLogger("SceneElementGOImpl");

	private EventGOFactory eventFactory;

	protected AssetHandler assetHandler;

	protected EAdPosition position;

	protected SceneElementGO<?> parent;

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

	protected String currentBundle;

	private List<String> statesList;

	private RuntimeCompoundDrawable<?> runtimeDrawable;

	private RuntimeDrawable<?, ?> currentDrawable;

	protected GameState gameState;

	protected boolean mouseOver;

	protected InputActionProcessor inputProcessor;

	protected List<SceneElementGO<?>> sceneElements;

	protected SceneElementGOFactory sceneElementFactory;

	/**
	 * The game's asset handler
	 */

	protected GUI gui;

	protected EAdTransformation transformation;

	protected boolean enable;

	protected EAdSceneElement element;

	/**
	 * Comparator to order the scene elements
	 */
	private Comparator<SceneElementGO<?>> comparator;

	@Inject
	public SceneElementGOImpl(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		this.eventFactory = eventFactory;
		this.statesList = new ArrayList<String>();
		this.gameState = gameState;
		this.assetHandler = assetHandler;
		this.sceneElementFactory = sceneElementFactory;
		this.gui = gui;
		sceneElements = new ArrayList<SceneElementGO<?>>();
		eventGOList = new ArrayList<EventGO<?>>();
	}

	public SceneElementGO<?> getParent() {
		return parent;
	}

	public void setParent(SceneElementGO<?> parent) {
		this.parent = parent;
	}

	public List<SceneElementGO<?>> getChildren() {
		return sceneElements;
	}

	@Override
	public boolean contains(int x, int y) {
		if (isVisible()) {
			float[] mouse = transformation.getMatrix().multiplyPointInverse(x,
					y, true);
			x = (int) mouse[0];
			y = (int) mouse[1];
			if (this.currentDrawable != null) {
				return this.currentDrawable.contains(x, y);
			} else {
				return x >= 0 && y >= 0 && x < width && y < height;
			}
		}
		return false;
	}

	public void setInputProcessor(InputActionProcessor processor) {
		this.inputProcessor = processor;
	}

	@Override
	public SceneElementGO<?> processAction(InputAction<?> action) {
		if (inputProcessor != null) {
			inputProcessor.processAction(action);
		}

		if (!action.isConsumed()) {
			if (isEnable()) {
				// Effects in the scene element instance
				EAdList<EAdEffect> list = element.getEffects(action
						.getGUIEvent());
				int size = list == null ? 0 : list.size();
				addEffects(list, action);

				// Effects in the definition
				list = element.getDefinition().getEffects(action.getGUIEvent());
				size += list == null ? 0 : list.size();
				if (size > 0) {
					action.consume();
				}
				addEffects(list, action);
			}
		}

		if (!action.isConsumed() && parent != null) {
			return parent.processAction(action);
		} else {
			return this;
		}
	}

	/**
	 * Sets a comparator to automatically reorder the scene elements (modifies
	 * drawing order)
	 * 
	 * @param comparator
	 */
	public void setComparator(Comparator<SceneElementGO<?>> comparator) {
		this.comparator = comparator;
	}

	private void addEffects(EAdList<EAdEffect> list, InputAction<?> action) {
		if (list != null && list.size() > 0) {
			action.consume();
			for (EAdEffect e : list) {
				logger.debug("GUI Action: '{}' effect '{}'", action, e);
				gameState.addEffect(e, action, element);
			}
		}
	}

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
	public void setElement(EAdSceneElement element) {
		this.element = element;
		transformation = new EAdTransformationImpl();

		// Caution: this should be removed if we want to remember the element
		// state when the scene changes
		gameState.remove(element);

		gameState.setValue(element.getDefinition(),
				SceneElementDef.VAR_SCENE_ELEMENT, element);
		gameState.checkForUpdates(element.getDefinition());

		// Initial vars
		// Bundle
		gameState.setValue(element, SceneElement.VAR_BUNDLE_ID,
				SceneElementDef.INITIAL_BUNDLE);

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

		enable = gameState.getValue(element, SceneElement.VAR_ENABLE);
		visible = gameState.getValue(element, SceneElement.VAR_VISIBLE);
		rotation = gameState.getValue(element, SceneElement.VAR_ROTATION);
		scale = gameState.getValue(element, SceneElement.VAR_SCALE);
		scaleX = gameState.getValue(element, SceneElement.VAR_SCALE_X);
		scaleY = gameState.getValue(element, SceneElement.VAR_SCALE_Y);
		alpha = gameState.getValue(element, SceneElement.VAR_ALPHA);
		orientation = gameState.getValue(element, SceneElement.VAR_ORIENTATION);
		state = gameState.getValue(element, SceneElement.VAR_STATE);

		statesList.clear();
		statesList.add(state);
		statesList.add(orientation.toString());

		position.setX(gameState.getValue(element, SceneElement.VAR_X));
		position.setY(gameState.getValue(element, SceneElement.VAR_Y));
		position.setDispX(gameState.getValue(element, SceneElement.VAR_DISP_X));
		position.setDispY(gameState.getValue(element, SceneElement.VAR_DISP_Y));

		updateBundle();

	}

	private void updateBundle() {
		// Bundle and mouse over update
		String newBundle = gameState.getValue(element,
				SceneElement.VAR_BUNDLE_ID);

		boolean newMouseOver = gameState.getValue(element,
				SceneElement.VAR_MOUSE_OVER);

		if (currentBundle == null || !currentBundle.equals(newBundle)
				|| newMouseOver != mouseOver) {
			currentBundle = newBundle;
			mouseOver = newMouseOver;
			AssetDescriptor a = element.getDefinition().getAsset(
					currentBundle,
					mouseOver ? SceneElementDef.overAppearance
							: SceneElementDef.appearance);
			if (a == null) {
				a = element.getDefinition().getAppearance(currentBundle);
			}

			if (a != null) {
				runtimeDrawable = (RuntimeCompoundDrawable<?>) assetHandler
						.getRuntimeAsset(a, true);
				if (runtimeDrawable != null) {
					currentDrawable = runtimeDrawable.getDrawable(
							timeDisplayed, statesList, 0);
					if (currentDrawable != null) {
						if (currentDrawable.getWidth() != width) {
							setWidth(currentDrawable.getWidth());
						}
						if (currentDrawable.getHeight() != height) {
							setHeight(currentDrawable.getHeight());
						}
					}

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
		gameState.setValue(element, SceneElement.VAR_LEFT, x);
		gameState.setValue(element, SceneElement.VAR_RIGHT, x + scaleW);
		gameState.setValue(element, SceneElement.VAR_TOP, y);
		gameState.setValue(element, SceneElement.VAR_BOTTOM, y + scaleH);
		gameState.setValue(element, SceneElement.VAR_CENTER_X, x + scaleW / 2);
		gameState.setValue(element, SceneElement.VAR_CENTER_Y, y + scaleH / 2);

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

		for (SceneElementGO<?> go : this.sceneElements) {
			go.update();
		}

		// Reorder list
		if (comparator != null) {
			Collections.sort(sceneElements, comparator);
		}

		if (eventGOList != null) {
			for (EventGO<?> eventGO : eventGOList) {
				eventGO.update();
			}
		}

		gameState.setUpdateListEnable(false);
		timeDisplayed += gui.getSkippedMilliseconds();
		gameState.setValue(element, SceneElement.VAR_TIME_DISPLAYED,
				timeDisplayed);
		gameState.setUpdateListEnable(true);

		updateCurrentDawable();
		if (gameState.checkForUpdates(element)) {
			gameState.setUpdateListEnable(false);
			updateVars();
			setVars();
			resetTransfromation();
			gameState.setUpdateListEnable(true);
		}

	}

	private void updateCurrentDawable() {
		if (runtimeDrawable != null) {
			currentDrawable = runtimeDrawable.getDrawable(timeDisplayed,
					statesList, 0);
			if (currentDrawable != null) {
				if (!currentDrawable.isLoaded()) {
					currentDrawable.loadAsset();
				}
				if (currentDrawable.getWidth() != width) {
					setWidth(currentDrawable.getWidth());
				}
				if (currentDrawable.getHeight() != height) {
					setHeight(currentDrawable.getHeight());
				}
			}
		}
	}

	@Override
	public EAdSceneElement getElement() {
		return element;
	}

	@Override
	public void setPosition(EAdPosition position) {
		gameState.setValue(element, SceneElement.VAR_X, position.getX());
		gameState.setValue(element, SceneElement.VAR_Y, position.getY());
		gameState.setValue(element, SceneElement.VAR_DISP_X, position
				.getDispX());
		gameState.setValue(element, SceneElement.VAR_DISP_Y, position
				.getDispY());
	}

	@Override
	public void setOrientation(Orientation orientation) {
		gameState.setValue(element, SceneElement.VAR_ORIENTATION, orientation);
	}

	@Override
	public Orientation getOrientation() {
		return orientation;
	}

	@Override
	public List<AssetDescriptor> getAssets(List<AssetDescriptor> assetList,
			boolean allAssets) {
		List<String> bundles = new ArrayList<String>();
		if (allAssets)
			bundles
					.addAll(getElement().getDefinition().getResources()
							.keySet());
		else
			bundles
					.add(gameState
							.getValue(element, SceneElement.VAR_BUNDLE_ID));

		for (String bundle : bundles) {
			AssetDescriptor a = getElement().getDefinition().getAsset(bundle,
					SceneElementDef.appearance);
			assetList.add(a);
		}

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
		gameState.setValue(element, SceneElement.VAR_SCALE, scale);
	}

	public void setWidth(int width) {
		this.width = width;
		gameState.setValue(element, SceneElement.VAR_WIDTH, width);
	}

	public void setHeight(int height) {
		this.height = height;
		gameState.setValue(element, SceneElement.VAR_HEIGHT, height);
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

	public float getScale() {
		return scale;
	}

	public boolean isEnable() {
		return enable;
	}

	public RuntimeDrawable<?, ?> getDrawable() {
		return currentDrawable;
	}

	public void setX(int x) {
		this.position.set(x, position.getY());
		gameState.setValue(element, SceneElement.VAR_X, x);
	}

	public void setY(int y) {
		this.position.set(position.getX(), y);
		gameState.setValue(element, SceneElement.VAR_Y, y);
	}

	public int getX() {
		return position.getX();
	}

	public int getY() {
		return position.getY();
	}

	public void setAlpha(float alpha) {
		gameState.setValue(element, SceneElement.VAR_ALPHA, alpha);
		this.alpha = alpha;
	}

	public void setEnabled(boolean enable) {
		gameState.setValue(element, SceneElement.VAR_ENABLE, enable);
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

	public void setVisible(boolean visible) {
		gameState.setValue(element, SceneElement.VAR_VISIBLE, visible);
	}

	public SceneElementGO<?> getChild(EAdSceneElement sceneElement) {
		if (sceneElement == this.element) {
			return this;
		} else {
			for (SceneElementGO<?> s : getChildren()) {
				return s.getChild(sceneElement);
			}
		}
		return null;
	}

	public EAdTransformation getTransformation() {
		return transformation;
	}

	@Override
	public void addSceneElement(SceneElementGO<?> sceneElement) {
		this.sceneElements.add(sceneElement);
		sceneElement.setParent(this);
	}

	@Override
	public void removeSceneElement(SceneElementGO<?> sceneElement) {
		this.sceneElements.remove(sceneElement);
	}

	@Override
	public SceneElementGO<?> getFirstGOIn(int x, int y) {
		SceneElementGO<?> result = null;
		for (SceneElementGO<?> e : sceneElements) {
			result = e.getFirstGOIn(x, y);
			if (result != null) {
				break;
			}
		}
		if (result == null && this.contains(x, y)) {
			return this;
		}
		return result;
	}

	@Override
	public void getAllGOIn(int x, int y, List<SceneElementGO<?>> list) {
		if (this.contains(x, y)) {
			list.add(this);
		}
		for (SceneElementGO<?> e : sceneElements) {
			e.getAllGOIn(x, y, list);
		}
	}

}
