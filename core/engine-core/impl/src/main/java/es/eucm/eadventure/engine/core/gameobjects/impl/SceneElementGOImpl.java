package es.eucm.eadventure.engine.core.gameobjects.impl;

import java.util.logging.Logger;

import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public abstract class SceneElementGOImpl<T extends EAdSceneElement> extends AbstractGameObject<T> implements
		SceneElementGO<T> {

	private static final Logger logger = Logger.getLogger("SceneElementGOImpl");

	protected EAdPosition position;
	
	protected float scale;
	
	protected Orientation orientation;
	
	private int width;
	
	private int height;
	
	protected boolean visible;

	public SceneElementGOImpl() {
		logger.info("New instance");
		visible = true;
	}
	
	@Override
	public abstract boolean processAction(GUIAction action);

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.gameobjects.impl.AbstractGameObject#setElement(es.eucm.eadventure.common.model.EAdElement)
	 * 
	 * Should be implemented to get position, scale, orientation and other values
	 */
	@Override
	public void setElement(T element) {
		super.setElement(element);
		this.position = new EAdPosition(element.getPosition());
		valueMap.setValue(element.positionXVar(), position.getX());
		valueMap.setValue(element.positionYVar(), position.getY());
		this.visible = true;
		valueMap.setValue(element.visibleVar(), Boolean.TRUE);
		this.scale = element.getScale();
		//TODO
		//this.orientation = new Orientation(element.getInitialOrientation());
		this.orientation = element.getInitialOrientation();
	}

	@Override
	public GameObject<?> getDraggableElement(MouseState mouseState) {
		return null;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.gameobjects.impl.AbstractGameObject#doLayout()
	 * 
	 * Should layout all sub-elements and resource
	 */
	@Override
	public void doLayout(int offsetX, int offsetY) {
		super.doLayout(offsetX, offsetY);
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.gameobjects.impl.AbstractGameObject#update(es.eucm.eadventure.engine.core.GameState)
	 * 
	 * Should update the state of all sub-elements and resources
	 */
	@Override
	public void update(GameState state) {
		super.update(state);
		this.getAsset().update(state);
		this.position.setX(valueMap.getValue(element.positionXVar()));
		this.position.setY(valueMap.getValue(element.positionYVar()));
		this.setVisible( valueMap.getValue(element.visibleVar()) );
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
	public abstract RuntimeAsset<?> getAsset();
	
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
	
}
