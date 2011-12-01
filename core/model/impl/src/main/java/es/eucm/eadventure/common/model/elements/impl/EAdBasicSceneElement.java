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

package es.eucm.eadventure.common.model.elements.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.interfaces.features.enums.Orientation;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.extra.EAdMap;
import es.eucm.eadventure.common.model.extra.impl.EAdMapImpl;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.annotation.Asset;
import es.eucm.eadventure.common.resources.annotation.Bundled;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;

@Element(detailed = EAdBasicSceneElement.class, runtime = EAdBasicSceneElement.class)
public class EAdBasicSceneElement extends EAdSceneElementDefImpl implements
		EAdSceneElement {

	public static final EAdVarDef<Orientation> VAR_ORIENTATION = new EAdVarDefImpl<Orientation>(
			"orientation", Orientation.class, Orientation.S);

	public static final EAdVarDef<String> VAR_STATE = new EAdVarDefImpl<String>(
			"state", String.class, CommonStates.EAD_STATE_DEFAULT.toString());

	public static final EAdVarDef<Float> VAR_SCALE = new EAdVarDefImpl<Float>(
			"scale", Float.class, 1.0f);

	public static final EAdVarDef<Float> VAR_ALPHA = new EAdVarDefImpl<Float>(
			"alpha", Float.class, 1.0f);

	public static final EAdVarDef<Float> VAR_ROTATION = new EAdVarDefImpl<Float>(
			"rotation", Float.class, 0.0f);

	public static final EAdVarDef<Boolean> VAR_VISIBLE = new EAdVarDefImpl<Boolean>(
			"visible", Boolean.class, Boolean.TRUE);

	public static final EAdVarDef<Boolean> VAR_ENABLE = new EAdVarDefImpl<Boolean>(
			"enable", Boolean.class, Boolean.TRUE);

	public static final EAdVarDef<Integer> VAR_X = new EAdVarDefImpl<Integer>(
			"x", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_Y = new EAdVarDefImpl<Integer>(
			"y", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_LEFT = new EAdVarDefImpl<Integer>(
			"left", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_TOP = new EAdVarDefImpl<Integer>(
			"top", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_RIGHT = new EAdVarDefImpl<Integer>(
			"right", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_BOTTOM = new EAdVarDefImpl<Integer>(
			"bottom", Integer.class, 0);
	
	public static final EAdVarDef<Integer> VAR_CENTER_X = new EAdVarDefImpl<Integer>(
			"center_x", Integer.class, 0);
	
	public static final EAdVarDef<Integer> VAR_CENTER_Y = new EAdVarDefImpl<Integer>(
			"center_y", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_Z = new EAdVarDefImpl<Integer>(
			"z", Integer.class, 0);

	public static final EAdVarDef<Float> VAR_DISP_X = new EAdVarDefImpl<Float>(
			"disp_x", Float.class, 0.0f);

	public static final EAdVarDef<Float> VAR_DISP_Y = new EAdVarDefImpl<Float>(
			"disp_y", Float.class, 0.0f);

	public static final EAdVarDef<Integer> VAR_WIDTH = new EAdVarDefImpl<Integer>(
			"width", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_HEIGHT = new EAdVarDefImpl<Integer>(
			"height", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_TIME_DISPLAYED = new EAdVarDefImpl<Integer>(
			"timeDisplayed", Integer.class, 0);

	public static final EAdVarDef<EAdString> VAR_NAME = new EAdVarDefImpl<EAdString>(
			"name", EAdString.class, null);

	@Bundled
	@Asset({ Drawable.class })
	public static final String appearance = "appearance";

	@Param("vars")
	private EAdMap<EAdVarDef<?>, Object> vars;

	@Param("definitino")
	private EAdSceneElementDef definition;

	private boolean clone;

	public EAdBasicSceneElement() {
		super();
		vars = new EAdMapImpl<EAdVarDef<?>, Object>(EAdVarDef.class,
				Object.class);
	}

	public boolean isClone() {
		return clone;
	}

	public void setClone(boolean b) {
		this.clone = b;
	}

	public boolean equals(Object o) {
		boolean temp = this == o;
		return temp;
	}
	
	/**
	 * Creates a basic scene element
	 * 
	 * @param id
	 *            the id
	 * @param appearance
	 *            the initial appearance
	 */
	public EAdBasicSceneElement(Drawable appearance) {
		this();
		getResources().addAsset(getInitialBundle(),
				EAdBasicSceneElement.appearance, appearance);
	}

	public EAdBasicSceneElement(EAdSceneElementDef actor) {
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
		setPosition(EAdPositionImpl.volatileEAdPosition(corner, x, y));
	}

	@Override
	public EAdSceneElementDef getDefinition() {
		return definition == null ? this : definition;
	}

	/**
	 * An enumerate with common states for scene elements
	 * 
	 * 
	 */
	public enum CommonStates {
		/**
		 * Default state
		 */
		EAD_STATE_DEFAULT,

		/**
		 * Talking state
		 */
		EAD_STATE_TALKING,

		/**
		 * Walking state
		 */
		EAD_STATE_WALKING,

		/**
		 * Using state
		 */
		EAD_STATE_USING;

	}

	public void setDefinition(EAdSceneElementDef def) {
		this.definition = def;
	}

}
