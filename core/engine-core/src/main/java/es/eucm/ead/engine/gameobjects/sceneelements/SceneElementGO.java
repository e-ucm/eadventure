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

package es.eucm.ead.engine.gameobjects.sceneelements;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.google.inject.Inject;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.assets.drawables.RuntimeDrawable;
import es.eucm.ead.engine.canvas.GdxCanvas;
import es.eucm.ead.engine.events.DragEvent;
import es.eucm.ead.engine.factories.EventFactory;
import es.eucm.ead.engine.factories.SceneElementFactory;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.game.GameState;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.engine.gameobjects.GameObject;
import es.eucm.ead.engine.gameobjects.events.EventGO;
import es.eucm.ead.model.assets.AssetDescriptor;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.events.Event;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.model.interfaces.features.Oriented;
import es.eucm.ead.model.interfaces.features.enums.Orientation;
import es.eucm.ead.model.params.guievents.EAdGUIEvent;
import es.eucm.ead.model.params.guievents.KeyGEv;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.guievents.enums.KeyEventType;
import es.eucm.ead.model.params.guievents.enums.KeyGEvCode;
import es.eucm.ead.model.params.util.Position;
import es.eucm.ead.model.params.variables.EAdVarDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;

/**
 * 
 */
public class SceneElementGO extends Group implements GameObject<SceneElement>,
		Oriented, EventListener {

	static protected Logger logger = LoggerFactory
			.getLogger(SceneElementGO.class);

	/**
	 * Scene element factory
	 */
	protected SceneElementFactory sceneElementFactory;

	/**
	 * Game
	 */
	protected Game game;

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
	private EventFactory eventFactory;

	/**
	 * Scene element
	 */
	protected SceneElement element;

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

	// Relative positions

	private boolean updateRelatives;

	private Vector2 center;

	private Vector2 topLeft;

	private Vector2 bottomRight;

	private Comparator<SceneElementGO> comparator;

	// Aux
	private List reorderList = new ArrayList();

	private Map<EAdVarDef<?>, ElementField<?>> fields;

	@Inject
	public SceneElementGO(AssetHandler assetHandler,
			SceneElementFactory sceneElementFactory, Game game,
			EventFactory eventFactory) {
		this.game = game;
		this.eventFactory = eventFactory;
		this.gameState = game.getGameState();
		this.assetHandler = assetHandler;
		this.sceneElementFactory = sceneElementFactory;
		this.gui = game.getGUI();
		this.fields = new HashMap<EAdVarDef<?>, ElementField<?>>();

		addListener(this);

		// Relative positions
		center = new Vector2();
		topLeft = new Vector2();
		bottomRight = new Vector2();

		statesList = new ArrayList<String>();
		eventGOList = new ArrayList<EventGO<?>>();
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	@Override
	public void setElement(SceneElement element) {
		this.element = element;
		fields.clear();
		resetVars();

		setName(element.getId());

		// We only set initial values if it is the first time this element is
		// shown
		if (!gameState.contains(element)) {
			// Definition values
			for (Entry<EAdVarDef<?>, Object> e : element.getDefinition()
					.getVars().entrySet()) {
				EAdVarDef def = e.getKey();
				gameState.setValue(element, def, e.getValue());
			}

			// Scene element values
			for (Entry<EAdVarDef<?>, Object> e : element.getVars().entrySet()) {
				EAdVarDef def = e.getKey();
				gameState.setValue(element, def, e.getValue());
			}
		}

		// Scene element events
		initEvents(element.getEvents());
		// Definition events
		initEvents(element.getDefinition().getEvents());

		updateVars();
		setExtraVars();
	}

	private void resetVars() {
		statesList.clear();
		eventGOList.clear();
		currentBundle = null;
		currentDrawable = null;
		inputProcessor = null;
		mouseOver = false;
		reorder = false;
		runtimeDrawable = null;
		scale = 1.0f;
		state = null;
		timeDisplayed = 0;
		this.setColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	private void initEvents(EAdList<Event> events) {
		if (events != null) {
			for (Event event : events) {
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
		gameState.setValue(element, SceneElement.VAR_LEFT, topLeft.x);
		gameState.setValue(element, SceneElement.VAR_TOP, topLeft.y);
		gameState.setValue(element, SceneElement.VAR_RIGHT, bottomRight.x);
		gameState.setValue(element, SceneElement.VAR_BOTTOM, bottomRight.y);
		gameState.setValue(element, SceneElement.VAR_CENTER_X, center.x);
		gameState.setValue(element, SceneElement.VAR_CENTER_Y, center.y);

	}

	@Override
	public SceneElement getElement() {
		return element;
	}

	/**
	 * Adds an scene element as a child of this element
	 * 
	 * @param element the element to add
	 * @return the game object created for the element
	 */
	public SceneElementGO addSceneElement(SceneElement element) {
		SceneElementGO go = sceneElementFactory.get(element);
		addSceneElement(go);
		return go;
	}

	/**
	 * Sets position for this element
	 * 
	 * @param position the new position
	 */
	public void setPosition(Position position) {
		setX(position.getX());
		setY(position.getY());
		setDispX(position.getDispX());
		setDispY(position.getDispY());
	}

	public void setRotation(float rotation) {
		super.setRotation(rotation);
		gameState.setValue(getElement(), SceneElement.VAR_ROTATION, rotation);
		updateRelatives = true;
	}

	/**
	 * Sets x position for this element
	 * 
	 * @param x the x coordinate
	 */
	public void setX(float x) {
		updateRelatives = true;
		super.setX(x - this.getOriginX());
		gameState.setValue(getElement(), SceneElement.VAR_X, x);
	}

	private void updateRelatives() {
		center.x = getWidth() / 2;
		center.y = getHeight() / 2;
		topLeft.x = 0;
		topLeft.y = 0;
		bottomRight.y = getWidth();
		bottomRight.x = getHeight();
		this.localToStageCoordinates(center);
		this.localToStageCoordinates(topLeft);
		this.localToStageCoordinates(bottomRight);
		setExtraVars();
	}

	public float getCenterX() {
		return center.x;
	}

	public float getCenterY() {
		return center.y;
	}

	public float getRight() {
		return bottomRight.x;
	}

	public float getBottom() {
		return bottomRight.y;
	}

	public float getTop() {
		return topLeft.y;
	}

	public float getLeft() {
		return topLeft.x;
	}

	/**
	 * Sets y position for this element
	 * 
	 * @param y the y coordinate
	 */
	public void setY(float y) {
		updateRelatives = true;
		super.setY(y - this.getOriginY());
		gameState.setValue(getElement(), SceneElement.VAR_Y, y);
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
	 *
	 * @return
	 * Returns displacement proportion in x coordination
	 */
	public float getDispX() {
		return dispX;
	}

	/** * Returns displacement proportion in x coordination
	 *
	 * @return
	 * Returns displacement proportion in x coordination
	 */
	public float getDispY() {
		return dispY;
	}

	/**
	 *
	 * @return
	 * Return the z order for this element
	 */
	public int getZ() {
		return z;
	}

	/**
	 * Sets the z order for this element
	 * 
	 * @param z the z order
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
	 * @param scale the scale
	 */
	public void setScale(float scale) {
		if (scale != this.scale) {
			this.scale = scale;
			updateRelatives = true;
			gameState.setValue(element, SceneElement.VAR_SCALE, scale);
		}
	}

	/**
	 *
	 * @return
	 * Returns the current scale of the element
	 */
	public float getScale() {
		return scale;
	}

	/**
	 *
	 * @param alpha
	 * Sets the alpha for this element
	 */
	public void setAlpha(float alpha) {
		if (alpha != this.getColor().a) {
			getColor().a = alpha;
			gameState.setValue(element, SceneElement.VAR_ALPHA, alpha);
		}
	}

	/**
	 * Sets an input processor for this element. This processor will process the
	 * actions before the default process
	 * 
	 * @param processor the processor
	 * @param transmitToChildren if event must be transmitted to children
	 *
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
	 *
	 * @return
	 * Returns the drawable that represents this element
	 */
	public RuntimeDrawable<?> getDrawable() {
		return currentDrawable;
	}

	/**
	 *
	 * @param state
	 * Sets the state for this element
	 */
	public void setState(String state) {
		this.state = state;
		gameState.setValue(getElement(), SceneElement.VAR_STATE, state);
	}

	public void setVisible(boolean visible) {
		if (visible != this.isVisible()) {
			super.setVisible(visible);
			gameState.setValue(getElement(), SceneElement.VAR_VISIBLE, visible);
		}
	}

	/**
	 * Returns the field for this variable of this element
	 * 
	 * @param var the var definition
	 */
	@SuppressWarnings("unchecked")
	public <S> ElementField<S> getField(EAdVarDef<S> var) {
		ElementField<S> field = (ElementField<S>) fields.get(var);
		if (field == null) {
			field = new ElementField<S>(getElement(), var);
			fields.put(var, field);
		}
		return field;
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
	}

	public void addActor(Actor a) {
		super.addActor(a);
		invalidateOrder();
	}

	public SceneElementGO getChild(SceneElement e) {
		if (e.getId() != null) {
			return (SceneElementGO) super.findActor(e.getId());
		}
		return null;
	}

	public SceneElementGO getFirstGOIn(float virtualX, float virtualY,
			boolean touchable) {
		return (SceneElementGO) this.hit(virtualX, virtualY, touchable);
	}

	/**
	 * Sets a comparator to automatically reorder the scene elements (modifies
	 * drawing order)
	 * 
	 * @param comparator the comparator
	 */
	public void setComparator(Comparator<SceneElementGO> comparator) {
		this.comparator = comparator;
	}

	/**
	 * Launches a list of effects
	 * 
	 * @param list the list
	 * @param action the action that launched the effects
	 */
	private void addEffects(EAdList<Effect> list,
			com.badlogic.gdx.scenes.scene2d.Event action) {
		if (list != null && list.size() > 0) {
			for (Effect e : list) {
				game.addEffect(e, action, element);
			}
		}
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	@Override
	public void act(float delta) {
		// Reorder list
		if (reorder) {
			if (comparator != null) {
				reorderList.clear();
				for (Actor a : this.getChildren()) {
					reorderList.add(a);
				}
				getChildren().clear();
				Collections.sort(reorderList, comparator);
				for (Object a : reorderList) {
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
		timeDisplayed += delta;
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

		if (updateRelatives) {
			updateRelatives = false;
			updateRelatives();
		}

		if (this.isVisible()) {
			gameState.setValue(element.getDefinition(),
					SceneElementDef.VAR_SCENE_ELEMENT, element);
		}

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
		return element.isContainsBounds()
				|| (currentDrawable != null && currentDrawable.contains(
						(int) x, (int) y));
	}

	@Override
	public boolean handle(com.badlogic.gdx.scenes.scene2d.Event event) {
		if (inputProcessor != null) {
			inputProcessor.handle(event);
		}

		// Due the way Stage fireEnterAndExit works, enter and exit events never
		// are cancelled
		boolean cancel = true;

		if (this.getTouchable() == Touchable.enabled
				&& event instanceof InputEvent) {
			Type t = ((InputEvent) event).getType();
			if (t != null) {
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
		}

		if (!event.isCancelled()) {
			// Effects in the scene element instance
			EAdList<Effect> list = element.getEffects(getGUIEvent(event));
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

		return event.isCancelled();
	}

	public EAdGUIEvent getGUIEvent(com.badlogic.gdx.scenes.scene2d.Event e) {
		EAdGUIEvent guiEvent = null;
		// First check DragEvent, because it is also an InputEvent
		if (e instanceof DragEvent) {
			return ((DragEvent) e).getDragEvent();
		} else if (e instanceof InputEvent) {
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
				// XXX Fixme, news!!
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

	public float getRelativeX() {
		return getX() + getOriginX();
	}

	public float getRelativeY() {
		return getY() + getOriginY();
	}

	public void setWidth(float width) {
		updateRelatives = true;
		super.setWidth(width);
		setOriginX(width * dispX);
		gameState.setValue(element, SceneElement.VAR_WIDTH, (int) width);
	}

	public void setHeight(float height) {
		updateRelatives = true;
		super.setHeight(height);
		setOriginY(height * dispY);
		gameState.setValue(element, SceneElement.VAR_HEIGHT, (int) height);
	}

	public void setMouseOver(boolean mouseOver) {
		gameState.setValue(element, SceneElement.VAR_MOUSE_OVER, mouseOver);
	}

	public void setScaleX(float scaleX) {
		updateRelatives = true;
		super.setScaleX(scaleX * scale);
		gameState.setValue(getElement(), SceneElement.VAR_SCALE_X, scaleX);
	}

	public void setScaleY(float scaleY) {
		updateRelatives = true;
		super.setScaleY(scaleY * scale);
		gameState.setValue(getElement(), SceneElement.VAR_SCALE_Y, scaleY);
	}

	public void free() {
		for (Actor a : getChildren()) {
			if (a instanceof SceneElementGO) {
				((SceneElementGO) a).free();
			}
		}
		sceneElementFactory.remove(this);
		for (EventGO<?> eventGO : this.eventGOList) {
			eventFactory.remove(eventGO);
		}
		getChildren().clear();
		setParent(null);
		this.currentDrawable = null;
		this.runtimeDrawable = null;
	}

	@Override
	public void release() {
		remove();
	}

}
