package es.eucm.eadventure.common.model.elements.impl;

import es.eucm.eadventure.common.Param;
import es.eucm.eadventure.common.interfaces.Oriented;
import es.eucm.eadventure.common.interfaces.Oriented.Orientation;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.model.variables.impl.vars.BooleanVar;
import es.eucm.eadventure.common.model.variables.impl.vars.IntegerVar;
import es.eucm.eadventure.common.resources.annotation.Asset;
import es.eucm.eadventure.common.resources.annotation.Bundled;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;

public class EAdBasicSceneElement extends AbstractEAdElementWithBehavior implements
		EAdSceneElement {

	@Param("position")
	private EAdPosition position;

	@Param("scale")
	private float scale;

	@Param("orientation")
	private Oriented.Orientation orientation;
	
	@Param("draggable")
	private EAdCondition draggable;
	
	@Bundled
	@Asset({ Drawable.class })
	public static final String appearance = "appearance";
	
	protected IntegerVar positionX = new IntegerVar("positionX", this);

	protected IntegerVar positionY = new IntegerVar("positionY", this);
	
	protected IntegerVar width = new IntegerVar("width", this );
	
	protected IntegerVar height = new IntegerVar("height", this);
	
	protected BooleanVar visible = new BooleanVar("visible", this);

	public EAdBasicSceneElement(String id) {
		super(id);
		setScale(1.0f);
		setPosition(new EAdPosition(0, 0));
		setInitialOrientation(Orientation.SOUTH);
		((BooleanVar) visibleVar()).setInitialValue(true);
		draggable = EmptyCondition.FALSE_EMPTY_CONDITION;
	}

	@Override
	public EAdPosition getPosition() {
		return position;
	}

	public void setPosition(EAdPosition position) {
		this.position = position;
	}

	@Override
	public float getScale() {
		return scale;
	}

	/**
	 * Sets scale for this reference
	 * 
	 * @param scale
	 *            the scale
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}

	@Override
	public Orientation getInitialOrientation() {
		return orientation;
	}

	/**
	 * Sets the initial orientation for the actor reference
	 * 
	 * @param orientation
	 *            the orientation
	 */
	public void setInitialOrientation(Orientation orientation) {
		this.orientation = orientation;

	}
	
	public EAdCondition getDraggabe() {
		return draggable;
	}
	
	public void setDraggabe(EAdCondition draggable) {
		this.draggable = draggable;
	}
	
	public String toString( ){
		return id + " - Scene element";
	}

	@Override
	public EAdVar<Integer> positionXVar() {
		return positionX;
	}

	@Override
	public EAdVar<Integer> positionYVar() {
		return positionY;
	}
	
	public EAdVar<Boolean> visibleVar(){
		return visible;
	}

	@Override
	public EAdVar<Integer> widthVar() {
		return width;
	}

	@Override
	public EAdVar<Integer> heightVar() {
		return height;
	}

}
