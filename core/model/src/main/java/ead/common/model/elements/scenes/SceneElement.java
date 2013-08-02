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

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.interfaces.features.enums.Orientation;
import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.elements.AbstractElementWithBehavior;
import ead.common.model.elements.ResourcedElement;
import ead.common.model.elements.enums.CommonStates;
import ead.common.model.elements.extra.EAdMap;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.params.util.Position;
import ead.common.model.params.util.Position.Corner;
import ead.common.model.params.variables.EAdVarDef;
import ead.common.model.params.variables.VarDef;

@Element
public class SceneElement extends AbstractElementWithBehavior implements
		EAdSceneElement {

	public static final EAdVarDef<Orientation> VAR_ORIENTATION = new VarDef<Orientation>(
			"orientation", Orientation.class, Orientation.S);

	public static final EAdVarDef<String> VAR_STATE = new VarDef<String>(
			"state", String.class, CommonStates.DEFAULT.toString());

	public static final EAdVarDef<String> VAR_BUNDLE_ID = new VarDef<String>(
			"bundleId", String.class, ResourcedElement.INITIAL_BUNDLE);

	public static final EAdVarDef<Float> VAR_SCALE = new VarDef<Float>("scale",
			Float.class, 1.0f);

	public static final EAdVarDef<Float> VAR_SCALE_X = new VarDef<Float>(
			"scale_x", Float.class, 1.0f);

	public static final EAdVarDef<Float> VAR_SCALE_Y = new VarDef<Float>(
			"scale_y", Float.class, 1.0f);

	public static final EAdVarDef<Float> VAR_ALPHA = new VarDef<Float>("alpha",
			Float.class, 1.0f);

	/**
	 * Rotation in degrees
	 */
	public static final EAdVarDef<Float> VAR_ROTATION = new VarDef<Float>(
			"rotation", Float.class, 0.0f);

	public static final EAdVarDef<Boolean> VAR_VISIBLE = new VarDef<Boolean>(
			"visible", Boolean.class, Boolean.TRUE);

	public static final EAdVarDef<Boolean> VAR_ENABLE = new VarDef<Boolean>(
			"enable", Boolean.class, Boolean.TRUE);

	public static final EAdVarDef<Float> VAR_X = new VarDef<Float>("x",
			Float.class, 0.f);

	public static final EAdVarDef<Float> VAR_Y = new VarDef<Float>("y",
			Float.class, 0.f);

	public static final EAdVarDef<Float> VAR_LEFT = new VarDef<Float>("left",
			Float.class, 0.f);

	public static final EAdVarDef<Float> VAR_TOP = new VarDef<Float>("top",
			Float.class, 0.f);

	public static final EAdVarDef<Float> VAR_RIGHT = new VarDef<Float>("right",
			Float.class, 0.f);

	public static final EAdVarDef<Float> VAR_BOTTOM = new VarDef<Float>(
			"bottom", Float.class, 0.f);

	public static final EAdVarDef<Float> VAR_CENTER_X = new VarDef<Float>(
			"center_x", Float.class, 0.f);

	public static final EAdVarDef<Float> VAR_CENTER_Y = new VarDef<Float>(
			"center_y", Float.class, 0.f);

	public static final EAdVarDef<Integer> VAR_Z = new VarDef<Integer>("z",
			Integer.class, 0);

	public static final EAdVarDef<Float> VAR_DISP_X = new VarDef<Float>(
			"disp_x", Float.class, 0.0f);

	public static final EAdVarDef<Float> VAR_DISP_Y = new VarDef<Float>(
			"disp_y", Float.class, 0.0f);

	public static final EAdVarDef<Integer> VAR_WIDTH = new VarDef<Integer>(
			"width", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_HEIGHT = new VarDef<Integer>(
			"height", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_TIME_DISPLAYED = new VarDef<Integer>(
			"timeDisplayed", Integer.class, 0);

	public static final EAdVarDef<Boolean> VAR_MOUSE_OVER = new VarDef<Boolean>(
			"mouse_over", Boolean.class, Boolean.FALSE);

	@Param
	private EAdMap<EAdVarDef<?>, Object> vars;

	@Param
	protected EAdSceneElementDef definition;

	/**
	 * Sets if the for the contains method, must be used only the scene element bounds
	 */
	@Param
	protected boolean containsBounds;

	public SceneElement() {
		super();
		definition = new SceneElementDef();
		vars = new EAdMap<EAdVarDef<?>, Object>();
		this.setPosition(Corner.TOP_LEFT, 0, 0);
	}

	/**
	 * Creates a basic scene element
	 * 
	 * @param id
	 *            the id
	 * @param appearance
	 *            the initial appearance
	 */
	public SceneElement(EAdDrawable appearance) {
		this();
		this.definition = new SceneElementDef(appearance);
	}

	public SceneElement(EAdDrawable appearance, EAdDrawable overAppearance) {
		this();
		this.definition = new SceneElementDef(appearance, overAppearance);
	}

	public SceneElement(EAdSceneElementDef actor) {
		this();
		this.definition = actor;
	}

	public void setVars(EAdMap<EAdVarDef<?>, Object> vars) {
		this.vars = vars;
	}

	public <T> void setVarInitialValue(EAdVarDef<T> var, T value) {
		vars.put(var, value);
	}

	public void setPosition(Position position) {
		vars.put(VAR_X, position.getX());
		vars.put(VAR_Y, position.getY());
		vars.put(VAR_DISP_X, position.getDispX());
		vars.put(VAR_DISP_Y, position.getDispY());
	}

	public void setCenter(Corner center) {
		vars.put(VAR_DISP_X, center.getDispX());
		vars.put(VAR_DISP_Y, center.getDispY());
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

	public void setPosition(float x, float y) {
		vars.put(VAR_X, x);
		vars.put(VAR_Y, y);
	}

	public void setPosition(Corner corner, float x, float y) {
		setPosition(Position.volatileEAdPosition(corner, x, y));
	}

	@Override
	public EAdSceneElementDef getDefinition() {
		return definition;
	}

	public void setDefinition(EAdSceneElementDef def) {
		this.definition = def;
	}

	public void setInitialAlpha(float f) {
		this.setVarInitialValue(SceneElement.VAR_ALPHA, f);

	}

	/**
	 * Sets the initial scale for the scene element
	 * 
	 * @param scale
	 *            the initial scale
	 */
	public void setInitialScale(float scale) {
		setVarInitialValue(SceneElement.VAR_SCALE, scale);
	}

	public void setInitialScale(float scaleX, float scaleY) {
		setVarInitialValue(SceneElement.VAR_SCALE_X, scaleX);
		setVarInitialValue(SceneElement.VAR_SCALE_Y, scaleY);
	}

	/**
	 * Sets the initial appearance for the scene element
	 * 
	 * @param appearance
	 *            the initial appearance
	 */
	public void setAppearance(EAdDrawable appearance) {
		getDefinition().setAppearance(appearance);
	}

	public void setInitialEnable(boolean enable) {
		setVarInitialValue(SceneElement.VAR_ENABLE, enable);
	}

	public void setInitialRotation(float rotation) {
		setVarInitialValue(SceneElement.VAR_ROTATION, rotation);
	}

	public <T> EAdField<T> getField(EAdVarDef<T> varDef) {
		return new BasicField<T>(this, varDef);
	}

	public String toString() {
		return getId();
	}

	public void setAppearance(String bundle, EAdDrawable drawable) {
		getDefinition().setAppearance(bundle, drawable);
	}

	public void setOverAppearance(String bundle, EAdDrawable drawable) {
		getDefinition().setOverAppearance(bundle, drawable);
	}

	public void setInitialVisible(boolean visible) {
		setVarInitialValue(SceneElement.VAR_VISIBLE, visible);
	}

	public void setOverAppearance(EAdDrawable d) {
		this.getDefinition().setOverAppearance(d);
	}

	public void setInitialZ(int z) {
		setVarInitialValue(SceneElement.VAR_Z, z);

	}

	public void setInitialState(String state) {
		setVarInitialValue(SceneElement.VAR_STATE, state);
	}

	public boolean isContainsBounds() {
		return containsBounds;
	}

	public void setContainsBounds(boolean containsBounds) {
		this.containsBounds = containsBounds;
	}

	public void setInitialBundle(String bundleId) {
		this.setVarInitialValue(SceneElement.VAR_BUNDLE_ID, bundleId);

	}

}
