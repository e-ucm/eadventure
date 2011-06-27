package es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements;

import java.util.logging.Logger;

import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.AbstractGameObject;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;

public abstract class SceneElementGOImpl<T extends EAdSceneElement> extends
		AbstractGameObject<T> implements SceneElementGO<T> {

	private static final Logger logger = Logger.getLogger("SceneElementGOImpl");

	protected EAdPosition position;

	protected float scale;

	protected Orientation orientation;

	protected float rotation;

	private int width;

	private int height;

	private float alpha;

	protected boolean visible;

	public SceneElementGOImpl() {
		logger.info("New instance");
		visible = true;
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setElement(T element) {
		super.setElement(element);
		this.position = new EAdPosition(element.getPosition());
		element.positionXVar().setInitialValue(position.getX());
		element.positionYVar().setInitialValue(position.getY());

		// If element is a clone, sets its vars to its initial values, if not,
		// it preserves the values contained by the valueMap
		if (element.isClone()) {
			for (EAdVar var : element.getVars()) {
				valueMap.setValue(var, var.getInitialValue());
			}
		}

		visible = valueMap.getValue(element.visibleVar());
		rotation = valueMap.getValue(element.rotationVar());
		scale = valueMap.getValue(element.scaleVar());
		alpha = valueMap.getValue(element.alphaVar());
		orientation = element.getInitialOrientation();
	}

	@Override
	public GameObject<?> getDraggableElement(MouseState mouseState) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.impl.AbstractGameObject#doLayout
	 * ()
	 * 
	 * Should layout all sub-elements and resource
	 */
	@Override
	public void doLayout(int offsetX, int offsetY) {
		super.doLayout(offsetX, offsetY);
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
	public void update(GameState state) {
		super.update(state);
		this.getAsset().update(state);
		this.position.setX(valueMap.getValue(element.positionXVar()));
		this.position.setY(valueMap.getValue(element.positionYVar()));
		this.setVisible(valueMap.getValue(element.visibleVar()));
		this.rotation = valueMap.getValue(element.rotationVar());
		this.alpha = valueMap.getValue(element.alphaVar());
	}

	@Override
	public T getElement() {
		return super.getElement();
	}

	@Override
	public EAdPosition getPosition() {
		return position;
	}

	@Override
	public void setPosition(EAdPosition position) {
		this.position = position;
	}

	@Override
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;

	}

	@Override
	public Orientation getOrientation() {
		return orientation;
	}

	@Override
	public abstract DrawableAsset<?> getAsset();

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public float getScale() {
		return scale;
	}

	@Override
	public void setScale(float scale) {
		this.scale = scale;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setWidth(int width) {
		this.width = width;
		valueMap.setValue(element.widthVar(), width);
	}

	public void setHeight(int height) {
		this.height = height;
		valueMap.setValue(element.heightVar(), height);
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	@Override
	public int getCenterX() {
		return (int) ((position.getJavaX(width) + width / 2) * scale);
	}

	@Override
	public int getCenterY() {
		return (int) ((position.getJavaY(height) + height / 2) * scale);
	}

	public float getAlpha() {
		return alpha;
	}

}
