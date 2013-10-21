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

package es.eucm.ead.model.elements.scenes;

import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.model.elements.AbstractElementWithBehavior;
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.interfaces.features.Variabled;
import es.eucm.ead.model.interfaces.features.enums.Orientation;
import es.eucm.ead.model.params.util.Position;
import es.eucm.ead.model.params.util.Position.Corner;

@Element
public class SceneElement extends AbstractElementWithBehavior implements
		Variabled {

	public static final String VAR_ORIENTATION = "orientation";

	public static final String VAR_STATE = "state";

	public static final String VAR_BUNDLE_ID = "bundleId";

	public static final String VAR_SCALE = "scale";

	public static final String VAR_SCALE_X = "scale_x";

	public static final String VAR_SCALE_Y = "scale_y";

	public static final String VAR_ALPHA = "alpha";

	/**
	 * Rotation in degrees
	 */
	public static final String VAR_ROTATION = "rotation";

	public static final String VAR_VISIBLE = "visible";

	public static final String VAR_ENABLE = "enable";

	public static final String VAR_X = "x";

	public static final String VAR_Y = "y";

	public static final String VAR_Z = "z";

	public static final String VAR_DISP_X = "disp_x";

	public static final String VAR_DISP_Y = "disp_y";

	public static final String VAR_WIDTH = "width";

	public static final String VAR_HEIGHT = "height";
	public static final String VAR_CENTER_X = "center_x";
	public static final String VAR_CENTER_Y = "center_y";
	public static final String VAR_TOP = "top";
	public static final String VAR_LEFT = "left";
	public static final String VAR_BOTTOM = "bottom";
	public static final String VAR_RIGHT = "right";

	@Param
	private EAdMap<Object> vars;

	@Param
	protected SceneElementDef definition;

	/**
	 * Sets if the for the contains method, must be used only the scene element bounds
	 */
	@Param
	protected boolean containsBounds;

	/**
	 * Creates an empty scene element
	 */
	public SceneElement() {

	}

	/**
	 * Creates a basic scene element
	 *
	 * @param appearance the initial appearance
	 */
	public SceneElement(EAdDrawable appearance) {
		this.definition = new SceneElementDef(appearance);
	}

	public SceneElement(EAdDrawable appearance, EAdDrawable overAppearance) {
		this.definition = new SceneElementDef(appearance, overAppearance);
	}

	public SceneElement(SceneElementDef actor) {
		this.definition = actor;
	}

	public void setVars(EAdMap<Object> vars) {
		this.vars = vars;
	}

	public void setPosition(Position position) {
		setVar(VAR_X, position.getX());
		setVar(VAR_Y, position.getY());
		setVar(VAR_DISP_X, position.getDispX());
		setVar(VAR_DISP_Y, position.getDispY());
	}

	public void setCenter(Corner center) {
		setVar(VAR_DISP_X, center.getDispX());
		setVar(VAR_DISP_Y, center.getDispY());
	}

	/**
	 * Sets the initial orientation for the actor reference
	 *
	 * @param orientation the orientation
	 */
	public void setInitialOrientation(Orientation orientation) {
		setVar(VAR_ORIENTATION, orientation);
	}

	public EAdMap<Object> getVars() {
		return vars;
	}

	@Override
	public void setVar(String varName, Object value) {
		if (vars == null) {
			vars = new EAdMap<Object>();
		}
		vars.put(varName, value);
	}

	@SuppressWarnings("all")
	@Override
	public <T> T getVar(String varName, T defaultValue) {
		return (T) (vars != null && vars.containsKey(varName) ? vars
				.get(varName) : defaultValue);
	}

	public void setPosition(float x, float y) {
		setVar(VAR_X, x);
		setVar(VAR_Y, y);
	}

	public void setPosition(Corner corner, float x, float y) {
		setPosition(Position.volatileEAdPosition(corner, x, y));
	}

	public SceneElementDef getDefinition() {
		return definition;
	}

	public void setDefinition(SceneElementDef def) {
		this.definition = def;
	}

	public void setInitialAlpha(float f) {
		this.setVar(SceneElement.VAR_ALPHA, f);

	}

	/**
	 * Sets the initial scale for the scene element
	 *
	 * @param scale the initial scale
	 */
	public void setInitialScale(float scale) {
		setVar(SceneElement.VAR_SCALE, scale);
	}

	public void setInitialScale(float scaleX, float scaleY) {
		setVar(SceneElement.VAR_SCALE_X, scaleX);
		setVar(SceneElement.VAR_SCALE_Y, scaleY);
	}

	/**
	 * Sets the initial appearance for the scene element
	 *
	 * @param appearance the initial appearance
	 */
	public void setAppearance(EAdDrawable appearance) {
		if (definition == null) {
			definition = new SceneElementDef();
		}
		definition.setAppearance(appearance);
	}

	public void setInitialEnable(boolean enable) {
		setVar(SceneElement.VAR_ENABLE, enable);
	}

	public void setInitialRotation(float rotation) {
		setVar(SceneElement.VAR_ROTATION, rotation);
	}

	public ElementField getField(String varName) {
		return new ElementField(this, varName);
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
		setVar(SceneElement.VAR_VISIBLE, visible);
	}

	public void setOverAppearance(EAdDrawable d) {
		this.getDefinition().setOverAppearance(d);
	}

	public void setInitialZ(int z) {
		setVar(SceneElement.VAR_Z, z);

	}

	public void setInitialState(String state) {
		setVar(SceneElement.VAR_STATE, state);
	}

	/**
	 * If for the contains method, must be used only the scene element bounds
	 */
	public boolean isContainsBounds() {
		return containsBounds;
	}

	public void setContainsBounds(boolean containsBounds) {
		this.containsBounds = containsBounds;
	}

	public void setInitialBundle(String bundleId) {
		this.setVar(SceneElement.VAR_BUNDLE_ID, bundleId);

	}

}
