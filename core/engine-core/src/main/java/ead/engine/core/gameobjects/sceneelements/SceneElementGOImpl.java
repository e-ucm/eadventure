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
import ead.common.util.EAdRectangle;
import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.InputActionProcessor;
import ead.engine.core.gameobjects.events.EventGO;
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

	// Engine components

	/**
	 * GUI
	 */
	protected GUI gui;

	/**
	 * Events factory
	 */
	protected EventGOFactory eventFactory;

	/**
	 * Asset handler
	 */
	protected AssetHandler assetHandler;

	/**
	 * Game state
	 */
	protected GameState gameState;

	/**
	 * Scene element factory
	 */
	protected SceneElementGOFactory sceneElementFactory;

	// Scene element properties

	/**
	 * Scene element parent
	 */
	protected SceneElementGO<?> parent;

	/**
	 * Children scene elements
	 */
	protected List<SceneElementGO<?>> children;

	/**
	 * Comparator to order the scene elements
	 */
	private Comparator<SceneElementGO<?>> comparator;

	/**
	 * Scene element
	 */
	protected EAdSceneElement element;

	/**
	 * Current position
	 */
	protected EAdPosition position;

	/**
	 * Current z
	 */
	private int z;

	/**
	 * Global scale
	 */
	protected float scale;

	/**
	 * Scale in x axis
	 */
	protected float scaleX;

	/**
	 * Scale in y axis
	 */
	protected float scaleY;

	/**
	 * Element orientation
	 */
	protected Orientation orientation;

	/**
	 * Current state
	 */
	protected String state;

	/**
	 * Current rotation
	 */
	protected float rotation;

	/**
	 * Element with in the screen
	 */
	private int width;

	/**
	 * Element height in the screen
	 */
	private int height;

	/**
	 * Time since element is displayed
	 */
	private int timeDisplayed;

	/**
	 * Element alpha
	 */
	protected float alpha;

	/**
	 * If the element is visible
	 */
	protected boolean visible;

	/**
	 * Current asset bundle
	 */
	protected String currentBundle;

	/**
	 * Current transformation
	 */
	protected EAdTransformation transformation;

	/**
	 * If the element receives input actions
	 */
	protected boolean enable;

	/**
	 * If the element is draggable
	 */
	protected boolean draggable;

	/**
	 * If the mouse is over the element
	 */
	protected boolean mouseOver;

	/**
	 * Current input processor that process all input actions (could be
	 * {@code null})
	 */
	protected InputActionProcessor inputProcessor;

	/**
	 * Current compound asset
	 */
	private RuntimeCompoundDrawable<?> runtimeDrawable;

	/**
	 * Current drawable (contained in {@link SceneElementGOImpl#runtimeDrawable}
	 * ) drawn
	 */
	private RuntimeDrawable<?, ?> currentDrawable;

	// Additional attributes

	/**
	 * List with the events of this element
	 */
	private ArrayList<EventGO<?>> eventGOList;

	// Auxiliary list to store removed children
	private ArrayList<SceneElementGO<?>> removedChildren;

	/**
	 * List with the states of this element
	 */
	private List<String> statesList;

	private boolean removed;

	private boolean reorder;

	@Inject
	public SceneElementGOImpl(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		this.eventFactory = eventFactory;
		this.gameState = gameState;
		this.assetHandler = assetHandler;
		this.sceneElementFactory = sceneElementFactory;
		this.gui = gui;

		statesList = new ArrayList<String>();
		children = new ArrayList<SceneElementGO<?>>();
		eventGOList = new ArrayList<EventGO<?>>();
		removedChildren = new ArrayList<SceneElementGO<?>>();
	}

	public SceneElementGO<?> getParent() {
		return parent;
	}

	public void setParent(SceneElementGO<?> parent) {
		this.parent = parent;
	}

	public List<SceneElementGO<?>> getChildren() {
		return children;
	}

	@Override
	public boolean contains(int x, int y) {
		if (isVisible() && isEnable()) {
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
	 * Launches a list of effects
	 * 
	 * @param list
	 * @param action
	 */
	private void addEffects(EAdList<EAdEffect> list, InputAction<?> action) {
		if (list != null && list.size() > 0) {
			action.consume();
			for (EAdEffect e : list) {
				logger.debug("GUI Action: '{}' effect '{}'", action, e);
				gameState.addEffect(e, action, element);
			}
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

	@Override
	public void setElement(EAdSceneElement element) {
		this.element = element;
		this.removed = false;
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
		initEvents(element.getEvents());
		// Definition events
		initEvents(element.getDefinition().getEvents());

		position = new EAdPosition(0, 0);
		updateVars();
		setExtraVars();
		resetTransformation();
	}

	private void initEvents(EAdList<EAdEvent> events) {
		if (element.getEvents() != null) {
			for (EAdEvent event : events) {
				EventGO<?> eventGO = eventFactory.get(event);
				eventGO.setParent(element);
				eventGO.initialize();
				eventGOList.add(eventGO);
			}

		}
	}

	/**
	 * Read vars values
	 */
	protected void updateVars() {

		enable = gameState.getValue(element, SceneElement.VAR_ENABLE);
		orientation = gameState.getValue(element, SceneElement.VAR_ORIENTATION);
		state = gameState.getValue(element, SceneElement.VAR_STATE);
		draggable = gameState.getValue(element, SceneElement.VAR_DRAGGABLE);
		setZ(gameState.getValue(element, SceneElement.VAR_Z));

		// Transformation
		setVisible(gameState.getValue(element, SceneElement.VAR_VISIBLE));
		setRotation(gameState.getValue(element, SceneElement.VAR_ROTATION));
		setScale(gameState.getValue(element, SceneElement.VAR_SCALE));
		setScaleX(gameState.getValue(element, SceneElement.VAR_SCALE_X));
		setScaleY(gameState.getValue(element, SceneElement.VAR_SCALE_Y));
		setAlpha(gameState.getValue(element, SceneElement.VAR_ALPHA));

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
	protected void setExtraVars() {
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

	public void invalidate() {
		transformation.setValidated(false);
		for (SceneElementGO<?> go : children) {
			go.invalidate();
		}
	}

	public void resetTransformation() {
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

		if (parent != null) {
			addTransformation(transformation, parent.getTransformation());
		}
		transformation.setValidated(true);
	}

	/**
	 * Adds to transformation t1 transformation t2.
	 * 
	 * @param t1
	 *            transformation 1
	 * @param t2
	 *            transformation 2
	 * @returns t1
	 */
	public EAdTransformation addTransformation(EAdTransformation t1,
			EAdTransformation t2) {
		float alpha = t1.getAlpha() * t2.getAlpha();
		boolean visible = t1.isVisible() && t2.isVisible();

		t1.setAlpha(alpha);
		t1.setVisible(visible);
		t1.getMatrix().multiply(t2.getMatrix().getFlatMatrix(), false);

		EAdRectangle clip1 = t1.getClip();
		EAdRectangle clip2 = t2.getClip();
		EAdRectangle newclip = new EAdRectangle(0, 0, 0, 0);

		// FIXME multiply for matrix to know where the clip actually is
		if (clip1 == null) {
			newclip = clip2;
		} else if (clip2 == null) {
			newclip = clip1;
		} else if (clip1 != null && clip2 != null) {
			newclip = clip2;
		}

		if (newclip != null)
			t1.setClip(newclip.x, newclip.y, newclip.width, newclip.height);

		return t1;
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
		// Reorder list
		if (reorder) {
			if (comparator != null) {
				Collections.sort(children, comparator);
			}
			reorder = false;
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
			setExtraVars();
			gameState.setUpdateListEnable(true);
		}

		if (!transformation.isValidated()) {
			resetTransformation();
		}

		// Check elements removed
		removedChildren.clear();
		for (SceneElementGO<?> go : this.children) {
			if (go.isRemoved()) {
				removedChildren.add(go);
			}
		}

		for (SceneElementGO<?> go : removedChildren) {
			this.removeSceneElement(go);
		}

		for (SceneElementGO<?> go : this.children) {
			go.update();
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
		this.position = position;
		gameState.setValue(element, SceneElement.VAR_X, position.getX());
		gameState.setValue(element, SceneElement.VAR_Y, position.getY());
		gameState.setValue(element, SceneElement.VAR_DISP_X, position
				.getDispX());
		gameState.setValue(element, SceneElement.VAR_DISP_Y, position
				.getDispY());
		this.invalidate();
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
		this.scale = scale;
		gameState.setValue(element, SceneElement.VAR_SCALE, scale);
		invalidate();
	}

	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
		gameState.setValue(element, SceneElement.VAR_SCALE_X, scaleX);
		invalidate();
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
		gameState.setValue(element, SceneElement.VAR_SCALE_Y, scaleY);
		invalidate();
	}

	/**
	 * Sets the width of the element. It can't be used from outside this class
	 * 
	 * @param width
	 */
	protected void setWidth(int width) {
		this.width = width;
		gameState.setValue(element, SceneElement.VAR_WIDTH, width);
		invalidate();
	}

	/**
	 * Sets the height of the element. It can't be used from outside this class
	 * 
	 * @param height
	 */
	protected void setHeight(int height) {
		this.height = height;
		gameState.setValue(element, SceneElement.VAR_HEIGHT, height);
		invalidate();
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
		invalidate();
	}

	public void setY(int y) {
		this.position.set(position.getX(), y);
		gameState.setValue(element, SceneElement.VAR_Y, y);
		invalidate();
	}

	public void setRotation(float r) {
		this.rotation = r;
		gameState.setValue(element, SceneElement.VAR_ROTATION, r);
		invalidate();
	}

	public float getDispX() {
		return position.getDispX();
	}

	public float getDispY() {
		return position.getDispY();
	}

	public int getX() {
		return position.getX();
	}

	public int getY() {
		return position.getY();
	}

	public int getZ() {
		return z;
	}

	/**
	 * Sets the z order for this element
	 * 
	 * @param z
	 */
	public void setZ(int z) {
		if (this.z != z) {
			gameState.setValue(element, SceneElement.VAR_Z, z);
			this.z = z;
			if (parent != null) {
				this.parent.invalidateOrder();
			}
		}
	}

	public void setAlpha(float alpha) {
		gameState.setValue(element, SceneElement.VAR_ALPHA, alpha);
		this.alpha = alpha;
		invalidate();
	}

	public void setEnabled(boolean enable) {
		gameState.setValue(element, SceneElement.VAR_ENABLE, enable);
		this.enable = enable;
	}

	public void setState(String state) {
		if (!this.state.equals(state)) {
			this.state = state;
			gameState.setValue(element, SceneElement.VAR_STATE, state);
		}
	}

	@Override
	public void collectSceneElements(List<EAdSceneElement> elements) {
		if (getElement() != null)
			elements.add(getElement());
		for (SceneElementGO<?> s : children) {
			s.collectSceneElements(elements);
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		gameState.setValue(element, SceneElement.VAR_VISIBLE, visible);
		invalidate();
	}

	public SceneElementGO<?> getChild(EAdSceneElement sceneElement) {
		SceneElementGO<?> go = null;
		if (sceneElement == this.element) {
			return this;
		} else {
			for (SceneElementGO<?> s : getChildren()) {
				go = s.getChild(sceneElement);
				if (go != null) {
					break;
				}
			}
		}
		return go;
	}

	public EAdTransformation getTransformation() {
		return transformation;
	}

	@Override
	public void addSceneElement(SceneElementGO<?> sceneElement) {
		this.children.add(sceneElement);
		this.removed = false;
		sceneElement.getTransformation().setValidated(false);
		sceneElement.setParent(this);
		if (parent != null) {
			parent.invalidateOrder();
		}
	}

	@Override
	public void removeSceneElement(SceneElementGO<?> sceneElement) {
		this.children.remove(sceneElement);
	}

	@Override
	public SceneElementGO<?> getFirstGOIn(int x, int y) {
		if (!this.isEnable()) {
			return null;
		}
		SceneElementGO<?> result = null;
		for (int i = children.size() - 1; i >= 0; i--) {
			SceneElementGO<?> e = children.get(i);
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
		for (int i = children.size() - 1; i >= 0; i--) {
			SceneElementGO<?> e = children.get(i);
			e.getAllGOIn(x, y, list);
		}
	}

	public boolean isDraggable() {
		return draggable;
	}

	public void remove() {
		removed = true;
	}

	public void invalidateOrder() {
		this.reorder = true;
	}

	public boolean isRemoved() {
		return removed;
	}

	public String toString() {
		return this.element + "";
	}

}
