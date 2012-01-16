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

package ead.common.model.elements.scenes;

import com.gwtent.reflection.client.Reflectable;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.interfaces.features.enums.Orientation;
import ead.common.model.elements.AbstractElementWithBehavior;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.enums.CommonStates;
import ead.common.model.elements.extra.EAdMap;
import ead.common.model.elements.extra.EAdMapImpl;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.variables.EAdVarDef;
import ead.common.model.elements.variables.VarDefImpl;
import ead.common.params.text.EAdString;
import ead.common.resources.assets.drawable.Drawable;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;

@Reflectable
@Element(detailed = SceneElementImpl.class, runtime = SceneElementImpl.class)
public class SceneElementImpl extends AbstractElementWithBehavior
		implements EAdSceneElement {

	public static final EAdVarDef<Orientation> VAR_ORIENTATION = new VarDefImpl<Orientation>(
			"orientation", Orientation.class, Orientation.S);

	public static final EAdVarDef<String> VAR_STATE = new VarDefImpl<String>(
			"state", String.class, CommonStates.EAD_STATE_DEFAULT.toString());

	public static final EAdVarDef<Float> VAR_SCALE = new VarDefImpl<Float>(
			"scale", Float.class, 1.0f);

	public static final EAdVarDef<Float> VAR_ALPHA = new VarDefImpl<Float>(
			"alpha", Float.class, 1.0f);

	public static final EAdVarDef<Float> VAR_ROTATION = new VarDefImpl<Float>(
			"rotation", Float.class, 0.0f);

	public static final EAdVarDef<Boolean> VAR_VISIBLE = new VarDefImpl<Boolean>(
			"visible", Boolean.class, Boolean.TRUE);

	public static final EAdVarDef<Boolean> VAR_ENABLE = new VarDefImpl<Boolean>(
			"enable", Boolean.class, Boolean.TRUE);

	public static final EAdVarDef<Integer> VAR_X = new VarDefImpl<Integer>(
			"x", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_Y = new VarDefImpl<Integer>(
			"y", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_LEFT = new VarDefImpl<Integer>(
			"left", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_TOP = new VarDefImpl<Integer>(
			"top", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_RIGHT = new VarDefImpl<Integer>(
			"right", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_BOTTOM = new VarDefImpl<Integer>(
			"bottom", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_CENTER_X = new VarDefImpl<Integer>(
			"center_x", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_CENTER_Y = new VarDefImpl<Integer>(
			"center_y", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_Z = new VarDefImpl<Integer>(
			"z", Integer.class, 0);

	public static final EAdVarDef<Float> VAR_DISP_X = new VarDefImpl<Float>(
			"disp_x", Float.class, 0.0f);

	public static final EAdVarDef<Float> VAR_DISP_Y = new VarDefImpl<Float>(
			"disp_y", Float.class, 0.0f);

	public static final EAdVarDef<Integer> VAR_WIDTH = new VarDefImpl<Integer>(
			"width", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_HEIGHT = new VarDefImpl<Integer>(
			"height", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_TIME_DISPLAYED = new VarDefImpl<Integer>(
			"timeDisplayed", Integer.class, 0);

	public static final EAdVarDef<EAdString> VAR_NAME = new VarDefImpl<EAdString>(
			"name", EAdString.class, null);
	
	/**
	 * Flag to indicate that the element will return to its initial position after being released from a drag action
	 */
	public static final EAdVarDef<Boolean> VAR_RETURN_WHEN_DRAGGED = new VarDefImpl<Boolean>(
			"returnWhenDragged", Boolean.class, Boolean.FALSE);

	@Param("vars")
	private EAdMap<EAdVarDef<?>, Object> vars;

	@Param("definition")
	protected EAdSceneElementDef definition;

	@Param("dragCond")
	protected EAdCondition dragCond;

	public SceneElementImpl() {
		super();
		dragCond = EmptyCond.FALSE_EMPTY_CONDITION;
		definition = new SceneElementDefImpl();
		vars = new EAdMapImpl<EAdVarDef<?>, Object>(EAdVarDef.class,
				Object.class);
	}

	/**
	 * Creates a basic scene element
	 * 
	 * @param id
	 *            the id
	 * @param appearance
	 *            the initial appearance
	 */
	public SceneElementImpl(Drawable appearance) {
		this();
		this.definition = new SceneElementDefImpl(appearance);
	}

	public SceneElementImpl(EAdSceneElementDef actor) {
		this();
		setId(actor.getId() + "_ref");
		this.definition = actor;
	}

	public <T> void setVarInitialValue(EAdVarDef<T> var, T value) {
		vars.put(var, value);
	}

	public void setPosition(EAdPosition position) {
		vars.put(VAR_X, position.getX());
		vars.put(VAR_Y, position.getY());
		vars.put(VAR_DISP_X, position.getDispX());
		vars.put(VAR_DISP_Y, position.getDispY());
	}

	/**
	 * Sets scale for this reference
	 * 
	 * @param scale
	 *            the scale
	 */
	public void setScale(float scale) {
		vars.put(VAR_SCALE, scale);
	}

	/**
	 * Sets the initial orientation for the actor reference
	 * 
	 * @param orientation
	 *            the orientation
	 */
	public void setInitialOrientation(Orientation orientation) {
		vars.put(VAR_ORIENTATION, orientation);
	}

	@Override
	public EAdMap<EAdVarDef<?>, Object> getVars() {
		return vars;
	}

	public void setPosition(int x, int y) {
		vars.put(VAR_X, x);
		vars.put(VAR_Y, y);
	}

	public void setPosition(Corner corner, int x, int y) {
		setPosition(EAdPosition.volatileEAdPosition(corner, x, y));
	}

	@Override
	public EAdSceneElementDef getDefinition() {
		return definition;
	}

	public void setDefinition(EAdSceneElementDef def) {
		this.definition = def;
	}

	public boolean equals(Object o) {
		boolean temp = this == o;
		return temp;
	}

	public void setDragCond(EAdCondition c) {
		this.dragCond = c;
	}

	@Override
	public EAdCondition getDragCond() {
		return dragCond;
	}

	public void setInitialAlpha(float f) {
		this.setVarInitialValue(SceneElementImpl.VAR_ALPHA, f);
		
	}

}
