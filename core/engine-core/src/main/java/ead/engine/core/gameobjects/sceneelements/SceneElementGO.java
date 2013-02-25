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
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.google.inject.Inject;

import ead.common.interfaces.features.Oriented;
import ead.common.interfaces.features.enums.Orientation;
import ead.common.model.assets.AssetDescriptor;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.EAdEvent;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.params.guievents.EAdGUIEvent;
import ead.common.model.params.guievents.KeyGEv;
import ead.common.model.params.guievents.MouseGEv;
import ead.common.model.params.guievents.enums.KeyEventType;
import ead.common.model.params.guievents.enums.KeyGEvCode;
import ead.common.model.params.util.Position;
import ead.common.model.params.variables.EAdVarDef;
import ead.engine.core.assets.AssetHandler;
import ead.engine.core.assets.drawables.RuntimeDrawable;
import ead.engine.core.canvas.GdxCanvas;
import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.GUIImpl.DragEvent;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.gameobjects.GameObject;
import ead.engine.core.gameobjects.events.EventGO;

/**
 * 
 */
public class SceneElementGO extends Group implements
		GameObject<EAdSceneElement>, Oriented, EventListener {

	protected static final Logger logger = LoggerFactory
			.getLogger("SceneElementGO");

	/**
	 * Scene element factory
	 */
	protected SceneElementGOFactory sceneElementFactory;

	/**
	 * Game state
	 */
	protected GameState gameState;

	/**
	 * GUI
	 */
	protected GUI gui;

	/**
	 * Asset handler
	 */
	protected AssetHandler assetHandler;

	/**
	 * Event factory
	 */
	private EventGOFactory eventFactory;

	/**
	 * Scene element
	 */
	protected EAdSceneElement element;

	/**
	 * Displacement in x coordinate
	 */
	private float dispX;

	/**
	 * Displacement in y coordinate
	 */
	private float dispY;

	/**
	 * Current z
	 */
	private int z;

	/**
	 * Global scale
	 */
	protected float scale;

	/**
	 * Element orientation
	 */
	protected Orientation orientation;

	/**
	 * Current state
	 */
	protected String state;

	/**
	 * Time since element is displayed
	 */
	private int timeDisplayed;

	/**
	 * Current asset bundle
	 */
	protected String currentBundle;

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
	protected EventListener inputProcessor;

	/**
	 * Current compound asset
	 */
	private RuntimeDrawable<?> runtimeDrawable;

	/**
	 * Current drawable (contained in {@link SceneElementGO#runtimeDrawable} )
	 * drawn
	 */
	private RuntimeDrawable<?> currentDrawable;

	// Additional attributes

	/**
	 * List with the events of this element
	 */
	private ArrayList<EventGO<?>> eventGOList;

	/**
	 * List with the states of this element
	 */
	private List<String> statesList;

	/**
	 * If the children must be z reordered
	 */
	private boolean reorder;

	private Comparator<SceneElementGO> comparator;

	@Inject
	public SceneElementGO(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		this.eventFactory = eventFactory;
		this.gameState = gameState;
		this.assetHandler = assetHandler;
		this.sceneElementFactory = sceneElementFactory;
		this.gui = gui;

		statesList = new ArrayList<String>();
		eventGOList = new ArrayList<EventGO<?>>();

		addListener(this);
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	@Override
	public void setElement(EAdSceneElement element) {
		this.element = element;
		setName(element.getId());

		for (Entry<EAdVarDef<?>, Object> e : element.getVars().entrySet()) {
			EAdVarDef def = e.getKey();
			gameState.setValue(element, def, e.getValue());
		}

		gameState.setValue(element.getDefinition(),
				SceneElementDef.VAR_SCENE_ELEMENT, element);

		// Initial vars
		// Bundle
		gameState.setValue(element, SceneElement.VAR_BUNDLE_ID,
				SceneElementDef.INITIAL_BUNDLE);

		// Scene element events
		initEvents(element.getEvents());
		// Definition events
		initEvents(element.getDefinition().getEvents());

		updateVars();
		setExtraVars();
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

		boolean enable = gameState.getValue(element, SceneElement.VAR_ENABLE);
		this.setTouchable(enable ? Touchable.enabled : Touchable.disabled);

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

		updateBundle();

		setX(gameState.getValue(element, SceneElement.VAR_X));
		setY(gameState.getValue(element, SceneElement.VAR_Y));
		setDispX(gameState.getValue(element, SceneElement.VAR_DISP_X));
		setDispY(gameState.getValue(element, SceneElement.VAR_DISP_Y));

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
				runtimeDrawable = (RuntimeDrawable<?>) assetHandler
						.getRuntimeAsset(a, true);
				if (runtimeDrawable != null) {
					currentDrawable = runtimeDrawable.getDrawable(
							timeDisplayed, statesList, 0);
					if (currentDrawable != null) {
						if (currentDrawable.getWidth() != this.getWidth()) {
							setWidth(currentDrawable.getWidth());
						}
						if (currentDrawable.getHeight() != this.getHeight()) {
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
		int scaleW = (int) (this.getWidth() * scale * this.getScaleX());
		int scaleH = (int) (this.getHeight() * scale * this.getScaleY());
		int x = (int) (this.getX() + dispX * this.getWidth());
		int y = (int) (this.getY() + dispY * this.getHeight());
		gameState.setValue(element, SceneElement.VAR_LEFT, x);
		gameState.setValue(element, SceneElement.VAR_RIGHT, x + scaleW);
		gameState.setValue(element, SceneElement.VAR_TOP, y);
		gameState.setValue(element, SceneElement.VAR_BOTTOM, y + scaleH);
		gameState.setValue(element, SceneElement.VAR_CENTER_X, x + scaleW / 2);
		gameState.setValue(element, SceneElement.VAR_CENTER_Y, y + scaleH / 2);

	}

	@Override
	public EAdSceneElement getElement() {
		return element;
	}

	/**
	 * Adds an scene element as a child of this element
	 * 
	 * @param sceneElement
	 */
	public void addSceneElement(EAdSceneElement element) {
		SceneElementGO go = sceneElementFactory.get(element);
		addSceneElement(go);
	}

	/**
	 * Sets position for this element
	 * 
	 * @param position
	 */
	public void setPosition(Position position) {
		setX(position.getX());
		setY(position.getY());
		setDispX(position.getDispX());
		setDispY(position.getDispY());
	}

	/**
	 * Sets x position for this element
	 * 
	 * @param x
	 */
	public void setX(float x) {
		super.setX(x);
		gameState.setValue(getElement(), SceneElement.VAR_X, x);
	}

	public float getX() {
		return super.getX() - this.getOriginX();
	}

	public float getRelativeX() {
		return super.getX();
	}

	/**
	 * Sets y position for this element
	 * 
	 * @param y
	 */
	public void setY(float y) {
		super.setY(y);
		gameState.setValue(getElement(), SceneElement.VAR_Y, y);
	}

	public float getY() {
		return super.getY() - this.getOriginY();
	}

	public float getRelativeY() {
		return super.getY();
	}

	public void setDispX(float dispX) {
		this.dispX = dispX;
		setOriginX(getWidth() * dispX);
		gameState.setValue(getElement(), SceneElement.VAR_DISP_X, dispX);
	}

	public void setDispY(float dispY) {
		this.dispY = dispY;
		setOriginY(getHeight() * dispY);
		gameState.setValue(getElement(), SceneElement.VAR_DISP_Y, dispY);
	}

	/**
	 * Returns displacement proportion in x coordination
	 * 
	 * @return
	 */
	public float getDispX() {
		return dispX;
	}

	/**
	 * Returns displacement proportion in x coordination
	 * 
	 * @return
	 */
	public float getDispY() {
		return dispY;
	}

	/**
	 * Return the z order for this element
	 * 
	 * @return
	 */
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
			if (getParent() != null) {
				((SceneElementGO) getParent()).invalidateOrder();
			}
		}
	}

	/**
	 * Sets scale for this element
	 * 
	 * @param scale
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}

	/**
	 * Returns the current scale of the element
	 * 
	 * @return
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Sets the alpha for this element
	 * 
	 * @param alpha
	 */
	public void setAlpha(float alpha) {
		if (alpha != this.getColor().a) {
			getColor().a = alpha;
			gameState.setValue(element, SceneElement.VAR_ALPHA, alpha);
		}
	}

	/**
	 * Sets an input processor for this element. This processor will process the
	 * actions before
	 * {@link SceneElementGO#processAction(ead.engine.core.input.InputAction)}
	 * 
	 * @param processor
	 * @param transmitToChildren
	 *            TODO
	 */
	public void setInputProcessor(EventListener processor,
			boolean transmitToChildren) {
		this.inputProcessor = processor;
		if (transmitToChildren) {
			for (Actor a : this.getChildren()) {
				if (a instanceof SceneElementGO) {
					((SceneElementGO) a).setInputProcessor(processor,
							transmitToChildren);
				}
			}
		}
	}

	/**
	 * Returns if this element is draggable
	 * 
	 */
	public boolean isDraggable() {
		return draggable;
	}

	/**
	 * Returns the drawable that represents this element
	 * 
	 * @return
	 */
	public RuntimeDrawable<?> getDrawable() {
		return currentDrawable;
	}

	/**
	 * Sets the state for this element
	 * 
	 * @param string
	 */
	public void setState(String state) {
		this.state = state;
		gameState.setValue(getElement(), SceneElement.VAR_STATE, state);
	}

	/**
	 * Returns the field for this variable of this element
	 * 
	 * @param var
	 */
	public <S> EAdField<S> getField(EAdVarDef<S> var) {
		return new BasicField<S>(getElement(), var);
	}

	@Override
	public Orientation getOrientation() {
		return orientation;
	}

	@Override
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
		gameState.setValue(getElement(), SceneElement.VAR_ORIENTATION,
				orientation);
	}

	public void addSceneElement(SceneElementGO e) {
		addActor(e);
		if (e.isDraggable()) {
			gui.addDragSource(e);
		}
	}

	public void addActor(Actor a) {
		super.addActor(a);
		invalidateOrder();
	}

	public SceneElementGO getChild(EAdSceneElement e) {
		return (SceneElementGO) super.findActor(e.getId());
	}

	public SceneElementGO getFirstGOIn(int virtualX, int virtualY) {
		return (SceneElementGO) this.hit(virtualX, virtualY, true);
	}

	/**
	 * Sets a comparator to automatically reorder the scene elements (modifies
	 * drawing order)
	 * 
	 * @param comparator
	 */
	public void setComparator(Comparator<SceneElementGO> comparator) {
		this.comparator = comparator;
	}

	/**
	 * Launches a list of effects
	 * 
	 * @param list
	 * @param action
	 */
	private void addEffects(EAdList<EAdEffect> list, Event action) {
		if (list != null && list.size() > 0) {
			for (EAdEffect e : list) {
				logger.debug("GUI Action: '{}' effect '{}'", action, e);
				gameState.addEffect(e, action, element);
			}
		}
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	@Override
	public void act(float delta) {
		// Reorder list
		if (reorder) {
			if (comparator != null) {
				List l = new ArrayList();
				for (Actor a : this.getChildren()) {
					l.add(a);
				}
				getChildren().clear();
				Collections.sort(l, comparator);
				for (Object a : l) {
					getChildren().add((Actor) a);
				}
			}
			reorder = false;
		}

		if (eventGOList != null) {
			for (EventGO<?> eventGO : eventGOList) {
				eventGO.act(delta);
			}
		}

		gameState.setUpdateListEnable(false);
		timeDisplayed += gui.getSkippedMilliseconds();
		gameState.setValue(element, SceneElement.VAR_TIME_DISPLAYED,
				timeDisplayed);
		gameState.setUpdateListEnable(true);

		if (gameState.checkForUpdates(element)) {
			gameState.setUpdateListEnable(false);
			updateVars();
			setExtraVars();
			gameState.setUpdateListEnable(true);
		}
		updateCurrentDawable();

		super.act(delta);

	}

	protected void updateCurrentDawable() {
		if (runtimeDrawable != null) {
			currentDrawable = runtimeDrawable.getDrawable(timeDisplayed,
					statesList, 0);
			if (currentDrawable != null) {
				if (!currentDrawable.isLoaded()) {
					currentDrawable.loadAsset();
				}
				if (currentDrawable.getWidth() != getWidth()) {
					setWidth(currentDrawable.getWidth());
				}
				if (currentDrawable.getHeight() != getHeight()) {
					setHeight(currentDrawable.getHeight());
				}
			}
		}
	}

	public void invalidateOrder() {
		this.reorder = true;
	}

	public String toString() {
		return this.element + "";
	}

	@Override
	public void drawChildren(SpriteBatch batch, float parentAlpha) {
		if (currentDrawable != null) {
			batch.setColor(this.getColor().r, this.getColor().g, this
					.getColor().b, this.getColor().a * parentAlpha);
			currentDrawable.render((GdxCanvas) batch);
		}
		super.drawChildren(batch, parentAlpha);
	}

	public Actor hit(float x, float y, boolean touchable) {
		Actor a = super.hit(x, y, touchable);
		if (a == this) {
			return contains(x, y) ? this : null;
		} else {
			return a;
		}
	}

	public boolean contains(float x, float y) {
		return currentDrawable != null
				&& currentDrawable.contains((int) x, (int) y);
	}

	@Override
	public boolean handle(Event event) {
		if (inputProcessor != null) {
			inputProcessor.handle(event);
		}

		// Due the way Stage fireEnterAndExit works, enter and exit events never are cancelled
		boolean cancel = true;

		if (this.getTouchable() == Touchable.enabled
				&& event instanceof InputEvent) {
			Type t = ((InputEvent) event).getType();
			switch (t) {
			case enter:
				setMouseOver(true);
				cancel = false;
				break;
			case exit:
				setMouseOver(false);
				cancel = false;
				break;
			default:
				cancel = true;
				break;
			}
		}

		if (!event.isCancelled()) {
			if (this.getTouchable() == Touchable.enabled) {
				// Effects in the scene element instance
				EAdList<EAdEffect> list = element
						.getEffects(getGUIEvent(event));
				int size = list == null ? 0 : list.size();
				addEffects(list, event);

				// Effects in the definition
				list = element.getDefinition().getEffects(getGUIEvent(event));
				size += list == null ? 0 : list.size();
				if (size > 0 && cancel) {
					event.cancel();
				}
				addEffects(list, event);
			}
		}

		return event.isCancelled();
	}

	public EAdGUIEvent getGUIEvent(Event e) {
		EAdGUIEvent guiEvent = null;
		if (e instanceof InputEvent) {
			InputEvent i = (InputEvent) e;
			switch (i.getType()) {
			case mouseMoved:
				guiEvent = MouseGEv.MOUSE_MOVED;
				break;
			case touchDown:
				switch (i.getButton()) {
				case Input.Buttons.LEFT:
					guiEvent = MouseGEv.MOUSE_LEFT_PRESSED;
					break;
				case Input.Buttons.RIGHT:
					guiEvent = MouseGEv.MOUSE_RIGHT_PRESSED;
					break;
				case Input.Buttons.MIDDLE:
					guiEvent = MouseGEv.MOUSE_MIDDLE_PRESSED;
					break;
				}
				break;
			case touchUp:
				switch (i.getButton()) {
				case Input.Buttons.LEFT:
					guiEvent = MouseGEv.MOUSE_LEFT_RELEASED;
					break;
				case Input.Buttons.RIGHT:
					guiEvent = MouseGEv.MOUSE_RIGHT_RELEASED;
					break;
				case Input.Buttons.MIDDLE:
					guiEvent = MouseGEv.MOUSE_MIDDLE_RELEASED;
					break;
				}
				break;
			case enter:
				guiEvent = MouseGEv.MOUSE_ENTERED;
				break;
			case exit:
				guiEvent = MouseGEv.MOUSE_EXITED;
				break;
			case keyDown:
				guiEvent = new KeyGEv(KeyEventType.KEY_PRESSED, getKeyCode(i
						.getKeyCode()));
				break;
			case keyUp:
				guiEvent = new KeyGEv(KeyEventType.KEY_RELEASED, getKeyCode(i
						.getKeyCode()));
				break;
			case keyTyped:
				guiEvent = new KeyGEv(KeyEventType.KEY_TYPED, getKeyCode(i
						.getKeyCode()));
				break;
			case touchDragged:
				if (i instanceof DragEvent) {
					//					DragEvent dragEvent = (DragEvent) i;
				}
			default:
				break;
			}
		}
		return guiEvent;
	}

	public KeyGEvCode getKeyCode(int code) {
		switch (code) {
		case Input.Keys.NUM_0:
			return KeyGEvCode.NUM_0;
		case Input.Keys.NUM_1:
			return KeyGEvCode.NUM_1;
		case Input.Keys.NUM_2:
			return KeyGEvCode.NUM_2;
		case Input.Keys.NUM_3:
			return KeyGEvCode.NUM_3;
		case Input.Keys.NUM_4:
			return KeyGEvCode.NUM_4;
		case Input.Keys.NUM_5:
			return KeyGEvCode.NUM_5;
		case Input.Keys.NUM_6:
			return KeyGEvCode.NUM_6;
		case Input.Keys.NUM_7:
			return KeyGEvCode.NUM_7;
		case Input.Keys.NUM_8:
			return KeyGEvCode.NUM_8;
		case Input.Keys.NUM_9:
			return KeyGEvCode.NUM_9;
		case Input.Keys.A:
			return KeyGEvCode.A;
		case Input.Keys.ALT_LEFT:
			return KeyGEvCode.ALT_LEFT;
		case Input.Keys.ALT_RIGHT:
			return KeyGEvCode.ALT_RIGHT;
		case Input.Keys.APOSTROPHE:
			return KeyGEvCode.APOSTROPHE;
		case Input.Keys.AT:
			return KeyGEvCode.AT;
		case Input.Keys.B:
			return KeyGEvCode.B;
		case Input.Keys.BACK:
			return KeyGEvCode.BACK;
		case Input.Keys.BACKSLASH:
			return KeyGEvCode.BACKSLASH;
		case Input.Keys.C:
			return KeyGEvCode.C;
		case Input.Keys.CALL:
			return KeyGEvCode.CALL;
		case Input.Keys.CAMERA:
			return KeyGEvCode.CAMERA;
		case Input.Keys.CLEAR:
			return KeyGEvCode.CLEAR;
		case Input.Keys.COMMA:
			return KeyGEvCode.COMMA;
		case Input.Keys.D:
			return KeyGEvCode.D;
		case Input.Keys.BACKSPACE:
			return KeyGEvCode.BACKSPACE;
		case Input.Keys.FORWARD_DEL:
			return KeyGEvCode.FORWARD_DEL;
		case Input.Keys.CENTER:
			return KeyGEvCode.CENTER;
		case Input.Keys.DOWN:
			return KeyGEvCode.DOWN;
		case Input.Keys.LEFT:
			return KeyGEvCode.LEFT;
		case Input.Keys.RIGHT:
			return KeyGEvCode.RIGHT;
		case Input.Keys.UP:
			return KeyGEvCode.UP;
		case Input.Keys.E:
			return KeyGEvCode.E;
		case Input.Keys.ENDCALL:
			return KeyGEvCode.ENDCALL;
		case Input.Keys.ENTER:
			return KeyGEvCode.ENTER;
		case Input.Keys.ENVELOPE:
			return KeyGEvCode.ENVELOPE;
		case Input.Keys.EQUALS:
			return KeyGEvCode.EQUALS;
		case Input.Keys.EXPLORER:
			return KeyGEvCode.EXPLORER;
		case Input.Keys.F:
			return KeyGEvCode.F;
		case Input.Keys.F1:
			return KeyGEvCode.F1;
		case Input.Keys.F2:
			return KeyGEvCode.F2;
		case Input.Keys.F3:
			return KeyGEvCode.F3;
		case Input.Keys.F4:
			return KeyGEvCode.F4;
		case Input.Keys.F5:
			return KeyGEvCode.F5;
		case Input.Keys.F6:
			return KeyGEvCode.F6;
		case Input.Keys.F7:
			return KeyGEvCode.F7;
		case Input.Keys.F8:
			return KeyGEvCode.F8;
		case Input.Keys.F9:
			return KeyGEvCode.F9;
		case Input.Keys.F10:
			return KeyGEvCode.F10;
		case Input.Keys.F11:
			return KeyGEvCode.F11;
		case Input.Keys.F12:
			return KeyGEvCode.F12;
		case Input.Keys.FOCUS:
			return KeyGEvCode.FOCUS;
		case Input.Keys.G:
			return KeyGEvCode.G;
		case Input.Keys.GRAVE:
			return KeyGEvCode.GRAVE;
		case Input.Keys.H:
			return KeyGEvCode.H;
		case Input.Keys.HEADSETHOOK:
			return KeyGEvCode.HEADSETHOOK;
		case Input.Keys.HOME:
			return KeyGEvCode.HOME;
		case Input.Keys.I:
			return KeyGEvCode.I;
		case Input.Keys.J:
			return KeyGEvCode.J;
		case Input.Keys.K:
			return KeyGEvCode.K;
		case Input.Keys.L:
			return KeyGEvCode.L;
		case Input.Keys.LEFT_BRACKET:
			return KeyGEvCode.LEFT_BRACKET;
		case Input.Keys.M:
			return KeyGEvCode.M;
		case Input.Keys.MEDIA_FAST_FORWARD:
			return KeyGEvCode.MEDIA_FAST_FORWARD;
		case Input.Keys.MEDIA_NEXT:
			return KeyGEvCode.MEDIA_NEXT;
		case Input.Keys.MEDIA_PLAY_PAUSE:
			return KeyGEvCode.MEDIA_PLAY_PAUSE;
		case Input.Keys.MEDIA_PREVIOUS:
			return KeyGEvCode.MEDIA_PREVIOUS;
		case Input.Keys.MEDIA_REWIND:
			return KeyGEvCode.MEDIA_REWIND;
		case Input.Keys.MEDIA_STOP:
			return KeyGEvCode.MEDIA_STOP;
		case Input.Keys.MENU:
			return KeyGEvCode.MENU;
		case Input.Keys.MINUS:
			return KeyGEvCode.MINUS;
		case Input.Keys.MUTE:
			return KeyGEvCode.MUTE;
		case Input.Keys.N:
			return KeyGEvCode.N;
		case Input.Keys.NOTIFICATION:
			return KeyGEvCode.NOTIFICATION;
		case Input.Keys.NUM:
			return KeyGEvCode.NUM;
		case Input.Keys.O:
			return KeyGEvCode.O;
		case Input.Keys.P:
			return KeyGEvCode.P;
		case Input.Keys.PERIOD:
			return KeyGEvCode.PERIOD;
		case Input.Keys.PLUS:
			return KeyGEvCode.PLUS;
		case Input.Keys.POUND:
			return KeyGEvCode.POUND;
		case Input.Keys.POWER:
			return KeyGEvCode.POWER;
		case Input.Keys.Q:
			return KeyGEvCode.Q;
		case Input.Keys.R:
			return KeyGEvCode.R;
		case Input.Keys.RIGHT_BRACKET:
			return KeyGEvCode.RIGHT_BRACKET;
		case Input.Keys.S:
			return KeyGEvCode.S;
		case Input.Keys.SEARCH:
			return KeyGEvCode.SEARCH;
		case Input.Keys.SEMICOLON:
			return KeyGEvCode.SEMICOLON;
		case Input.Keys.SHIFT_LEFT:
			return KeyGEvCode.SHIFT_LEFT;
		case Input.Keys.SHIFT_RIGHT:
			return KeyGEvCode.SHIFT_RIGHT;
		case Input.Keys.SLASH:
			return KeyGEvCode.SLASH;
		case Input.Keys.SOFT_LEFT:
			return KeyGEvCode.SOFT_LEFT;
		case Input.Keys.SOFT_RIGHT:
			return KeyGEvCode.SOFT_RIGHT;
		case Input.Keys.SPACE:
			return KeyGEvCode.SPACE;
		case Input.Keys.STAR:
			return KeyGEvCode.STAR;
		case Input.Keys.SYM:
			return KeyGEvCode.SYM;
		case Input.Keys.T:
			return KeyGEvCode.T;
		case Input.Keys.TAB:
			return KeyGEvCode.TAB;
		case Input.Keys.U:
			return KeyGEvCode.U;
		case Input.Keys.UNKNOWN:
			return KeyGEvCode.UNKNOWN;
		case Input.Keys.V:
			return KeyGEvCode.V;
		case Input.Keys.VOLUME_DOWN:
			return KeyGEvCode.VOLUME_DOWN;
		case Input.Keys.VOLUME_UP:
			return KeyGEvCode.VOLUME_UP;
		case Input.Keys.W:
			return KeyGEvCode.W;
		case Input.Keys.X:
			return KeyGEvCode.X;
		case Input.Keys.Y:
			return KeyGEvCode.Y;
		case Input.Keys.Z:
			return KeyGEvCode.Z;
		case Input.Keys.CONTROL_LEFT:
			return KeyGEvCode.CONTROL_LEFT;
		case Input.Keys.CONTROL_RIGHT:
			return KeyGEvCode.CONTROL_RIGHT;
		case Input.Keys.ESCAPE:
			return KeyGEvCode.ESCAPE;
		case Input.Keys.END:
			return KeyGEvCode.END;
		case Input.Keys.INSERT:
			return KeyGEvCode.INSERT;
		case Input.Keys.PAGE_UP:
			return KeyGEvCode.PAGE_UP;
		case Input.Keys.PAGE_DOWN:
			return KeyGEvCode.PAGE_DOWN;
		case Input.Keys.COLON:
			return KeyGEvCode.COLON;

		default:
			return KeyGEvCode.ANY_KEY;
		}
	}

	public void setWidth(float width) {
		super.setWidth(width);
		setOriginX(width * dispX);
		gameState.setValue(element, SceneElement.VAR_WIDTH,
				(Integer) (int) width);
	}

	public void setHeight(float height) {
		super.setHeight(height);
		setOriginY(height * dispY);
		gameState.setValue(element, SceneElement.VAR_HEIGHT,
				(Integer) (int) height);
	}

	public void setMouseOver(boolean mouseOver) {
		gameState.setValue(element, SceneElement.VAR_MOUSE_OVER, Boolean
				.valueOf(mouseOver));
	}

	public void setScaleX(float scaleX) {
		super.setScaleX(scaleX * scale);
	}

	public void setScaleY(float scaleY) {
		super.setScaleY(scaleY * scale);
	}

}
