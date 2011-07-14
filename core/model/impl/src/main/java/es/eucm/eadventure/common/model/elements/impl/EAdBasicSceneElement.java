package es.eucm.eadventure.common.model.elements.impl;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Oriented.Orientation;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.model.variables.impl.EAdVarImpl;
import es.eucm.eadventure.common.model.variables.impl.vars.BooleanVar;
import es.eucm.eadventure.common.model.variables.impl.vars.FloatVar;
import es.eucm.eadventure.common.model.variables.impl.vars.IntegerVar;
import es.eucm.eadventure.common.model.variables.impl.vars.StringVar;
import es.eucm.eadventure.common.resources.annotation.Asset;
import es.eucm.eadventure.common.resources.annotation.Bundled;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;

@Element(detailed = EAdBasicSceneElement.class, runtime = EAdBasicSceneElement.class)
public class EAdBasicSceneElement extends AbstractEAdElementWithBehavior
		implements EAdSceneElement {

	@Param("position")
	private EAdPosition position;

	@Param("draggable")
	private EAdCondition draggable;

	@Bundled
	@Asset({ Drawable.class })
	public static final String appearance = "appearance";
	
	private List<EAdVar<?>> vars;

	protected IntegerVar positionX = new IntegerVar("positionX", this);

	protected IntegerVar positionY = new IntegerVar("positionY", this);

	protected IntegerVar width = new IntegerVar("width", this);

	protected IntegerVar height = new IntegerVar("height", this);

	protected BooleanVar visible = new BooleanVar("visible", this);

	protected FloatVar rotation = new FloatVar("roation", this);

	protected FloatVar alpha = new FloatVar("alpha", this);
	
	protected FloatVar scale = new FloatVar("scale", this);

	protected StringVar state = new StringVar("state", this);
	
	protected EAdVar<Orientation> orientation = new EAdVarImpl<Orientation>(Orientation.class, "orientation", this);

	private boolean clone;

	public EAdBasicSceneElement(String id) {
		super(id);
		setScale(1.0f);
		clone = false;
		setPosition(new EAdPosition(0, 0));
		setInitialOrientation(Orientation.S);
		draggable = EmptyCondition.FALSE_EMPTY_CONDITION;
		visible.setInitialValue(Boolean.TRUE);
		alpha.setInitialValue(1.0f);
		vars = new ArrayList<EAdVar<?>>();
		state.setInitialValue("default");
		vars.add(positionX);
		vars.add(positionY);
		vars.add(width);
		vars.add(height);
		vars.add(visible);
		vars.add(rotation);
		vars.add(alpha);
		vars.add(scale);
		vars.add(state);
		vars.add(orientation);
	}

	@Override
	public EAdPosition getPosition() {
		return position;
	}

	public void setPosition(EAdPosition position) {
		this.position = position;
	}

	@Override
	public EAdVar<Float> scaleVar() {
		return scale;
	}

	/**
	 * Sets scale for this reference
	 * 
	 * @param scale
	 *            the scale
	 */
	public void setScale(float scale) {
		this.scale.setInitialValue(scale);
	}

	/**
	 * Sets the initial orientation for the actor reference
	 * 
	 * @param orientation
	 *            the orientation
	 */
	public void setInitialOrientation(Orientation orientation) {
		this.orientation.setInitialValue(orientation);

	}

	public EAdCondition getDraggabe() {
		return draggable;
	}

	public void setDraggabe(EAdCondition draggable) {
		this.draggable = draggable;
	}

	public String toString() {
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

	public EAdVar<Boolean> visibleVar() {
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

	@Override
	public EAdVar<Float> rotationVar() {
		return rotation;
	}

	@Override
	public EAdVar<Float> alphaVar() {
		return alpha;
	}

	@Override
	public boolean isClone() {
		return clone;
	}

	/**
	 * Sets whether if this scene element must be cloned whenever is added to
	 * the game. This means that all its variables will be set with its initial
	 * values, instead of storing their last values
	 * 
	 * @param clone
	 *            if this elements produces clones when its added to the game
	 */
	public void setClone(boolean clone) {
		this.clone = clone;
	}
	
	@Override
	public EAdVar<String> stateVar(){
		return state;
	}

	@Override
	public List<EAdVar<?>> getVars() {
		return vars;
	}

	@Override
	public EAdVar<Orientation> orientationVar() {
		return orientation;
	}

}
